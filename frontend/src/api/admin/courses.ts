import type { Course, CourseRequest } from '@/types/api'
import { request } from '@/utils/request'

interface AdminCourseQuery {
  deptId?: string
  keyword?: string
}

export function adminListCourses(params?: AdminCourseQuery): Promise<Course[]> {
  return request<Course[]>({
    url: '/admin/courses',
    method: 'GET',
    params,
  })
}

export function adminCreateCourse(payload: CourseRequest): Promise<Course> {
  return request<Course>({
    url: '/admin/courses',
    method: 'POST',
    data: payload,
  })
}

export function adminUpdateCourse(courseId: string, payload: CourseRequest): Promise<Course> {
  return request<Course>({
    url: `/admin/courses/${courseId}`,
    method: 'PUT',
    data: payload,
  })
}
