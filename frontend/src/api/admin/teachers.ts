import type { Teacher, TeacherRequest } from '@/types/api'
import { request } from '@/utils/request'

interface TeacherQuery {
  deptId?: string
  keyword?: string
}

export function adminListTeachers(params?: TeacherQuery): Promise<Teacher[]> {
  return request<Teacher[]>({
    url: '/admin/teachers',
    method: 'GET',
    params,
  })
}

export function adminCreateTeacher(payload: TeacherRequest): Promise<Teacher> {
  return request<Teacher>({
    url: '/admin/teachers',
    method: 'POST',
    data: payload,
  })
}

export function adminUpdateTeacher(staffId: string, payload: TeacherRequest): Promise<Teacher> {
  return request<Teacher>({
    url: `/admin/teachers/${staffId}`,
    method: 'PUT',
    data: payload,
  })
}
