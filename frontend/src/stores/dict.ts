import { defineStore } from 'pinia'

import { listCourses, listDepartments } from '@/api/common'
import type { Course, Department } from '@/types/api'

interface DictState {
  departments: Department[]
  courses: Course[]
  loading: boolean
}

export const useDictStore = defineStore('dict', {
  state: (): DictState => ({
    departments: [],
    courses: [],
    loading: false,
  }),
  actions: {
    async loadDepartments(force = false) {
      if (!force && this.departments.length > 0) {
        return this.departments
      }

      this.loading = true
      try {
        this.departments = await listDepartments()
      } finally {
        this.loading = false
      }
      return this.departments
    },
    async loadCourses(force = false) {
      if (!force && this.courses.length > 0) {
        return this.courses
      }

      this.loading = true
      try {
        this.courses = await listCourses()
      } finally {
        this.loading = false
      }
      return this.courses
    },
  },
})
