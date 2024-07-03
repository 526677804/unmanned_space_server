package com.yanzu.module.member.service.member;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.profitsharing.ProfitSharingReceiverRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.wxpay.vo.*;
import com.yanzu.module.member.convert.member.StoreWxpayConfigConvert;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.dal.mysql.member.StoreWxpayConfigMapper;
import com.yanzu.module.member.service.wx.MyWxService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 门店微信支付配置 Service 实现类
 *
 * @author MrGuan
 */
@Service
@Validated
public class StoreWxpayConfigServiceImpl implements StoreWxpayConfigService {

    @Resource
    private StoreWxpayConfigMapper storeWxpayConfigMapper;

    @Resource
    private MyWxService myWxService;


    @Value("${wx.pay.splitMchId}")
    private String splitMchId;

    @Value("${wx.pay.splitMchName}")
    private String splitMchName;

    @Override
    @Transactional
    public Long createStoreWxpayConfig(StoreWxpayConfigCreateReqVO createReqVO) {
        // 插入
        StoreWxpayConfigDO storeWxpayConfig = StoreWxpayConfigConvert.INSTANCE.convert(createReqVO);
        //参数判断
        if (createReqVO.getServiceModel()) {
            //服务商模式
        } else {
            //商户模式  支付密钥和证书必须上传
            if (ObjectUtils.isEmpty(createReqVO.getMchKey()) || ObjectUtils.isEmpty(createReqVO.getP12())) {
                throw exception(WXPAY_CONFIG_PARAM_ERROR);
            }
        }
        storeWxpayConfigMapper.insert(storeWxpayConfig);
        // 返回
        return storeWxpayConfig.getId();
    }

    @Override
    @Transactional
    public void updateStoreWxpayConfig(StoreWxpayConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreWxpayConfigExists(updateReqVO.getId());
        // 更新
        StoreWxpayConfigDO updateObj = StoreWxpayConfigConvert.INSTANCE.convert(updateReqVO);
        //参数判断
        if (updateReqVO.getServiceModel()) {
            //服务商模式
        } else {
            //商户模式  支付密钥和证书必须上传
            if (ObjectUtils.isEmpty(updateReqVO.getMchKey()) || ObjectUtils.isEmpty(updateReqVO.getP12())) {
                throw exception(WXPAY_CONFIG_PARAM_ERROR);
            }
        }
        storeWxpayConfigMapper.updateById(updateObj);
    }

    @Override
    @Transactional
    public void deleteStoreWxpayConfig(Long id) {
        // 校验存在
        validateStoreWxpayConfigExists(id);
        // 删除
        storeWxpayConfigMapper.deleteById(id);
    }

    private void validateStoreWxpayConfigExists(Long id) {
        if (storeWxpayConfigMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public StoreWxpayConfigDO getStoreWxpayConfig(Long id) {
        return storeWxpayConfigMapper.selectById(id);
    }

    @Override
    public List<StoreWxpayConfigDO> getStoreWxpayConfigList(Collection<Long> ids) {
        return storeWxpayConfigMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreWxpayConfigPageRespVO> getStoreWxpayConfigPage(StoreWxpayConfigPageReqVO reqVO) {
        IPage<StoreWxpayConfigPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        storeWxpayConfigMapper.getStoreWxpayConfigPage(page, reqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<StoreWxpayConfigDO> getStoreWxpayConfigList(StoreWxpayConfigExportReqVO exportReqVO) {
        return storeWxpayConfigMapper.selectList(exportReqVO);
    }

    @Override
    @SneakyThrows
    public void profitsharing(Long id) {
        StoreWxpayConfigDO configDO = storeWxpayConfigMapper.selectById(id);
        if (configDO.getSplit()) {
            ProfitSharingReceiverRequest request = new ProfitSharingReceiverRequest();
//        示例值：{
//            "type": "MERCHANT_ID",
//                    "account": "190001001",
//                    "name": "示例商户全称",
//                    "relation_type": "STORE_OWNER"
//        }
            JSONObject json = new JSONObject();
            json.put("type", "MERCHANT_ID");
            json.put("account", splitMchId);
            json.put("name", splitMchName);
            json.put("relation_type", "SERVICE_PROVIDER");
            request.setReceiver(json.toJSONString());
            WxPayService wxPayService = myWxService.initWxPay(configDO.getStoreId());
            try {
                wxPayService.getProfitSharingService().addReceiver(request);
            } catch (WxPayException e) {
                throw exception(ADMIN_WEIXIN_PAY_SPLIT_ERROR);
//                throw new RuntimeException(e);
            }
        } else {
            throw exception(ADMIN_WEIXIN_PAY_SPLIT_ERROR);
        }
    }

}
