<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { computed, onMounted, reactive, ref, watch } from 'vue'

import { studentCreateSelection, studentListAvailableClasses, studentListGrades, studentListSelections } from '@/api/student'
import PageContainer from '@/components/common/PageContainer.vue'
import type { SelectionRequest, StudentClassOption } from '@/types/api'
import { ApiBusinessError } from '@/utils/request'
import { uniqueSemestersFrom } from '@/utils/semester'

const loading = ref(false)
const classes = ref<StudentClassOption[]>([])
const semesterSource = ref<Array<{ semester: string }>>([])

const query = reactive({
  semester: '',
})

const selectionModel = reactive<SelectionRequest>({
  semester: '',
  courseId: '',
  staffId: '',
})

const selectedClassKey = ref('')

const semesterOptions = computed(() => uniqueSemestersFrom(semesterSource.value, (item) => item.semester))

const classSelectOptions = computed(() =>
  classes.value.map((item) => ({
    key: `${item.courseId}|${item.staffId}`,
    label: `${item.courseName} - ${item.teacherName}`,
    item,
  })),
)

async function loadSemesterOptions() {
  try {
    const [selections, grades] = await Promise.all([studentListSelections(), studentListGrades()])
    semesterSource.value = [...selections, ...grades]
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
    }
  }
}

async function loadClasses() {
  if (!query.semester) {
    ElMessage.warning('请先选择学期')
    return
  }

  loading.value = true
  try {
    classes.value = await studentListAvailableClasses({ semester: query.semester })
    selectionModel.semester = query.semester
    selectedClassKey.value = ''
    selectionModel.courseId = ''
    selectionModel.staffId = ''
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
  selectedClassKey.value = `${row.courseId}|${row.staffId}`
}

function handleClassSelect(key: string) {
  if (!key) {
    selectionModel.courseId = ''
    selectionModel.staffId = ''
    return
  }
  const option = classSelectOptions.value.find((item) => item.key === key)
  if (option) {
    chooseClass(option.item)
  }
}

async function selectClass() {
  if (!selectionModel.semester || !selectionModel.courseId || !selectionModel.staffId) {
    ElMessage.warning('请先选择要选修的课程')
    return
  }

  try {
    await studentCreateSelection(selectionModel)
    ElMessage.success('选课成功')
    await loadClasses()
    await loadSemesterOptions()
  } catch (error) {
    if (error instanceof ApiBusinessError) {
      ElMessage.error(error.message)
      return
    }
    ElMessage.error('选课失败')
  }
}

watch(
  () => query.semester,
  (semester) => {
    if (semester) {
      loadClasses()
    } else {
      classes.value = []
    }
  },
)

onMounted(async () => {
  await loadSemesterOptions()
})
</script>

<template>
  <PageContainer title="可选课程" description="按学期查询可选课程并完成选课。">
    <el-card>
      <el-form :inline="true">
        <el-form-item label="学期">
          <el-select
            v-model="query.semester"
            filterable
            allow-create
            default-first-option
            clearable
            placeholder="请选择学期"
            style="width: 180px"
          >
            <el-option v-for="item in semesterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="!query.semester" @click="loadClasses">查询可选课程</el-button>
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
      <template #header>选课提交</template>
      <el-form inline>
        <el-form-item label="课程">
          <el-select
            v-model="selectedClassKey"
            clearable
            placeholder="请选择课程"
            style="width: 320px"
            :disabled="!classes.length"
            @change="handleClassSelect"
          >
            <el-option
              v-for="item in classSelectOptions"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="!selectedClassKey" @click="selectClass">确认选课</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </PageContainer>
</template>
