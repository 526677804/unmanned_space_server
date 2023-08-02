import request from '@/utils/request'

// 创建设备管理
export function createDeviceInfo(data) {
  return request({
    url: '/member/device-info/create',
    method: 'post',
    data: data
  })
}

// 更新设备管理
export function updateDeviceInfo(data) {
  return request({
    url: '/member/device-info/update',
    method: 'put',
    data: data
  })
}

// 删除设备管理
export function deleteDeviceInfo(id) {
  return request({
    url: '/member/device-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备管理
export function getDeviceInfo(id) {
  return request({
    url: '/member/device-info/get?id=' + id,
    method: 'get'
  })
}

// 获得设备管理分页
export function getDeviceInfoPage(query) {
  return request({
    url: '/member/device-info/page',
    method: 'get',
    params: query
  })
}

// 导出设备管理 Excel
export function exportDeviceInfoExcel(query) {
  return request({
    url: '/member/device-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

// 获得门店下拉列表
export function getStoreList() {
  return request({
    url: '/member/index/getStoreList',
    method: 'get',
    params: query
  })
}

// 获得房间下拉列表
export function getRoomList(storeId) {
  return request({
    url: '/member/index/getRoomList/'+storeId,
    method: 'get',
    params: query
  })
}

// 配网
export function config(deviceId) {
  return request({
    url: '/member/device-info/config/'+storeId,
    method: 'get',
    params: query
  })
}