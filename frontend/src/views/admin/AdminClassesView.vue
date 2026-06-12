<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'

import { adminCreateClass, adminListClasses, adminUpdateClass } from '@/api/admin/classes'
import { adminListTeachers } from '@/api/admin/teachers'
import PageContainer from '@/components/common/PageContainer.vue'
import { useDictStore } from '@/stores/dict'
import type { ClassInfo, ClassInfoRequest, Teacher } from '@/types/api'
import { getCourseName, getTeacherName } from '@/utils/dict'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const dictStore = useDictStore()

const loading = ref(false)
const allClasses = ref<ClassInfo[]>([])
const classes = ref<ClassInfo[]>([])
const teachers = ref<Teacher[]>([])

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

const querySemesterOptions = computed(() => uniqueSemestersFrom(allClasses.value, (item) => item.semester))

const queryCourseOptions = computed(() => {
  const source = query.semester
    ? allClasses.value.filter((item) => item.semester === query.semester)
    : allClasses.value
  const courseIds = [...new Set(source.map((item) => item.courseId))]
  return courseIds.map((courseId) => ({
    courseId,
    courseName: getCourseName(courseId, dictStore.courses),
  }))
})

const queryStaffOptions = computed(() => {
  let source = allClasses.value
  if (query.semester) {
    source = source.filter((item) => item.semester === query.semester)
  }
  if (query.courseId) {
    source = source.filter((item) => item.courseId === query.courseId)
  }
  const staffIds = [...new Set(source.map((item) => item.staffId))]
  return staffIds.map((staffId) => ({
    staffId,
    name: getTeacherName(staffId, teachers.value),
  }))
})

const createSemesterOptions = computed(() => querySemesterOptions.value)

function handleQuerySemesterChange() {
  query.courseId = ''
  query.staffId = ''
}

function handleQueryCourseChange() {
  query.staffId = ''
}

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

async function loadAllClasses() {
  try {
    allClasses.value = await adminListClasses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
    }
  }
}

async function createClass() {
  try {
    await adminCreateClass(createModel)
    ElMessage.success('新增开课成功')
    await loadAllClasses()
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
    ElMessage.warning('请先选择学期、课程和教师')
    return
  }

  try {
    await adminUpdateClass(createModel.semester, createModel.courseId, createModel.staffId, {
      classTime: createModel.classTime,
    })
    ElMessage.success('修改开课成功')
    await loadAllClasses()
    await loadClasses()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('修改开课失败')
  }
}

async function loadTeachers() {
  try {
    teachers.value = await adminListTeachers()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('加载教师列表失败')
  }
}

onMounted(async () => {
  await Promise.all([dictStore.loadCourses(), loadTeachers()])
  await loadAllClasses()
  await loadClasses()
})
</script>

<template>
  <PageContainer title="开课管理" description="维护学期开课信息，支持查询、新增与修改。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select
            v-model="query.semester"
            clearable
            placeholder="全部学期"
            style="width: 160px"
            @change="handleQuerySemesterChange"
          >
            <el-option v-for="item in querySemesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select
            v-model="query.courseId"
            clearable
            placeholder="全部课程"
            style="width: 220px"
            @change="handleQueryCourseChange"
          >
            <el-option
              v-for="item in queryCourseOptions"
              :key="item.courseId"
              :label="item.courseName"
              :value="item.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教师">
          <el-select v-model="query.staffId" clearable placeholder="全部教师" style="width: 180px">
            <el-option
              v-for="item in queryStaffOptions"
              :key="item.staffId"
              :label="item.name"
              :value="item.staffId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadClasses">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <el-table :data="classes" border>
        <el-table-column prop="semester" label="学期" width="140" />
        <el-table-column prop="courseId" label="课程编号" width="120" />
        <el-table-column label="课程名称" min-width="180">
          <template #default="{ row }">
            {{ getCourseName(row.courseId, dictStore.courses) }}
          </template>
        </el-table-column>
        <el-table-column prop="staffId" label="教师编号" width="120" />
        <el-table-column label="教师姓名" width="120">
          <template #default="{ row }">
            {{ getTeacherName(row.staffId, teachers) }}
          </template>
        </el-table-column>
        <el-table-column prop="classTime" label="上课时间" min-width="220" />
      </el-table>
    </el-card>

    <el-card>
      <template #header>新增/修改</template>
      <el-form :model="createModel" label-width="110">
        <el-form-item label="学期">
          <el-select
            v-model="createModel.semester"
            filterable
            allow-create
            default-first-option
            placeholder="请选择或输入学期"
            style="width: 220px"
          >
            <el-option v-for="item in createSemesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="createModel.courseId" filterable placeholder="请选择课程" style="width: 280px">
            <el-option
              v-for="item in dictStore.courses"
              :key="item.courseId"
              :label="item.courseName"
              :value="item.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教师">
          <el-select v-model="createModel.staffId" filterable placeholder="请选择教师" style="width: 220px">
            <el-option v-for="item in teachers" :key="item.staffId" :label="item.name" :value="item.staffId" />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="createModel.classTime" placeholder="请输入上课时间" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="createClass">新增</el-button>
          <el-button @click="updateClassTime">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
