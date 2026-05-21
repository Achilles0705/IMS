import type { Role } from '@/types/api'

export const ROLE_LABEL_MAP: Record<Role, string> = {
  ADMIN: '系统管理员',
  TEACHER: '教师',
  STUDENT: '学生',
}

export const ROLE_OPTIONS: Array<{ label: string; value: Role }> = [
  { label: '系统管理员', value: 'ADMIN' },
  { label: '教师', value: 'TEACHER' },
  { label: '学生', value: 'STUDENT' },
]
