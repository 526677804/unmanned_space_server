import request from '@/utils/request'

// 创建门店用户管理
export function createStoreUser(data) {
  return request({
    url: '/member/store-user/create',
    method: 'post',
    data: data
  })
}

// 更新门店用户管理
export function updateStoreUser(data) {
  return request({
    url: '/member/store-user/update',
    method: 'put',
    data: data
  })
}

// 删除门店用户管理
export function deleteStoreUser(id) {
  return request({
    url: '/member/store-user/delete?id=' + id,
    method: 'delete'
  })
}

// 获得门店用户管理
export function getStoreUser(id) {
  return request({
    url: '/member/store-user/get?id=' + id,
    method: 'get'
  })
}

// 获得门店用户管理分页
export function getStoreUserPage(query) {
  return request({
    url: '/member/store-user/page',
    method: 'get',
    params: query
  })
}

// 导出门店用户管理 Excel
export function exportStoreUserExcel(query) {
  return request({
    url: '/member/store-user/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
