<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

import PageContainer from '@/components/common/PageContainer.vue'
import { useAuthStore } from '@/stores/auth'
import type { LoginUser } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const authStore = useAuthStore()
const profile = ref<LoginUser | null>(null)
const loading = ref(false)

async function loadProfile() {
  loading.value = true
  try {
    profile.value = await authStore.refreshProfile()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('加载个人信息失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<template>
  <PageContainer title="个人信息" description="用于展示登录态与基础身份信息。">
    <el-card v-loading="loading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="userId">{{ profile?.userId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="username">{{ profile?.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="displayName">{{ profile?.displayName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="role">{{ profile?.role || '-' }}</el-descriptions-item>
        <el-descriptions-item label="relatedId">{{ profile?.relatedId || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </PageContainer>
</template>
