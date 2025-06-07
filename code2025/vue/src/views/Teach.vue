<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'TEACHER'">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
        <el-table-column prop="img" label="课程">
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]" :preview-teleported="true"
                      style="width: 40px; height: 40px; border-radius: 5px; display: block" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="price" label="课程课时" />
        <el-table-column prop="teacherName" label="教师" />
        <el-table-column prop="divisionTitle" label="班级" />
        <el-table-column prop="num" label="学生数量" />
        <el-table-column label="操作" width="100">
          <template #default="scope" v-if="data.user.role === 'TEACHER'">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
          <template #default="scope" v-else>
            <el-button type="primary" @click="borrow(scope.row)">申请</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <el-dialog title="课程" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="img" label="课程">
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="课程名称">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item prop="price" label="课程课时">
          <el-input v-model="data.form.price" autocomplete="off" placeholder="请输入课程课时" />
        </el-form-item>
        <el-form-item prop="divisionId" label="班级">
          <el-select v-model="data.form.divisionId" placeholder="请选择班级">
            <el-option
                v-for="item in data.divisionList"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="num" label="学生数量">
          <el-input v-model="data.form.num" autocomplete="off" placeholder="请输入学生数量" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { Search } from "@element-plus/icons-vue";
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  divisionList: [], // 班级数据
  rules: {
    img: [{ required: true, message: '请上传课程照片', trigger: 'blur' }],
    name: [{ required: true, message: '请填写课程名称', trigger: 'blur' }],
    price: [{ required: true, message: '请填写课程课时', trigger: 'blur' }],
    divisionId: [{ required: true, message: '请选择班级', trigger: 'change' }],
    num: [{ required: true, message: '请填写学生数量', trigger: 'blur' }]
  }
})

const load = () => {
  request.get('/teach/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const loadDivisions = () => {
  request.get('/division/selectAll').then(res => {
    if (res.code === '200') {
      data.divisionList = res.data
    }
  })
}
loadDivisions()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/teach/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/teach/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/teach/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

const reset = () => {
  data.name = null
  load()
}

const handleFileSuccess = (res) => {
  data.form.img = res.data
}

const borrow = (row) => {
  request.post('/record/add', {
    teacherId: data.user.id,
    bookId: row.id,
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功，等待管理员审核')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>
