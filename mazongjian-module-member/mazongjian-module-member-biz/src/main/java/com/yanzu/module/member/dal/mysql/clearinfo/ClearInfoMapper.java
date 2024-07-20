package com.yanzu.module.member.dal.mysql.clearinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.clear.vo.AppClearInfoRespVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageRespVO;
import com.yanzu.module.member.controller.app.manager.vo.AppSettlementClearUserReqVO;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保洁信息管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearInfoMapper extends BaseMapperX<ClearInfoDO> {


    List<ClearInfoDO> getByUserIdAndStatusAndStoreIds(@Param("userId") Long userId, @Param("status") Integer status, @Param("storeIds") String storeIds);

    List<ClearInfoDO> getByUserIdAndStatusAndStoreId(@Param("userId") Long userId, @Param("status") Integer status, @Param("storeId") Long storeId);

    AppClearInfoRespVO getDetail(Long clearId);

    //tpye  ->  1=日 2=月 3=全部
    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status, @Param("type") int type, @Param("dateStr") String dateStr);

    int cancelByRoomId(Long roomId);

    IPage<AppClearPageRespVO> getClearManagerPage(@Param("page") IPage<AppClearPageRespVO> page, @Param("reqVO") AppClearPageReqVO reqVO);

    int cancelByRoomIds(String roomIds);

    int countCurrentByRoomId(Long roomId);

    List<Long> getSettlementList(AppSettlementClearUserReqVO reqVO);

    int settlementByIds(List<Long> ids);

    ClearInfoDO getCurrent(@Param("userId") Long userId, @Param("roomId") Long roomId);

    int cancelById(Long clearId);

}
