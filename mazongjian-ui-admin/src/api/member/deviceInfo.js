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
    url: '/index/getStoreList',
    method: 'get'
  })
}

// 获得房间下拉列表
export function getRoomList(storeId) {
  return request({
    url: '/index/getRoomList/'+storeId,
    method: 'get'
  })
}

// 配网
export function configWifi(data) {
  return request({
    url: '/member/device-info/configWifi',
    method: 'post',
    data: data
  })
}

// 绑定
export function bind(data) {
  return request({
    url: '/member/device-info/bind',
    method: 'post',
    data: data
  })
}

export function getIotScope(){
  return request({
    url: '/member/device-info/iotScope',
    method: 'get'
  })
}

export function setAutoLock(data){
  return request({
    url: '/member/device-info/setAutoLock',
    method: 'post',
    data: data
  })
}

export function control(data){
  return request({
    url: '/member/device-info/control',
    method: 'post',
    data: data
  })
}
