import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: {
      title: '登录',
      requiresAuth: false,
    },
  },
  {
    path: '/403',
    name: 'forbidden',
    component: () => import('@/views/error/ForbiddenView.vue'),
    meta: {
      title: '无权限访问',
      requiresAuth: false,
    },
  },
  {
    path: '/',
    component: () => import('@/layout/AppLayout.vue'),
    meta: {
      requiresAuth: true,
    },
    children: [
      {
        path: '',
        redirect: '/dashboard',
      },
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/DashboardRedirectView.vue'),
        meta: {
          title: '首页',
          requiresAuth: true,
        },
      },
      {
        path: 'profile',
        name: 'profile',
        component: () => import('@/views/profile/ProfileView.vue'),
        meta: {
          title: '个人信息',
          requiresAuth: true,
        },
      },
      {
        path: 'admin/dashboard',
        name: 'admin-dashboard',
        component: () => import('@/views/admin/AdminDashboardView.vue'),
        meta: {
          title: '管理员首页',
          requiresAuth: true,
          roles: ['ADMIN'],
        },
      },
      {
        path: 'admin/students',
        name: 'admin-students',
        component: () => import('@/views/admin/AdminStudentsView.vue'),
        meta: {
          title: '学生管理',
          requiresAuth: true,
          roles: ['ADMIN'],
        },
      },
      {
        path: 'admin/teachers',
        name: 'admin-teachers',
        component: () => import('@/views/admin/AdminTeachersView.vue'),
        meta: {
          title: '教师管理',
          requiresAuth: true,
          roles: ['ADMIN'],
        },
      },
      {
        path: 'admin/courses',
        name: 'admin-courses',
        component: () => import('@/views/admin/AdminCoursesView.vue'),
        meta: {
          title: '课程管理',
          requiresAuth: true,
          roles: ['ADMIN'],
        },
      },
      {
        path: 'admin/classes',
        name: 'admin-classes',
        component: () => import('@/views/admin/AdminClassesView.vue'),
        meta: {
          title: '开课管理',
          requiresAuth: true,
          roles: ['ADMIN'],
        },
      },
      {
        path: 'admin/statistics',
        name: 'admin-statistics',
        component: () => import('@/views/admin/AdminStatisticsView.vue'),
        meta: {
          title: '统计分析',
          requiresAuth: true,
          roles: ['ADMIN'],
        },
      },
      {
        path: 'student/dashboard',
        name: 'student-dashboard',
        component: () => import('@/views/student/StudentDashboardView.vue'),
        meta: {
          title: '学生首页',
          requiresAuth: true,
          roles: ['STUDENT'],
        },
      },
      {
        path: 'student/classes',
        name: 'student-classes',
        component: () => import('@/views/student/StudentClassesView.vue'),
        meta: {
          title: '可选课程',
          requiresAuth: true,
          roles: ['STUDENT'],
        },
      },
      {
        path: 'student/selections',
        name: 'student-selections',
        component: () => import('@/views/student/StudentSelectionsView.vue'),
        meta: {
          title: '我的选课',
          requiresAuth: true,
          roles: ['STUDENT'],
        },
      },
      {
        path: 'student/grades',
        name: 'student-grades',
        component: () => import('@/views/student/StudentGradesView.vue'),
        meta: {
          title: '我的成绩',
          requiresAuth: true,
          roles: ['STUDENT'],
        },
      },
      {
        path: 'student/statistics',
        name: 'student-statistics',
        component: () => import('@/views/student/StudentStatisticsView.vue'),
        meta: {
          title: '个人统计',
          requiresAuth: true,
          roles: ['STUDENT'],
        },
      },
      {
        path: 'student/credit-summary',
        name: 'student-credit-summary',
        component: () => import('@/views/student/StudentCreditSummaryView.vue'),
        meta: {
          title: '学分汇总',
          requiresAuth: true,
          roles: ['STUDENT'],
        },
      },
      {
        path: 'teacher/dashboard',
        name: 'teacher-dashboard',
        component: () => import('@/views/teacher/TeacherDashboardView.vue'),
        meta: {
          title: '教师首页',
          requiresAuth: true,
          roles: ['TEACHER'],
        },
      },
      {
        path: 'teacher/classes',
        name: 'teacher-classes',
        component: () => import('@/views/teacher/TeacherClassesView.vue'),
        meta: {
          title: '授课列表',
          requiresAuth: true,
          roles: ['TEACHER'],
        },
      },
      {
        path: 'teacher/class-students',
        name: 'teacher-class-students',
        component: () => import('@/views/teacher/TeacherClassStudentsView.vue'),
        meta: {
          title: '课程学生',
          requiresAuth: true,
          roles: ['TEACHER'],
        },
      },
      {
        path: 'teacher/class-students/:semester/:courseId',
        name: 'teacher-class-students-detail',
        component: () => import('@/views/teacher/TeacherClassStudentsView.vue'),
        meta: {
          title: '课程学生详情',
          requiresAuth: true,
          roles: ['TEACHER'],
          hiddenInMenu: true,
        },
      },
      {
        path: 'teacher/grades',
        name: 'teacher-grades',
        component: () => import('@/views/teacher/TeacherGradesView.vue'),
        meta: {
          title: '成绩录入',
          requiresAuth: true,
          roles: ['TEACHER'],
        },
      },
      {
        path: 'teacher/statistics',
        name: 'teacher-statistics',
        component: () => import('@/views/teacher/TeacherStatisticsView.vue'),
        meta: {
          title: '教学统计',
          requiresAuth: true,
          roles: ['TEACHER'],
        },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('@/views/error/NotFoundView.vue'),
    meta: {
      title: '页面不存在',
      requiresAuth: false,
    },
  },
]
