<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

import { adminCreateCourse, adminListCourses, adminUpdateCourse } from '@/api/admin/courses'
import PageContainer from '@/components/common/PageContainer.vue'
import { useDictStore } from '@/stores/dict'
import type { Course, CourseRequest } from '@/types/api'
import { getDeptName } from '@/utils/dict'
import { ApiBusinessError } from '@/utils/request'

const dictStore = useDictStore()
const loading = ref(false)
const courses = ref<Course[]>([])

const query = reactive({
  deptId: '',
  keyword: '',
})

const editModel = reactive<CourseRequest>({
  courseId: '',
  courseName: '',
  credit: 0,
  creditHours: 0,
  deptId: '',
})

async function loadCourses() {
  loading.value = true
  try {
    courses.value = await adminListCourses({
      deptId: query.deptId || undefined,
      keyword: query.keyword || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询课程失败')
  } finally {
    loading.value = false
  }
}

async function createCourse() {
  try {
    await adminCreateCourse(editModel)
    ElMessage.success('新增课程请求已发送')
    await loadCourses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('新增课程失败')
  }
}

async function updateCourse() {
  if (!editModel.courseId) {
    ElMessage.warning('请先输入 courseId')
    return
  }

  try {
    await adminUpdateCourse(editModel.courseId, editModel)
    ElMessage.success('修改课程请求已发送')
    await loadCourses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('修改课程失败')
  }
}

onMounted(async () => {
  await dictStore.loadDepartments()
  await loadCourses()
})
</script>

<template>
  <PageContainer title="课程管理" description="维护课程信息，支持查询、新增与修改。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="院系">
          <el-select v-model="query.deptId" clearable placeholder="全部院系" style="width: 180px">
            <el-option
              v-for="dept in dictStore.departments"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" clearable placeholder="课程编号/课程名" style="width: 220px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadCourses">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="courses" border>
        <el-table-column prop="courseId" label="课程编号" width="140" />
        <el-table-column prop="courseName" label="课程名称" min-width="220" />
        <el-table-column prop="credit" label="学分" width="100" />
        <el-table-column prop="creditHours" label="学时" width="100" />
        <el-table-column label="院系" width="160">
          <template #default="{ row }">
            {{ getDeptName(row.deptId, dictStore.departments) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card>
      <template #header>新增/修改</template>
      <el-form :model="editModel" label-width="100">
        <el-form-item label="课程编号">
          <el-input v-model="editModel.courseId" placeholder="courseId" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="editModel.courseName" placeholder="courseName" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input-number v-model="editModel.credit" :min="0" />
        </el-form-item>
        <el-form-item label="学时">
          <el-input-number v-model="editModel.creditHours" :min="0" />
        </el-form-item>
        <el-form-item label="院系">
          <el-select v-model="editModel.deptId" clearable style="width: 220px">
            <el-option
              v-for="dept in dictStore.departments"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="createCourse">新增</el-button>
          <el-button @click="updateCourse">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
