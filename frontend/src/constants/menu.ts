import type { Role } from '@/types/api'

export interface MenuItem {
  title: string
  path: string
}

export const ROLE_MENU_MAP: Record<Role, MenuItem[]> = {
  ADMIN: [
    { title: '管理员首页', path: '/admin/dashboard' },
    { title: '学生管理', path: '/admin/students' },
    { title: '教师管理', path: '/admin/teachers' },
    { title: '课程管理', path: '/admin/courses' },
    { title: '开课管理', path: '/admin/classes' },
    { title: '统计分析', path: '/admin/statistics' },
  ],
  STUDENT: [
    { title: '学生首页', path: '/student/dashboard' },
    { title: '可选课程', path: '/student/classes' },
    { title: '我的选课', path: '/student/selections' },
    { title: '我的成绩', path: '/student/grades' },
    { title: '个人统计', path: '/student/statistics' },
    { title: '学分汇总', path: '/student/credit-summary' },
  ],
  TEACHER: [
    { title: '教师首页', path: '/teacher/dashboard' },
    { title: '授课列表', path: '/teacher/classes' },
    { title: '课程学生', path: '/teacher/class-students' },
    { title: '成绩录入', path: '/teacher/grades' },
    { title: '教学统计', path: '/teacher/statistics' },
  ],
}
