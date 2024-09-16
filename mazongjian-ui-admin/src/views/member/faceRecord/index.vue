<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="门店" prop="storeId">
        <el-select v-model="queryParams.storeId" placeholder="请选择门店" clearable size="small" @change="loadRoomList">
          <el-option v-for="item in storeList" :key="item.value" :label="item.key" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceSn">
        <el-input v-model="queryParams.deviceSn" placeholder="请输入设备编号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="识别时间" prop="showTime">
        <el-date-picker v-model="queryParams.showTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item label="比对结果" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择" clearable size="small">
          <el-option label="比对成功" value="1" />
          <el-option label="陌生人" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['member:face-record:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="设备编号" align="center" prop="deviceSn" />
      <el-table-column label="门店名称" align="center" prop="storeName" />
      <el-table-column label="照片" align="center" prop="photoData" max-width="80px">
        <template v-slot="scope">
          <img :src="scope.row.photoData" class="photoData" width="60px" height="90px" />
        </template>
      </el-table-column>
      <el-table-column label="识别时间" align="center" prop="showTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.showTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="比对结果" align="center" prop="type">
        <template v-slot="scope">
          <span>{{ scope.row.type === 1 ? '黑名单' : '陌生人' }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" v-if="scope.row.type==1"  @click="handleMove(scope.row)"
          v-hasPermi="['member:device-info:update']">移出黑名单</el-button>
          <el-button size="mini" type="text" v-if="scope.row.type==2"  @click="handleUpdate(scope.row)"
          v-hasPermi="['member:device-info:update']">加入黑名单</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['member:face-record:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="拉黑备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createFaceRecord, updateFaceRecord, deleteFaceRecord, getFaceRecord, getFaceRecordPage, getStoreList, exportFaceRecordExcel } from "@/api/member/faceRecord";

export default {
  name: "FaceRecord",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 人脸识别记录列表
      list: [],
      storeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        admitGuid: null,
        showTime: [],
        type: null,
        createTime: [],
        deviceSn: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
     // 执行查询
     getStoreList().then(response => {
      this.storeList = response.data;
    });
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getFaceRecordPage(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        remark: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleMove(row) {
      const id = row.id;
      const form={
        id: id
      };
      this.$modal.confirm('是否移出黑名单?').then(function() {
        updateFaceRecord(form);
      }).then(() => {
        this.$modal.msgSuccess("操作成功");
        setTimeout(() => {
          this.getList();
        }, 1000);
      }).catch(() => {});
      
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      this.form={
        id: id
      };
      this.open = true;
      this.title = "添加人脸黑名单";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        console.log(this.form);
        // 修改的提交
        if (this.form.id != null) {
          updateFaceRecord(this.form).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.open = false;
            this.getList();
          });
          return;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除人脸识别记录编号为"' + id + '"的数据项?').then(function() {
          return deleteFaceRecord(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有人脸识别记录数据项?').then(() => {
          this.exportLoading = true;
          return exportFaceRecordExcel(params);
        }).then(response => {
          this.$download.excel(response, '人脸识别记录.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
