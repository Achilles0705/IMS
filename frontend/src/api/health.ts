import type { DatabaseHealthData, HealthData } from '@/types/api'
import { request } from '@/utils/request'

export function getHealth(): Promise<HealthData> {
  return request<HealthData>({
    url: '/health',
    method: 'GET',
  })
}

export function getDatabaseHealth(): Promise<DatabaseHealthData> {
  return request<DatabaseHealthData>({
    url: '/health/db',
    method: 'GET',
  })
}
