import type {
  CourseSelection,
  DeleteResult,
  SelectionRequest,
  StudentClassOption,
  StudentCreditSummary,
  StudentGradeView,
  StudentSelectionView,
  StudentStatistics,
} from '@/types/api'
import { request } from '@/utils/request'

interface SemesterQuery {
  semester?: string
}

export function studentListAvailableClasses(params: { semester: string }): Promise<StudentClassOption[]> {
  return request<StudentClassOption[]>({
    url: '/student/classes',
    method: 'GET',
    params,
  })
}

export function studentListSelections(params?: SemesterQuery): Promise<StudentSelectionView[]> {
  return request<StudentSelectionView[]>({
    url: '/student/selections',
    method: 'GET',
    params,
  })
}

export function studentCreateSelection(payload: SelectionRequest): Promise<CourseSelection> {
  return request<CourseSelection>({
    url: '/student/selections',
    method: 'POST',
    data: payload,
  })
}

export function studentDeleteSelection(payload: SelectionRequest): Promise<DeleteResult> {
  return request<DeleteResult>({
    url: '/student/selections',
    method: 'DELETE',
    data: payload,
  })
}

export function studentListGrades(params?: SemesterQuery): Promise<StudentGradeView[]> {
  return request<StudentGradeView[]>({
    url: '/student/grades',
    method: 'GET',
    params,
  })
}

export function studentStatistics(): Promise<StudentStatistics> {
  return request<StudentStatistics>({
    url: '/student/statistics',
    method: 'GET',
  })
}

export function studentCreditSummary(): Promise<StudentCreditSummary> {
  return request<StudentCreditSummary>({
    url: '/student/credit-summary',
    method: 'GET',
  })
}
