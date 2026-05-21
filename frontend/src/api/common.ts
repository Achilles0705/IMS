import type { Course, Department } from '@/types/api'
import { request } from '@/utils/request'

interface CourseQuery {
  deptId?: string
  keyword?: string
}

export function listDepartments(): Promise<Department[]> {
  return request<Department[]>({
    url: '/departments',
    method: 'GET',
  })
}

export function listCourses(params?: CourseQuery): Promise<Course[]> {
  return request<Course[]>({
    url: '/courses',
    method: 'GET',
    params,
  })
}
