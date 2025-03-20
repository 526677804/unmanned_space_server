package com.yanzu.module.member.service.storevipconfig;

import com.yanzu.module.member.controller.app.store.vo.AppAddMemberVipReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppEditMemberVipReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigListRespVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigSaveReqVO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.storevipconfig.StoreVipConfigDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import com.yanzu.module.member.service.user.AppUserService;
import org.springframework.beans.BeanUtils;
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
import static com.yanzu.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

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
    @Lazy
    private AppUserService appUserService;

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
    public void saveVipConfig(AppStoreVipConfigSaveReqVO reqVO) {
        //权限检查
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        //判断新增还是修改
        StoreVipConfigDO storeVipConfigDO = null;
        List<StoreVipConfigDO> list = storeVipConfigMapper.selectbyStoreId(reqVO.getStoreId());
        if (!ObjectUtils.isEmpty(reqVO.getVipId())) {
            //修改
            storeVipConfigDO = storeVipConfigMapper.selectById(reqVO.getVipId());
            if (ObjectUtils.isEmpty(storeVipConfigDO) || storeVipConfigDO.getStoreId().compareTo(reqVO.getStoreId()) != 0) {
                throw exception(OPRATION_ERROR);
            }
            //检查参数  不能存在相同的积分门槛
            list.forEach(x -> {
                if (x.getScore().compareTo(reqVO.getScore()) == 0 && x.getVipId().compareTo(reqVO.getVipId()) != 0) {
                    throw exception(STORE_VIP_CONFIG_SCORE_ERROR);
                }
            });
            BeanUtils.copyProperties(reqVO, storeVipConfigDO);
            storeVipConfigMapper.updateById(storeVipConfigDO);
        } else {
            //新增
            storeVipConfigDO = new StoreVipConfigDO();
            BeanUtils.copyProperties(reqVO, storeVipConfigDO);
            int count = 0;
            if (!CollectionUtils.isEmpty(list)) {
                count = list.size();
            }
            //暂时只支持最多三个等级
            if (count >= 3) {
                throw exception(STORE_VIP_CONFIG_MAX_ERROR);
            }
            //检查参数  不能存在相同的积分门槛
            list.forEach(x -> {
                if (x.getScore().compareTo(reqVO.getScore()) == 0) {
                    throw exception(STORE_VIP_CONFIG_SCORE_ERROR);
                }
            });
            count++;//会员等级是从1开始
            storeVipConfigDO.setVipLevel(Byte.valueOf(count + ""));
            storeVipConfigMapper.insert(storeVipConfigDO);
        }
    }

    @Override
    @Transactional
    public void editMemberVip(AppEditMemberVipReqVO reqVO) {
        //权限检查
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //先查出用户
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(reqVO.getUserId(), reqVO.getStoreId());
        if (ObjectUtils.isEmpty(storeUserDO)) {
            //直接新增
            storeUserDO = new StoreUserDO()
                    .setStoreId(reqVO.getStoreId())
                    .setUserId(reqVO.getUserId())
                    .setType(AppEnum.member_user_type.MEMBER.getValue())
                    .setVipLevel(reqVO.getVipLevel());
            storeUserMapper.insert(storeUserDO);
        } else {
            //修改
            storeUserDO.setVipLevel(reqVO.getVipLevel());
            storeUserMapper.updateById(storeUserDO);
        }
    }

    @Override
    @Transactional
    public void addMemberVip(AppAddMemberVipReqVO reqVO) {
        //权限检查
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //先查出用户
        MemberUserDO userByMobile = appUserService.getUserByMobile(reqVO.getMobile().trim());
        if (ObjectUtils.isEmpty(userByMobile)) {
            //新增用户
            userByMobile = appUserService.createUserIfAbsent(reqVO.getMobile().trim(), getClientIP());
        }
        //先查出用户
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(userByMobile.getId(), reqVO.getStoreId());
        if (ObjectUtils.isEmpty(storeUserDO)) {
            //直接新增
            storeUserDO = new StoreUserDO()
                    .setStoreId(reqVO.getStoreId())
                    .setUserId(userByMobile.getId())
                    .setType(AppEnum.member_user_type.MEMBER.getValue())
                    .setVipLevel(reqVO.getVipLevel());
            storeUserMapper.insert(storeUserDO);
        } else {
            //修改
            storeUserDO.setVipLevel(reqVO.getVipLevel());
            storeUserMapper.updateById(storeUserDO);
        }
    }
}
