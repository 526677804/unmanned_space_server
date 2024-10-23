<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="门店名称" prop="storeName">
        <el-input v-model="queryParams.storeName" placeholder="请输入门店名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="城市名称" prop="cityName">
        <el-input v-model="queryParams.cityName" placeholder="请输入城市名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="详细地址" prop="address">
        <el-input v-model="queryParams.address" placeholder="请输入详细地址" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="门店状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择门店状态" clearable size="small">
          <el-option label="正常" value="0" />
          <el-option label="审核中" value="1" />
          <el-option label="已到期" value="2" />
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
                  v-hasPermi="['member:store-info:create']">新增</el-button>
     </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"-->
<!--                   v-hasPermi="['member:store-info:export']">导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="门店ID" align="center" prop="storeId" />
      <el-table-column label="门店名称" align="center" prop="storeName" />
      <el-table-column label="城市名称" align="center" prop="cityName" />
      <!-- <el-table-column label="门店公告" align="center" prop="notice" /> -->
      <el-table-column label="纬度" align="center" prop="lat" />
      <el-table-column label="经度" align="center" prop="lon" />
      <el-table-column label="详细地址" align="center" prop="address" />
      <el-table-column label="客服电话" align="center" prop="kefuPhone" />
      <!-- <el-table-column label="美团店铺uuid" align="center" prop="meituanOpenShopUuid" />
      <el-table-column label="美团key" align="center" prop="meituanKey" />
      <el-table-column label="美团Secret" align="center" prop="meituanSecret" /> -->
      <el-table-column label="房间数量" align="center" prop="roomNum" />
      <el-table-column label="门店状态" align="center" :formatter="statusFomat" />
      <el-table-column label="到期时间" align="center" prop="expireTime" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['member:store-info:update']">修改</el-button>
                     <el-button size="mini" type="text" icon="el-icon-edit" @click="handleRenew(scope.row)"
                     v-hasPermi="['member:store-info:update']">续费</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['member:store-info:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="title" :visible.sync="open" width="800px" v-dialogDrag append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="门店名称" prop="storeName">
          <el-input v-model="form.storeName" placeholder="请输入门店名称" />
        </el-form-item>
        <el-form-item label="房间数量" prop="roomNum" v-show="!form.storeId">
          <el-input v-model="form.roomNum" placeholder="请输入房间数量"  />
        </el-form-item>
        <el-form-item label="超管手机号" prop="mobile" v-show="!form.storeId">
          <el-input v-model="form.mobile" placeholder="请输入超管用户手机号"  />
        </el-form-item>
        <el-form-item label="城市名称" prop="cityName">
          <el-input v-model="form.cityName" placeholder="请输入城市名称" />
        </el-form-item>
        <el-form-item label="富文本详情">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="门店公告" prop="notice">
          <editor v-model="form.notice" :min-height="192"/>
        </el-form-item>
        <el-form-item>
          经纬度在这个网页地址去查询：https://lbs.qq.com/tool/getpoint/get-point.html
        </el-form-item>
        <el-form-item label="纬度" prop="lat">
          <el-input v-model="form.lat" placeholder="请输入纬度" />
        </el-form-item>
        <el-form-item label="经度" prop="lon">
          <el-input v-model="form.lon" placeholder="请输入经度" />
        </el-form-item>

        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="门店状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择门店状态">
            <el-option
            v-for="item in optionsStas"
            :key="item.id"
            :label="item.name"
            :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="wifi名称" prop="wifiInfo">
          <el-input v-model="form.wifiInfo" placeholder="请输入wifi名称" />
        </el-form-item>
        <el-form-item label="wifi密码" prop="wifiPwd">
          <el-input v-model="form.wifiPwd" placeholder="请输入wifi密码" />
        </el-form-item>
        <el-form-item label="客服电话" prop="kefuPhone">
          <el-input v-model="form.kefuPhone" placeholder="请输入客服电话" />
        </el-form-item>
        <el-form-item label="企业微信webhook" prop="orderWebhook">
          <el-input v-model="form.orderWebhook" placeholder="请输入企业微信webhook地址" />
        </el-form-item>
        <!-- <el-form-item label="组局通知webhook" prop="gameWebhook">
          <el-input v-model="form.gameWebhook" placeholder="请输入组局通知webhook地址" />
        </el-form-item> -->
        <el-form-item label="抖音poi_id" prop="douyinPoiId">
          <el-input v-model="form.douyinPoiId" placeholder="请输入抖音poi_id" />
        </el-form-item>
        <el-form-item label="美团店铺uuid" prop="meituanOpenShopUuid">
          <el-input v-model="form.meituanOpenShopUuid" placeholder="请输入美团店铺uuid" />
        </el-form-item>
        <!--
        <el-form-item label="美团key" prop="meituanKey">
          <el-input v-model="form.meituanKey" placeholder="请输入美团key" />
        </el-form-item>
        <el-form-item label="美团Secret" prop="meituanSecret">
          <el-input v-model="form.meituanSecret" placeholder="请输入美团Secret" />
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 对话框 续费 -->
    <el-dialog title="门店续费" :visible.sync="openRenew" width="500px" v-dialogDrag append-to-body>
      <el-form ref="renewForm" :model="renewForm" :rules="renewRules" label-width="150px">
        <el-form-item label="门店名称" prop="storeName">
          <el-input v-model="renewForm.storeName" readonly/>
        </el-form-item>
        <el-form-item label="门店状态" prop="status">
          <el-select v-model="renewForm.status" placeholder="请选择门店状态">
            <el-option
            v-for="item in optionsStas"
            :key="item.id"
            :label="item.name"
            :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="到期时间" prop="expireTime">
          <el-input v-model="renewForm.expireTime" readonly />
        </el-form-item>
        到期时间剩余超过1个月不允许续费。每次续费将为门店增加一年有效期！
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRenew">确 定</el-button>
        <el-button @click="cancelRenew">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createStoreInfo, updateStoreInfo, deleteStoreInfo, getStoreInfo, getStoreInfoPage, exportStoreInfoExcel,getMeituanScope,renewStore } from "@/api/member/storeInfo";
import Editor from '@/components/Editor';

export default {
  name: "StoreInfo",
  components: {
    Editor,
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
      // 门店管理列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openRenew: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        storeName: null,
        cityName: null,
        address: null,
        status: null,
        createTime: [],
      },
      // 表单参数
      form: {
        roomNum: 1
      },
      renewForm: {
        storeId: undefined,
        expireTime: undefined,
        status: undefined,
      },
      // 表单校验
      rules: {
        storeName: [{ required: true, message: "门店名称不能为空", trigger: "blur" }],
        cityName: [{ required: true, message: "城市名称不能为空", trigger: "blur" }],
        status: [{ required: true, message: "门店状态不能为空", trigger: "change" }],
      },
      renewRules: {
        status: [{ required: true, message: "门店状态不能为空", trigger: "change" }],
      },
      optionsStas: [
        {
          name: "正常",
          id: 0,
        },
        {
          name: "审核中",
          id: 1,
        },
        {
          name: "已到期",
          id: 2,
        },
      ],
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
      getStoreInfoPage(this.queryParams).then(response => {
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
    cancelRenew() {
      this.openRenew = false;
      this.resetRenew();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        storeId: undefined,
        roomNum: undefined,
        mobile: undefined,
        storeName: undefined,
        cityName: undefined,
        content: undefined,
        notice: undefined,
        lat: undefined,
        lon: undefined,
        address: undefined,
        status: undefined,
        wifiInfo: undefined,
        wifiPwd: undefined,
        kefuPhone: undefined,
        wxWebhook: undefined,
        meituanOpenShopUuid: undefined,
        meituanKey: undefined,
        meituanSecret: undefined,
      };
      this.resetForm("form");
    },
    resetRenew() {
      this.renewForm = {
        storeId: undefined,
        storeName: undefined,
        expireTime: undefined,
        status: undefined,
      }
      this.resetForm("renewForm");
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
      this.title = "添加门店管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const storeId = row.storeId;
      getStoreInfo(storeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改门店管理";
      });
    },
    /** 续费按钮操作 */
    handleRenew(row) {
      this.renewForm = {
        storeId: row.storeId,
        storeName: row.storeName,
        expireTime: row.expireTime,
        status: row.status,
      }
      this.openRenew = true;
    },
    /** 美团授权按钮操作 */
    handleMeituan(row) {
      const storeId = row.storeId;
      getMeituanScope(storeId).then(response => {
        window.open(response.data, '_blank'); // 在新窗口中打开
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        this.form.expireTime= undefined;
        // 修改的提交
        if (this.form.storeId != null) {
          updateStoreInfo(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createStoreInfo(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    submitRenew(){
      this.$refs["renewForm"].validate(valid => {
        if (!valid) {
          return;
        }
        // 提交
        renewStore(this.renewForm).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.openRenew = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const storeId = row.storeId;
      this.$modal.confirm('是否确认删除门店管理编号为"' + storeId + '"的数据项?').then(function() {
          return deleteStoreInfo(storeId);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    statusFomat(row, column) {
      if (row.status == 0) {
        return "正常";
      } else if (row.status == 1) {
        return "审核中";
      } else {
        return "-";
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有门店管理数据项?').then(() => {
          this.exportLoading = true;
          return exportStoreInfoExcel(params);
        }).then(response => {
          this.$download.excel(response, '门店管理.xls');
          this.exportLoading = false;
        }).catch(() => {});
    }
  }
};
</script>
