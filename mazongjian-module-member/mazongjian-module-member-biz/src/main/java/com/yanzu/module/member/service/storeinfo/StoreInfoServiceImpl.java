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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
        if (ObjectUtils.isEmpty(reqVO.getStoreId())) {
            // 校验用户类型
            checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
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
            //校验门店权限
            checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
            StoreInfoDO storeInfoDO = StoreInfoConvert.INSTANCE.convert3(reqVO);
            storeInfoMapper.updateById(storeInfoDO);
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
            //校验门店权限
            checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
            roomInfoDO.setRoomName(reqVO.getRoomName());
            roomInfoDO.setType(reqVO.getType());
            roomInfoDO.setPrice(reqVO.getPrice());
            roomInfoDO.setLabel(reqVO.getLabel());
            roomInfoDO.setImageUrls(reqVO.getImageUrls());
            roomInfoDO.setStoreId(reqVO.getStoreId());
            roomInfoDO.setBanTimeStart(reqVO.getBanTimeStart());
            roomInfoDO.setBanTimeEnd(reqVO.getBanTimeEnd());
            roomInfoDO.setSortId(reqVO.getSortId());
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
        //校验门店权限
        checkPermisson(discountRulesDO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
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
        //校验门店权限
        checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        if (ObjectUtils.isEmpty(reqVO.getDiscountId())) {
            //新增
            //如果本门店已有相同的充值支付金额，则不允许再添加
            int count = discountRulesMapper.countByStoreIdAndPayMoney(reqVO.getStoreId(), reqVO.getPayMoney(), null);
            if (count > 0) {
                throw exception(DISCOUNTRULE_REPETITION_ERROR);
            }
            DiscountRulesDO discountRulesDO = DiscountRulesConvert.INSTANCE.convert2(reqVO);
            discountRulesMapper.insert(discountRulesDO);
        } else {
            //修改 校验金额重复时要排除当前门店
            int count = discountRulesMapper.countByStoreIdAndPayMoney(reqVO.getStoreId(), reqVO.getPayMoney(), reqVO.getDiscountId());
            if (count > 0) {
                throw exception(DISCOUNTRULE_REPETITION_ERROR);
            }
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

    @Override
    public void checkPermisson(Long storeId, Long userId, Integer userType, Integer checkType) {
        // 12加盟商 13管理员 14保洁员
        if (ObjectUtils.isEmpty(storeId)) {
            //不存在门店Id  仅判断用户类型
            if (userType > checkType) {
                throw exception(AUTH_PROMISSION_ERROR);
            }
        } else {
            //存在门店id  需要根据门店来校验
            String type = "";
            switch (checkType) {
                case 12:
                    type = "12";
                    break;
                case 13:
                    type = "12,13";
                    break;
                case 14:
                    type = "12,13,14";
                    break;
            }
            Integer count = storeUserMapper.checkStorePromission(storeId, userId, type);
            if (count < 1) {
                throw exception(AUTH_PROMISSION_ERROR);
            }
        }
    }

    @Override
    public List<KeyValue<Long, String>> getNameMapByIds(Set<String> storeIdSet) {
        return storeInfoMapper.getNameMapByIds(storeIdSet);
    }

}
