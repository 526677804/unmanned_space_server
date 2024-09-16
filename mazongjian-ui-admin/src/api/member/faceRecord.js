import request from '@/utils/request'

// 创建人脸识别记录
export function createFaceRecord(data) {
  return request({
    url: '/member/face-record/create',
    method: 'post',
    data: data
  })
}

// 添加人脸黑名单
export function updateFaceRecord(data) {
  return request({
    url: '/member/face-record/moveFaceByRecord',
    method: 'post',
    data: data
  })
}

// 删除人脸识别记录
export function deleteFaceRecord(id) {
  return request({
    url: '/member/face-record/delete?id=' + id,
    method: 'delete'
  })
}

// 获得人脸识别记录
export function getFaceRecord(id) {
  return request({
    url: '/member/face-record/get?id=' + id,
    method: 'get'
  })
}

// 获得人脸识别记录分页
export function getFaceRecordPage(query) {
  return request({
    url: '/member/face-record/page',
    method: 'get',
    params: query
  })
}

// 导出人脸识别记录 Excel
export function exportFaceRecordExcel(query) {
  return request({
    url: '/member/face-record/export-excel',
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