import request from '@/utils/request'

// 创建订单管理
export function createOrderInfo(data) {
  return request({
    url: '/member/order-info/create',
    method: 'post',
    data: data
  })
}

// 更新订单管理
export function updateOrderInfo(data) {
  return request({
    url: '/member/order-info/update',
    method: 'put',
    data: data
  })
}

// 删除订单管理
export function deleteOrderInfo(id) {
  return request({
    url: '/member/order-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得订单管理
export function getOrderInfo(id) {
  return request({
    url: '/member/order-info/get?id=' + id,
    method: 'get'
  })
}

// 获得订单管理分页
export function getOrderInfoPage(query) {
  return request({
    url: '/member/order-info/page',
    method: 'get',
    params: query
  })
}

// 导出订单管理 Excel
export function exportOrderInfoExcel(query) {
  return request({
    url: '/member/order-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
