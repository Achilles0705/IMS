<script setup lang="ts">
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { ROLE_OPTIONS } from '@/constants/roles'
import { useAuthStore } from '@/stores/auth'
import type { LoginRequest } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const loading = ref(false)
const formRef = ref<FormInstance>()

const formModel = reactive<LoginRequest>({
  username: '',
  password: '',
  role: 'ADMIN',
})

const rules: FormRules<LoginRequest> = {
  username: [{ required: true, message: '请输入登录账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
}

async function handleLogin() {
  if (!formRef.value) {
    return
  }

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    return
  }

  loading.value = true
  try {
    await authStore.loginByPassword(formModel)
    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    await router.replace(redirect)
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="header">
          <h2>教学事务管理系统</h2>
          <p>Vue 3 + Element Plus 前端基础框架</p>
        </div>
      </template>

      <el-form ref="formRef" :model="formModel" :rules="rules" label-position="top">
        <el-form-item label="登录账号" prop="username">
          <el-input v-model="formModel.username" placeholder="请输入 username" />
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="formModel.password" type="password" show-password placeholder="请输入 password" />
        </el-form-item>
        <el-form-item label="登录角色" prop="role">
          <el-select v-model="formModel.role" placeholder="请选择角色">
            <el-option v-for="item in ROLE_OPTIONS" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-alert
        type="info"
        show-icon
        :closable="false"
        title="当前为初始框架，登录逻辑已对接 /api/auth/login，具体账号以数据库和后端实现为准。"
      />

      <el-button class="submit" type="primary" :loading="loading" @click="handleLogin">
        登录并进入系统
      </el-button>
    </el-card>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #eef2ff 0%, #f8fbff 100%);
}

.login-card {
  width: 440px;
}

.header h2 {
  margin: 0;
}

.header p {
  margin: 8px 0 0;
  color: #667085;
}

.submit {
  width: 100%;
  margin-top: 16px;
}
</style>
