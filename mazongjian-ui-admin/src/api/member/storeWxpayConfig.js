import request from '@/utils/request'

// 创建门店微信支付配置
export function createStoreWxpayConfig(data) {
  return request({
    url: '/member/store-wxpay-config/create',
    method: 'post',
    data: data
  })
}

// 更新门店微信支付配置
export function updateStoreWxpayConfig(data) {
  return request({
    url: '/member/store-wxpay-config/update',
    method: 'put',
    data: data
  })
}

// 删除门店微信支付配置
export function deleteStoreWxpayConfig(id) {
  return request({
    url: '/member/store-wxpay-config/delete?id=' + id,
    method: 'delete'
  })
}

// 获得门店微信支付配置
export function getStoreWxpayConfig(id) {
  return request({
    url: '/member/store-wxpay-config/get?id=' + id,
    method: 'get'
  })
}
//分账授权
export function profitsharing(id) {
  return request({
    url: '/member/store-wxpay-config/profitsharing?id=' + id,
    method: 'get'
  })
}

// 获得门店微信支付配置分页
export function getStoreWxpayConfigPage(query) {
  return request({
    url: '/member/store-wxpay-config/page',
    method: 'get',
    params: query
  })
}

// 获得门店下拉列表
export function getStoreList() {
  return request({
    url: '/index/getStoreList',
    method: 'get'
  })
}

// 导出门店微信支付配置 Excel
export function exportStoreWxpayConfigExcel(query) {
  return request({
    url: '/member/store-wxpay-config/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
