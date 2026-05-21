import type {
  CourseSelection,
  GradeUpdateRequest,
  TeacherClassStudent,
  TeacherClassView,
  TeacherStatistics,
} from '@/types/api'
import { request } from '@/utils/request'

interface TeacherClassQuery {
  semester?: string
}

export function teacherListClasses(params?: TeacherClassQuery): Promise<TeacherClassView[]> {
  return request<TeacherClassView[]>({
    url: '/teacher/classes',
    method: 'GET',
    params,
  })
}

export function teacherListClassStudents(semester: string, courseId: string): Promise<TeacherClassStudent[]> {
  return request<TeacherClassStudent[]>({
    url: `/teacher/classes/${semester}/${courseId}/students`,
    method: 'GET',
  })
}

export function teacherUpdateGrade(payload: GradeUpdateRequest): Promise<CourseSelection> {
  return request<CourseSelection>({
    url: '/teacher/grades',
    method: 'PUT',
    data: payload,
  })
}

export function teacherStatistics(params?: TeacherClassQuery): Promise<TeacherStatistics> {
  return request<TeacherStatistics>({
    url: '/teacher/statistics',
    method: 'GET',
    params,
  })
}
