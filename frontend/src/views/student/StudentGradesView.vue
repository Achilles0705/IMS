<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'

import { studentListGrades } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { StudentGradeView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const loading = ref(false)
const allGrades = ref<StudentGradeView[]>([])
const grades = ref<StudentGradeView[]>([])

const query = reactive({
  semester: '',
})

const semesterOptions = computed(() => uniqueSemestersFrom(allGrades.value, (item) => item.semester))

async function loadAllGrades() {
  loading.value = true
  try {
    allGrades.value = await studentListGrades()
    applyFilter()
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

function applyFilter() {
  if (!query.semester) {
    grades.value = allGrades.value
    return
  }
  grades.value = allGrades.value.filter((item) => item.semester === query.semester)
}

watch(
  () => query.semester,
  () => {
    applyFilter()
  },
)

onMounted(() => {
  loadAllGrades()
})
</script>

<template>
  <PageContainer title="我的成绩" description="查看各学期课程成绩及通过情况。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select v-model="query.semester" clearable placeholder="全部学期" style="width: 180px">
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadAllGrades">刷新</el-button>
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
