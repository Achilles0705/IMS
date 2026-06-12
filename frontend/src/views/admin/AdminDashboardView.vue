<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

import { adminStatisticsOverview } from '@/api/admin/statistics'
import { getDatabaseHealth, getHealth } from '@/api/health'
import PageContainer from '@/components/common/PageContainer.vue'
import type { AdminOverviewStatistics, DatabaseHealthData, HealthData } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const overview = ref<AdminOverviewStatistics | null>(null)
const serviceHealth = ref<HealthData | null>(null)
const dbHealth = ref<DatabaseHealthData | null>(null)

async function loadOverview() {
  loading.value = true
  try {
    overview.value = await adminStatisticsOverview()
    serviceHealth.value = await getHealth()
    dbHealth.value = await getDatabaseHealth()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('加载管理员统计失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadOverview()
})
</script>

<template>
  <PageContainer title="管理员首页" description="展示系统核心统计数据与运行状态。">
    <el-card v-loading="loading">
      <div class="skeleton-grid">
        <el-statistic title="院系总数" :value="overview?.departmentCount ?? 0" />
        <el-statistic title="学生总数" :value="overview?.studentCount ?? 0" />
        <el-statistic title="教师总数" :value="overview?.teacherCount ?? 0" />
        <el-statistic title="课程总数" :value="overview?.courseCount ?? 0" />
        <el-statistic title="开课总数" :value="overview?.classCount ?? 0" />
        <el-statistic title="选课记录" :value="overview?.selectionCount ?? 0" />
      </div>
    </el-card>

    <el-card>
      <template #header>系统健康检查</template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="/api/health">
          {{ serviceHealth?.status || '-' }} / {{ serviceHealth?.message || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="/api/health/db">
          {{ dbHealth?.status || '-' }} / {{ dbHealth?.message || '-' }}，departmentCount={{ dbHealth?.departmentCount ?? 0 }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </PageContainer>
</template>
