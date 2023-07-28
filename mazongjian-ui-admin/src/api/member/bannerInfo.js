import request from '@/utils/request'

// 创建广告管理
export function createBannerInfo(data) {
  return request({
    url: '/member/banner-info/create',
    method: 'post',
    data: data
  })
}

// 更新广告管理
export function updateBannerInfo(data) {
  return request({
    url: '/member/banner-info/update',
    method: 'put',
    data: data
  })
}

// 删除广告管理
export function deleteBannerInfo(id) {
  return request({
    url: '/member/banner-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得广告管理
export function getBannerInfo(id) {
  return request({
    url: '/member/banner-info/get?id=' + id,
    method: 'get'
  })
}

// 获得广告管理分页
export function getBannerInfoPage(query) {
  return request({
    url: '/member/banner-info/page',
    method: 'get',
    params: query
  })
}

// 导出广告管理 Excel
export function exportBannerInfoExcel(query) {
  return request({
    url: '/member/banner-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
