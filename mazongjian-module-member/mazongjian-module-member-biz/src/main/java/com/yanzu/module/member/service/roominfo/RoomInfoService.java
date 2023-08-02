package com.yanzu.module.member.service.roominfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.roominfo.vo.*;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 房间管理 Service 接口
 *
 * @author 芋道源码
 */
public interface RoomInfoService {

    /**
     * 创建房间管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRoomInfo(@Valid RoomInfoCreateReqVO createReqVO);

    /**
     * 更新房间管理
     *
     * @param updateReqVO 更新信息
     */
    void updateRoomInfo(@Valid RoomInfoUpdateReqVO updateReqVO);

    /**
     * 删除房间管理
     *
     * @param id 编号
     */
    void deleteRoomInfo(Long id);

    /**
     * 获得房间管理
     *
     * @param id 编号
     * @return 房间管理
     */
    RoomInfoDO getRoomInfo(Long id);

    /**
     * 获得房间管理列表
     *
     * @param ids 编号
     * @return 房间管理列表
     */
    List<RoomInfoDO> getRoomInfoList(Collection<Long> ids);

    /**
     * 获得房间管理分页
     *
     * @param pageReqVO 分页查询
     * @return 房间管理分页
     */
    PageResult<RoomInfoDO> getRoomInfoPage(RoomInfoPageReqVO pageReqVO);

    /**
     * 获得房间管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 房间管理列表
     */
    List<RoomInfoDO> getRoomInfoList(RoomInfoExportReqVO exportReqVO);

}
