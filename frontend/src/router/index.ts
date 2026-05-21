import { createRouter, createWebHistory } from 'vue-router'

import { setupRouterGuards } from '@/router/guards'
import { routes } from '@/router/routes'

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  },
})

setupRouterGuards(router)

export default router
