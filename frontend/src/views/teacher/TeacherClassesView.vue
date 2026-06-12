<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

import { teacherListClasses } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { TeacherClassView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const router = useRouter()
const loading = ref(false)
const allClasses = ref<TeacherClassView[]>([])
const classes = ref<TeacherClassView[]>([])

const query = reactive({
  semester: '',
})

const semesterOptions = computed(() => uniqueSemestersFrom(allClasses.value, (item) => item.semester))

async function loadAllClasses() {
  loading.value = true
  try {
    allClasses.value = await teacherListClasses()
    applyFilter()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询授课列表失败')
  } finally {
    loading.value = false
  }
}

function applyFilter() {
  if (!query.semester) {
    classes.value = allClasses.value
    return
  }
  classes.value = allClasses.value.filter((item) => item.semester === query.semester)
}

function goStudents(row: TeacherClassView) {
  router.push(`/teacher/class-students/${row.semester}/${row.courseId}`)
}

watch(
  () => query.semester,
  () => {
    applyFilter()
  },
)

onMounted(() => {
  loadAllClasses()
})
</script>

<template>
  <PageContainer title="授课列表" description="查看本人授课课程及选课人数。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select v-model="query.semester" clearable placeholder="全部学期" style="width: 180px">
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadAllClasses">刷新</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="classes" border>
        <el-table-column prop="semester" label="学期" width="130" />
        <el-table-column prop="courseId" label="课程编号" width="130" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column prop="classTime" label="上课时间" min-width="180" />
        <el-table-column prop="studentCount" label="学生人数" width="120" />
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button text type="primary" @click="goStudents(row)">查看学生</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </PageContainer>
</template>
