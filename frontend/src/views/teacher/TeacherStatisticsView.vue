<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'

import { teacherStatistics } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { TeacherStatistics } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const statistics = ref<TeacherStatistics | null>(null)

const query = reactive({
  semester: '',
})

async function loadStatistics() {
  loading.value = true
  try {
    statistics.value = await teacherStatistics({
      semester: query.semester || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询教学统计失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <PageContainer title="教学统计" description="对应 /api/teacher/statistics 的查询页面骨架。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-input v-model="query.semester" clearable placeholder="不填查询全部" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStatistics">查询统计</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="授课班级">{{ statistics?.classCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="学生总数">{{ statistics?.studentCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="已录入成绩">{{ statistics?.gradedCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="未录入成绩">{{ statistics?.ungradedCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="平均分">{{ statistics?.averageScore ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="不及格人数">{{ statistics?.failedCount ?? 0 }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </PageContainer>
</template>
