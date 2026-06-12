<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'

import { teacherListClassStudents, teacherListClasses, teacherUpdateGrade } from '@/api/teacher'
import PageContainer from '@/components/common/PageContainer.vue'
import type { GradeUpdateRequest, TeacherClassStudent, TeacherClassView } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const classLoading = ref(false)
const teacherClasses = ref<TeacherClassView[]>([])
const classStudents = ref<TeacherClassStudent[]>([])

const gradeModel = reactive<GradeUpdateRequest>({
  studentId: '',
  semester: '',
  courseId: '',
  score: 0,
})

const semesterOptions = computed(() => uniqueSemestersFrom(teacherClasses.value, (item) => item.semester))

const courseOptions = computed(() => {
  if (!gradeModel.semester) {
    return []
  }
  return teacherClasses.value.filter((item) => item.semester === gradeModel.semester)
})

const studentOptions = computed(() => classStudents.value)

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

async function loadClassStudents() {
  if (!gradeModel.semester || !gradeModel.courseId) {
    classStudents.value = []
    return
  }

  try {
    classStudents.value = await teacherListClassStudents(gradeModel.semester, gradeModel.courseId)
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('加载学生名单失败')
  }
}

function handleSemesterChange() {
  gradeModel.courseId = ''
  gradeModel.studentId = ''
  classStudents.value = []
}

function handleCourseChange() {
  gradeModel.studentId = ''
  loadClassStudents()
}

async function submitGrade() {
  if (!gradeModel.studentId || !gradeModel.semester || !gradeModel.courseId) {
    ElMessage.warning('请先选择学期、课程和学生')
    return
  }

  try {
    await teacherUpdateGrade(gradeModel)
    ElMessage.success('成绩保存成功')
    await loadClassStudents()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('更新成绩失败')
  }
}

watch(
  () => gradeModel.studentId,
  (studentId) => {
    const student = classStudents.value.find((item) => item.studentId === studentId)
    if (student?.score !== null && student?.score !== undefined) {
      gradeModel.score = student.score
    }
  },
)

onMounted(() => {
  loadTeacherClasses()
})
</script>

<template>
  <PageContainer title="成绩录入" description="录入或修改学生课程成绩。">
    <el-card v-loading="classLoading">
      <el-form :model="gradeModel" label-width="120">
        <el-form-item label="学期">
          <el-select
            v-model="gradeModel.semester"
            clearable
            placeholder="请选择学期"
            style="width: 220px"
            @change="handleSemesterChange"
          >
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
          <el-select
            v-model="gradeModel.courseId"
            clearable
            placeholder="请选择课程"
            style="width: 280px"
            :disabled="!gradeModel.semester"
            @change="handleCourseChange"
          >
            <el-option
              v-for="item in courseOptions"
              :key="item.courseId"
              :label="item.courseName"
              :value="item.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学生">
          <el-select
            v-model="gradeModel.studentId"
            clearable
            filterable
            placeholder="请选择学生"
            style="width: 280px"
            :disabled="!gradeModel.courseId"
          >
            <el-option
              v-for="item in studentOptions"
              :key="item.studentId"
              :label="`${item.studentName}（${item.studentId}）`"
              :value="item.studentId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="成绩">
          <el-input-number v-model="gradeModel.score" :min="0" :max="100" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitGrade">保存成绩</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
