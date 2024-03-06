import request from '@/utils/request'

// 创建加盟信息
export function createFranchiseInfo(data) {
  return request({
    url: '/member/franchise-info/create',
    method: 'post',
    data: data
  })
}

// 更新加盟信息
export function updateFranchiseInfo(data) {
  return request({
    url: '/member/franchise-info/update',
    method: 'put',
    data: data
  })
}

// 删除加盟信息
export function deleteFranchiseInfo(id) {
  return request({
    url: '/member/franchise-info/delete?id=' + id,
    method: 'delete'
  })
}

// 获得加盟信息
export function getFranchiseInfo(id) {
  return request({
    url: '/member/franchise-info/get?id=' + id,
    method: 'get'
  })
}

// 获得加盟信息分页
export function getFranchiseInfoPage(query) {
  return request({
    url: '/member/franchise-info/page',
    method: 'get',
    params: query
  })
}

// 导出加盟信息 Excel
export function exportFranchiseInfoExcel(query) {
  return request({
    url: '/member/franchise-info/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
