<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户昵称" prop="nickname">
        <el-input v-model="queryParams.nickname" placeholder="请输入用户昵称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户类型" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.MEMBER_USER_TYPE)"
                       :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="queryParams.mobile" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      
      <!-- <el-form-item label="收入" prop="money">
        <el-input v-model="queryParams.money" placeholder="请输入收入" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="提现金额" prop="withdrawalMoney">
        <el-input v-model="queryParams.withdrawalMoney" placeholder="请输入提现金额" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="账户余额" prop="balance">
        <el-input v-model="queryParams.balance" placeholder="请输入账户余额" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item> -->
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
                   v-hasPermi="['member:app-user:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['member:app-user:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="用户昵称" align="center" prop="nickname" />
      
      <el-table-column label="头像" align="center" prop="avatar" max-width="80px">
        <template v-slot="scope">
          <img :src="scope.row.avatar" class="avatar" width="60px" height="60px" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="mobile" min-width="120px"/>
     
      <el-table-column label="用户类型" align="center" prop="userType">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.MEMBER_USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="收入" align="center" prop="money" />
      <el-table-column label="提现金额" align="center" prop="withdrawalMoney" />
      <el-table-column label="账户余额" align="center" prop="balance" /> -->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册 IP" align="center" prop="registerIp" />
      <el-table-column label="最后登录IP" align="center" prop="loginIp" />
      <el-table-column label="最后登录时间" align="center" prop="loginDate" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.loginDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleRecharge(scope.row)"
                     v-hasPermi="['member:app-user:update']">余额充值</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['member:app-user:update']">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="500px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.COMMON_STATUS)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="form.userType" placeholder="请选择用户类型">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.MEMBER_USER_TYPE)"
                       :key="dict.value" :label="dict.label" :value="parseInt(dict.value)"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    
    <!-- 对话框(余额充值) -->
    <el-dialog :title="title" :visible.sync="rechargeUserShow" width="500px" v-dialogDrag append-to-body>
      <el-form ref="rechargeForm" :model="rechargeForm" :rules="rechargerules" label-width="100px">
        <el-form-item label="用户手机号" prop="mobile">
          <el-input v-model="rechargeForm.mobile" readonly />
        </el-form-item>
        <el-form-item label="充值门店" prop="storeId">
          <el-select v-model="rechargeForm.storeId" placeholder="请选择门店" clearable size="small"
            required="true">
            <el-option v-for="item in storeList" :key="item.value" :label="item.key" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="充值金额" prop="money">
          <el-input v-model="rechargeForm.money" placeholder="输入0代表清空用户在门店的余额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRechargeForm">确 定</el-button>
        <el-button @click="cancelRecharge">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createAppUser, updateAppUser, deleteAppUser, getAppUser, getAppUserPage, exportAppUserExcel,getStoreList,rechargedata } from "@/api/member/appUser";

export default {
  name: "AppUser",
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
      // 用户管理列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      rechargeUserShow: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        nickname: null,
        status: null,
        mobile: null,
        loginDate: [],
        userType: null,
        money: [],
        withdrawalMoney: [],
        balance: [],
        createTime: [],
      },
      // 表单参数
      form: {},
      rechargeForm: {},
      // 表单校验
      rules: {
        status: [{ required: true, message: "状态不能为空", trigger: "change" }],
        mobile: [{ required: true, message: "手机号不能为空", trigger: "blur" }],
        userType: [{ required: true, message: "用户类型不能为空", trigger: "change" }],
      },
      rechargerules: {
        storeId: [{ required: true, message: "门店不能为空", trigger: "change" }],
        money: [{ required: true, message: "金额不能为空", trigger: "blur" }],
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
      getAppUserPage(this.queryParams).then(response => {
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
    cancelRecharge(){
      this.rechargeUserShow = false;
      this.resetRechargeForm();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        status: undefined,
        mobile: undefined,
        userType: undefined,
      };
      this.resetForm("form");
    },
    resetRechargeForm() {
      this.rechargeForm = {
        storeId: undefined,
        mobile: undefined,
        money: undefined,
        userId: undefined,
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
      this.title = "添加用户管理";
    },
    handleRecharge(row){
      this.resetRechargeForm();
      this.rechargeForm={
          userId: row.id,
          mobile: row.mobile
      };
      console.log(this.rechargeForm);
      this.rechargeUserShow=true;
      this.title = "用户余额充值";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getAppUser(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户管理";
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
          updateAppUser(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createAppUser(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    submitRechargeForm(){
      console.log(this.rechargeForm);
      this.$refs["rechargeForm"].validate(valid => {
        if (!valid) {
          return;
        }
        rechargedata(this.rechargeForm).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.rechargeUserShow = false;
        });
        
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除用户管理编号为"' + id + '"的数据项?').then(function() {
          return deleteAppUser(id);
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
      this.$modal.confirm('是否确认导出所有用户管理数据项?').then(() => {
          this.exportLoading = true;
          return exportAppUserExcel(params);
        }).then(response => {
          this.$download.excel(response, '用户管理.xls');
          this.exportLoading = false;
        }).catch(() => {});
    },
    
  }
};
</script>
