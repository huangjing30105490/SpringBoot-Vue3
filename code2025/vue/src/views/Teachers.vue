  <template>
    <div>
      <div class="card" style="margin-bottom: 5px">
        <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"></el-input>
        <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button @click="reset">重 置</el-button>
      </div>
      <div class="card" style="display: flex; align-items: center; margin-bottom: 5px">
        <el-button type="primary" @click="handleAdd">新 增</el-button>
        <el-button type="danger" @click="deleteBatch">批量删除</el-button>
        <el-button type="info" @click="exportData">批量导出</el-button>
        <el-upload
            style="display: inline-block; margin-left: 10px"
            action="http://localhost:9999/teacher/import"
            :show-file-list="false"
            :on-success="handleImportSuccess"
        >
          <el-button type="success">批量导入</el-button>
        </el-upload>
      </div>

      <div class="card" style="overflow-x: auto; margin-bottom: 5px">
        <el-table :data="data.tableData" style="min-width: 1800px" @selection-change="handleSelectionChange"
                  :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
          <el-table-column type="selection" width="55" />
          <el-table-column label="头像" width="100">
  <!--          ="scope"：接收子组件通过插槽传递的数据（通常是一个对象，包含子组件内部的状态或属性-->
            <template #default="scope">
              <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" :preview-teleported="true"
                        style="width: 40px; height: 40px; border-radius: 50%; display: block" />
            </template>
          </el-table-column>
          <el-table-column prop="username" label="账号" />
          <el-table-column prop="employeeId" label="工号" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="gender" label="性别" />
          <el-table-column prop="collegeTitle" label="院系部门" />
          <el-table-column prop="title" label="职称" />
          <el-table-column prop="phone" label="电话" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="degree" label="最终学历" />
          <el-table-column prop="school" label="毕业学校" />
          <el-table-column prop="graduate" label="毕业时间" />
          <el-table-column prop="enrollment" label="入学时间" />
          <el-table-column prop="outlook" label="政治面貌" />
          <el-table-column prop="qualification" label="教师资格证号" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
              <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
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

      <el-dialog title="普通用户信息" v-model="data.formVisible" width="50%" destroy-on-close>
        <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
          <el-form-item prop="username" label="账号">
            <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item prop="employeeId" label="工号">
            <el-input v-model="data.form.employeeId" autocomplete="off" placeholder="请输入工号" />
          </el-form-item>
          <el-form-item prop="name" label="名称">
            <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称" />
          </el-form-item>
          <el-form-item prop="gender" label="性别">
            <el-input v-model="data.form.gender" autocomplete="off" placeholder="请输入性别" />
          </el-form-item>
          <el-form-item prop="title" label="院系部门">
            <el-select
                v-model="data.form.collegeId"
                placeholder="请选择院系部门"
                style="width: 100%"
            >
              <el-option
                  v-for="item in data.collegeData"
                  :key="item.id"
                  :label="item.title"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item prop="name" label="职称">
            <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入职称" />
          </el-form-item>
          <el-form-item prop="phone" label="电话">
            <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item prop="degree" label="最终学历">
            <el-input v-model="data.form.degree" autocomplete="off" placeholder="请输入最终学历" />
          </el-form-item>
          <el-form-item prop="school" label="毕业学校">
            <el-input v-model="data.form.school" autocomplete="off" placeholder="请输入毕业学校" />
          </el-form-item>
          <el-form-item prop="graduate" label="毕业时间">
            <el-input v-model="data.form.graduate" autocomplete="off" placeholder="请输入毕业时间" />
          </el-form-item>
          <el-form-item prop="enrollment" label="入学时间">
            <el-input v-model="data.form.enrollment" autocomplete="off" placeholder="请输入入学时间" />
          </el-form-item>
          <el-form-item prop="outlook" label="政治面貌">
            <el-input v-model="data.form.outlook" autocomplete="off" placeholder="请输入政治面貌" />
          </el-form-item>
          <el-form-item prop="qualification" label="教师资格证号">
            <el-input v-model="data.form.qualification" autocomplete="off" placeholder="请输入教师资格证号" />
          </el-form-item>
          <el-form-item prop="avatar" label="头像">
            <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture"
            >
              <el-button type="primary">上传头像</el-button>
            </el-upload>
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
  import { reactive, ref } from "vue";
  import {Search} from "@element-plus/icons-vue";
  import request from "@/utils/request.js";
  import {ElMessage, ElMessageBox} from "element-plus";

  const data = reactive({
    user: JSON.parse(localStorage.getItem('code_user') || '{}'),
    username: null,
    name: null,
    pageNum: 1,
    pageSize: 5,
    total: 0,
    tableData: [],
    formVisible: false,
    form: {},
    collegeData: [],
    rules: {
      username: [
        { required: true, message: '请填写账号', trigger: 'blur' }
      ],
      employeeId: [
        { required: true, message: '请填写工号', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请填写名称', trigger: 'blur' }
      ],
      gender: [
        { required: true, message: '请填写性别', trigger: 'blur' }
      ],
      title: [
        { required: true, message: '请填写职称', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请填写手机', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请填写邮箱', trigger: 'blur' }
      ],
      degree: [
        { required: true, message: '请填写最终学历', trigger: 'blur' }
      ],
      school: [
        { required: true, message: '请填写毕业学校', trigger: 'blur' }
      ],
      graduate: [
        { required: true, message: '请填写毕业时间', trigger: 'blur' }
      ],
      enrollment: [
        { required: true, message: '请填写入学时间', trigger: 'blur' }
      ],
      outlook: [
        { required: true, message: '请填写政治面貌', trigger: 'blur' }
      ],
      qualification: [
        { required: true, message: '请填写教师资格证号', trigger: 'blur' }
      ],
    },
    rows: [],
    ids: []
  })

  const formRef = ref()

  const load = () => {
    request.get('/teacher/selectPage', {
      params: {
        pageNum: data.pageNum,
        pageSize: data.pageSize,
        username: data.username,
        name: data.name
      }
    }).then(res => {
      if (res.code === '200') {
        data.tableData = res.data.list
        data.total = res.data.total
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
  load()

  const reset = () => {
    data.username = null
    data.name = null
    load()
  }

  const handleAdd = () => {
    data.formVisible = true
    data.form = {}
  }

  const add = () => {
    // formRef 是表单的引用
    formRef.value.validate((valid) => {
      if (valid) {   // 验证通过的情况下
        request.post('/teacher/add', data.form).then(res => {
          if (res.code === '200') {
            data.formVisible = false
            ElMessage.success('新增成功')
            load()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })
  }

  const handleEdit = (row) => {
    data.form = JSON.parse(JSON.stringify(row))  // 深度拷贝数据
    data.formVisible = true
  }

  const update = () => {
    // formRef 是表单的引用
    formRef.value.validate((valid) => {
      if (valid) {   // 验证通过的情况下
        request.put('/teacher/update', data.form).then(res => {
          if (res.code === '200') {
            data.formVisible = false
            ElMessage.success('修改成功')
            load()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })
  }

  const save = () => {
    data.form.id ? update() : add()
  }

  const del = (id) => {
    ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
      request.delete('/teacher/delete/' + id).then(res => {
        if (res.code === '200') {
          ElMessage.success('删除成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }).catch(err => {})
  }

  const handleSelectionChange = (rows) => {  // rows 就是实际选择的数组
    data.rows = rows
    data.ids = data.rows.map(v => v.id)  // map可以把对象的数组 转换成一个纯数字的数组  [1,2,3]
  }

  const deleteBatch = () => {
    if (data.rows.length === 0) {
      ElMessage.warning('请选择数据')
      return
    }
    ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
      request.delete('/teacher/deleteBatch', { data: data.rows }).then(res => {
        if (res.code === '200') {
          ElMessage.success('批量删除成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }).catch(err => {})
  }

  const exportData = () => {
    let idsStr = data.ids.join(",")  // 把数组转换成  字符串  [1,2,3]  ->  "1,2,3"
    let url = `http://localhost:9999/teacher/export?username=${data.username === null ? '' : data.username}`
    + `&name=${data.name === null ? '' : data.name}`
    + `&ids=${idsStr}`
    + `&token=${data.user.token}`
    window.open(url)
  }

  const handleImportSuccess = (res) => {
    if (res.code === '200') {
      ElMessage.success('批量导入数据成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  }

  const handleFileSuccess = (res) => {
    data.form.avatar = res.data
  }

  const loadCollege = () => {
    request.get('/college/selectAll').then(res => {
      if (res.code === '200') {
        data.collegeData = res.data
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
  loadCollege() // Changed from loadCategory() to loadCollege()
  </script>

