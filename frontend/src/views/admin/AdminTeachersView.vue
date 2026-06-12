<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'

import { adminCreateTeacher, adminListTeachers, adminUpdateTeacher } from '@/api/admin/teachers'
import PageContainer from '@/components/common/PageContainer.vue'
import { useDictStore } from '@/stores/dict'
import type { Teacher, TeacherRequest } from '@/types/api'
import { getDeptName } from '@/utils/dict'
import { ApiBusinessError } from '@/utils/request'

const dictStore = useDictStore()
const loading = ref(false)
const teachers = ref<Teacher[]>([])

const query = reactive({
  deptId: '',
  keyword: '',
})

const editModel = reactive<TeacherRequest>({
  staffId: '',
  name: '',
  sex: '男',
  deptId: '',
  professionalTitle: '',
})

async function loadTeachers() {
  loading.value = true
  try {
    teachers.value = await adminListTeachers({
      deptId: query.deptId || undefined,
      keyword: query.keyword || undefined,
    })
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询教师失败')
  } finally {
    loading.value = false
  }
}

async function createTeacher() {
  try {
    await adminCreateTeacher(editModel)
    ElMessage.success('新增教师请求已发送')
    await loadTeachers()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('新增教师失败')
  }
}

async function updateTeacher() {
  if (!editModel.staffId) {
    ElMessage.warning('请先输入 staffId')
    return
  }

  try {
    await adminUpdateTeacher(editModel.staffId, editModel)
    ElMessage.success('修改教师请求已发送')
    await loadTeachers()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('修改教师失败')
  }
}

onMounted(async () => {
  await dictStore.loadDepartments()
  await loadTeachers()
})
</script>

<template>
  <PageContainer title="教师管理" description="维护教师信息，支持查询、新增与修改。">
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
          <el-input v-model="query.keyword" clearable placeholder="工号/姓名" style="width: 220px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadTeachers">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="teachers" border>
        <el-table-column prop="staffId" label="工号" width="140" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="sex" label="性别" width="90" />
        <el-table-column prop="professionalTitle" label="职称" width="160" />
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
        <el-form-item label="工号">
          <el-input v-model="editModel.staffId" placeholder="staffId" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editModel.name" placeholder="name" />
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="editModel.professionalTitle" placeholder="professionalTitle" />
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
          <el-button type="primary" @click="createTeacher">新增</el-button>
          <el-button @click="updateTeacher">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
