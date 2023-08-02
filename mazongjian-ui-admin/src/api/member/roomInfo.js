import request from '@/utils/request'

// 创建房间管理
export function createRoomInfo(data) {
  return request({
    url: '/member/room-info/create',
    method: 'post',
    data: data
  })
}

// 更新房间管理
export function updateRoomInfo(data) {
  return request({
    url: '/member/room-info/update',
    method: 'put',
    data: data
  })
}

// 删除房间管理
export function deleteRoomInfo(id) {
  return request({
    url: '/member/room-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得房间管理
export function getRoomInfo(id) {
  return request({
    url: '/member/room-info/get?id=' + id,
    method: 'get'
  })
}

// 获得房间管理分页
export function getRoomInfoPage(query) {
  return request({
    url: '/member/room-info/page',
    method: 'get',
    params: query
  })
}

// 导出房间管理 Excel
export function exportRoomInfoExcel(query) {
  return request({
    url: '/member/room-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
