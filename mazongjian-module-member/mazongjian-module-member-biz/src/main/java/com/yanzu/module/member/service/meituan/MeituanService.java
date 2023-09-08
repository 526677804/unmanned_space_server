package com.yanzu.module.member.service.meituan;

import cn.hutool.json.JSONObject;
import com.yanzu.module.member.dal.dataobject.storemeituaninfo.StoreMeituanInfoDO;
import com.yanzu.module.member.dal.mysql.storemeituaninfo.StoreMeituanInfoMapper;
import com.yanzu.module.member.forest.MeituanClient;
import com.yanzu.module.member.service.meituan.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.GROUP_NO_CHECK_ERROR;
import static com.yanzu.module.member.enums.ErrorCodeConstants.STORE_TUANGOU_PAY_ERROR;

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
        if (!ObjectUtils.isEmpty(storeId)) {
            MeituanGetTokenReqVO reqVO = new MeituanGetTokenReqVO();
            reqVO.setApp_key(appKey);
            reqVO.setApp_secret(secret);
            reqVO.setRedirect_url(redirectUrl);
            reqVO.setAuth_code(authCode);
            JSONObject result = meituanClient.getToken(reqVO);
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
                return "success";
            }
        }
        log.info("获取美团授权token失败，{}", state);
        return "fail";
    }

    public String refreshToken(Long storeId, String refreshToken) {
        MeituanRefreshTokenReqVO reqVO = new MeituanRefreshTokenReqVO();
        reqVO.setApp_key(appKey);
        reqVO.setApp_secret(secret);
        reqVO.setRefresh_token(refreshToken);
        JSONObject result = meituanClient.refreshToken(reqVO);
        log.info("result:{}", result);
        if (result.getInt("code").intValue() == 200) {
            StoreMeituanInfoDO storeMeituanInfoDO = storeMeituanInfoMapper.getByStoreId(storeId);
            String access_token = result.getStr("access_token");
            Integer remain_refresh_count = result.getInt("remain_refresh_count");
            Long expires_in = result.getLong("expires_in");
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresDate = now.plusSeconds(expires_in);
            storeMeituanInfoDO.setAccessToken(access_token);
            storeMeituanInfoDO.setRemainRefreshCount(remain_refresh_count);
            storeMeituanInfoDO.setExpiresIn(expiresDate);
            storeMeituanInfoMapper.updateById(storeMeituanInfoDO);
            return "success";
        }
        log.info("更新美团授权token失败，storeId：{}", storeId);
        return "fail";
    }

    //查询美团券信息
    public JSONObject prepare(Long storeId, String receiptCode) {
        //查询出店铺id
        StoreMeituanInfoDO meituan = storeMeituanInfoMapper.getByStoreId(storeId);
        if (ObjectUtils.isEmpty(meituan) || ObjectUtils.isEmpty(meituan.getOpenShopUuid())) {
            throw exception(STORE_TUANGOU_PAY_ERROR);
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
        if (prepare.getInt("code") != 200) {
            throw exception(GROUP_NO_CHECK_ERROR);
        }
        return prepare.getJSONObject("data");
    }


    public JSONObject consume(Long storeId, Long userId, String receiptCode) {
        //查询出店铺id
        StoreMeituanInfoDO meituan = storeMeituanInfoMapper.getByStoreId(storeId);
        if (ObjectUtils.isEmpty(meituan) || ObjectUtils.isEmpty(meituan.getOpenShopUuid())) {
            throw exception(STORE_TUANGOU_PAY_ERROR);
        }
        MeituanConsumeReqVO reqVO = new MeituanConsumeReqVO();
        reqVO.setApp_key(appKey);
        reqVO.setSession(meituan.getAccessToken());
        reqVO.setOpen_shop_uuid(meituan.getOpenShopUuid());
        reqVO.setReceipt_code(receiptCode);
        reqVO.setApp_shop_accountname(String.valueOf(userId));
        reqVO.setApp_shop_account(String.valueOf(userId));
        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
        String sign = MeituanSignUtils.generateSign(paramMap, secret, MeituanConstants.SIGN_METHOD_MD5);
        reqVO.setSign(sign);
        JSONObject consume = meituanClient.consume(reqVO);
        log.info("美团验券:{}", consume);
        if (consume.getInt("code") != 200) {
            throw exception(GROUP_NO_CHECK_ERROR);
        }
        return (JSONObject) consume.getJSONArray("data").get(0);
    }

    public JSONObject reverseconsume(Long storeId, Long userId, String receiptCode, String dealId) {
        //查询出店铺id
        StoreMeituanInfoDO meituan = storeMeituanInfoMapper.getByStoreId(storeId);
        if (ObjectUtils.isEmpty(meituan) || ObjectUtils.isEmpty(meituan.getOpenShopUuid())) {
            throw exception(STORE_TUANGOU_PAY_ERROR);
        }
        MeituanReverseconsumeReqVO reqVO = new MeituanReverseconsumeReqVO();
        reqVO.setApp_deal_id(dealId);
        reqVO.setReceipt_code(receiptCode);
        reqVO.setSession(meituan.getAccessToken());
        reqVO.setOpen_shop_uuid(meituan.getOpenShopUuid());
        reqVO.setApp_key(appKey);
        reqVO.setApp_shop_accountname(String.valueOf(userId));
        reqVO.setApp_shop_account(String.valueOf(userId));
        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
        String sign = MeituanSignUtils.generateSign(paramMap, secret, MeituanConstants.SIGN_METHOD_MD5);
        reqVO.setSign(sign);
        JSONObject reverseconsume = meituanClient.reverseconsume(reqVO);
        log.info("美团退款:{}", reverseconsume);
        if (reverseconsume.getInt("code") != 200) {
            throw exception(GROUP_NO_CHECK_ERROR);
        }
        return (JSONObject) reverseconsume.getJSONArray("data").get(0);
    }
}
