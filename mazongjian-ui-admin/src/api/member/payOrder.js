import request from '@/utils/request'

// 创建支付订单
export function createPayOrder(data) {
  return request({
    url: '/member/pay-order/create',
    method: 'post',
    data: data
  })
}

// 更新支付订单
export function updatePayOrder(data) {
  return request({
    url: '/member/pay-order/update',
    method: 'put',
    data: data
  })
}
//退款
export function refundOrder(id) {
  return request({
    url: '/member/pay-order/refund?id=' + id,
    method: 'post'
  })
}
// 删除支付订单
export function deletePayOrder(id) {
  return request({
    url: '/member/pay-order/delete?id=' + id,
    method: 'delete'
  })
}

// 获得支付订单
export function getPayOrder(id) {
  return request({
    url: '/member/pay-order/get?id=' + id,
    method: 'get'
  })
}

// 获得支付订单分页
export function getPayOrderPage(query) {
  return request({
    url: '/member/pay-order/page',
    method: 'get',
    params: query
  })
}

// 导出支付订单 Excel
export function exportPayOrderExcel(query) {
  return request({
    url: '/member/pay-order/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
