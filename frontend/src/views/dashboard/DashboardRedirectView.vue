<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

onMounted(async () => {
  if (authStore.role === 'ADMIN') {
    await router.replace('/admin/dashboard')
    return
  }
  if (authStore.role === 'TEACHER') {
    await router.replace('/teacher/dashboard')
    return
  }
  if (authStore.role === 'STUDENT') {
    await router.replace('/student/dashboard')
    return
  }
  await router.replace('/login')
})
</script>

<template>
  <el-card>
    <el-skeleton :rows="3" animated />
  </el-card>
</template>
