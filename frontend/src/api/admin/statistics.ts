import type { AdminOverviewStatistics, CourseGradeStatistics } from '@/types/api'
import { request } from '@/utils/request'

interface CourseGradeStatisticsQuery {
  semester?: string
  courseId?: string
}

export function adminStatisticsOverview(): Promise<AdminOverviewStatistics> {
  return request<AdminOverviewStatistics>({
    url: '/admin/statistics/overview',
    method: 'GET',
  })
}

export function adminCourseGradeStatistics(params?: CourseGradeStatisticsQuery): Promise<CourseGradeStatistics[]> {
  return request<CourseGradeStatistics[]>({
    url: '/admin/statistics/course-grades',
    method: 'GET',
    params,
  })
}
