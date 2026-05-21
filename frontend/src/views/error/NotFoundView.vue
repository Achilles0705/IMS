<template>
  <div class="error-page">
    <el-result icon="info" title="404 页面不存在" sub-title="请检查地址是否正确，或从左侧菜单重新进入。">
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
