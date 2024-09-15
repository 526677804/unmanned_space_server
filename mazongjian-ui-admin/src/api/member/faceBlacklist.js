import request from '@/utils/request'

// 创建人脸黑名单
export function createFaceBlacklist(data) {
  return request({
    url: '/member/face-blacklist/create',
    method: 'post',
    data: data
  })
}

// 更新人脸黑名单
export function updateFaceBlacklist(data) {
  return request({
    url: '/member/face-blacklist/update',
    method: 'put',
    data: data
  })
}

// 删除人脸黑名单
export function deleteFaceBlacklist(id) {
  return request({
    url: '/member/face-blacklist/delete?id=' + id,
    method: 'delete'
  })
}

// 获得人脸黑名单
export function getFaceBlacklist(id) {
  return request({
    url: '/member/face-blacklist/get?id=' + id,
    method: 'get'
  })
}

// 获得人脸黑名单分页
export function getFaceBlacklistPage(query) {
  return request({
    url: '/member/face-blacklist/page',
    method: 'get',
    params: query
  })
}

// 导出人脸黑名单 Excel
export function exportFaceBlacklistExcel(query) {
  return request({
    url: '/member/face-blacklist/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 获得门店下拉列表
export function getStoreList() {
  return request({
    url: '/index/getStoreList',
    method: 'get'
  })
}