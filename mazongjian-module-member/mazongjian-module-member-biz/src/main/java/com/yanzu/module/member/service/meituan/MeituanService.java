package com.yanzu.module.member.service.meituan;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.yanzu.module.member.dal.dataobject.storemeituaninfo.StoreMeituanInfoDO;
import com.yanzu.module.member.dal.mysql.storemeituaninfo.StoreMeituanInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.forest.MeituanClient;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;
import com.yanzu.module.member.service.meituan.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 17:07
 */
@Component
@Slf4j
public class MeituanService {

    @Value("${meituan.appKey}")
    private String appKey;

    @Value("${meituan.secret}")
    private String secret;

    @Value("${meituan.redirectUrl}")
    private String redirectUrl;

    @Autowired
    private MeituanClient meituanClient;

    @Autowired
    private StoreMeituanInfoMapper storeMeituanInfoMapper;

    public String getToken(String authCode, String state) {
        Long storeId = Long.valueOf(state.split("-")[1]);
        JSONObject result = null;
        if (!ObjectUtils.isEmpty(storeId)) {
            MeituanGetTokenReqVO reqVO = new MeituanGetTokenReqVO();
            reqVO.setApp_key(appKey);
            reqVO.setApp_secret(secret);
            reqVO.setRedirect_url(redirectUrl);
            reqVO.setAuth_code(authCode);
            result = meituanClient.getToken(reqVO);
            log.info("result:{}", result);
//            {"code":200,"msg":"success","access_token":"be887662bc3c89b855f572517f4055a6c03cf888","expires_in":2591999,
//            "remain_refresh_count":12,"tokenType":"bearer","scope":"tuangou","bid":"c4408e57fb4058237e70beacb2945e49",
//            "refresh_token":"1ae8013180fc4095b09c508c2ffe5d31d6a5cd62"}
            if (result.getInt("code").intValue() == 200) {
                StoreMeituanInfoDO storeMeituanInfoDO = storeMeituanInfoMapper.getByStoreId(storeId);
                String access_token = result.getStr("access_token");
                String refresh_token = result.getStr("refresh_token");
                Integer remain_refresh_count = result.getInt("remain_refresh_count");
                Long expires_in = result.getLong("expires_in");
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiresDate = now.plusSeconds(expires_in);
                //把对应美团的open_shop_uuid查询出来
                String bid = result.getStr("bid");
                MeituanScopeReqVO scopeReqVO = new MeituanScopeReqVO();
                scopeReqVO.setBid(bid);
                scopeReqVO.setSession(access_token);
                scopeReqVO.setApp_key(appKey);
                Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
                String sign = MeituanSignUtils.generateSign(paramMap, secret, MeituanConstants.SIGN_METHOD_MD5);
                scopeReqVO.setSign(sign);
                JSONObject scope = meituanClient.scope(scopeReqVO);
                log.info("首次获取授权,店铺信息查询结果:{}", scope);
                if (ObjectUtils.isEmpty(storeMeituanInfoDO)) {
                    //不存在时 属于第一次获取
                    storeMeituanInfoDO = new StoreMeituanInfoDO();
                    storeMeituanInfoDO.setStoreId(storeId);
                    storeMeituanInfoDO.setAccessToken(access_token);
                    storeMeituanInfoDO.setRefreshToken(refresh_token);
                    storeMeituanInfoDO.setRemainRefreshCount(remain_refresh_count);
                    storeMeituanInfoDO.setExpiresIn(expiresDate);
                    storeMeituanInfoMapper.insert(storeMeituanInfoDO);
                } else {
                    storeMeituanInfoDO.setStoreId(storeId);
                    storeMeituanInfoDO.setAccessToken(access_token);
                    storeMeituanInfoDO.setRefreshToken(refresh_token);
                    storeMeituanInfoDO.setRemainRefreshCount(remain_refresh_count);
                    storeMeituanInfoDO.setExpiresIn(expiresDate);
                    storeMeituanInfoMapper.updateById(storeMeituanInfoDO);
                }
                return scope.toString();
            }
        }
        log.info("获取美团授权token失败，{}", state);
        return "获取美团授权失败,错误信息:" + result.toString();
    }

    public String refreshToken(Long storeId, String refreshToken) {
        log.info("刷新美团授权，storeId：{}", storeId);
        MeituanRefreshTokenReqVO reqVO = new MeituanRefreshTokenReqVO();
        reqVO.setApp_key(appKey);
        reqVO.setApp_secret(secret);
        reqVO.setRefresh_token(refreshToken);
        JSONObject result = meituanClient.refreshToken(reqVO);
        log.info("result:{}", result);
        if (result.getInt("code").intValue() == 200) {
            StoreMeituanInfoDO storeMeituanInfoDO = storeMeituanInfoMapper.getByStoreId(storeId);
            String access_token = result.getStr("access_token");
            String refresh_token = result.getStr("refresh_token");
            Integer remain_refresh_count = result.getInt("remain_refresh_count");
            Long expires_in = result.getLong("expires_in");
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresDate = now.plusSeconds(expires_in);
            storeMeituanInfoDO.setAccessToken(access_token);
            storeMeituanInfoDO.setRefreshToken(refresh_token);
            storeMeituanInfoDO.setRemainRefreshCount(remain_refresh_count);
            storeMeituanInfoDO.setExpiresIn(expiresDate);
            storeMeituanInfoMapper.updateById(storeMeituanInfoDO);
            return "success";
        }
        log.info("更新美团授权token失败，storeId：{}", storeId);
        return "fail";
    }

    //查询美团券信息
    public IotGroupPayPrepareRespVO prepare(Long storeId, String receiptCode) {
        //查询出店铺id
        StoreMeituanInfoDO meituan = storeMeituanInfoMapper.getByStoreId(storeId);
        if (ObjectUtils.isEmpty(meituan) || ObjectUtils.isEmpty(meituan.getOpenShopUuid())) {
            throw exception(STORE_MT_TUANGOU_PAY_ERROR);
        }
        MeituanPrepareReqVO reqVO = new MeituanPrepareReqVO();
        reqVO.setApp_key(appKey);
        reqVO.setSession(meituan.getAccessToken());
        reqVO.setReceipt_code(receiptCode);
        reqVO.setOpen_shop_uuid(meituan.getOpenShopUuid());
        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
        String sign = MeituanSignUtils.generateSign(paramMap, secret, MeituanConstants.SIGN_METHOD_MD5);
        reqVO.setSign(sign);
        JSONObject prepare = meituanClient.prepare(reqVO);
        log.info("美团查询券信息:{}", prepare);
        if (prepare.getInt("code") == 1141) {
            throw exception(GROUP_NO_CHECK_REFLASH_ERROR);
        } else if (prepare.getInt("code") == 1012) {
            throw exception(GROUP_NO_CHECK_STORE_ERROR);
        } else if (prepare.getInt("code") == 645) {
            throw exception(GROUP_MEITUAN_SCOPE_ERROR);
        } else if (prepare.getInt("code") != 200) {
            throw exception(GROUP_NO_CHECK_ERROR);
        }
        JSONObject data = prepare.getJSONObject("data");
        IotGroupPayPrepareRespVO respVO = new IotGroupPayPrepareRespVO();
        respVO.setTicketName(data.getStr("deal_title"));
        respVO.setTicketInfo(data.getStr("deal_id"));
        //商品的售价
        BigDecimal dealPrice = data.getBigDecimal("deal_price");
        JSONArray paymentDetail = data.getJSONArray("payment_detail");
        int price = getPrice(paymentDetail, dealPrice);
        respVO.setPayAmount(price);
        respVO.setGroupPayType(AppEnum.member_group_no_type.MEITUAN.getValue());
        return respVO;
    }


    //获取团购券实际的到账金额
    private int getPrice(JSONArray paymentDetail, BigDecimal dealPrice) {
        //用户实际支付的价格
        BigDecimal payPrice = BigDecimal.ZERO;
        //所有优惠的价格
        BigDecimal coupinPrice = BigDecimal.ZERO;
        for (Object obj : paymentDetail) {
            //amount_type = 8，17，18，22，24,28表示商家优惠，10，23，25，26，29表示用户支付；其余为平台优惠
            //到账金额的计算 应为用户支付+平台优惠-商家优惠
            com.alibaba.fastjson.JSONObject jsonObj = (com.alibaba.fastjson.JSONObject) obj;
            Integer type = jsonObj.getInteger("amount_type");
            if (type == 8 || type == 17 || type == 18 || type == 22 || type == 24 || type == 28) {
                coupinPrice = coupinPrice.add(jsonObj.getBigDecimal("amount"));
            } else {
                payPrice = payPrice.add(jsonObj.getBigDecimal("amount"));
            }
        }
        //总价
        BigDecimal totalPrice = payPrice.add(coupinPrice);
        //数量  用包含优惠的总价  除以 销售单价可以得到数量（用户可能一次购买多张券）
        BigDecimal saleCount = totalPrice.divide(dealPrice);
        payPrice = payPrice.multiply(new BigDecimal(100.0));
        //计算这张券的实际单价
        int price = payPrice.divide(saleCount, 2, RoundingMode.HALF_UP).intValue();
        return price;
    }

    public JSONObject consume(Long storeId, String receiptCode, String dealId) {
        //查询出店铺id
        StoreMeituanInfoDO meituan = storeMeituanInfoMapper.getByStoreId(storeId);
        if (ObjectUtils.isEmpty(meituan) || ObjectUtils.isEmpty(meituan.getOpenShopUuid())) {
            throw exception(STORE_MT_TUANGOU_PAY_ERROR);
        }
        MeituanConsumeReqVO reqVO = new MeituanConsumeReqVO();
        reqVO.setApp_key(appKey);
        reqVO.setSession(meituan.getAccessToken());
        reqVO.setOpen_shop_uuid(meituan.getOpenShopUuid());
        reqVO.setReceipt_code(receiptCode);
        reqVO.setApp_shop_accountname("system");
        reqVO.setApp_shop_account("system");
        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
        String sign = MeituanSignUtils.generateSign(paramMap, secret, MeituanConstants.SIGN_METHOD_MD5);
        reqVO.setSign(sign);
        JSONObject consume = meituanClient.consume(reqVO);
        log.info("美团验券:{}", consume);
        if (consume.getInt("code") != 200) {
            //异常了
            throw exception(GROUP_NO_CHECK_ERROR);
        }
        return (JSONObject) consume.getJSONArray("data").get(0);
    }

    public JSONObject reverseconsume(Long storeId, String receiptCode, String dealId) {
        //查询出店铺id
        StoreMeituanInfoDO meituan = storeMeituanInfoMapper.getByStoreId(storeId);
        if (ObjectUtils.isEmpty(meituan) || ObjectUtils.isEmpty(meituan.getOpenShopUuid())) {
            throw exception(STORE_MT_TUANGOU_PAY_ERROR);
        }
        MeituanReverseconsumeReqVO reqVO = new MeituanReverseconsumeReqVO();
        reqVO.setApp_deal_id(dealId);
        reqVO.setReceipt_code(receiptCode);
        reqVO.setSession(meituan.getAccessToken());
        reqVO.setOpen_shop_uuid(meituan.getOpenShopUuid());
        reqVO.setApp_key(appKey);
        reqVO.setApp_shop_accountname("system");
        reqVO.setApp_shop_account("system");
        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
        String sign = MeituanSignUtils.generateSign(paramMap, secret, MeituanConstants.SIGN_METHOD_MD5);
        reqVO.setSign(sign);
        JSONObject reverseconsume = meituanClient.reverseconsume(reqVO);
        log.info("美团退款:{}", reverseconsume);
        if (reverseconsume.getInt("code") != 200) {
//            if (reverseconsume.getInt("code") == 1029) {
//                throw exception(GROUP_NO_CANCEL_TIMEOUT_ERROR);
//            }
            throw exception(GROUP_NO_CANCEL_TIMEOUT_ERROR);
        }
        return (JSONObject) reverseconsume.getJSONArray("data").get(0);
    }
}
