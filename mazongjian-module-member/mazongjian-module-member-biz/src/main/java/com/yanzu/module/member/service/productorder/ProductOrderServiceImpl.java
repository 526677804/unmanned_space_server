package com.yanzu.module.member.service.productorder;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.security.core.LoginUser;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.framework.web.core.util.WebFrameworkUtils;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.*;
import com.yanzu.module.member.dal.dataobject.productorder.ProductOrderDO;
import com.yanzu.module.member.dal.dataobject.storeproduct.StoreProductDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.productorder.ProductOrderMapper;
import com.yanzu.module.member.dal.mysql.storeproduct.StoreProductMapper;
import com.yanzu.module.member.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.service.storeproduct.StoreProductService;
import com.yanzu.module.member.service.storeproduct.dto.FromatDetailDto;
import com.yanzu.module.member.service.storeproduct.dto.ProductDto;
import com.yanzu.module.member.service.storeproduct.dto.ProductFormatDto;
import com.yanzu.module.member.service.storeproductattrresult.StoreProductAttrResultService;
import com.yanzu.module.member.service.wx.MyWxService;
import com.yanzu.module.system.api.social.SocialUserApi;
import com.yanzu.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.AppEnum.WX_PAY_ORDER;
import static com.yanzu.module.member.enums.AppEnum.WX_PRODUCT_PAY_ORDER;
import static com.yanzu.module.member.enums.ErrorCodeConstants.AUTH_USER_BIND_MINIAPP_ERROR;
import static com.yanzu.module.member.enums.ErrorCodeConstants.USER_WEIXIN_PAY_ERROR;

@Service
@Validated
@Slf4j
public class ProductOrderServiceImpl implements ProductOrderService {

    @Resource
    private ProductOrderMapper productOrderMapper;

    @Resource
    private SocialUserApi socialUserApi;

    @Resource
    private StoreProductService productService;

    @Resource
    private StoreProductMapper productMapper;

    @Resource
    private StoreProductAttrResultService storeProductAttrResultService;

    @Resource
    private StoreProductAttrValueMapper productAttrValueMapper;

    @Resource
    private StoreUserMapper storeUserMapper;

    @Autowired
    private MyWxService myWxService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MemberUserMapper userMapper;

    @Override
    public WxPayOrderRespVO createOrder(AppSaveOrderReqVo reqVo) {
        Long uid = getLoginUserId();
        MemberUserDO memberUserDO = userMapper.selectById(uid);
        String orderNo = getOrderNo();
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setOrderNo(orderNo);
        productOrderDO.setStoreId(reqVo.getStoreId());
        productOrderDO.setStoreName(reqVo.getStoreName());
        productOrderDO.setUserId(uid);
        productOrderDO.setUserName(memberUserDO.getNickname());
        productOrderDO.setUserPhone(memberUserDO.getMobile());
        productOrderDO.setMark(reqVo.getMark());
        productOrderDO.setProductInfo(JSON.toJSONString(reqVo.getProductInfo()));
        productOrderDO.setTotalPrice(reqVo.getTotalPrice());
        productOrderDO.setPayPrice(reqVo.getTotalPrice());

        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        if (reqVo.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) {
            //需要微信下单  先获取到该用户的openId
            String openId = socialUserApi.getUserOpenIdByType(WebFrameworkUtils.getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());
            if (ObjectUtils.isEmpty(openId)) {
                throw exception(AUTH_USER_BIND_MINIAPP_ERROR);
            }
            //创建微信支付实例
            WxPayService wxPayService = myWxService.initWxPay(reqVo.getStoreId());
            //生成微信支付的订单
            try {
                WxPayMpOrderResult wxPayMpOrderResult = myWxService.createProductOrder(wxPayService, reqVo.getStoreId(),
                        orderNo, reqVo.getTotalPrice().multiply(BigDecimal.valueOf(100D)).intValue(), openId);
                getWxPayOrderRespVo(respVO, wxPayMpOrderResult, reqVo.getTotalPrice(), productOrderDO);
                respVO.setOrderNo(orderNo);
            } catch (Exception e) {
                e.printStackTrace();
                throw exception(USER_WEIXIN_PAY_ERROR);
            }
            // 生成商品订单
            productOrderMapper.insert(productOrderDO);
            // 遍历传递过来的产品  // todo 修改产品数量 和 商品信息有问题
            for (int i = 0; i < reqVo.getProductInfo().size(); i++) {
                // 查到相关产品 以及详细属性
                Map<String, Object> productInfo = productService.getProductInfo(reqVo.getProductInfo().get(i).getId());
                ProductDto productDto = (ProductDto) productInfo.get("productInfo");
                JSONObject productResult = productDto.getProductResult();
                List<FromatDetailDto> fromatDetailDtoArrayList = new ArrayList<>();
                List<ProductFormatDto> productFormatDtoList = new ArrayList<>();

                Object attrObject = productResult.get("attr");
                if (attrObject instanceof List) {
                    List<?> attrList = (List<?>) attrObject;
                    for (Object item : attrList) {
                        FromatDetailDto fromatDetailDto = JSON.toJavaObject(JSON.parseObject(item.toString()), FromatDetailDto.class);
                        fromatDetailDtoArrayList.add(fromatDetailDto);
                    }
                }
                Object valueObject = productResult.get("value");
                if (valueObject instanceof List) {
                    List<?> valueList = (List<?>) valueObject;
                    for (Object item : valueList) {
                        ProductFormatDto productFormatDto = JSON.toJavaObject(JSON.parseObject(item.toString()), ProductFormatDto.class);
                        productFormatDtoList.add(productFormatDto);
                    }
                }

                // 判断是哪个属性被购买
                for (int j = 0; j < productFormatDtoList.size(); j++) {
                    System.out.println(productFormatDtoList.get(j));
                    StringBuilder sku = new StringBuilder(productFormatDtoList.get(j).getValue1());
                    if (!ObjectUtils.isEmpty(productFormatDtoList.get(j).getValue2())){
                        sku.append(",").append(productFormatDtoList.get(j).getValue2());
                    }
                    // 产品属性一致 减少该产品的库存
                    if (sku.toString().equals(reqVo.getProductInfo().get(i).getValueStr())) {
                        int afterNum = (int) (productFormatDtoList.get(j).getStock() - reqVo.getProductInfo().get(i).getNumber());
                        if (afterNum<0){
                            throw new RuntimeException("商品库存不足。");
                        }
                        productFormatDtoList.get(j).setStock(afterNum);
                        productDto.setStock(productDto.getStock()-reqVo.getProductInfo().get(i).getNumber());
                        productDto.setSales(productDto.getSales()+reqVo.getProductInfo().get(i).getNumber());
                        StoreProductDO productDO = BeanUtil.toBean(productDto, StoreProductDO.class);
                        productMapper.updateById(productDO);

                        Map<String,Object> map = new LinkedHashMap<>();
                        map.put("attr",fromatDetailDtoArrayList);
                        map.put("value",productFormatDtoList);
                        storeProductAttrResultService.insertYxStoreProductAttrResult(map,productDto.getId());

                        productAttrValueMapper.decStockIncSales(Math.toIntExact(reqVo.getProductInfo().get(i).getNumber()),productDto.getId(),sku.toString());
                    }
                }
            }
            Long tenantId = TenantContextHolder.getTenantId();
            // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
            if (tenantId == null) {
                LoginUser user = SecurityFrameworkUtils.getLoginUser();
                tenantId = user.getTenantId();
            }
            redisTemplate.opsForValue().set(String.format(WX_PRODUCT_PAY_ORDER, orderNo), tenantId, 1, TimeUnit.DAYS);
        }
        return respVO;
    }

    @Override
    public void cancelPay(String orderNo) {
        productOrderMapper.updateByOrderNo(orderNo);
    }

    @Override
    public PageResult<AppUserOrderPageRespVo> userOrderByPage(AppUserOrderPageReqVo reqVo) {

        LambdaQueryWrapperX<ProductOrderDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eq(ProductOrderDO::getUserId, getLoginUserId())
                .eqIfPresent(ProductOrderDO::getStoreId, reqVo.getStoreId())
                .eqIfPresent(ProductOrderDO::getStatus, reqVo.getStatus())
                .orderByDesc(ProductOrderDO::getCreateTime);

        return getAppUserOrderPageRespVoPageResult(reqVo, queryWrapper);
    }

    @Override
    public List<AppHaveOrderStoreRespVo> selectHaveOrderStore() {
        return productOrderMapper.selectHaveOrderStore(getLoginUserId());
    }

    @Override
    public WxPayOrderRespVO pay(Long orderId) {
        ProductOrderDO productOrderDO = productOrderMapper.selectById(orderId);
        //创建微信支付实例
        WxPayService wxPayService = myWxService.initWxPay(productOrderDO.getStoreId());
        //需要微信下单  先获取到该用户的openId
        String openId = socialUserApi.getUserOpenIdByType(WebFrameworkUtils.getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());

        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        WxPayMpOrderResult wxPayMpOrderResult = myWxService.createProductOrder(wxPayService, productOrderDO.getStoreId(),
                productOrderDO.getOrderNo(), productOrderDO.getTotalPrice().multiply(BigDecimal.valueOf(100D)).intValue(), openId);
        getWxPayOrderRespVo(respVO, wxPayMpOrderResult, productOrderDO.getTotalPrice(), productOrderDO);
        respVO.setOrderNo(productOrderDO.getOrderNo());

        Long tenantId = TenantContextHolder.getTenantId();
        // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
        if (tenantId == null) {
            LoginUser user = SecurityFrameworkUtils.getLoginUser();
            tenantId = user.getTenantId();
        }
        redisTemplate.opsForValue().set(String.format(WX_PRODUCT_PAY_ORDER, productOrderDO.getOrderNo()), tenantId, 1, TimeUnit.DAYS);
        return respVO;
    }


    @Override
    public PageResult<AppUserOrderPageRespVo> managerProductOrder(AppUserOrderPageReqVo reqVo) {
        // 获取自己管理的门店列表
        List<Long> longs = storeUserMapper.selectSelfStoreIds(getLoginUserId());

        LambdaQueryWrapperX<ProductOrderDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.in(ProductOrderDO::getStoreId,longs)
                .eqIfPresent(ProductOrderDO::getStoreId, reqVo.getStoreId())
                .eqIfPresent(ProductOrderDO::getStatus, reqVo.getStatus())
                .orderByDesc(ProductOrderDO::getCreateTime);

        return getAppUserOrderPageRespVoPageResult(reqVo, queryWrapper);
    }

    @Override
    public void finishOrder(Long id) {
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setOrderId(id);
        productOrderDO.setStatus(2L);
        productOrderMapper.updateById(productOrderDO);
    }

    @Override
    public AppUserOrderPageRespVo orderInfo(Long orderId) {
        ProductOrderDO productOrderDO = productOrderMapper.selectById(orderId);
        AppUserOrderPageRespVo bean = BeanUtil.toBean(productOrderDO, AppUserOrderPageRespVo.class);
        bean.setUserPhone(bean.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        bean.setProductInfoVoList(JSONObject.parseArray(productOrderDO.getProductInfo(), ProductInfoVo.class));
        return bean;
    }

    private PageResult<AppUserOrderPageRespVo> getAppUserOrderPageRespVoPageResult(AppUserOrderPageReqVo reqVo, LambdaQueryWrapperX<ProductOrderDO> queryWrapper) {
        PageResult<ProductOrderDO> pageResult = productOrderMapper.selectPage(reqVo, queryWrapper);

        return pageResult.getList().stream()
                .map(item -> {
                    AppUserOrderPageRespVo bean = BeanUtil.toBean(item, AppUserOrderPageRespVo.class);
                    bean.setUserPhone(bean.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    bean.setProductInfoVoList(JSONObject.parseArray(item.getProductInfo(), ProductInfoVo.class));
                    return bean;
                })
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    PageResult<AppUserOrderPageRespVo> result = new PageResult<>();
                    result.setList(list);
                    result.setTotal(pageResult.getTotal());
                    return result;
                }));
    }

    private void getWxPayOrderRespVo(WxPayOrderRespVO respVO, WxPayMpOrderResult wxPayMpOrderResult, BigDecimal totalPrice, ProductOrderDO productOrderDO) {
        respVO.setPkg(wxPayMpOrderResult.getPackageValue());
        respVO.setAppId(wxPayMpOrderResult.getAppId());
        respVO.setNonceStr(wxPayMpOrderResult.getNonceStr());
        respVO.setPaySign(wxPayMpOrderResult.getPaySign());
        respVO.setSignType("MD5");
        respVO.setTimeStamp(wxPayMpOrderResult.getTimeStamp());
        respVO.setPayPrice(totalPrice.multiply(BigDecimal.valueOf(100D)).intValue());
        respVO.setPrice(totalPrice.multiply(BigDecimal.valueOf(100D)).intValue());
    }

    private String getOrderNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(10000000);
        String randomNumString = String.format("%09d", randomNum);
        return "SH"+currentDate + randomNumString;
    }

}
