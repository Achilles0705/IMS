<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

import { teacherStatistics } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { TeacherStatistics } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const stats = ref<TeacherStatistics | null>(null)

async function loadStatistics() {
  loading.value = true
  try {
    stats.value = await teacherStatistics()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('加载教学统计失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<template>
  <PageContainer title="教师首页" description="展示教师教学统计概览，后续可扩展成绩分布图。">
    <el-card v-loading="loading">
      <div class="skeleton-grid">
        <el-statistic title="授课班级数" :value="stats?.classCount ?? 0" />
        <el-statistic title="学生总数" :value="stats?.studentCount ?? 0" />
        <el-statistic title="已录入成绩" :value="stats?.gradedCount ?? 0" />
        <el-statistic title="未录入成绩" :value="stats?.ungradedCount ?? 0" />
        <el-statistic title="平均分" :value="stats?.averageScore ?? 0" />
        <el-statistic title="不及格人数" :value="stats?.failedCount ?? 0" />
      </div>
    </el-card>
  </PageContainer>
</template>
