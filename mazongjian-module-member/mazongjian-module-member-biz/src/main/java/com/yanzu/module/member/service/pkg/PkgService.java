package com.yanzu.module.member.service.pkg;


import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.pkg.vo.*;

public interface PkgService {
    PageResult<AppAdminPkgPageRespVO> getAdminPkgPage(AppAdminPkgPageReqVO reqVO);

    void saveAdminPkg(AppAdminPkgSaveReqVO reqVO);

    void enable(Long pkgId);

    PageResult<AppPkgPageRespVO> getPkgPage(AppPkgPageReqVO reqVO);

    PageResult<AppPkgMyPageRespVO> getMyPkgPage(AppMyPkgPageReqVO reqVO);

    WxPayOrderRespVO preBuyPkg(Long pkgId);


    void buyPkg(AppBuyPkgReqVO reqVO);

    void delete(Long pkgId);

}
