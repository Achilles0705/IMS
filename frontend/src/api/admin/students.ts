import type { Student, StudentRequest } from '@/types/api'
import { request } from '@/utils/request'

interface StudentQuery {
  deptId?: string
  keyword?: string
}

export function adminListStudents(params?: StudentQuery): Promise<Student[]> {
  return request<Student[]>({
    url: '/admin/students',
    method: 'GET',
    params,
  })
}

export function adminCreateStudent(payload: StudentRequest): Promise<Student> {
  return request<Student>({
    url: '/admin/students',
    method: 'POST',
    data: payload,
  })
}

export function adminUpdateStudent(studentId: string, payload: StudentRequest): Promise<Student> {
  return request<Student>({
    url: `/admin/students/${studentId}`,
    method: 'PUT',
    data: payload,
  })
}
