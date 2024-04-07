<template>
  <div>
    <h1>四川彦祖科技有限公司</h1>
    <h2>自助预定后台管理系统</h2>
  <el-col :span="3">
      <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleIotScope"
                 v-hasPermi="['infra:config:update']">硬件平台授权
      </el-button>
    </el-col>
  </div>
</template>

<style lang="scss" scoped>
.h1 {
  text-align: center;
}


</style>
<script>
import { getIotScope } from "@/api/member/deviceInfo";

export default {
  name: "DeviceInfo",
  components: {},
  data() {
    return {
    }
  },
  methods: {
    handleIotScope() {
      getIotScope().then(response => {
        this.$modal.msgSuccess("操作成功");
      });
    },
  }
};
</script>



