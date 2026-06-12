<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

import { studentStatistics } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { StudentStatistics } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const stats = ref<StudentStatistics | null>(null)

async function loadStatistics() {
  loading.value = true
  try {
    stats.value = await studentStatistics()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询个人统计失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<template>
  <PageContainer title="个人统计" description="查看选课、成绩与学分等个人学习统计。">
    <el-card v-loading="loading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="已选课程数">{{ stats?.selectedCourseCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="已评分课程数">{{ stats?.gradedCourseCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="通过课程数">{{ stats?.passedCourseCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="不及格课程数">{{ stats?.failedCourseCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="已获得学分">{{ stats?.earnedCredits ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="平均成绩">{{ stats?.averageScore ?? '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </PageContainer>
</template>
