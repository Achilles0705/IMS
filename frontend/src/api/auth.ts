import type { LoginRequest, LoginUser } from '@/types/api'
import { request } from '@/utils/request'

export function login(payload: LoginRequest): Promise<LoginUser> {
  return request<LoginUser>({
    url: '/auth/login',
    method: 'POST',
    data: payload,
  })
}

export function getProfile(): Promise<LoginUser> {
  return request<LoginUser>({
    url: '/auth/profile',
    method: 'GET',
  })
}
