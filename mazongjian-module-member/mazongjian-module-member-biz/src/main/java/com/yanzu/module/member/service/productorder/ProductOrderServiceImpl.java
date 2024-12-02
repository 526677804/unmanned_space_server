package com.yanzu.module.member.service.productorder;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.web.core.util.WebFrameworkUtils;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.AppSaveOrderReqVo;
import com.yanzu.module.member.dal.dataobject.productorder.ProductOrderDO;
import com.yanzu.module.member.dal.dataobject.storeproduct.StoreProductDO;
import com.yanzu.module.member.dal.mysql.productorder.ProductOrderMapper;
import com.yanzu.module.member.dal.mysql.storeproduct.StoreProductMapper;
import com.yanzu.module.member.dal.mysql.storeproductattrvalue.StoreProductAttrValueMapper;
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

    @Autowired
    private MyWxService myWxService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public WxPayOrderRespVO createOrder(AppSaveOrderReqVo reqVo) {
        Long uid = getLoginUserId();
        String orderNo = getOrderNo();
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setOrderNo(orderNo);
        productOrderDO.setStoreId(reqVo.getStoreId());
        productOrderDO.setUserId(uid);
        productOrderDO.setProductInfo(JSON.toJSONString(reqVo.getProductInfo()));
        productOrderDO.setTotalPrice(reqVo.getTotalPrice());

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
                respVO.setPkg(wxPayMpOrderResult.getPackageValue());
                respVO.setAppId(wxPayMpOrderResult.getAppId());
                respVO.setNonceStr(wxPayMpOrderResult.getNonceStr());
                respVO.setPaySign(wxPayMpOrderResult.getPaySign());
                respVO.setSignType("MD5");
                respVO.setTimeStamp(wxPayMpOrderResult.getTimeStamp());
                respVO.setPayPrice(reqVo.getTotalPrice().multiply(BigDecimal.valueOf(100D)).intValue());
                respVO.setPrice(reqVo.getTotalPrice().multiply(BigDecimal.valueOf(100D)).intValue());
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
                    String sku = productFormatDtoList.get(j).getValue1();
                    if (!ObjectUtils.isEmpty(productFormatDtoList.get(j).getValue2())){
                        sku = ","+productFormatDtoList.get(j).getValue2();
                    }
                    // 产品属性一致 减少该产品的库存
                    if (sku.equals(reqVo.getProductInfo().get(i).getValueStr())) {
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

                        productAttrValueMapper.decStockIncSales(Math.toIntExact(reqVo.getProductInfo().get(i).getNumber()),productDto.getId(),sku);
                    }
                }
            }
        }
        return respVO;
    }

    @Override
    public void cancelPay(String orderNo) {
        productOrderMapper.updateByOrderNo(orderNo);
    }


    private String getOrderNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(1000000000);
        String randomNumString = String.format("%09d", randomNum);
        return currentDate + randomNumString;
    }

}
