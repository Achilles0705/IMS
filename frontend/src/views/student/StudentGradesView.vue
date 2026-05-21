<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'

import { studentListGrades } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { StudentGradeView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const grades = ref<StudentGradeView[]>([])

const query = reactive({
  semester: '',
})

async function loadGrades() {
  loading.value = true
  try {
    grades.value = await studentListGrades({
      semester: query.semester || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询成绩失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <PageContainer title="我的成绩" description="对应 /api/student/grades 查询接口骨架。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-input v-model="query.semester" clearable placeholder="不填查询全部" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadGrades">查询成绩</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="grades" border>
        <el-table-column prop="semester" label="学期" width="130" />
        <el-table-column prop="courseId" label="课程编号" width="130" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column prop="teacherName" label="任课教师" width="140" />
        <el-table-column prop="score" label="成绩" width="100" />
        <el-table-column prop="passed" label="是否通过" width="110">
          <template #default="{ row }">
            <el-tag :type="row.passed ? 'success' : 'danger'">{{ row.passed ? '通过' : '未通过' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </PageContainer>
</template>
