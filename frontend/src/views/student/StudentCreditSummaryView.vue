<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

import { studentCreditSummary } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { StudentCreditSummary } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const summary = ref<StudentCreditSummary | null>(null)

async function loadSummary() {
  loading.value = true
  try {
    summary.value = await studentCreditSummary()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询学分汇总失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadSummary()
})
</script>

<template>
  <PageContainer
    title="学分汇总"
    description="查看已通过课程、已获得学分与平均成绩汇总。"
  >
    <el-card v-loading="loading">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="学生编号">{{ summary?.studentId ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="通过课程数">{{ summary?.passedCourseCount ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="已获得学分">{{ summary?.earnedCredits ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="平均成绩">{{ summary?.averageScore ?? '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </PageContainer>
</template>
