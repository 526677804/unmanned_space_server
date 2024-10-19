package com.yanzu.module.member.service.groupPay;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoExportReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoPageReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoRespVO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.mysql.groupPay.GroupPayInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.douyin.DouyinService;
import com.yanzu.module.member.service.douyin.vo.DouyinCancelReqVO;
import com.yanzu.module.member.service.douyin.vo.DouyinPrepareRespVO;
import com.yanzu.module.member.service.iot.IotGroupPayService;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayConsumeReqVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareReqVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.wx.WorkWxService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.ErrorCodeConstants.GROUP_NO_CHECK_ERROR;

/**
 * 团购支付信息 Service 实现类
 *
 * @author MrGuan
 */
@Service
@Validated
public class GroupPayInfoServiceImpl implements GroupPayInfoService {

    @Resource
    private GroupPayInfoMapper groupPayInfoMapper;


    @Value("${iot.groupPay:false}")
    private boolean iotGroupPay;

    @Resource
    private IotGroupPayService iotGroupPayService;

    @Resource
    private DouyinService douyinService;

    @Resource
    private MeituanService meituanService;

    @Resource
    private WorkWxService workWxService;

    @Override
    public GroupPayInfoDO getGroupPayInfo(Long id) {
        return groupPayInfoMapper.selectById(id);
    }

    @Override
    public List<GroupPayInfoDO> getGroupPayInfoList(Collection<Long> ids) {
        return groupPayInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<GroupPayInfoRespVO> getGroupPayInfoPage(GroupPayInfoPageReqVO pageReqVO) {
        IPage<GroupPayInfoRespVO> page = new Page(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        groupPayInfoMapper.getPage(page, pageReqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<GroupPayInfoDO> getGroupPayInfoList(GroupPayInfoExportReqVO exportReqVO) {
        return groupPayInfoMapper.selectList(exportReqVO);
    }

    @Override
    public IotGroupPayPrepareRespVO prepare(Long storeId, String ticketNo) {
        ticketNo = ticketNo.replaceAll(" ", "");
        //因为无法区分快手券和抖音券  所以这里不传groupPayType
        boolean flag = false;
        Integer groupPayType = null;
        //美团的券码暂时没有发现超过13位
        if (ticketNo.length() < 13) {
            //一定是美团
            groupPayType = AppEnum.member_group_no_type.MEITUAN.getValue();
        } else if (ticketNo.contains("douyin")) {
            //一定是抖音
            groupPayType = AppEnum.member_group_no_type.DOUYIN.getValue();
        } else if (ticketNo.contains("ksurl")) {
            //一定是快手
            groupPayType = AppEnum.member_group_no_type.KUAISHOU.getValue();
        } else {
            //无法区分是快手还是抖音
            flag = true;
        }
        if (flag) {
            //无法区分
            //如果长度=15 券码是以24\25\26开头的  那么大概率是快手（不是100%）
            if (ticketNo.length() == 15 && (ticketNo.startsWith("24") || ticketNo.startsWith("25") || ticketNo.startsWith("26"))) {
                groupPayType = AppEnum.member_group_no_type.KUAISHOU.getValue();
                //先查快手
                if (iotGroupPay) {
                    //统一团购平台
                    try {
                        return iotGroupPayService.prepare(new IotGroupPayPrepareReqVO().setStoreId(storeId).setTicketNo(ticketNo).setGroupPayType(groupPayType));
                    } catch (Exception e) {
                        //报错了  可能是抖音   如果抖音也报错 就让他报错
                        groupPayType = AppEnum.member_group_no_type.DOUYIN.getValue();
                        return iotGroupPayService.prepare(new IotGroupPayPrepareReqVO().setStoreId(storeId).setTicketNo(ticketNo).setGroupPayType(groupPayType));
                    }
                } else {
                    //直接团购对接 没有对接快手的 尝试一下抖音吧
                    groupPayType = AppEnum.member_group_no_type.DOUYIN.getValue();
                    return douyinService.prepare(ticketNo);
                }
            } else {
                //否则大概率是抖音
                groupPayType = AppEnum.member_group_no_type.DOUYIN.getValue();
                if (iotGroupPay) {
                    try {
                        return iotGroupPayService.prepare(new IotGroupPayPrepareReqVO().setStoreId(storeId).setTicketNo(ticketNo).setGroupPayType(groupPayType));
                    } catch (Exception e) {
                        //报错了  可能是快手   如果快手也报错 就让他报错
                        groupPayType = AppEnum.member_group_no_type.KUAISHOU.getValue();
                        return iotGroupPayService.prepare(new IotGroupPayPrepareReqVO().setStoreId(storeId).setTicketNo(ticketNo).setGroupPayType(groupPayType));
                    }
                } else {
                    //因为没有对接快手   所以直接尝试抖音
                    return douyinService.prepare(ticketNo);
                }
            }
        } else {
            //说明是可以区分的
            if (iotGroupPay) {
                return iotGroupPayService.prepare(new IotGroupPayPrepareReqVO().setStoreId(storeId).setTicketNo(ticketNo).setGroupPayType(groupPayType));
            } else {
                switch (groupPayType) {
                    case 1:
                        return meituanService.prepare(storeId, ticketNo);
                    case 2:
                        return douyinService.prepare(ticketNo);
                    default:
                        throw exception(GROUP_NO_CHECK_ERROR);
                }
            }
        }
    }

    @Override
    public GroupPayInfoDO consume(Long storeId, String ticketNo, IotGroupPayPrepareRespVO vo) {
        Integer groupPayType = vo.getGroupPayType();
        //保存验券记录
        GroupPayInfoDO groupPayInfoDO = new GroupPayInfoDO();
        groupPayInfoDO.setTicketInfo(vo.getTicketInfo());
        if (iotGroupPay) {
            //统一团购平台
            iotGroupPayService.consume(new IotGroupPayConsumeReqVO()
                    .setStoreId(storeId)
                    .setGroupPayType(groupPayType)
                    .setTicketInfo(vo.getTicketInfo())
                    .setTicketNo(ticketNo));
        } else {
            switch (groupPayType) {
                case 1:
                    meituanService.consume(storeId, ticketNo, vo.getTicketInfo());
                    break;
                case 2:
                    String verify = douyinService.verify(storeId, vo.getTicketInfo());
                    groupPayInfoDO.setTicketInfo(verify);
                    break;
                default:
                    throw exception(GROUP_NO_CHECK_ERROR);
            }
        }
        groupPayInfoDO.setGroupNo(ticketNo);
        groupPayInfoDO.setStoreId(storeId);
        groupPayInfoDO.setGroupPayType(groupPayType);
        groupPayInfoDO.setGroupName(vo.getTicketName());
        groupPayInfoDO.setGroupShopId(vo.getShopId());
        //把单位从分 转化成元
        groupPayInfoDO.setGroupPayPrice(new BigDecimal(vo.getPayAmount() / 100.0));
        groupPayInfoMapper.insert(groupPayInfoDO);
        return groupPayInfoDO;
    }

    @Override
    public void revoke(Long storeId, Integer groupPayType, String ticketNo, String ticketInfo) {
        if (iotGroupPay) {
            //统一团购平台
            iotGroupPayService.revoke(new IotGroupPayConsumeReqVO()
                    .setStoreId(storeId)
                    .setGroupPayType(groupPayType)
                    .setTicketInfo(ticketInfo)
                    .setTicketNo(ticketNo));
        } else {
            switch (groupPayType) {
                case 1:
                    meituanService.reverseconsume(storeId, ticketNo, ticketInfo);
                    break;
                case 2:
                    String[] split = ticketInfo.split("&");
                    douyinService.cancel(new DouyinCancelReqVO().setVerify_id(split[0]).setCertificate_id(split[1]));
                    break;
                default:
                    throw exception(GROUP_NO_CHECK_ERROR);
            }
        }
    }

}
