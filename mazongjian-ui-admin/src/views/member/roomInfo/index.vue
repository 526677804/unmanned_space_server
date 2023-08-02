<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="房间名称" prop="roomName">
        <el-input v-model="queryParams.roomName" placeholder="请输入房间名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="门店id" prop="storeId">
        <el-input v-model="queryParams.storeId" placeholder="请输入门店id" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="房间类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择房间类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.MEMBER_ROOM_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['member:room-info:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['member:room-info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="房间id" align="center" prop="roomId" />
      <el-table-column label="房间名称" align="center" prop="roomName" />
      <el-table-column label="门店id" align="center" prop="storeId" />
      <el-table-column label="房间类型" align="center" prop="type">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.MEMBER_ROOM_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="单价" align="center" prop="price" />
      <el-table-column label="房间标签" align="center" prop="label" />
      <el-table-column label="排序位置" align="center" prop="sortId" />
      <el-table-column label="禁用开始时间" align="center" prop="banTimeStart" />
      <el-table-column label="禁用结束时间" align="center" prop="banTimeEnd" />
      <el-table-column label="总完成订单数" align="center" prop="totalOrderNum" />
      <el-table-column label="总收益" align="center" prop="totalMoney" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['member:room-info:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['member:room-info:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="房间名称" prop="roomName">
          <el-input v-model="form.roomName" placeholder="请输入房间名称" />
        </el-form-item>
        <el-form-item label="门店id" prop="storeId">
          <el-input v-model="form.storeId" placeholder="请输入门店id" />
        </el-form-item>
        <el-form-item label="房间类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择房间类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.MEMBER_ROOM_TYPE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input v-model="form.price" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="排序位置" prop="sortId">
          <el-input v-model="form.sortId" placeholder="请输入排序位置" />
        </el-form-item>
        <el-form-item label="总完成订单数" prop="totalOrderNum">
          <el-input v-model="form.totalOrderNum" placeholder="请输入总完成订单数" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="请选择字典生成" value="" />
          </el-select>
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
import { createRoomInfo, updateRoomInfo, deleteRoomInfo, getRoomInfo, getRoomInfoPage, exportRoomInfoExcel } from "@/api/member/roomInfo";

export default {
  name: "RoomInfo",
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
      // 房间管理列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        roomName: null,
        storeId: null,
        type: null,
        status: null,
        createTime: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roomName: [{ required: true, message: "房间名称不能为空", trigger: "blur" }],
        storeId: [{ required: true, message: "门店id不能为空", trigger: "blur" }],
        type: [{ required: true, message: "房间类型不能为空", trigger: "change" }],
        price: [{ required: true, message: "单价不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getRoomInfoPage(this.queryParams).then(response => {
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
        roomId: undefined,
        roomName: undefined,
        storeId: undefined,
        type: undefined,
        price: undefined,
        sortId: undefined,
        totalOrderNum: undefined,
        status: undefined,
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
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加房间管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const roomId = row.roomId;
      getRoomInfo(roomId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改房间管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.roomId != null) {
          updateRoomInfo(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createRoomInfo(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const roomId = row.roomId;
      this.$modal.confirm('是否确认删除房间管理编号为"' + roomId + '"的数据项?').then(function() {
          return deleteRoomInfo(roomId);
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
      this.$modal.confirm('是否确认导出所有房间管理数据项?').then(() => {
          this.exportLoading = true;
          return exportRoomInfoExcel(params);
        }).then(response => {
          this.$download.excel(response, '房间管理.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
