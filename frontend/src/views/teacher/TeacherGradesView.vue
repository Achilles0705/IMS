<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive } from 'vue'

import { teacherUpdateGrade } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { GradeUpdateRequest } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const gradeModel = reactive<GradeUpdateRequest>({
  studentId: '',
  semester: '',
  courseId: '',
  score: 0,
})

async function submitGrade() {
  try {
    await teacherUpdateGrade(gradeModel)
    ElMessage.success('成绩更新请求已发送')
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('更新成绩失败')
  }
}
</script>

<template>
  <PageContainer title="成绩录入" description="对应 /api/teacher/grades 的录分接口骨架。">
    <el-card>
      <el-form :model="gradeModel" label-width="120">
        <el-form-item label="学生编号">
          <el-input v-model="gradeModel.studentId" placeholder="studentId" />
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="gradeModel.semester" placeholder="semester" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="gradeModel.courseId" placeholder="courseId" />
        </el-form-item>
        <el-form-item label="成绩">
          <el-input-number v-model="gradeModel.score" :min="0" :max="100" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitGrade">调用录分接口</el-button>
        </el-form-item>
      </el-form>
      <el-alert title="当前页面为骨架实现，后续可扩展为“班级名单联动录分”。" type="info" :closable="false" />
    </el-card>
  </PageContainer>
</template>
