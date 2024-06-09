package com.yanzu.module.member.service.game;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoReqVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoRespVO;
import com.yanzu.module.member.controller.app.game.vo.AppGamePageReqVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameUserListRespVO;
import com.yanzu.module.member.dal.dataobject.gameinfo.GameInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;
import com.yanzu.module.member.dal.mysql.gameinfo.GameInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.wx.WorkWxService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

@Service
@Validated
public class AppGameServiceImpl implements AppGameService {


    @Autowired
    private WorkWxService workWxService;

    @Resource
    private GameInfoMapper gameInfoMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Override
    @Transactional
    public void save(AppGameInfoReqVO reqVO) {
        Long loginUserId = getLoginUserId();
        //检查限制  普通用户一天最多发5场  管理员不受限制
        if (getLoginUserType().compareTo(AppEnum.member_user_type.BOSS.getValue()) == 0 || getLoginUserType().compareTo(AppEnum.member_user_type.BOSS.getValue()) == 0) {
            //管理员 暂时不做限制
        } else {
            //普通用户
            int count = gameInfoMapper.countDayByUserId(loginUserId);
            if (count >= 5) {
                throw exception(GAME_CREATE_NUM_MAX_ERROR);
            }
        }
        //开始的时间不能小于当前的时间
        if (reqVO.getStartTime().before(new Date())) {
            throw exception(GAME_START_TIME_ERROR);
        }
        //开始的时间与结束时间必须大于4小时
        long l = (reqVO.getEndTime().getTime() - reqVO.getStartTime().getTime()) / 1000 / 60;
        if (l < 240) {
            throw exception(ORDER_TIME_MIN_ERROR);
        }
        //这里只有新增  暂时不检查选的时间与订单冲突的问题 todo..
        GameInfoDO gameInfoDO = new GameInfoDO();
        BeanUtils.copyProperties(reqVO, gameInfoDO);
        gameInfoDO.setUserId(loginUserId);
        gameInfoDO.setPlayUserIds(String.valueOf(loginUserId));
        gameInfoMapper.insert(gameInfoDO);
        //异步发送微信通知
        sendGameMsgtoWx(loginUserId, reqVO, 1);
    }

    @Async
    protected void sendGameMsgtoWx(Long userId, AppGameInfoReqVO reqVO, Integer userNum) {
        AppUserDO appUserDO = appUserMapper.selectById(userId);
        StringBuffer sb = new StringBuffer();
        sb.append("在线组局信息\n");
//        sb.append(">门店名称:<font color=\"warning\">").append(appUserDO.getNickname()).append("</font>\n");
        sb.append(">发起用户:<font color=\"warning\">").append(appUserDO.getNickname()).append("</font>\n");
        sb.append(">玩法规则:<font color=\"warning\">").append(reqVO.getRuleDesc()).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(reqVO.getStartTime(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">玩家人数:<font color=\"warning\">").append(reqVO.getUserNum()).append("</font>人\n");
        sb.append(">当前人数:<font color=\"warning\">").append(userNum).append("</font>人");

        workWxService.sendGameMsg(reqVO.getStoreId(), sb.toString());
    }

    @Override
    public PageResult<AppGameInfoRespVO> getOrderPage(AppGamePageReqVO reqVO) {
        reqVO.setCurrentUserId(getLoginUserId());
        IPage<AppGameInfoRespVO> page=new Page<>(reqVO.getPageNo(),reqVO.getPageSize());
        gameInfoMapper.getOrderPage(page,reqVO);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            //取出所有玩家
            String playUserIds = page.getRecords().stream().map(x -> x.getPlayUserIds()).collect(Collectors.joining(","));
            //查询出这些人的信息
            List<AppGameUserListRespVO> userListRespVOList = appUserMapper.getInfoByUserIds(playUserIds);
            //转map
            Map<String, AppGameUserListRespVO> userMap = userListRespVOList.stream().collect(Collectors.toMap(x -> String.valueOf(x.getUserId()), Function.identity()));
            //填充数据
            for (AppGameInfoRespVO appGameInfoRespVO : page.getRecords()) {
                List<AppGameUserListRespVO> playUserList = new ArrayList<>(4);
                String[] split = appGameInfoRespVO.getPlayUserIds().split(",");
                for (String s : split) {
                    playUserList.add(userMap.get(s));
                }
                appGameInfoRespVO.setPlayUserList(playUserList);
            }
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    @Transactional
    public void deleteUser(Long gameId, Long userId) {
        GameInfoDO gameInfoDO = gameInfoMapper.selectById(gameId);
        //房主才能踢出用户
        if (gameInfoDO.getUserId().compareTo(getLoginUserId()) == 0) {
            //不能踢出自己
            if (userId.compareTo(getLoginUserId()) == 0) {
                throw exception(GAME_DELETE_ME_ERROR);
            }
            //只有组局中和已组局的状态（未支付）才能踢出
            if (gameInfoDO.getStatus().compareTo(AppEnum.game_status.PROGRESS.getValue()) == 0 || gameInfoDO.getStatus().compareTo(AppEnum.game_status.SUCCESS.getValue()) == 0) {
                List<String> strings = new ArrayList<>(Arrays.asList(gameInfoDO.getPlayUserIds().split(",")));
                if (strings.contains(String.valueOf(userId))) {
                    strings.remove(String.valueOf(userId));
                    gameInfoDO.setPlayUserIds(strings.stream().collect(Collectors.joining(",")));
                    //如果人本来是满的，那就把状态改回组局中
                    if (gameInfoDO.getStatus().compareTo(AppEnum.game_status.SUCCESS.getValue()) == 0) {
                        gameInfoDO.setStatus(AppEnum.game_status.PROGRESS.getValue());
                    }
                    gameInfoMapper.updateById(gameInfoDO);
                    //todo发送微信通知
                }
            } else {
                throw exception(GAME_DELETE_USER_ERROR);
            }
        } else {
            throw exception(OPRATION_ERROR);
        }
    }

    @Override
    @Transactional
    public void join(Long gameId) {
        Long loginUserId = getLoginUserId();
        GameInfoDO gameInfoDO = gameInfoMapper.selectById(gameId);
        List<String> strings = new ArrayList<>(Arrays.asList(gameInfoDO.getPlayUserIds().split(",")));
        //只有组局中和已组局的状态（未支付）才能加入或退出
        if (gameInfoDO.getStatus().compareTo(AppEnum.game_status.PROGRESS.getValue()) == 0 || gameInfoDO.getStatus().compareTo(AppEnum.game_status.SUCCESS.getValue()) == 0) {
            //判断在不在对局里面存在
            if (strings.contains(String.valueOf(loginUserId))) {
                //已存在对局中  那么需要执行的是退出操作  判断是不是房主
                if (gameInfoDO.getUserId().compareTo(loginUserId) == 0) {
                    //是房主 直接解散该对局
                    gameInfoDO.setStatus(AppEnum.game_status.CANCEL.getValue());
                    gameInfoMapper.updateById(gameInfoDO);
                    //todo发送微信通知
                } else {
                    //不是房主  直接退出
                    strings.remove(String.valueOf(loginUserId));
                    gameInfoDO.setPlayUserIds(strings.stream().collect(Collectors.joining(",")));
                    //如果人本来是满的，那就把状态改回组局中
                    if (gameInfoDO.getStatus().compareTo(AppEnum.game_status.SUCCESS.getValue()) == 0) {
                        gameInfoDO.setStatus(AppEnum.game_status.PROGRESS.getValue());
                    }
                    gameInfoMapper.updateById(gameInfoDO);
                    //todo发送微信通知
                }
            } else {
                //不存在对局中 执行的是加入操作 判断人数有没有满
                if (gameInfoDO.getUserNum().intValue() > strings.size()) {
                    strings.add(String.valueOf(loginUserId));
                    gameInfoDO.setPlayUserIds(strings.stream().collect(Collectors.joining(",")));
                    //加入后如果人满了，那就把状态改回组局完成
                    if (gameInfoDO.getUserNum().intValue() == strings.size()) {
                        gameInfoDO.setStatus(AppEnum.game_status.SUCCESS.getValue());
                    }
                    gameInfoMapper.updateById(gameInfoDO);
                    //todo发送微信通知
                    sendGameJoinMsgtoWx(loginUserId,gameInfoDO);
                } else {
                    throw exception(GAME_MAX_USER_ERROR);
                }
            }
        } else {
            throw exception(GAME_JOIN_USER_ERROR);
        }
    }

    @Async
    protected void sendGameJoinMsgtoWx(Long userId, GameInfoDO gameInfoDO) {
        int userNum = gameInfoDO.getPlayUserIds().split(",").length;
        AppUserDO appUserDO = appUserMapper.selectById(userId);
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(gameInfoDO.getRoomId());
        StringBuffer sb = new StringBuffer();
        sb.append("用户:").append(appUserDO.getNickname()).append("加入了组局！\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomInfoDO.getRoomName()).append("</font>\n");
        sb.append(">玩法规则:<font color=\"warning\">").append(gameInfoDO.getRuleDesc()).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(gameInfoDO.getStartTime(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">玩家人数:<font color=\"warning\">").append(gameInfoDO.getUserNum()).append("</font>人\n");
        sb.append(">当前人数:<font color=\"warning\">").append(userNum).append("</font>人");
        if(userNum==gameInfoDO.getUserNum()){
            sb.append("组局已成功！请及时预订该消费时段，祝您玩的开心~");
        }
        workWxService.sendGameMsg(gameInfoDO.getStoreId(), sb.toString());
    }

}
