import type { AuthSnapshot } from '@/types/api'

const AUTH_STORAGE_KEY = 'ims-auth-snapshot'

export function saveAuthSnapshot(snapshot: AuthSnapshot): void {
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(snapshot))
}

export function getAuthSnapshot(): AuthSnapshot | null {
  const raw = localStorage.getItem(AUTH_STORAGE_KEY)
  if (!raw) {
    return null
  }

  try {
    const parsed = JSON.parse(raw) as AuthSnapshot
    if (!parsed.userId || !parsed.role) {
      return null
    }
    return parsed
  } catch {
    return null
  }
}

export function clearAuthSnapshot(): void {
  localStorage.removeItem(AUTH_STORAGE_KEY)
}
