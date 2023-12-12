<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备sn" prop="deviceSn">
        <el-input v-model="queryParams.deviceSn" placeholder="请输入设备sn" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择设备类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.MEMBER_DEVICE_TYPE)" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="门店" prop="storeId">
        <el-select v-model="queryParams.storeId" placeholder="请选择门店" clearable size="small" @change="loadRoomList">
          <el-option v-for="item in storeList" :key="item.value" :label="item.key" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="房间" prop="roomId">
        <el-select v-model="queryParams.roomId" placeholder="请选择房间" clearable size="small">
          <el-option v-for="item in roomList" :key="item.value" :label="item.key" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option label="在线" value="1" />
          <el-option label="离线" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']" />
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
          v-hasPermi="['member:device-info:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
          v-hasPermi="['member:device-info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="设备id" align="center" prop="deviceId" />
      <el-table-column label="设备sn" align="center" prop="deviceSn" />
      <el-table-column label="设备类型" align="center" prop="type">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.MEMBER_DEVICE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="门店" align="center" prop="storeId" /> -->
      <el-table-column label="门店名称" align="center" prop="storeName" />
      <!-- <el-table-column label="房间" align="center" prop="roomId" /> -->
      <el-table-column label="房间名称" align="center" prop="roomName" />
      <el-table-column label="状态" align="center" :formatter="statusFomat" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleBindStore(scope.row)"
            v-hasPermi="['member:device-info:update']">绑定</el-button>
          
          <el-button size="mini" type="text" @click="handleConfigYunlaba(scope.row.deviceId)"
            v-hasPermi="['member:device-info:update']" v-if="scope.row.type==3">初始化</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['member:device-info:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备sn" prop="deviceSn">
          <el-input v-model="form.deviceSn" placeholder="请输入设备sn" />
        </el-form-item>
        <el-form-item label="设备类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择设备类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.MEMBER_DEVICE_TYPE)" :key="dict.value"
              :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 对话框(绑定门店) -->
    <el-dialog :title="title" :visible.sync="bindStore" width="500px" v-dialogDrag append-to-body>
      <el-form ref="bindForm" :model="bindForm" :rules="bindrules" label-width="80px">
        <!-- <el-form-item label="设备sn" prop="deviceSn">
          <el-label v-model="form.deviceSn" readonly />
        </el-form-item> -->
        <el-form-item label="门店" prop="storeId">
          <el-select v-model="bindForm.storeId" placeholder="请选择门店" clearable size="small" @change="loadRoomList"
            required="true">
            <el-option v-for="item in storeList" :key="item.value" :label="item.key" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间" prop="roomId">
          <el-select v-model="bindForm.roomId" placeholder="请选择房间" clearable size="small">
            <el-option v-for="item in roomList" :key="item.value" :label="item.key" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBindForm">确 定</el-button>
        <el-button @click="cancelBind">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createDeviceInfo, updateDeviceInfo, deleteDeviceInfo, getDeviceInfo, getDeviceInfoPage, exportDeviceInfoExcel, configYunlaba, getStoreList, getRoomList,bind } from "@/api/member/deviceInfo";
import { DICT_TYPE, getDictDatas} from "@/utils/dict";
export default {
  name: "DeviceInfo",
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
      // 设备管理列表
      list: [],
      storeList: [],
      roomList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      bindStore: false,
      bindRoom: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        deviceSn: null,
        type: null,
        storeId: null,
        roomId: null,
        status: null,
        createTime: [],
      },
      // 表单参数
      form: {},
      bindForm: {
        deviceSn:null,
        storeId:null,
        roomId:null
      },
      // 表单校验
      rules: {
        deviceSn: [{ required: true, message: "设备sn不能为空", trigger: "blur" }],
        type: [{ required: true, message: "设备类型不能为空", trigger: "change" }],
      },
      bindrules: {
        storeId: [{ required: true, message: "门店不能为空", trigger: "blur" }],
      },
      optionsStas: [
        {
          name: "离线",
          id: 0,
        },
        {
          name: "在线",
          id: 1,
        },
      ],

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
      getDeviceInfoPage(this.queryParams).then(response => {
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
    cancelBind(){
      this.bindStore = false;
      this.bindForm={
        deviceId: undefined,
        storeId: undefined,
        roomId: undefined,
      }
    },
    /** 表单重置 */
    reset() {
      this.form = {
        deviceId: undefined,
        deviceSn: undefined,
        type: undefined,
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
      this.title = "添加设备管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const deviceId = row.deviceId;
      getDeviceInfo(deviceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.deviceId != null) {
          updateDeviceInfo(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createDeviceInfo(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    submitBindForm() {
      this.$refs["bindForm"].validate(valid => {
        if (!valid) {
          return;
        }
        bind(this.bindForm).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.bindStore = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deviceId = row.deviceId;
      this.$modal.confirm('是否确认删除设备管理编号为"' + deviceId + '"的数据项?').then(function () {
        return deleteDeviceInfo(deviceId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = { ...this.queryParams };
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有设备管理数据项?').then(() => {
        this.exportLoading = true;
        return exportDeviceInfoExcel(params);
      }).then(response => {
        this.$download.excel(response, '设备管理.xls');
        this.exportLoading = false;
      }).catch(() => { });
    },
    loadRoomList(storeId) {
      if (storeId) {
        getRoomList(storeId).then(response => {
          this.roomList = response.data;
        });
      }
    },
    handleConfigYunlaba(deviceId) {
      this.$modal.confirm('是否确认设备编号为"' + deviceId + '"的数据项进行初始化操作?').then(function () {
        return configYunlaba(deviceId);
      }).then(() => {
        this.$modal.msgSuccess("操作成功");
      }).catch(() => { });
    },
    statusFomat(row, column) {
      if (row.status == 0) {
        return "离线";
      } else if (row.status == 1) {
        return "在线";
      }
    },
    /** 绑定门店 */
    handleBindStore(row) {
      this.bindForm.deviceId = row.deviceId;
      this.bindStore = true;
      this.title = "修改设备绑定";
    }
  }
};
</script>
