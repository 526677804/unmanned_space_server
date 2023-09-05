package com.yanzu.module.member.service.storeinfo;

import cn.hutool.core.io.IoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.web.core.util.WebFrameworkUtils;
import com.yanzu.module.infra.api.file.FileApi;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoCreateReqVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoExportReqVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoPageReqVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoUpdateReqVO;
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
import com.yanzu.module.member.utils.StorePermissionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

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
    @Resource
    private FileApi fileApi;

    @Override
    public PageResult<AppStoreAdminRespVO> getPageList(AppStoreAdminReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        PageHelper.startPage(reqVO);
        List<AppStoreAdminRespVO> list = storeInfoMapper.getPageList(reqVO);
        PageInfo<AppStoreAdminRespVO> page = new PageInfo(list);
        return new PageResult(page.getList(), page.getTotal());
    }

    @Override
    public AppStoreInfoRespVO getDetail(Long storeId) {
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        AppStoreInfoRespVO appStoreInfoRespVO = StoreInfoConvert.INSTANCE.convert2(storeInfoDO);

        return appStoreInfoRespVO;
    }

    @Override
    @Transactional
    public void save(AppStoreInfoReqVO reqVO) {
        // 只有创建者才可以操作
        StorePermissionUtils.checkBoss(getLoginUserType());
        if (ObjectUtils.isEmpty(reqVO.getStoreId())) {
            //新增
            StoreInfoDO storeInfoDO = StoreInfoConvert.INSTANCE.convert3(reqVO);
            storeInfoDO.setStatus(1);
            storeInfoMapper.insert(storeInfoDO);
            //还要保存一个门店关系
            StoreUserDO storeUserDO = new StoreUserDO();
            storeUserDO.setStoreId(storeInfoDO.getStoreId());
            storeUserDO.setUserId(getLoginUserId());
            storeUserDO.setType(AppEnum.member_user_type.BOSS.getValue());
            storeUserMapper.insert(storeUserDO);
        } else {
            //修改
            //检查修改的权限 只有创建者才可以修改
            checkStorePromission(reqVO.getStoreId(), getLoginUserId(), "1");
            StoreInfoDO storeInfoDO = StoreInfoConvert.INSTANCE.convert3(reqVO);
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
        return roomInfoMapper.getRoomInfoList(storeId, getLoginUserId());
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
            RoomInfoDO roomInfoDO = RoomInfoConvert.INSTANCE.convert3(reqVO);
            roomInfoMapper.insert(roomInfoDO);
            //门店的房间数量+1
            StoreInfoDO storeInfoDO = storeInfoMapper.selectById(reqVO.getStoreId());
            storeInfoDO.setRoomNum(storeInfoDO.getRoomNum() + 1);
            storeInfoMapper.updateById(storeInfoDO);
        } else {
            //修改 只有所有者才可以修改
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
            checkStorePromission(roomInfoDO.getStoreId(), getLoginUserId(), "1");
            roomInfoDO.setRoomName(reqVO.getRoomName());
            roomInfoDO.setType(reqVO.getType());
            roomInfoDO.setPrice(reqVO.getPrice());
            roomInfoDO.setLabel(reqVO.getLabel());
            roomInfoDO.setImageUrls(reqVO.getImageUrls());
            roomInfoDO.setStoreId(reqVO.getStoreId());
            roomInfoDO.setBanTimeStart(reqVO.getBanTimeStart());
            roomInfoDO.setBanTimeEnd(reqVO.getBanTimeEnd());
            roomInfoMapper.updateById(roomInfoDO);
        }

    }


    @Override
    public PageResult<AppDiscountRulesPageRespVO> getDiscountRulesPage(AppDiscountRulesPageReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
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
//        DiscountRulesDO discountRulesDO = discountRulesMapper.selectById(reqVO.getId());
        checkStorePromission(reqVO.getStoreId(), getLoginUserId(), "1");
        //如果已有相同的充值支付金额，则不允许再添加
        int count = discountRulesMapper.countByStoreIdAndPayMoney(reqVO.getStoreId(), reqVO.getPayMoney());
        if (count > 0) {
            throw exception(DISCOUNTRULE_REPETITION_ERROR);
        }
        if (ObjectUtils.isEmpty(reqVO.getDiscountId())) {
            //新增
            DiscountRulesDO discountRulesDO = DiscountRulesConvert.INSTANCE.convert2(reqVO);
            discountRulesMapper.insert(discountRulesDO);
        } else {
            DiscountRulesDO discountRulesDO = DiscountRulesConvert.INSTANCE.convert2(reqVO);
            discountRulesDO.setStoreId(reqVO.getStoreId());
            discountRulesDO.setPayMoney(reqVO.getPayMoney());
            discountRulesDO.setGiftMoney(reqVO.getGiftMoney());
            discountRulesMapper.updateById(discountRulesDO);
        }
    }

    @Override
    public String uploadImg(InputStream inputStream) {
        // 创建文件
        return fileApi.createFile(IoUtil.readBytes(inputStream));
    }

    @Override
    public Long createStoreInfo(StoreInfoCreateReqVO createReqVO) {
        // 插入
        StoreInfoDO storeInfo = StoreInfoConvert.INSTANCE.convert(createReqVO);
        storeInfoMapper.insert(storeInfo);
        // 返回
        return storeInfo.getStoreId();
    }

    @Override
    public void updateStoreInfo(StoreInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreInfoExists(updateReqVO.getStoreId());
        // 更新
        StoreInfoDO updateObj = StoreInfoConvert.INSTANCE.convert(updateReqVO);
        storeInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreInfo(Long id) {
        // 校验存在
        validateStoreInfoExists(id);
        // 删除
        storeInfoMapper.deleteById(id);
    }

    private void validateStoreInfoExists(Long id) {
        if (storeInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public StoreInfoDO getStoreInfo(Long id) {
        return storeInfoMapper.selectById(id);
    }

    @Override
    public List<StoreInfoDO> getStoreInfoList(Collection<Long> ids) {
        return storeInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreInfoDO> getStoreInfoPage(StoreInfoPageReqVO pageReqVO) {
        return storeInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StoreInfoDO> getStoreInfoList(StoreInfoExportReqVO exportReqVO) {
        return storeInfoMapper.selectList(exportReqVO);
    }

    @Override
    public List<KeyValue<String, Long>> getStoreList(String name, String cityName) {
        return storeInfoMapper.getStoreList(name, cityName, WebFrameworkUtils.getLoginUserId());
    }

    @Override
    public List<KeyValue<String, Long>> getRoomList(Long storeId) {
        return roomInfoMapper.getRoomList(storeId, WebFrameworkUtils.getLoginUserId());
    }

}
