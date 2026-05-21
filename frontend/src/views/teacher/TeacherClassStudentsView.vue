<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

import { teacherListClassStudents } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { TeacherClassStudent } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const route = useRoute()
const loading = ref(false)
const students = ref<TeacherClassStudent[]>([])

const query = reactive({
  semester: '',
  courseId: '',
})

async function loadStudents() {
  if (!query.semester || !query.courseId) {
    ElMessage.warning('请先填写 semester 和 courseId')
    return
  }

  loading.value = true
  try {
    students.value = await teacherListClassStudents(query.semester, query.courseId)
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询课程学生失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const semester = route.params.semester
  const courseId = route.params.courseId

  if (typeof semester === 'string') {
    query.semester = semester
  }
  if (typeof courseId === 'string') {
    query.courseId = courseId
  }

  if (query.semester && query.courseId) {
    loadStudents()
  }
})
</script>

<template>
  <PageContainer
    title="课程学生名单"
    description="对应 /api/teacher/classes/{semester}/{courseId}/students 查询接口骨架。"
  >
    <el-card>
      <el-form :inline="true">
        <el-form-item label="semester">
          <el-input v-model="query.semester" placeholder="semester" style="width: 180px" />
        </el-form-item>
        <el-form-item label="courseId">
          <el-input v-model="query.courseId" placeholder="courseId" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStudents">查询学生</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="students" border>
        <el-table-column prop="studentId" label="学号" width="130" />
        <el-table-column prop="studentName" label="姓名" width="130" />
        <el-table-column prop="sex" label="性别" width="90" />
        <el-table-column prop="deptId" label="院系" width="120" />
        <el-table-column prop="mobilePhone" label="手机号" width="160" />
        <el-table-column prop="score" label="成绩" width="100" />
      </el-table>
    </el-card>
  </PageContainer>
</template>
