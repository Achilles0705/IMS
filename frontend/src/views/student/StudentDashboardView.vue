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
    ElMessage.error('加载学生统计失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<template>
  <PageContainer title="学生首页" description="展示个人学习统计概览。">
    <el-card v-loading="loading">
      <div class="skeleton-grid">
        <el-statistic title="已选课程" :value="stats?.selectedCourseCount ?? 0" />
        <el-statistic title="已评分课程" :value="stats?.gradedCourseCount ?? 0" />
        <el-statistic title="通过课程" :value="stats?.passedCourseCount ?? 0" />
        <el-statistic title="不及格课程" :value="stats?.failedCourseCount ?? 0" />
        <el-statistic title="已获学分" :value="stats?.earnedCredits ?? 0" />
        <el-statistic title="平均成绩" :value="stats?.averageScore ?? 0" />
      </div>
    </el-card>
  </PageContainer>
</template>
