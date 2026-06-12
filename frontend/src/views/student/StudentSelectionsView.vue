<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'

import { studentDeleteSelection, studentListSelections } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { SelectionRequest, StudentSelectionView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const loading = ref(false)
const allSelections = ref<StudentSelectionView[]>([])
const selections = ref<StudentSelectionView[]>([])

const query = reactive({
  semester: '',
})

const deleteModel = reactive<SelectionRequest>({
  semester: '',
  courseId: '',
  staffId: '',
})

const semesterOptions = computed(() => uniqueSemestersFrom(allSelections.value, (item) => item.semester))

async function loadAllSelections() {
  loading.value = true
  try {
    allSelections.value = await studentListSelections()
    applyFilter()
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

function applyFilter() {
  if (!query.semester) {
    selections.value = allSelections.value
    return
  }
  selections.value = allSelections.value.filter((item) => item.semester === query.semester)
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
    ElMessage.success('退课成功')
    await loadAllSelections()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('退课失败')
  }
}

watch(
  () => query.semester,
  () => {
    applyFilter()
  },
)

onMounted(() => {
  loadAllSelections()
})
</script>

<template>
  <PageContainer title="我的选课" description="查看已选课程，支持按学期筛选与退课。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select v-model="query.semester" clearable placeholder="全部学期" style="width: 180px">
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadAllSelections">刷新</el-button>
          <el-button type="danger" plain @click="dropSelection">退课</el-button>
        </el-form-item>
      </el-form>
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
