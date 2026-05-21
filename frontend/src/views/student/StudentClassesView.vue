<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'

import { studentCreateSelection, studentListAvailableClasses } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { SelectionRequest, StudentClassOption } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const classes = ref<StudentClassOption[]>([])

const query = reactive({
  semester: '',
})

const selectionModel = reactive<SelectionRequest>({
  semester: '',
  courseId: '',
  staffId: '',
})

async function loadClasses() {
  if (!query.semester) {
    ElMessage.warning('请输入学期后再查询')
    return
  }

  loading.value = true
  try {
    classes.value = await studentListAvailableClasses({ semester: query.semester })
    selectionModel.semester = query.semester
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询可选课程失败')
  } finally {
    loading.value = false
  }
}

function chooseClass(row: StudentClassOption) {
  selectionModel.semester = row.semester
  selectionModel.courseId = row.courseId
  selectionModel.staffId = row.staffId
}

async function selectClass() {
  if (!selectionModel.semester || !selectionModel.courseId || !selectionModel.staffId) {
    ElMessage.warning('请选择一条课程记录再选课')
    return
  }

  try {
    await studentCreateSelection(selectionModel)
    ElMessage.success('选课请求已发送')
    await loadClasses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('选课失败')
  }
}
</script>

<template>
  <PageContainer title="可选课程" description="对应 /api/student/classes 与 /api/student/selections(POST) 的页面骨架。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-input v-model="query.semester" placeholder="例如 2025-Fall" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadClasses">查询可选课程</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="classes" border @row-click="chooseClass">
        <el-table-column prop="semester" label="学期" width="130" />
        <el-table-column prop="courseId" label="课程编号" width="130" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column prop="teacherName" label="任课教师" width="140" />
        <el-table-column prop="classTime" label="上课时间" min-width="180" />
        <el-table-column prop="credit" label="学分" width="90" />
        <el-table-column prop="selected" label="是否已选" width="110">
          <template #default="{ row }">
            <el-tag :type="row.selected ? 'success' : 'info'">{{ row.selected ? '已选' : '未选' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card>
      <template #header>选课提交（骨架）</template>
      <el-form :model="selectionModel" inline>
        <el-form-item label="semester">
          <el-input v-model="selectionModel.semester" style="width: 140px" />
        </el-form-item>
        <el-form-item label="courseId">
          <el-input v-model="selectionModel.courseId" style="width: 140px" />
        </el-form-item>
        <el-form-item label="staffId">
          <el-input v-model="selectionModel.staffId" style="width: 140px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="selectClass">调用选课接口</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
