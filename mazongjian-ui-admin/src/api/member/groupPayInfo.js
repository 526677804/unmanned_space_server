import request from '@/utils/request'

// 创建团购支付信息
export function createGroupPayInfo(data) {
  return request({
    url: '/member/group-pay-info/create',
    method: 'post',
    data: data
  })
}

// 更新团购支付信息
export function updateGroupPayInfo(data) {
  return request({
    url: '/member/group-pay-info/update',
    method: 'put',
    data: data
  })
}

// 删除团购支付信息
export function deleteGroupPayInfo(id) {
  return request({
    url: '/member/group-pay-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得团购支付信息
export function getGroupPayInfo(id) {
  return request({
    url: '/member/group-pay-info/get?id=' + id,
    method: 'get'
  })
}

// 获得团购支付信息分页
export function getGroupPayInfoPage(query) {
  return request({
    url: '/member/group-pay-info/page',
    method: 'get',
    params: query
  })
}

// 导出团购支付信息 Excel
export function exportGroupPayInfoExcel(query) {
  return request({
    url: '/member/group-pay-info/export-excel',
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