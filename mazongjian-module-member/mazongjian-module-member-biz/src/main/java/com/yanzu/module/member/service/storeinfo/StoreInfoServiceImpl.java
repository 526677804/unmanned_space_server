package com.yanzu.module.member.service.storeinfo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.store.vo.*;
import com.yanzu.module.member.convert.discountrules.DiscountRulesConvert;
import com.yanzu.module.member.convert.roominfo.RoomInfoConvert;
import com.yanzu.module.member.convert.storeinfo.StoreInfoConvert;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.AUTH_PROMISSION_ERROR;
import static com.yanzu.module.member.enums.ErrorCodeConstants.OPRATION_ERROR;

/**
 * 门店管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class StoreInfoServiceImpl implements StoreInfoService {

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Override
    public PageResult<AppStoreAdminRespVO> getPageList(AppStoreAdminReqVO reqVO) {
        PageHelper.startPage(reqVO);
        List<AppStoreAdminRespVO> list = storeInfoMapper.getPageList(reqVO);
        PageInfo<AppStoreAdminRespVO> page = new PageInfo(list);
        return new PageResult(page.getList(), page.getTotal());
    }

    @Override
    public AppStoreInfoRespVO getDetail(Long storeId) {
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        return StoreInfoConvert.INSTANCE.convert2(storeInfoDO);
    }

    @Override
    @Transactional
    public void save(AppStoreInfoReqVO reqVO) {
        // 只有创建者才可以操作
        if (getLoginUserType() != 12) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        if (ObjectUtils.isEmpty(reqVO.getStoreId())) {
            //新增
            StoreInfoDO storeInfoDO = StoreInfoConvert.INSTANCE.convert(reqVO);
            storeInfoDO.setStatus(1);
            storeInfoMapper.insert(storeInfoDO);
            //还要保存一个门店关系
            StoreUserDO storeUserDO = new StoreUserDO();
            storeUserDO.setStoreId(storeInfoDO.getStoreId());
            storeUserDO.setUserId(getLoginUserId());
            storeUserDO.setType(AppEnum.store_user_type.CREATOR.getValue());
            storeUserMapper.insert(storeUserDO);
        } else {
            //修改
            //检查修改的权限 只有创建者才可以修改
            checkStorePromission(reqVO.getStoreId(), getLoginUserId(), "1");
            StoreInfoDO storeInfoDO = StoreInfoConvert.INSTANCE.convert(reqVO);
            storeInfoMapper.updateById(storeInfoDO);
        }
    }

    private void checkStorePromission(Long storeId, Long userId, String type) {
        //检查是不是管理员   1创建者 2管理员 3保洁员
        switch (type) {//type决定了允许哪些角色访问
            case "1":
                type = "1";
                break;
            case "2":
                type = "1,2";
                break;
            case "3":
                type = "1,2,3";
                break;
        }
        Long id = storeUserMapper.checkStorePromission(storeId, userId, type);
        if (ObjectUtils.isEmpty(id)) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
    }

    @Override
    public List<AppRoomListRespVO> getRoomInfoList(Long storeId) {
        return roomInfoMapper.getRoomInfoList(storeId);
    }

    @Override
    public AppRoomDetailRespVO getRoomDetail(Long roomId) {
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        return RoomInfoConvert.INSTANCE.convert2(roomInfoDO);
    }

    @Override
    @Transactional
    public void saveRoomDetail(AppRoomDetailReqVO reqVO) {
        if (ObjectUtils.isEmpty(reqVO.getRoomId())) {
            //新增
            RoomInfoDO roomInfoDO = RoomInfoConvert.INSTANCE.convert(reqVO);
            roomInfoMapper.insert(roomInfoDO);
        } else {
            //修改 只有所有者才可以修改
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
            checkStorePromission(roomInfoDO.getStoreId(), getLoginUserId(), "1");
        }

    }


    @Override
    public PageResult<AppDiscountRulesPageRespVO> getDiscountRulesPage(AppDiscountRulesPageReqVO reqVO) {
        PageHelper.startPage(reqVO);
        List<AppDiscountRulesPageRespVO> list = discountRulesMapper.getDiscountRulesPage(reqVO);
        PageInfo page = new PageInfo(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    @Transactional
    public void changeDiscountRulesStatus(Long id) {
        //检查修改的权限 只有创建者才可以修改充值优惠规则
        DiscountRulesDO discountRulesDO = discountRulesMapper.selectById(id);
        checkStorePromission(discountRulesDO.getStoreId(), getLoginUserId(), "1");
        if (discountRulesDO.getStatus().compareTo(AppEnum.discount_rules_status.ENABLE.getValue()) == 0) {
            //改成禁用
            discountRulesMapper.changeDiscountRulesStatus(id, 0);
        } else if (discountRulesDO.getStatus().compareTo(AppEnum.discount_rules_status.DISABLE.getValue()) == 0) {
            //改成启用
            discountRulesMapper.changeDiscountRulesStatus(id, 1);
        } else if (discountRulesDO.getStatus().compareTo(AppEnum.discount_rules_status.EXPIRE.getValue()) == 0) {
            //过期  不允许修改
            throw exception(OPRATION_ERROR);
        }
    }

    @Override
    public AppDiscountRulesDetailRespVO getDiscountRuleDetail(Long id) {
        DiscountRulesDO discountRulesDO = discountRulesMapper.selectById(id);
        return DiscountRulesConvert.INSTANCE.convert2(discountRulesDO);
    }

    @Override
    @Transactional
    public void saveDiscountRuleDetail(AppDiscountRulesDetailReqVO reqVO) {
        //检查修改的权限 只有创建者才可以修改充值优惠规则
        DiscountRulesDO discountRulesDO = discountRulesMapper.selectById(reqVO.getId());
        checkStorePromission(reqVO.getStoreId(), getLoginUserId(), "1");
        discountRulesMapper.updateById(DiscountRulesConvert.INSTANCE.convert2(reqVO));
    }

}
