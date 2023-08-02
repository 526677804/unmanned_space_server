import request from '@/utils/request'

// 创建设备使用记录
export function createDeviceUseInfo(data) {
  return request({
    url: '/member/device-use-info/create',
    method: 'post',
    data: data
  })
}

// 更新设备使用记录
export function updateDeviceUseInfo(data) {
  return request({
    url: '/member/device-use-info/update',
    method: 'put',
    data: data
  })
}

// 删除设备使用记录
export function deleteDeviceUseInfo(id) {
  return request({
    url: '/member/device-use-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备使用记录
export function getDeviceUseInfo(id) {
  return request({
    url: '/member/device-use-info/get?id=' + id,
    method: 'get'
  })
}

// 获得设备使用记录分页
export function getDeviceUseInfoPage(query) {
  return request({
    url: '/member/device-use-info/page',
    method: 'get',
    params: query
  })
}

// 导出设备使用记录 Excel
export function exportDeviceUseInfoExcel(query) {
  return request({
    url: '/member/device-use-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
