import request from '@/utils/request'

// 创建用户管理
export function createAppUser(data) {
  return request({
    url: '/member/app-user/create',
    method: 'post',
    data: data
  })
}

// 更新用户管理
export function updateAppUser(data) {
  return request({
    url: '/member/app-user/update',
    method: 'put',
    data: data
  })
}

// 删除用户管理
export function deleteAppUser(id) {
  return request({
    url: '/member/app-user/delete?id=' + id,
    method: 'delete'
  })
}

// 获得用户管理
export function getAppUser(id) {
  return request({
    url: '/member/app-user/get?id=' + id,
    method: 'get'
  })
}

// 获得用户管理分页
export function getAppUserPage(query) {
  return request({
    url: '/member/app-user/page',
    method: 'get',
    params: query
  })
}

// 导出用户管理 Excel
export function exportAppUserExcel(query) {
  return request({
    url: '/member/app-user/export-excel',
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
// 用户余额充值
export function rechargedata(data) {
  return request({
    url: '/member/app-user/recharge',
    method: 'post',
    data: data
  })
}
