<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

import { adminCreateStudent, adminListStudents, adminUpdateStudent } from '@/api/admin/students'
import PageContainer from '@/components/common/PageContainer.vue'
import { useDictStore } from '@/stores/dict'
import type { StudentRequest } from '@/types/api'
import { getDeptName } from '@/utils/dict'
import { ApiBusinessError } from '@/utils/request'

const dictStore = useDictStore()
const loading = ref(false)
const students = ref<StudentRequest[]>([])

const query = reactive({
  deptId: '',
  keyword: '',
})

const editModel = reactive<StudentRequest>({
  studentId: '',
  name: '',
  sex: '男',
  deptId: '',
  status: '在读',
})

async function loadStudents() {
  loading.value = true
  try {
    const data = await adminListStudents({
      deptId: query.deptId || undefined,
      keyword: query.keyword || undefined,
    })
    students.value = data
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询学生失败')
  } finally {
    loading.value = false
  }
}

async function createStudent() {
  try {
    await adminCreateStudent(editModel)
    ElMessage.success('新增学生请求已发送')
    await loadStudents()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('新增学生失败')
  }
}

async function updateStudent() {
  if (!editModel.studentId) {
    ElMessage.warning('请先输入 studentId')
    return
  }

  try {
    await adminUpdateStudent(editModel.studentId, editModel)
    ElMessage.success('修改学生请求已发送')
    await loadStudents()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('修改学生失败')
  }
}

onMounted(async () => {
  await dictStore.loadDepartments()
  await loadStudents()
})
</script>

<template>
  <PageContainer title="学生管理" description="维护学生信息，支持查询、新增与修改。">
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
          <el-input v-model="query.keyword" clearable placeholder="学号/姓名" style="width: 220px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStudents">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="students" border>
        <el-table-column prop="studentId" label="学号" width="140" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="sex" label="性别" width="90" />
        <el-table-column label="院系" width="160">
          <template #default="{ row }">
            {{ getDeptName(row.deptId, dictStore.departments) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120" />
      </el-table>
    </el-card>

    <el-card>
      <template #header>新增/修改</template>
      <el-form :model="editModel" label-width="100">
        <el-form-item label="学号">
          <el-input v-model="editModel.studentId" placeholder="studentId" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editModel.name" placeholder="name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="editModel.sex" style="width: 160px">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
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
          <el-button type="primary" @click="createStudent">新增</el-button>
          <el-button @click="updateStudent">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
