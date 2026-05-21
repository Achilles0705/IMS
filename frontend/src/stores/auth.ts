import { defineStore } from 'pinia'

import { getProfile, login } from '@/api/auth'
import { ROLE_LABEL_MAP } from '@/constants/roles'
import type { AuthSnapshot, LoginRequest, LoginUser, Role } from '@/types/api'
import { clearAuthSnapshot, getAuthSnapshot, saveAuthSnapshot } from '@/utils/storage'

interface AuthState {
  token: string
  userId: string
  role: Role | ''
  relatedId: string
  username: string
  displayName: string
  profileLoaded: boolean
}

function createToken(userId: string, role: Role): string {
  return `${role}-${userId}-${Date.now()}`
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    token: '',
    userId: '',
    role: '',
    relatedId: '',
    username: '',
    displayName: '',
    profileLoaded: false,
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.userId && state.role),
    roleLabel: (state) => {
      if (!state.role) {
        return ''
      }
      return ROLE_LABEL_MAP[state.role]
    },
  },
  actions: {
    hydrateFromStorage() {
      const snapshot = getAuthSnapshot()
      if (!snapshot) {
        return
      }

      this.token = snapshot.token
      this.userId = snapshot.userId
      this.role = snapshot.role
      this.relatedId = snapshot.relatedId
      this.displayName = snapshot.displayName
      this.profileLoaded = false
    },
    setFromLoginUser(user: LoginUser) {
      const role = user.role
      const snapshot: AuthSnapshot = {
        token: this.token || createToken(user.userId, role),
        userId: user.userId,
        role,
        relatedId: user.relatedId || '',
        displayName: user.displayName || user.username,
      }

      this.token = snapshot.token
      this.userId = snapshot.userId
      this.role = snapshot.role
      this.relatedId = snapshot.relatedId
      this.username = user.username
      this.displayName = snapshot.displayName
      this.profileLoaded = true
      saveAuthSnapshot(snapshot)
    },
    clearAuth() {
      this.$reset()
      clearAuthSnapshot()
    },
    async loginByPassword(payload: LoginRequest): Promise<LoginUser> {
      const user = await login(payload)
      this.setFromLoginUser(user)
      return user
    },
    async refreshProfile(): Promise<LoginUser | null> {
      if (!this.userId || !this.role) {
        return null
      }

      const user = await getProfile()
      this.setFromLoginUser(user)
      return user
    },
  },
})
