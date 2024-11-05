package com.yanzu.module.member.service.storeinfo;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.web.core.util.WebFrameworkUtils;
import com.yanzu.module.infra.api.file.FileApi;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;
import com.yanzu.module.member.controller.app.store.vo.*;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.convert.discountrules.DiscountRulesConvert;
import com.yanzu.module.member.convert.roominfo.RoomInfoConvert;
import com.yanzu.module.member.convert.storeinfo.StoreInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.storemeituaninfo.StoreMeituanInfoDO;
import com.yanzu.module.member.dal.dataobject.storesound.StoreSoundInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storemeituaninfo.StoreMeituanInfoMapper;
import com.yanzu.module.member.dal.mysql.storesound.StoreSoundInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.iot.IotDeviceService;
import com.yanzu.module.member.service.iot.IotGroupPayService;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.user.AppUserService;
import com.yanzu.module.member.service.wx.MyWxService;
import com.yanzu.module.member.service.wx.WorkWxService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.util.servlet.ServletUtils.getClientIP;
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
@Slf4j
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
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private DeviceService deviceService;

    @Resource
    private AppUserService appUserService;
    @Resource
    private ClearInfoMapper clearInfoMapper;

    @Resource
    private StoreMeituanInfoMapper storeMeituanInfoMapper;

    @Resource
    private StoreSoundInfoMapper storeSoundInfoMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;
    @Resource
    private AppOrderService appOrderService;

    @Resource
    private FileApi fileApi;

    @Resource
    private WorkWxService workWxService;

    @Resource
    private MyWxService myWxService;


    @Value("${meituan.appKey}")
    private String meituanAppKey;


    @Value("${meituan.redirectUrl}")
    private String meituanRedirectUrl;


    @Resource
    private IotGroupPayService iotGroupPayService;

    @Resource
    private IotDeviceService iotDeviceService;

    @Value("${iot.groupPay:false}")
    private boolean iotGroupPay;

    private static final String MINIAPP_IMG_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid='%s'&secret='%s'";

    @Override
    public PageResult<AppStoreAdminRespVO> getPageList(AppStoreAdminReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        IPage<AppStoreAdminRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        storeInfoMapper.getPageList(page, reqVO);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(x -> {
                String url = "https://e.dianping.com/dz-open/merchant/auth?app_key=" + meituanAppKey + "&redirect_url=" + meituanRedirectUrl + "&state=storeId-" + x.getStoreId();
                x.setMeituanScope(url);
            });
        }
        return new PageResult(page.getRecords(), page.getTotal());
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
            storeInfoDO.setStatus(1);//默认禁用
            storeInfoMapper.insert(storeInfoDO);
            //还要保存一个门店关系
            StoreUserDO storeUserDO = new StoreUserDO();
            storeUserDO.setStoreId(storeInfoDO.getStoreId());
            storeUserDO.setUserId(getLoginUserId());
            storeUserDO.setType(AppEnum.member_user_type.BOSS.getValue());
            storeUserDO.setGiftBalance(BigDecimal.valueOf(999999));
            storeUserMapper.insert(storeUserDO);
            //生成小程序码
            WxMaService wxMaService = myWxService.initWxMa();
            // 获取小程序二维码生成实例
            try {
                WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
                String path = "pages/index/index?storeId=" + storeInfoDO.getStoreId();
                byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
                String file = fileApi.createFile(bytes);
                storeInfoMapper.updateById(new StoreInfoDO().setStoreId(storeInfoDO.getStoreId()).setQrCode(file));
            } catch (WxErrorException e) {
//                throw new RuntimeException(e);
            }
        } else {
            //修改
            //校验门店权限
//            checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
            StoreInfoDO storeInfoDO = StoreInfoConvert.INSTANCE.convert3(reqVO);
            StoreInfoDO infoDO = storeInfoMapper.selectById(reqVO.getStoreId());
            if (ObjectUtils.isEmpty(infoDO.getQrCode())) {
                //生成小程序码
                WxMaService wxMaService = myWxService.initWxMa();
                // 获取小程序二维码生成实例
                try {
                    WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
                    String path = "pages/index/index?storeId=" + storeInfoDO.getStoreId();
                    byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
                    String file = fileApi.createFile(bytes);
                    storeInfoDO.setQrCode(file);
                } catch (WxErrorException e) {
                    //                throw new RuntimeException(e);
                }
            }
            storeInfoMapper.updateById(storeInfoDO);
        }
    }

    @Override
    public List<AppRoomListRespVO> getRoomInfoList(Long storeId) {
        List<AppRoomListRespVO> roomInfoList = roomInfoMapper.getRoomInfoList(storeId, getLoginUserId());
        if (!CollectionUtils.isEmpty(roomInfoList)) {
            if (deviceService.countGateway(storeId) > 0) {
                roomInfoList.forEach(x -> x.setGatewayId(1L));
            }
        }
        return roomInfoList;
    }

    @Override
    public AppRoomDetailRespVO getRoomDetail(Long roomId) {
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        return RoomInfoConvert.INSTANCE.convert2(roomInfoDO);
    }

    @Override
    @Transactional
    public void saveRoomDetail(AppRoomDetailReqVO reqVO) {
        if (ObjectUtils.isEmpty(reqVO.getTongxiaoPrice())) {
            //没有填通宵场价格  那么默认设置为单价*6个小时
            reqVO.setTongxiaoPrice(reqVO.getPrice().multiply(BigDecimal.valueOf(6)));
        }
        if (ObjectUtils.isEmpty(reqVO.getWorkPrice())) {
            reqVO.setWorkPrice(reqVO.getPrice());
        }
        if (ObjectUtils.isEmpty(reqVO.getYunlabaSound())) {
            //默认设置音量为2
            reqVO.setYunlabaSound(2);
        }
        if (!StringUtils.isEmpty(reqVO.getBanTimeStart()) && StringUtils.isEmpty(reqVO.getBanTimeEnd())) {
            //任意一个为空都不行
            throw exception(ROOM_BAN_TIME_ERROR);

        } else if (!StringUtils.isEmpty(reqVO.getBanTimeEnd()) && StringUtils.isEmpty(reqVO.getBanTimeStart())) {
            //任意一个为空都不行
            throw exception(ROOM_BAN_TIME_ERROR);
        }

        if (ObjectUtils.isEmpty(reqVO.getRoomId())) {
            //新增
            RoomInfoDO roomInfoDO = RoomInfoConvert.INSTANCE.convert3(reqVO);
            roomInfoMapper.insert(roomInfoDO);
            //门店的房间数量+1
            StoreInfoDO storeInfoDO = storeInfoMapper.selectById(reqVO.getStoreId());
            storeInfoDO.setRoomNum(storeInfoDO.getRoomNum() + 1);
            storeInfoMapper.updateById(storeInfoDO);
            //生成小程序码
            WxMaService wxMaService = myWxService.initWxMa();
            // 获取小程序二维码生成实例
            try {
                WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
                String path = "pages/orderSubmit/orderSubmit?storeId=" + reqVO.getStoreId() + "&roomId=" + roomInfoDO.getRoomId() + "&timeselectindex=0";
                byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
                String file = fileApi.createFile(bytes);
                roomInfoMapper.updateById(new RoomInfoDO().setRoomId(roomInfoDO.getRoomId()).setQrCode(file));
            } catch (WxErrorException e) {
//                throw new RuntimeException(e);
            }
            //生成续费码
            try {
                WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
                String path = "pages/roomRenew/roomRenew?storeId=" + storeInfoDO.getStoreId() + "&roomId=" + roomInfoDO.getRoomId();
                byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
                String file = fileApi.createFile(bytes);
                roomInfoMapper.updateById(new RoomInfoDO().setRoomId(roomInfoDO.getRoomId()).setRenewCode(file));
            } catch (WxErrorException e) {
//                throw new RuntimeException(e);
            }
        } else {
            //校验门店权限
            checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
            RoomInfoDO roomInfoDO = RoomInfoConvert.INSTANCE.convert3(reqVO);
            WxMaService wxMaService = myWxService.initWxMa();
            if (ObjectUtils.isEmpty(roomInfoDO.getRenewCode())) {
                //生成续费码
                try {
                    WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
                    String path = "pages/roomRenew/roomRenew?storeId=" + roomInfoDO.getStoreId() + "&roomId=" + roomInfoDO.getRoomId();
                    byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
                    String file = fileApi.createFile(bytes);
                    roomInfoMapper.updateById(new RoomInfoDO().setRoomId(roomInfoDO.getRoomId()).setRenewCode(file));
                } catch (WxErrorException e) {
//                throw new RuntimeException(e);
                }
            }
            //生成小程序码
            if (ObjectUtils.isEmpty(roomInfoDO.getQrCode())) {
                // 获取小程序二维码生成实例
                try {
                    WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
                    String path = "pages/orderSubmit/orderSubmit?storeId=" + reqVO.getStoreId() + "&roomId=" + roomInfoDO.getRoomId() + "&timeselectindex=0";
                    byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
                    String file = fileApi.createFile(bytes);
                    roomInfoDO.setQrCode(file);
                } catch (WxErrorException e) {
//                throw new RuntimeException(e);
                }
            }
            roomInfoMapper.updateById(roomInfoDO);
        }
    }


    @Override
    public PageResult<AppDiscountRulesPageRespVO> getDiscountRulesPage(AppDiscountRulesPageReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        IPage<AppDiscountRulesPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        discountRulesMapper.getDiscountRulesPage(page, reqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
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
            //如果过期时间是正常的 那么就改为正常
            if (discountRulesDO.getExpriceTime().isAfter(LocalDateTime.now())) {
                discountRulesDO.setStatus(AppEnum.discount_rules_status.ENABLE.getValue());
            }
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
        createReqVO.setMobile(createReqVO.getMobile().trim());
        //根据手机号 查询出用户
        MemberUserDO user = appUserService.getUserByMobile(createReqVO.getMobile());
        if (ObjectUtils.isEmpty(user)) {
//            throw exception(USER_NOT_EXISTS);
            //用户不存在则自动创建
            user = appUserService.createUserIfAbsent(createReqVO.getMobile(), getClientIP());
        }
        if (user.getUserType().compareTo(AppEnum.member_user_type.BOSS.getValue()) != 0) {
            appUserService.updateUserType(user.getId(), AppEnum.member_user_type.BOSS.getValue());
        }
        // 插入
        StoreInfoDO storeInfo = StoreInfoConvert.INSTANCE.convert(createReqVO);
        //后台添加的门店 默认都是简洁模式
        storeInfo.setSimpleModel(true);
        //默认1年过期
        storeInfo.setExpireTime(LocalDateTime.now().plusYears(1));
        storeInfoMapper.insert(storeInfo);
        //建立用户关系
        StoreUserDO storeUserDO = new StoreUserDO();
        storeUserDO.setStoreId(storeInfo.getStoreId());
        storeUserDO.setUserId(user.getId());
        storeUserDO.setType(AppEnum.member_user_type.BOSS.getValue());
        storeUserDO.setGiftBalance(new BigDecimal(999999));
        storeUserMapper.insert(storeUserDO);
        //生成默认房间
        List<RoomInfoDO> roomInfoDOList = new ArrayList<>(createReqVO.getRoomNum());
        for (int i = 1; i <= createReqVO.getRoomNum(); i++) {
            RoomInfoDO roomInfoDO = new RoomInfoDO();
            roomInfoDO.setRoomName("房间" + i);
            roomInfoDO.setStoreId(storeInfo.getStoreId());
            roomInfoDO.setType(AppEnum.room_type.DA.getValue());
            roomInfoDO.setPrice(BigDecimal.ONE);
            roomInfoDO.setWorkPrice(BigDecimal.ONE);
            roomInfoDO.setTongxiaoPrice(BigDecimal.TEN);
            roomInfoDOList.add(roomInfoDO);
        }
        roomInfoMapper.insertBatch(roomInfoDOList);
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
        //更新美团uuid
        if (!StringUtils.isEmpty(updateReqVO.getMeituanOpenShopUuid())) {
            storeMeituanInfoMapper.update(new StoreMeituanInfoDO().setOpenShopUuid(updateReqVO.getMeituanOpenShopUuid()), new LambdaUpdateWrapper<StoreMeituanInfoDO>().eq(StoreMeituanInfoDO::getStoreId, updateReqVO.getStoreId()));
        }
    }

    @Override
    public void deleteStoreInfo(Long id) {
        // 校验存在
        validateStoreInfoExists(id);
        // 删除
        storeInfoMapper.deleteById(id);
        //清除该门店绑定的设备
        deviceInfoMapper.deleteStoreId(id);
    }

    private void validateStoreInfoExists(Long id) {
        if (storeInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public StoreInfoRespVO getStoreInfo(Long id) {
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(id);
        StoreInfoRespVO convert = StoreInfoConvert.INSTANCE.convert(storeInfoDO);
        StoreMeituanInfoDO meituanInfoDO = storeMeituanInfoMapper.getByStoreId(id);
        if (!ObjectUtils.isEmpty(meituanInfoDO)) {
            convert.setMeituanOpenShopUuid(meituanInfoDO.getOpenShopUuid());
        }
        return convert;
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
            if (count == 0) {
                throw exception(AUTH_PROMISSION_ERROR);
            }
        }
    }

    @Override
    public List<KeyValue<Long, String>> getNameMapByIds(Set<String> storeIdSet) {
        return storeInfoMapper.getNameMapByIds(storeIdSet);
    }

    @Override
    @Transactional
    public void clearAndFinish(Long roomId) {
        //检查权限
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        checkPermisson(roomInfoDO.getStoreId(), getLoginUserId(), null, AppEnum.member_user_type.CLEAR.getValue());
        //只有状态为进行中 或 待清洁，才能处理
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.USED.getValue()) == 0) {
            //使用中  订单结束时间改为当前  房间状态改为空闲
            OrderInfoDO orderInfoDO = orderInfoMapper.getByRoomCurrent(roomId);
            if (!ObjectUtils.isEmpty(orderInfoDO)) {
                Date now = new Date();
                orderInfoDO.setEndTime(now);
                orderInfoDO.setStatus(AppEnum.order_status.FINISH.getValue());
                orderInfoMapper.updateById(orderInfoDO);
            }
        } else if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.CLEAR.getValue()) == 0) {
            //待清洁  房间状态改为空闲
        } else {
            throw exception(CLEAR_AND_FINISH_ROOM_STATUS_ERROR);
        }
        //取消掉该房间 未完成的所有保洁订单
        clearInfoMapper.cancelByRoomId(roomId);
        //刷新房间状态
        appOrderService.flushRoomStatus(roomId);
        //关电
        deviceService.closeRoomDoor(getLoginUserId(), roomInfoDO.getStoreId(), roomId, 2);
        //发通知
        workWxService.sendClearRoomMsg(roomInfoDO.getStoreId(), roomInfoDO.getRoomId(), getLoginUserId(), "设置房间空闲");
    }

    @Override
    @Transactional
    public void finishRoomOrder(Long roomId) {
        //查询出该房间正在进行中的订单
        OrderInfoDO orderInfoDO = orderInfoMapper.getByRoomCurrent(roomId);
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            orderInfoDO.setEndTime(new Date());
            orderInfoMapper.updateById(orderInfoDO);
            //发通知
            workWxService.sendClearRoomMsg(orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), getLoginUserId(), "结束订单");
        }
    }

    @Override
    public String meituanScope(Long storeId) {
        String url = "https://e.dianping.com/dz-open/merchant/auth?app_key=" + meituanAppKey + "&redirect_url=" + meituanRedirectUrl + "&state=storeId-" + storeId;
        return url;
    }

    @Override
    @Transactional
    public void disableRoom(Long roomId) {
        //检查权限
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        checkPermisson(roomInfoDO.getStoreId(), getLoginUserId(), null, AppEnum.member_user_type.ADMIN.getValue());
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.DISABLE.getValue()) == 0) {
            //改成空闲
            if (orderInfoMapper.countByRoomCurrent(roomId, null) > 0) {
                // 如果房间当前有订单进行 就改成进行中
                roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), roomId);
            } else if (clearInfoMapper.countCurrentByRoomId(roomId) > 0) {
                //如果有未完成的保洁订单 状态就是待保洁
                roomInfoMapper.updateStatusById(AppEnum.room_status.CLEAR.getValue(), roomId);
            } else if (orderInfoMapper.countByRoomId(roomId, null) > 0) {
                // 如果后面还有预约 就改成已预定
                roomInfoMapper.updateStatusById(AppEnum.room_status.PENDING.getValue(), roomId);
            } else {
                // 否则 改成空闲
                roomInfoMapper.updateStatusById(AppEnum.room_status.ENABLE.getValue(), roomId);
            }
        } else {
            //改成禁用
            roomInfoMapper.updateStatusById(AppEnum.room_status.DISABLE.getValue(), roomId);
        }
    }

    @Override
    @Transactional
    public void syncPrice(Long storeId) {
        //校验门店权限
        checkPermisson(storeId, getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        storeInfoMapper.updateById(new StoreInfoDO().setStoreId(storeId).setWorkPrice(!storeInfoDO.getWorkPrice()));
    }

    @Override
    @Transactional
    public void deleteRoomInfo(Long roomId) {
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
        //校验门店权限
        checkPermisson(storeInfoDO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        //删除房间  房间不能有未完成的订单
        int i = orderInfoMapper.countByRoomId(roomId, null);
        if (i < 0) {
            throw exception(DELETE_ROOM_ERROR);
        }
        //开始删除
        roomInfoMapper.deleteById(roomId);
        //房间数量-1
        storeInfoMapper.updateById(new StoreInfoDO().setStoreId(storeInfoDO.getStoreId()).setRoomNum(storeInfoDO.getRoomNum() - 1));
        //清除该房间的设备
        deviceService.clearByRoomId(roomId);
    }

    @Override
    public List<AppRoomInfoListRespVO> getRoomInfoList2(Long storeId) {
        List<String> storeIds = storeUserMapper.getIdsByEmploy(getLoginUserId());
        if (CollectionUtils.isEmpty(storeIds)) {
            return new ArrayList<>();
        }

        List<AppRoomInfoListRespVO> list = roomInfoMapper.getRoomInfoList2(storeIds, storeId);
        if (!CollectionUtils.isEmpty(list)) {
            //找出所有房间的订单
            List<OrderInfoDO> orderList = orderInfoMapper.getByRoomIds(list.stream().map(x -> x.getRoomId()).collect(Collectors.toList()));
            //把订单按照房间id分组
            Map<String, List<OrderInfoDO>> orederMap;
            if (!CollectionUtils.isEmpty(orderList)) {
                orederMap = orderList.stream().collect(Collectors.groupingBy(x -> String.valueOf(x.getRoomId())));
            } else {
                orederMap = new HashMap<>();
            }
            //是否存在网关
            if (deviceService.countGateway(storeId) > 0) {
                list.forEach(x -> x.setGatewayId(1L));
            }
            list.forEach(x -> {
                //如果有空调控制器，设置一下
                if (deviceService.countKongtiao(x.getRoomId()) > 0) {
                    x.setKongtiaoCount(1);
                }
                //找出该房间所有订单
                if (orederMap.containsKey(x.getRoomId().toString())) {
                    List<OrderInfoDO> sortOrder = orederMap.get(x.getRoomId().toString()).stream().sorted(Comparator.comparing(OrderInfoDO::getStartTime)).collect(Collectors.toList());
                    //把第一个订单的开始和结束时间 设置给房间
                    x.setStartTime(sortOrder.get(0).getStartTime());
                    x.setEndTime(sortOrder.get(0).getEndTime());
                }
            });
        }
        return list;

    }

    @Override
    public String getGroupPayAuthUrl(GroupPayAuthUrlReqVO reqVO) {
        if (iotGroupPay) {
            return iotGroupPayService.getScopeUrl(reqVO.getStoreId(), reqVO.getGroupPayType());
        } else {
            if (reqVO.getGroupPayType().intValue() == 1) {
                return "https://e.dianping.com/dz-open/merchant/auth?app_key=" + meituanAppKey + "&redirect_url=" + meituanRedirectUrl + "&state=storeId-" + reqVO.getStoreId();
            } else {
                return "暂不支持此平台授权";
            }
        }
    }

    @Override
    public AppStoreSoundInfoRespVO getStoreSoundInfo(Long storeId) {
        AppStoreSoundInfoRespVO storeSoundInfo = storeSoundInfoMapper.getStoreSoundInfo(storeId);
        if (ObjectUtils.isEmpty(storeSoundInfo)) {
            storeSoundInfo = new AppStoreSoundInfoRespVO();
        }
        return storeSoundInfo;
    }

    @Override
    @Transactional
    public void saveStoreSoundInfo(AppStoreSoundInfoReqVO reqVO) {
        //校验门店权限
        checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //如果已经有了，就更新  没有就新增
        StoreSoundInfoDO soundInfoDO = storeSoundInfoMapper.getByStoreId(reqVO.getStoreId());
        if (!ObjectUtils.isEmpty(soundInfoDO)) {
            BeanUtils.copyProperties(reqVO, soundInfoDO);
            storeSoundInfoMapper.updateById(soundInfoDO);
        } else {
            soundInfoDO = new StoreSoundInfoDO();
            BeanUtils.copyProperties(reqVO, soundInfoDO);
            storeSoundInfoMapper.insert(soundInfoDO);
        }
    }

    @Override
    @Transactional
    public void addDevice(AppAddDeviceReqVO reqVO) {
        //校验门店权限
        checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //如果不是共用设备  那新增的设备不能存在
        if (!reqVO.getShareDevice()) {
            int i = deviceInfoMapper.countBySN(reqVO.getDeviceSn());
            if (i > 0) {
                throw exception(DEVICE_DATA_EXISTS_ERROR);
            }
        }
        //有的设备每个房间只能存在一个
        if (!ObjectUtils.isEmpty(reqVO.getRoomId())) {
            switch (reqVO.getDeviceType()) {
                case 1:
                case 3:
                case 5:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    int c = deviceInfoMapper.countByTypeAndRoomId(reqVO.getDeviceType(), reqVO.getRoomId());
                    if (c > 0) {
                        throw exception(DEVICE_ADD_MAX_NUM_ERROR);
                    }
                    break;
            }
        }
        //先在iot平台绑定设备
        String data = iotDeviceService.bind(reqVO.getDeviceSn());
        // 插入
        DeviceInfoDO deviceInfo = new DeviceInfoDO()
                .setDeviceSn(reqVO.getDeviceSn())
                .setType(reqVO.getDeviceType())
                .setStoreId(reqVO.getStoreId())
                .setRoomId(reqVO.getRoomId())
                .setShare(reqVO.getShareDevice())
                .setDeviceData(data);
        deviceInfoMapper.insert(deviceInfo);
    }

    @Override
    @Transactional
    public void delDevice(Long deviceId) {
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(deviceId);
        if (!ObjectUtils.isEmpty(deviceInfoDO)) {
            //避免操作失误 仅允许超管删除
            checkPermisson(deviceInfoDO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
            //如果是多房间共用  只有全部删除绑定关系时才去解绑
            if (deviceInfoDO.getShare()) {
                if (deviceInfoMapper.countBySN(deviceInfoDO.getDeviceSn()) == 1) {
                    //解绑
                    iotDeviceService.unbind(deviceInfoDO.getDeviceSn());
                }
            } else {
                //解绑
                iotDeviceService.unbind(deviceInfoDO.getDeviceSn());
            }
            //删除
            deviceInfoMapper.deleteById(deviceId);
            log.info("用户:{}，删除设备:{}", getLoginUserId(), deviceId);
        }
    }

    @Override
    public void controlKT(AppStoreControlKTReqVO reqVO) {
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
        //权限校验
        checkPermisson(roomInfoDO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //执行
        deviceService.controlKT(reqVO.getCmd(), roomInfoDO.getStoreId(), roomInfoDO.getRoomId());
    }

    @Override
    @Transactional
    public void renew(StoreRenewReqVO reqVO) {
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(reqVO.getStoreId());
        if (!ObjectUtils.isEmpty(storeInfoDO)) {
            LocalDateTime newDate = null;
            if (ObjectUtils.isEmpty(storeInfoDO.getExpireTime())) {
                //没有到期时间  那么取创建时间+1年作为初始的到期时间
                newDate = storeInfoDO.getCreateTime().plusYears(1);
            } else {
                //判断与当前时间是否超过一个月  超过一个月不允许续费
                if(storeInfoDO.getExpireTime().isBefore(LocalDateTime.now().plusMonths(1))){
                    newDate = storeInfoDO.getExpireTime().plusYears(1);
                }else{
                    throw exception(STORE_RENEW_TIME_ERROR);
                }
            }
            storeInfoMapper.renew(reqVO.getStoreId(),reqVO.getStatus(),newDate);
        }
    }

    @Override
    public void addLock(AppAddLockReqVO reqVO) {
        iotDeviceService.addLock(reqVO);
    }

}
