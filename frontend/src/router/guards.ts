import { ElMessage } from 'element-plus'
import type { Router } from 'vue-router'

import type { Role } from '@/types/api'
import { useAuthStore } from '@/stores/auth'

function getRoleHomePath(role: Role | ''): string {
  if (role === 'ADMIN') {
    return '/admin/dashboard'
  }
  if (role === 'TEACHER') {
    return '/teacher/dashboard'
  }
  if (role === 'STUDENT') {
    return '/student/dashboard'
  }
  return '/login'
}

export function setupRouterGuards(router: Router): void {
  let hydrated = false

  router.beforeEach(async (to) => {
    const authStore = useAuthStore()

    if (!hydrated) {
      authStore.hydrateFromStorage()
      hydrated = true
    }

    const requiresAuth = to.meta.requiresAuth !== false
    if (!requiresAuth) {
      if (to.path === '/login' && authStore.isLoggedIn) {
        return getRoleHomePath(authStore.role)
      }
      return true
    }

    if (!authStore.isLoggedIn) {
      return {
        path: '/login',
        query: { redirect: to.fullPath },
      }
    }

    if (!authStore.profileLoaded) {
      try {
        await authStore.refreshProfile()
      } catch (error) {
        authStore.clearAuth()
        ElMessage.error('登录状态已失效，请重新登录')
        return {
          path: '/login',
          query: { redirect: to.fullPath },
        }
      }
    }

    const allowedRoles = to.meta.roles as Role[] | undefined
    if (allowedRoles && authStore.role && !allowedRoles.includes(authStore.role)) {
      return '/403'
    }

    if (to.path === '/dashboard') {
      return getRoleHomePath(authStore.role)
    }

    return true
  })

  router.afterEach((to) => {
    const title = typeof to.meta.title === 'string' ? `${to.meta.title} - IMS` : 'IMS'
    document.title = title
  })
}
