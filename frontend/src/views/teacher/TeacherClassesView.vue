<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import { teacherListClasses } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { TeacherClassView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const classes = ref<TeacherClassView[]>([])

const query = reactive({
  semester: '',
})

async function loadClasses() {
  loading.value = true
  try {
    classes.value = await teacherListClasses({
      semester: query.semester || undefined,
    })
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

function goStudents(row: TeacherClassView) {
  router.push(`/teacher/class-students/${row.semester}/${row.courseId}`)
}
</script>

<template>
  <PageContainer title="授课列表" description="对应 /api/teacher/classes 查询接口骨架。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-input v-model="query.semester" clearable placeholder="不填查询全部" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadClasses">查询授课</el-button>
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
