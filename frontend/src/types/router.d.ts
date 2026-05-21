import 'vue-router'

import type { Role } from '@/types/api'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    requiresAuth?: boolean
    roles?: Role[]
    hiddenInMenu?: boolean
  }
}
