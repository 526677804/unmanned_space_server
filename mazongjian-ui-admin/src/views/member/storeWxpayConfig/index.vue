<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
       <el-form-item label="门店" prop="storeId">
        <el-select v-model="queryParams.storeId" placeholder="请选择门店" clearable size="small" >
          <el-option v-for="item in storeList" :key="item.value" :label="item.key" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="商户号" prop="mchId">
        <el-input v-model="queryParams.mchId" placeholder="请输入商户号" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['member:store-wxpay-config:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['member:store-wxpay-config:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="门店" align="center" prop="storeName" />
      <el-table-column label="商户号" align="center" prop="mchId" />
      <el-table-column label="APPID" align="center" prop="appId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button v-show="scope.row.serviceModel" size="mini" type="text" icon="el-icon-edit" @click="handleProfitsharing(scope.row)"
                     v-hasPermi="['member:store-wxpay-config:update']">分账授权</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['member:store-wxpay-config:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['member:store-wxpay-config:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="门店" prop="storeId">
        <el-select v-model="form.storeId" placeholder="请选择门店" clearable size="small" >
          <el-option v-for="item in storeList" :key="item.value" :label="item.key" :value="item.value" />
        </el-select>
      </el-form-item>
        <el-form-item label="商户号" prop="mchId">
          <el-input v-model="form.mchId" placeholder="请输入商户号" />
        </el-form-item>
        <el-form-item label="appId" prop="appId">
          <el-input v-model="form.appId" placeholder="请输入APPID" />
        </el-form-item>
        <el-form-item label="支付密钥" prop="mchKey" v-show="!form.serviceModel">
          <el-input v-model="form.mchKey" placeholder="请输入支付密钥(服务商模式不填)" />
        </el-form-item>
        <el-form-item label="p12证书文件" prop="p12" v-show="!form.serviceModel">
            <input id="fileInput" type="file" accept=".p12"  @change="handleFileChange" />
            <el-input v-model="form.p12"   placeholder="p12证书(服务商模式不填)" readonly/>
          </el-form-item>
        <el-form-item label="支付服务商模式" prop="type">
          <el-radio-group v-model="form.serviceModel">
            <el-radio :key="true" :label="true">是</el-radio>
            <el-radio :key="false" :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否分账" prop="type" v-show="form.serviceModel">
          <el-radio-group v-model="form.split">
            <el-radio :key="true"  :label="true">是</el-radio>
            <el-radio :key="false" :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分账比例" prop="splitProp" v-show="form.serviceModel">
          <el-input v-model="form.splitProp" placeholder="请输入1-30的数字，最大允许30%" />
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
import { createStoreWxpayConfig, updateStoreWxpayConfig, deleteStoreWxpayConfig, getStoreWxpayConfig, profitsharing,getStoreWxpayConfigPage, getStoreList } from "@/api/member/storeWxpayConfig";

export default {
  name: "StoreWxpayConfig",
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
      // 门店微信支付配置列表
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
        storeId: null,
        mchId: null,
      },
      // 表单参数
      form: {
        serviceModel: false
      },
      // 表单校验
      rules: {
        storeId: [{ required: true, message: "门店不能为空", trigger: "change" }],
        mchId: [{ required: true, message: "商户号不能为空", trigger: "blur" }],
        appId: [{ required: true, message: "appId不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
    getStoreList().then(response => {
      this.storeList = response.data;
    });
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getStoreWxpayConfigPage(this.queryParams).then(response => {
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
      // fileInput.value = '';  // 清空文件输入的值
      this.form = {
        id: undefined,
        storeId: undefined,
        mchId: undefined,
        appId: undefined,
        mchKey: undefined,
        p12: undefined,
        serviceModel: false,
        split: undefined,
        splitProp: undefined,

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
      this.title = "添加门店微信支付配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getStoreWxpayConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改门店微信支付配置";
      });
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          // 获取完整的 Base64 编码结果
          const base64WithHeader = e.target.result;
          // 去掉文件头
          const base64 = base64WithHeader.split(',')[1];
          this.form.p12 = base64;
        };
        reader.readAsDataURL(file);
      }
    },
    handleProfitsharing(row) {
      const id = row.id;
      profitsharing(id).then(response => {
        this.$modal.msgSuccess("操作成功");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.id != null) {
          updateStoreWxpayConfig(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createStoreWxpayConfig(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除门店微信支付配置编号为"' + id + '"的数据项?').then(function() {
          return deleteStoreWxpayConfig(id);
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
      this.$modal.confirm('是否确认导出所有门店微信支付配置数据项?').then(() => {
          this.exportLoading = true;
          return exportStoreWxpayConfigExcel(params);
        }).then(response => {
          this.$download.excel(response, '门店微信支付配置.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
