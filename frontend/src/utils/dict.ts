import type { Course, Department, Teacher } from '@/types/api'

export function getDeptName(deptId: string | undefined, departments: Department[]): string {
  if (!deptId) {
    return '-'
  }
  return departments.find((item) => item.deptId === deptId)?.deptName ?? deptId
}

export function getCourseName(courseId: string | undefined, courses: Course[]): string {
  if (!courseId) {
    return '-'
  }
  return courses.find((item) => item.courseId === courseId)?.courseName ?? courseId
}

export function getTeacherName(staffId: string | undefined, teachers: Teacher[]): string {
  if (!staffId) {
    return '-'
  }
  return teachers.find((item) => item.staffId === staffId)?.name ?? staffId
}
