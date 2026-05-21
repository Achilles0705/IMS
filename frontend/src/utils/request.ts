import axios, { AxiosError, AxiosHeaders, type AxiosRequestConfig } from 'axios'

import type { ApiResult } from '@/types/api'
import { getAuthSnapshot } from '@/utils/storage'

export class ApiBusinessError extends Error {
  code: number

  constructor(code: number, message: string) {
    super(message)
    this.name = 'ApiBusinessError'
    this.code = code
  }
}

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
})

http.interceptors.request.use((config) => {
  const snapshot = getAuthSnapshot()
  if (!snapshot) {
    return config
  }

  const headers = AxiosHeaders.from(config.headers)
  headers.set('X-User-Id', snapshot.userId)
  headers.set('X-Role', snapshot.role)
  if (snapshot.relatedId) {
    headers.set('X-Related-Id', snapshot.relatedId)
  }

  config.headers = headers

  return config
})

function normalizeAxiosError(error: AxiosError<ApiResult<unknown>>): ApiBusinessError {
  const status = error.response?.status
  const messageFromBody = error.response?.data?.message
  const message = messageFromBody || error.message || '网络请求失败'
  return new ApiBusinessError(status || -1, message)
}

export async function request<T>(config: AxiosRequestConfig): Promise<T> {
  try {
    const response = await http.request<ApiResult<T>>(config)
    const body = response.data

    if (!body || typeof body.code !== 'number') {
      throw new ApiBusinessError(-1, '接口响应格式错误')
    }

    if (body.code !== 200) {
      throw new ApiBusinessError(body.code, body.message || '请求失败')
    }

    return body.data
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      throw error
    }

    if (axios.isAxiosError(error)) {
      throw normalizeAxiosError(error)
    }

    throw new ApiBusinessError(-1, '未知异常')
  }
}
