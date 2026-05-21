<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

import { adminCreateClass, adminListClasses, adminUpdateClass } from '@/api/admin/classes'
import PageContainer from '@/components/common/PageContainer.vue'
import type { ClassInfo, ClassInfoRequest } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const classes = ref<ClassInfo[]>([])

const query = reactive({
  semester: '',
  courseId: '',
  staffId: '',
})

const createModel = reactive<ClassInfoRequest>({
  semester: '',
  courseId: '',
  staffId: '',
  classTime: '',
})

async function loadClasses() {
  loading.value = true
  try {
    classes.value = await adminListClasses({
      semester: query.semester || undefined,
      courseId: query.courseId || undefined,
      staffId: query.staffId || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询开课失败')
  } finally {
    loading.value = false
  }
}

async function createClass() {
  try {
    await adminCreateClass(createModel)
    ElMessage.success('新增开课请求已发送')
    await loadClasses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('新增开课失败')
  }
}

async function updateClassTime() {
  if (!createModel.semester || !createModel.courseId || !createModel.staffId) {
    ElMessage.warning('修改开课需要 semester/courseId/staffId')
    return
  }

  try {
    await adminUpdateClass(createModel.semester, createModel.courseId, createModel.staffId, {
      classTime: createModel.classTime,
    })
    ElMessage.success('修改开课请求已发送')
    await loadClasses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('修改开课失败')
  }
}

onMounted(() => {
  loadClasses()
})
</script>

<template>
  <PageContainer title="开课管理" description="对应 /api/admin/classes 的查询、新增、修改接口骨架。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-input v-model="query.semester" clearable placeholder="例如 2025-Fall" style="width: 160px" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="query.courseId" clearable placeholder="courseId" style="width: 160px" />
        </el-form-item>
        <el-form-item label="教师编号">
          <el-input v-model="query.staffId" clearable placeholder="staffId" style="width: 160px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadClasses">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="classes" border>
        <el-table-column prop="semester" label="学期" width="140" />
        <el-table-column prop="courseId" label="课程编号" width="140" />
        <el-table-column prop="staffId" label="教师编号" width="140" />
        <el-table-column prop="classTime" label="上课时间" min-width="220" />
      </el-table>
    </el-card>

    <el-card>
      <template #header>新增/修改（骨架表单）</template>
      <el-form :model="createModel" label-width="110">
        <el-form-item label="学期">
          <el-input v-model="createModel.semester" placeholder="semester" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="createModel.courseId" placeholder="courseId" />
        </el-form-item>
        <el-form-item label="教师编号">
          <el-input v-model="createModel.staffId" placeholder="staffId" />
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="createModel.classTime" placeholder="classTime" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="createClass">调用新增接口</el-button>
          <el-button @click="updateClassTime">调用修改接口</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
