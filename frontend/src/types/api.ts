export type Role = 'ADMIN' | 'TEACHER' | 'STUDENT'

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

export interface AuthSnapshot {
  token: string
  userId: string
  role: Role
  relatedId: string
  displayName: string
}

export interface LoginRequest {
  username: string
  password: string
  role: Role
}

export interface LoginUser {
  userId: string
  username: string
  role: Role
  relatedId: string | null
  displayName: string
}

export interface HealthData {
  status: string
  message: string
}

export interface DatabaseHealthData extends HealthData {
  departmentCount: number
}

export interface Department {
  deptId: string
  deptName: string
  address: string
  phoneCode: string
}

export interface Student {
  studentId: string
  name: string
  sex: '男' | '女'
  dateOfBirth: string
  nativePlace: string
  mobilePhone: string
  deptId: string
  status: string
}

export interface StudentRequest {
  studentId: string
  name: string
  sex?: '男' | '女'
  dateOfBirth?: string
  nativePlace?: string
  mobilePhone?: string
  deptId?: string
  status?: string
}

export interface Teacher {
  staffId: string
  name: string
  sex: '男' | '女'
  dateOfBirth: string
  professionalTitle: string
  salary: number
  deptId: string
}

export interface TeacherRequest {
  staffId: string
  name: string
  sex?: '男' | '女'
  dateOfBirth?: string
  professionalTitle?: string
  salary?: number
  deptId?: string
}

export interface Course {
  courseId: string
  courseName: string
  credit: number
  creditHours: number
  deptId: string
}

export interface CourseRequest {
  courseId: string
  courseName: string
  credit?: number
  creditHours?: number
  deptId?: string
}

export interface ClassInfo {
  semester: string
  courseId: string
  staffId: string
  classTime: string
}

export interface ClassInfoRequest {
  semester: string
  courseId: string
  staffId: string
  classTime?: string
}

export interface ClassInfoUpdateRequest {
  classTime?: string
}

export interface CourseSelection {
  studentId: string
  semester: string
  courseId: string
  staffId: string
  score: number | null
}

export interface SelectionRequest {
  semester: string
  courseId: string
  staffId: string
}

export interface DeleteResult {
  deleted: boolean
}

export interface StudentClassOption {
  semester: string
  courseId: string
  courseName: string
  credit: number
  creditHours: number
  staffId: string
  teacherName: string
  classTime: string
  selected: boolean
}

export interface StudentSelectionView {
  studentId: string
  semester: string
  courseId: string
  courseName: string
  credit: number
  staffId: string
  teacherName: string
  classTime: string
  score: number | null
}

export interface StudentGradeView {
  semester: string
  courseId: string
  courseName: string
  credit: number
  staffId: string
  teacherName: string
  score: number | null
  passed: boolean
}

export interface StudentStatistics {
  selectedCourseCount: number
  gradedCourseCount: number
  passedCourseCount: number
  failedCourseCount: number
  earnedCredits: number
  averageScore: number | null
}

export interface StudentCreditSummary {
  studentId: string
  passedCourseCount: number
  earnedCredits: number
  averageScore: number | null
}

export interface TeacherClassView {
  semester: string
  courseId: string
  courseName: string
  credit: number
  staffId: string
  classTime: string
  studentCount: number
}

export interface TeacherClassStudent {
  studentId: string
  studentName: string
  sex: '男' | '女'
  deptId: string
  mobilePhone: string
  score: number | null
}

export interface GradeUpdateRequest {
  studentId: string
  semester: string
  courseId: string
  score: number
}

export interface TeacherStatistics {
  classCount: number
  studentCount: number
  gradedCount: number
  ungradedCount: number
  averageScore: number | null
  failedCount: number
}

export interface AdminOverviewStatistics {
  departmentCount: number
  studentCount: number
  teacherCount: number
  courseCount: number
  classCount: number
  selectionCount: number
}

export interface CourseGradeStatistics {
  semester: string
  courseId: string
  courseName: string
  studentCount: number
  gradedCount: number
  averageScore: number | null
  maxScore: number | null
  minScore: number | null
  failedCount: number
}
