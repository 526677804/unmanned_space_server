import request from '@/utils/request'

// 创建门店管理
export function createStoreInfo(data) {
  return request({
    url: '/member/store-info/create',
    method: 'post',
    data: data
  })
}

// 更新门店管理
export function updateStoreInfo(data) {
  return request({
    url: '/member/store-info/update',
    method: 'put',
    data: data
  })
}

// 删除门店管理
export function deleteStoreInfo(id) {
  return request({
    url: '/member/store-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得门店管理
export function getStoreInfo(id) {
  return request({
    url: '/member/store-info/get?id=' + id,
    method: 'get'
  })
}

// 获得门店管理分页
export function getStoreInfoPage(query) {
  return request({
    url: '/member/store-info/page',
    method: 'get',
    params: query
  })
}

// 导出门店管理 Excel
export function exportStoreInfoExcel(query) {
  return request({
    url: '/member/store-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
export function getMeituanScope(id){
  return request({
    url: '/member/store-info/meituanScope/'+id,
    method: 'get'
  })
}

export function renewStore(data){
  return request({
    url: '/member/store-info/renew',
    method: 'post',
    data: data
  })
}