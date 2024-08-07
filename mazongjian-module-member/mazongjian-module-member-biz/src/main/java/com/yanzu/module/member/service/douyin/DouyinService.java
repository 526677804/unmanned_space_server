package com.yanzu.module.member.service.douyin;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.http.ForestResponse;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.forest.DouyinClient;
import com.yanzu.module.member.service.douyin.vo.*;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/8 12:29
 */
@Component
@Slf4j
public class DouyinService {


    @Value("${douyin.appKey}")
    private String appKey;

    @Value("${douyin.secret}")
    private String secret;

    @Value("${douyin.redirectUrl}")
    private String redirectUrl;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DouyinClient douyinClient;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    public String getClientToken() {
        if (redisTemplate.hasKey(AppEnum.DOUYIN_CLIENT_TOKEN)) {
            return (String) redisTemplate.opsForValue().get(AppEnum.DOUYIN_CLIENT_TOKEN);
        }
        DouyinGetClientTokenReqVO reqVO = new DouyinGetClientTokenReqVO();
        reqVO.setClient_key(appKey);
        reqVO.setClient_secret(secret);
        JSONObject jsonObject = douyinClient.getClientToken(reqVO);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data.getInteger("error_code") != 0) {
            log.error("获取抖音client_token失败:{}", jsonObject.getString("message"));
            return "error";
        } else {
            String access_token = data.getString("access_token");
            Integer expires_in = data.getInteger("expires_in");
            //client_token 的有效时间为 2 个小时， 这里设置一个小时  提前获取
            redisTemplate.opsForValue().set(AppEnum.DOUYIN_CLIENT_TOKEN, access_token, 1, TimeUnit.HOURS);
            return access_token;
        }
    }

    public String getCode() {
        DouyinGetCodeReqParam reqVO = new DouyinGetCodeReqParam();
        reqVO.setClient_key(appKey);
        reqVO.setRedirect_uri(redirectUrl);
        //应用授权作用域，多个授权作用域以英文逗号（,）分隔
        //查询门店信息 life.capacity.shop
        reqVO.setScope("life.capacity.shop,");
        JSONObject jsonObject = douyinClient.getCode(reqVO);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data.getInteger("error_code") != 0) {
            log.error("获取抖音client_token失败:{}", jsonObject.getString("message"));
            return "error";
        } else {
            String access_token = data.getString("access_token");
            Integer expires_in = data.getInteger("expires_in");

            return access_token;
        }

    }


    public IotGroupPayPrepareRespVO prepare(String code) {
        String url = "";
        if (code.indexOf("http") != -1) {
            String pattern = "/coupon/(.*?)/";
            ForestResponse<String> forestResponse = douyinClient.queryCode(code);
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(forestResponse.redirectionRequest().getUrl());
            if (m.find()) {
                code = m.group(1);
            } else {
                log.error("查询抖音团购券信息失败:{}", code);
            }
            url = "https://open.douyin.com/goodlife/v1/fulfilment/certificate/prepare/?encrypted_data=" + code;

        } else {
            url = "https://open.douyin.com/goodlife/v1/fulfilment/certificate/prepare/?code=" + code;
        }
        String clientToken = getClientToken();
//        String clientToken = "clt.3efe71a01b1fc7a1a2eba0acc388e737VhlUsOfIP0X251tB7dSyNA1ReNJl";
//        System.out.println(clientToken);
        JSONObject prepare = douyinClient.prepare(url, clientToken);
        JSONObject data = prepare.getJSONObject("data");
        if (data.getInteger("error_code") != 0) {
            log.error("查询抖音团购券信息失败:{}", prepare.getString("description"));
            throw exception(GROUP_NO_CHECK_ERROR);
        } else {
            log.info("抖音团购券:{}", data);
            //券实付金额。券实付金额 = 用户实付金额 + 支付优惠金额，单位分
            Integer pay_amount = data.getJSONArray("certificates").getJSONObject(0).getJSONObject("amount").getInteger("coupon_pay_amount");
            //团购名称
            String title = data.getJSONArray("certificates").getJSONObject(0).getJSONObject("sku").getString("title");
            //一次验券的标识,在验券接口传入
            String verify_token = data.getString("verify_token");
            String encrypted_code = data.getJSONArray("certificates").getJSONObject(0).getString("encrypted_code");
            IotGroupPayPrepareRespVO respVO = new IotGroupPayPrepareRespVO();
            respVO.setPayAmount(pay_amount)
                    .setTicketName(title)
                    .setTicketInfo(verify_token + "&" + encrypted_code)
                    .setGroupPayType(AppEnum.member_group_no_type.DOUYIN.getValue());
            return respVO;
        }

    }

    public String verify(Long storeId, String ticketInfo) {
        //查询出门店对应的抖音poiId
        String poiId = storeInfoMapper.getDouyinPoiId(storeId);
        if (ObjectUtils.isEmpty(poiId)) {
            throw exception(STORE_DY_TUANGOU_PAY_ERROR);
        }
        String[] split = ticketInfo.split("&");
        DouyinVerifyReqVO reqVO = new DouyinVerifyReqVO();
        reqVO.setVerify_token(split[0]);
        reqVO.setEncrypted_codes(new String[]{split[1]});
        reqVO.setPoi_id(poiId);
        String clientToken = getClientToken();
        JSONObject jsonObject = douyinClient.verify(reqVO, clientToken);
        log.info("抖音验券:{}", jsonObject);
        JSONObject data = jsonObject.getJSONObject("data");
        String verify_id = "";
        String certificate_id = "";
        if (data.getInteger("error_code") == 0) {
            JSONObject verify_results = (JSONObject) data.getJSONArray("verify_results").get(0);
            verify_id = verify_results.getString("verify_id");
            certificate_id = verify_results.getString("certificate_id");
            return verify_id + "&" + certificate_id;
        } else if (data.getInteger("error_code") == 1228) {
            throw exception(GROUP_NO_CHECK_STORE_ERROR);
        } else {
            throw exception(GROUP_NO_CHECK_ERROR);
        }
    }

    public void cancel(DouyinCancelReqVO reqVO) {
        String clientToken = getClientToken();
        JSONObject jsonObject = douyinClient.cancel(reqVO, clientToken);
        log.info("抖音撤销验券:{}", jsonObject);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data.getInteger("error_code") == 0) {
        } else {
            throw exception(GROUP_NO_CANCEL_TIMEOUT_ERROR);
        }
    }
}
