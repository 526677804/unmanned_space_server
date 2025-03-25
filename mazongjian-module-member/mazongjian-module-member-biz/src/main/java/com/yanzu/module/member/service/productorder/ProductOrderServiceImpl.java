package com.yanzu.module.member.service.productorder;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.security.core.LoginUser;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.framework.web.core.util.WebFrameworkUtils;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.*;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListVO;
import com.yanzu.module.member.dal.dataobject.productorder.ProductOrderDO;
import com.yanzu.module.member.dal.dataobject.storeproduct.StoreProductDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.productorder.ProductOrderMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeproduct.StoreProductMapper;
import com.yanzu.module.member.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.AppEnum.WX_PRODUCT_PAY_ORDER;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

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

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private MyWxService myWxService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private StoreInfoService storeInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MemberUserMapper userMapper;

    @Override
    @Transactional
    public WxPayOrderRespVO createOrder(AppSaveOrderReqVO reqVo) {
        AppRoomListVO roomInfo = roomInfoMapper.getInfoById(reqVo.getRoomId());
        if (ObjectUtils.isEmpty(roomInfo)) {
            throw exception(DATA_NOT_EXISTS);
        }
        Long uid = getLoginUserId();
        MemberUserDO memberUserDO = userMapper.selectById(uid);
        String orderNo = getOrderNo();
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setOrderNo(orderNo);
        productOrderDO.setRoomId(reqVo.getRoomId());
        productOrderDO.setStoreId(roomInfo.getStoreId());
        productOrderDO.setUserId(uid);
        productOrderDO.setUserName(memberUserDO.getNickname());
        productOrderDO.setUserPhone(memberUserDO.getMobile());
        productOrderDO.setMark(reqVo.getMark());
        productOrderDO.setProductInfo(JSON.toJSONString(reqVo.getProductInfo()));
        int totalPrice = 0;//待计算
        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        //需要微信下单  先获取到该用户的openId
        String openId = socialUserApi.getUserOpenIdByType(WebFrameworkUtils.getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());
        if (ObjectUtils.isEmpty(openId)) {
            throw exception(AUTH_USER_BIND_MINIAPP_ERROR);
        }
        // 遍历传递过来的产品
        for (int i = 0; i < reqVo.getProductInfo().size(); i++) {
            // 计算金额
            Long number = reqVo.getProductInfo().get(i).getNumber();
            BigDecimal price = reqVo.getProductInfo().get(i).getPrice();
            totalPrice = totalPrice + new BigDecimal(number).multiply(price).multiply(BigDecimal.valueOf(100)).intValue();
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
                if (!ObjectUtils.isEmpty(productFormatDtoList.get(j).getValue2())) {
                    sku.append(",").append(productFormatDtoList.get(j).getValue2());
                }
                // 产品属性一致 减少该产品的库存
                if (sku.toString().equals(reqVo.getProductInfo().get(i).getValueStr())) {
                    int afterNum = (int) (productFormatDtoList.get(j).getStock() - reqVo.getProductInfo().get(i).getNumber());
                    if (afterNum < 0) {
                        throw exception(PRODUCT_OUT_OF_STOCK);
                    }
                    productFormatDtoList.get(j).setStock(afterNum);
                    productDto.setStock(productDto.getStock() - reqVo.getProductInfo().get(i).getNumber());
                    productDto.setSales(productDto.getSales() + reqVo.getProductInfo().get(i).getNumber());
                    StoreProductDO productDO = BeanUtil.toBean(productDto, StoreProductDO.class);
                    productMapper.updateById(productDO);

                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("attr", fromatDetailDtoArrayList);
                    map.put("value", productFormatDtoList);
                    storeProductAttrResultService.insertYxStoreProductAttrResult(map, productDto.getId());

                    productAttrValueMapper.decStockIncSales(Math.toIntExact(reqVo.getProductInfo().get(i).getNumber()), productDto.getId(), sku.toString());
                }
            }
        }
        productOrderDO.setTotalPrice(totalPrice);
        //创建微信支付实例
        WxPayService wxPayService = myWxService.initWxPay(roomInfo.getStoreId());
        //生成微信支付的订单
        try {
            WxPayMpOrderResult wxPayMpOrderResult = myWxService.createProductOrder(wxPayService, roomInfo.getStoreId(),
                    orderNo, totalPrice, openId);
            getWxPayOrderRespVo(respVO, wxPayMpOrderResult, totalPrice, productOrderDO);
            respVO.setOrderNo(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(USER_WEIXIN_PAY_ERROR);
        }
        // 生成商品订单
        productOrderMapper.insert(productOrderDO);
        Long tenantId = TenantContextHolder.getTenantId();
        // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
        if (tenantId == null) {
            LoginUser user = SecurityFrameworkUtils.getLoginUser();
            tenantId = user.getTenantId();
        }
        redisTemplate.opsForValue().set(String.format(WX_PRODUCT_PAY_ORDER, orderNo), tenantId, 1, TimeUnit.DAYS);
        return respVO;
    }

    @Override
    @Transactional
    public void cancelPay(AppCancelPayReqVO reqVo, boolean isAdmin) {
        ProductOrderDO productOrderDO = productOrderMapper.selectById(reqVo.getOrderId());
        if (ObjectUtils.isEmpty(productOrderDO)) {
            throw exception(DATA_NOT_EXISTS);
        }
        if (isAdmin) {
            //管理员取消 检查权限
            storeInfoService.checkPermisson(productOrderDO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        } else {
            //只能取消自己的订单
            if (!productOrderDO.getCreator().equals(String.valueOf(getLoginUserId()))) {
                throw exception(OPRATION_ERROR);
            }
        }
        if (productOrderDO.getPayPrice().intValue() > 0) {
            WxPayService wxPayService = myWxService.initWxPay(productOrderDO.getStoreId());
            //微信退款
            WxPayRefundRequest refundRequest = new WxPayRefundRequest();
            refundRequest.setOutTradeNo(productOrderDO.getOrderNo());
            refundRequest.setOutRefundNo("TK" + productOrderDO.getOrderNo());
            refundRequest.setTotalFee(productOrderDO.getPayPrice());
            refundRequest.setRefundFee(productOrderDO.getPayPrice());
            refundRequest.setRefundDesc("取消商品订单退款");
            try {
                wxPayService.refundV2(refundRequest);
                //状态改成取消
                productOrderDO.setStatus(3L);
                productOrderMapper.updateById(productOrderDO);
            } catch (WxPayException ex) {
                log.error("商品购买微信支付订单退款失败:{}", productOrderDO.getOrderNo());
                throw exception(PRODUCT_REFOUND_ERROR, ex.getMessage());
            }
        } else {
            //状态改成取消
            productOrderDO.setStatus(3L);
            productOrderMapper.updateById(productOrderDO);
        }
    }

    @Override
    public PageResult<AppUserOrderPageRespVO> userOrderByPage(AppUserOrderPageReqVO reqVO) {
        IPage<AppUserOrderPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        productOrderMapper.userOrderByPage(page, reqVO, getLoginUserId(), null);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(x -> {
                if (!ObjectUtils.isEmpty(x.getProductInfoJson())) {
                    x.setProductInfoVoList(JSONArray.parseArray(x.getProductInfoJson(), ProductInfoVo.class));
                }
            });
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<AppHaveOrderStoreRespVO> selectHaveOrderStore() {
        return productOrderMapper.selectHaveOrderStore(getLoginUserId());
    }

    @Override
    @Transactional
    public WxPayOrderRespVO pay(Long orderId) {
        ProductOrderDO productOrderDO = productOrderMapper.selectById(orderId);
        //创建微信支付实例
        WxPayService wxPayService = myWxService.initWxPay(productOrderDO.getStoreId());
        //需要微信下单  先获取到该用户的openId
        String openId = socialUserApi.getUserOpenIdByType(WebFrameworkUtils.getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());

        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        WxPayMpOrderResult wxPayMpOrderResult = myWxService.createProductOrder(wxPayService, productOrderDO.getStoreId(),
                productOrderDO.getOrderNo(), productOrderDO.getTotalPrice(), openId);
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
    public PageResult<AppUserOrderPageRespVO> managerProductOrder(AppUserOrderPageReqVO reqVO) {
        // 获取自己管理的门店列表
        List<String> storeIds = storeUserMapper.getIdsByUserIdAndAdmin(getLoginUserId());
        IPage<AppUserOrderPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        productOrderMapper.userOrderByPage(page, reqVO, getLoginUserId(), storeIds);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(x -> {
                if (!ObjectUtils.isEmpty(x.getProductInfoJson())) {
                    x.setProductInfoVoList(JSONArray.parseArray(x.getProductInfoJson(), ProductInfoVo.class));
                }
            });
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    @Transactional
    public void finishOrder(Long id) {
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setOrderId(id);
        productOrderDO.setStatus(2L);
        productOrderMapper.updateById(productOrderDO);
    }

    @Override
    public AppUserOrderPageRespVO orderInfo(Long orderId) {
        ProductOrderDO productOrderDO = productOrderMapper.selectById(orderId);
        AppUserOrderPageRespVO bean = BeanUtil.toBean(productOrderDO, AppUserOrderPageRespVO.class);
        bean.setUserPhone(bean.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        bean.setProductInfoVoList(JSONObject.parseArray(productOrderDO.getProductInfo(), ProductInfoVo.class));
        return bean;
    }

    @Override
    public String getPhone(Long orderId) {
        return productOrderMapper.getPhone(orderId);
    }

    private void getWxPayOrderRespVo(WxPayOrderRespVO respVO, WxPayMpOrderResult wxPayMpOrderResult, Integer totalPrice, ProductOrderDO productOrderDO) {
        respVO.setPkg(wxPayMpOrderResult.getPackageValue());
        respVO.setAppId(wxPayMpOrderResult.getAppId());
        respVO.setNonceStr(wxPayMpOrderResult.getNonceStr());
        respVO.setPaySign(wxPayMpOrderResult.getPaySign());
        respVO.setSignType("MD5");
        respVO.setTimeStamp(wxPayMpOrderResult.getTimeStamp());
        respVO.setPayPrice(totalPrice);
        respVO.setPrice(totalPrice);
    }

    private String getOrderNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(10000000);
        String randomNumString = String.format("%09d", randomNum);
        return "SH" + currentDate + randomNumString;
    }

}
