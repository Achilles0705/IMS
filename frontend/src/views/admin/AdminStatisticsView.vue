<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'

import { adminCourseGradeStatistics } from '@/api/admin/statistics'
import PageContainer from '@/components/common/PageContainer.vue'
import type { CourseGradeStatistics } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const loading = ref(false)
const allStatistics = ref<CourseGradeStatistics[]>([])
const statistics = ref<CourseGradeStatistics[]>([])

const query = reactive({
  semester: '',
  courseId: '',
})

const semesterOptions = computed(() => uniqueSemestersFrom(allStatistics.value, (item) => item.semester))

const courseOptions = computed(() => {
  if (!query.semester) {
    return []
  }
  return allStatistics.value.filter((item) => item.semester === query.semester)
})

async function loadStatistics() {
  loading.value = true
  try {
    statistics.value = await adminCourseGradeStatistics({
      semester: query.semester || undefined,
      courseId: query.courseId || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询课程成绩统计失败')
  } finally {
    loading.value = false
  }
}

async function loadAllStatistics() {
  try {
    allStatistics.value = await adminCourseGradeStatistics()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
    }
  }
}

function handleSemesterChange() {
  query.courseId = ''
}

onMounted(async () => {
  await loadAllStatistics()
  await loadStatistics()
})
</script>

<template>
  <PageContainer title="统计分析" description="查看各课程选课人数与成绩分布统计。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select
            v-model="query.semester"
            clearable
            placeholder="全部学期"
            style="width: 180px"
            @change="handleSemesterChange"
          >
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select
            v-model="query.courseId"
            clearable
            placeholder="全部课程"
            style="width: 240px"
            :disabled="!query.semester"
          >
            <el-option
              v-for="item in courseOptions"
              :key="item.courseId"
              :label="item.courseName"
              :value="item.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStatistics">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="statistics" border>
        <el-table-column prop="semester" label="学期" width="130" />
        <el-table-column prop="courseId" label="课程编号" width="140" />
        <el-table-column prop="courseName" label="课程名称" min-width="200" />
        <el-table-column prop="studentCount" label="选课人数" width="120" />
        <el-table-column prop="gradedCount" label="已评分人数" width="120" />
        <el-table-column prop="averageScore" label="平均分" width="100" />
        <el-table-column prop="maxScore" label="最高分" width="100" />
        <el-table-column prop="minScore" label="最低分" width="100" />
        <el-table-column prop="failedCount" label="不及格人数" width="120" />
      </el-table>
    </el-card>
  </PageContainer>
</template>
