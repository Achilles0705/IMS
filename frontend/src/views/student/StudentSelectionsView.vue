<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'

import { studentDeleteSelection, studentListSelections } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { SelectionRequest, StudentSelectionView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'

const loading = ref(false)
const selections = ref<StudentSelectionView[]>([])

const query = reactive({
  semester: '',
})

const deleteModel = reactive<SelectionRequest>({
  semester: '',
  courseId: '',
  staffId: '',
})

async function loadSelections() {
  loading.value = true
  try {
    selections.value = await studentListSelections({
      semester: query.semester || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询已选课程失败')
  } finally {
    loading.value = false
  }
}

function chooseSelection(row: StudentSelectionView) {
  deleteModel.semester = row.semester
  deleteModel.courseId = row.courseId
  deleteModel.staffId = row.staffId
}

async function dropSelection() {
  if (!deleteModel.semester || !deleteModel.courseId || !deleteModel.staffId) {
    ElMessage.warning('请先选择要退课的记录')
    return
  }

  try {
    await studentDeleteSelection(deleteModel)
    ElMessage.success('退课请求已发送')
    await loadSelections()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('退课失败')
  }
}
</script>

<template>
  <PageContainer title="我的选课" description="对应 /api/student/selections 的查询与退课接口骨架。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-input v-model="query.semester" clearable placeholder="不填查询全部" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadSelections">查询已选课程</el-button>
          <el-button type="danger" plain @click="dropSelection">调用退课接口</el-button>
        </el-form-item>
      </el-form>
      <el-alert
        title="点击表格行可自动填充退课参数（DELETE /api/student/selections 请求体）。"
        type="info"
        :closable="false"
      />
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="selections" border @row-click="chooseSelection">
        <el-table-column prop="semester" label="学期" width="130" />
        <el-table-column prop="courseId" label="课程编号" width="130" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column prop="teacherName" label="任课教师" width="140" />
        <el-table-column prop="classTime" label="上课时间" min-width="180" />
        <el-table-column prop="score" label="成绩" width="100" />
      </el-table>
    </el-card>
  </PageContainer>
</template>
