<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { ROLE_MENU_MAP } from '@/constants/menu'
import { useAppStore } from '@/stores/app'
import { useAuthStore } from '@/stores/auth'
import { useDictStore } from '@/stores/dict'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const authStore = useAuthStore()
const dictStore = useDictStore()

const menuItems = computed(() => {
  if (!authStore.role) {
    return []
  }
  return ROLE_MENU_MAP[authStore.role]
})

const activePath = computed(() => {
  if (route.path.startsWith('/teacher/class-students/')) {
    return '/teacher/class-students'
  }
  return route.path
})

function navigate(path: string): void {
  if (path === route.path) {
    return
  }
  router.push(path)
}

function toProfile(): void {
  router.push('/profile')
}

function logout(): void {
  authStore.clearAuth()
  router.push('/login')
}

onMounted(() => {
  dictStore.loadDepartments()
  dictStore.loadCourses()
})
</script>

<template>
  <el-container class="layout-root">
    <el-aside :width="appStore.sidebarCollapsed ? '72px' : '220px'" class="layout-aside">
      <div class="logo" @click="navigate('/dashboard')">
        {{ appStore.sidebarCollapsed ? 'IMS' : '教学事务 IMS' }}
      </div>
      <el-menu :default-active="activePath" class="menu" @select="navigate">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-button text @click="appStore.toggleSidebar">
            {{ appStore.sidebarCollapsed ? '展开菜单' : '收起菜单' }}
          </el-button>
        </div>
        <div class="header-right">
          <el-tag type="info">{{ authStore.roleLabel }}</el-tag>
          <span class="username">{{ authStore.displayName || authStore.userId }}</span>
          <el-button text @click="toProfile">个人信息</el-button>
          <el-button type="danger" plain @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-root {
  min-height: 100vh;
}

.layout-aside {
  border-right: 1px solid #e4e7ed;
  background: #ffffff;
  transition: width 0.2s ease;
}

.logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
}

.menu {
  border-right: none;
}

.layout-header {
  border-bottom: 1px solid #e4e7ed;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  color: #344054;
}

.layout-main {
  background: #f5f7fa;
  padding: 20px;
}
</style>
