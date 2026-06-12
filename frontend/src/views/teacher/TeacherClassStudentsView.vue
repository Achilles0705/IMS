<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'

import { teacherListClasses, teacherListClassStudents } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { TeacherClassStudent, TeacherClassView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const route = useRoute()
const loading = ref(false)
const classLoading = ref(false)
const students = ref<TeacherClassStudent[]>([])
const teacherClasses = ref<TeacherClassView[]>([])

const query = reactive({
  semester: '',
  courseId: '',
})

const semesterOptions = computed(() => uniqueSemestersFrom(teacherClasses.value, (item) => item.semester))

const courseOptions = computed(() => {
  if (!query.semester) {
    return []
  }
  return teacherClasses.value.filter((item) => item.semester === query.semester)
})

async function loadTeacherClasses() {
  classLoading.value = true
  try {
    teacherClasses.value = await teacherListClasses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('加载授课课程失败')
  } finally {
    classLoading.value = false
  }
}

async function loadStudents() {
  if (!query.semester || !query.courseId) {
    ElMessage.warning('请先选择学期和课程')
    return
  }

  loading.value = true
  try {
    students.value = await teacherListClassStudents(query.semester, query.courseId)
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('查询课程学生失败')
  } finally {
    loading.value = false
  }
}

function handleSemesterChange() {
  query.courseId = ''
  students.value = []
}

watch(
  () => query.courseId,
  (courseId) => {
    if (query.semester && courseId) {
      loadStudents()
    }
  },
)

onMounted(async () => {
  await loadTeacherClasses()

  const semester = route.params.semester
  const courseId = route.params.courseId

  if (typeof semester === 'string') {
    query.semester = semester
  }
  if (typeof courseId === 'string') {
    query.courseId = courseId
  }

  if (query.semester && query.courseId) {
    await loadStudents()
  }
})
</script>

<template>
  <PageContainer
    title="课程学生名单"
    description="查看指定课程的学生名单及成绩。"
  >
    <el-card v-loading="classLoading">
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select
            v-model="query.semester"
            clearable
            placeholder="请选择学期"
            style="width: 180px"
            @change="handleSemesterChange"
          >
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select
            v-model="query.courseId"
            clearable
            placeholder="请选择课程"
            style="width: 260px"
            :disabled="!query.semester"
          >
            <el-option
              v-for="item in courseOptions"
              :key="item.courseId"
              :label="item.courseName"
              :value="item.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="!query.semester || !query.courseId" @click="loadStudents">
            查询学生
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="students" border>
        <el-table-column prop="studentId" label="学号" width="130" />
        <el-table-column prop="studentName" label="姓名" width="130" />
        <el-table-column prop="sex" label="性别" width="90" />
        <el-table-column prop="deptId" label="院系" width="120" />
        <el-table-column prop="mobilePhone" label="手机号" width="160" />
        <el-table-column prop="score" label="成绩" width="100" />
      </el-table>
    </el-card>
  </PageContainer>
</template>
