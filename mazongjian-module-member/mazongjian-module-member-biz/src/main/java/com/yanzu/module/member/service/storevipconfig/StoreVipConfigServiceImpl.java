package com.yanzu.module.member.service.storevipconfig;

import com.yanzu.module.member.controller.app.store.vo.AppEditMemberVipReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigListRespVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigSaveReqVO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.storevipconfig.StoreVipConfigDO;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import com.yanzu.module.member.dal.mysql.storevipconfig.StoreVipConfigMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.STORE_VIP_CONFIG_MAX_ERROR;
import static com.yanzu.module.member.enums.ErrorCodeConstants.STORE_VIP_CONFIG_SCORE_ERROR;

/**
 * 门店会员配置 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class StoreVipConfigServiceImpl implements StoreVipConfigService {

    @Resource
    private StoreVipConfigMapper storeVipConfigMapper;

    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    @Lazy//避免循环依赖报错
    private StoreInfoService storeInfoService;

    @Override
    public List<AppStoreVipConfigListRespVO> getVipConfig(Long storeId) {
        //权限检查
        storeInfoService.checkPermisson(storeId, getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        return storeVipConfigMapper.getVipConfig(storeId);
    }

    @Override
    @Transactional
    public void saveVipConfig(List<AppStoreVipConfigSaveReqVO> reqVO, Long storeId) {
        //权限检查
        storeInfoService.checkPermisson(storeId, getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        //暂时只支持最多三个等级
        if (reqVO.size() > 3) {
            throw exception(STORE_VIP_CONFIG_MAX_ERROR);
        }
        //检查参数  不能存在相同的积分门槛
        boolean hasDuplicate = reqVO.stream()
                .map(AppStoreVipConfigSaveReqVO::getScore) // 获取每个对象的 score 属性
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // 根据 score 计数
                .values().stream() // 取出所有计数
                .anyMatch(count -> count > 1); // 如果有任何一个值的计数大于1，说明有重复
        if (hasDuplicate) {
            throw exception(STORE_VIP_CONFIG_SCORE_ERROR);
        }
        reqVO.sort((o1, o2) -> {
            return o1.getScore().compareTo(o2.getScore());
        });
        List<StoreVipConfigDO> saveList = new ArrayList<>(reqVO.size());
        for (byte i = 1; i <= reqVO.size(); i++) {
            StoreVipConfigDO vipConfigDO = new StoreVipConfigDO()
                    .setStoreId(storeId)
                    .setVipLevel(i)
                    .setVipDiscount(reqVO.get(i).getVipDiscount())
                    .setVipName(reqVO.get(i).getVipName())
                    .setScore(reqVO.get(i).getScore());
            saveList.add(vipConfigDO);
        }
        if (!CollectionUtils.isEmpty(saveList)) {
            //先删除
            storeVipConfigMapper.deleteByStoreId(storeId);
            //再新增
            storeVipConfigMapper.insertBatch(saveList);
        }
    }

    @Override
    @Transactional
    public void editMemberVip(AppEditMemberVipReqVO reqVO) {
        //权限检查
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //先查出用户
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(reqVO.getUserId(), reqVO.getStoreId());
        if(ObjectUtils.isEmpty(storeUserDO)){
            //直接新增
            storeUserDO =new StoreUserDO()
                    .setStoreId(reqVO.getStoreId())
                    .setUserId(reqVO.getUserId())
                    .setType(AppEnum.member_user_type.MEMBER.getValue())
                    .setVipLevel(reqVO.getVipLevel());
            storeUserMapper.insert(storeUserDO);
        }else{
            //修改
            storeUserDO.setVipLevel(reqVO.getVipLevel());
            storeUserMapper.updateById(storeUserDO);
        }
    }
}
