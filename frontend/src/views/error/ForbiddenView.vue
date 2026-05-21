<template>
  <div class="error-page">
    <el-result icon="warning" title="403 无权限访问" sub-title="当前账号没有访问该页面的权限，请联系管理员。">
      <template #extra>
        <el-button type="primary" @click="$router.push('/dashboard')">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<style scoped>
.error-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
}
</style>
