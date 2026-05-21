import type { ClassInfo, ClassInfoRequest, ClassInfoUpdateRequest } from '@/types/api'
import { request } from '@/utils/request'

interface ClassQuery {
  semester?: string
  courseId?: string
  staffId?: string
}

export function adminListClasses(params?: ClassQuery): Promise<ClassInfo[]> {
  return request<ClassInfo[]>({
    url: '/admin/classes',
    method: 'GET',
    params,
  })
}

export function adminCreateClass(payload: ClassInfoRequest): Promise<ClassInfo> {
  return request<ClassInfo>({
    url: '/admin/classes',
    method: 'POST',
    data: payload,
  })
}

export function adminUpdateClass(
  semester: string,
  courseId: string,
  staffId: string,
  payload: ClassInfoUpdateRequest,
): Promise<ClassInfo> {
  return request<ClassInfo>({
    url: `/admin/classes/${semester}/${courseId}/${staffId}`,
    method: 'PUT',
    data: payload,
  })
}
