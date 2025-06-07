<template>
  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
    <el-button type="primary" @click="load">查 询</el-button>
    <el-button @click="reset">重 置</el-button>
  </div>
<!--  <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'USER'">-->
  <div class="card" style="margin-bottom: 5px" v-if="data.user.role != 'ADMIN'">
    <el-button type="primary" @click="handleAdd">新 增</el-button>
  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
      <el-table-column label="项目文件" width="120">
        <template #default="scope">
          <template v-if="scope.row.file">
            <el-image
                v-if="/\.(jpg|jpeg|png|gif)$/i.test(scope.row.file)"
                :src="scope.row.file"
                :preview-src-list="[scope.row.file]"
                :preview-teleported="true"
                style="width: 50px; height: 50px; border-radius: 5px; display: block"
            />
            <a v-else :href="scope.row.file" target="_blank" style="color: #409EFF">点击下载</a>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="项目标题" />
      <el-table-column prop="subjectTitle" label="学科方向" />
      <el-table-column prop="teacherName" label="项目负责人" />
      <el-table-column prop="collegeTitle" label="所属院系" />
      <el-table-column prop="content" label="项目简介">
        <template v-slot="scope">
          <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="提交时间" />
      <el-table-column prop="status" label="审核状态">
        <template v-slot="scope">
          <el-tag type="warning" v-if="scope.row.status === '待审核'">{{ scope.row.status }}</el-tag>
          <el-tag type="success" v-if="scope.row.status === '审核通过'">{{ scope.row.status }}</el-tag>
          <el-tag type="danger" v-if="scope.row.status === '审核拒绝'">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核说明" />
      <el-table-column label="操作" width="100">
        <template #default="scope" v-if="data.user.role === 'TEACHER'">
          <el-button :disabled="scope.row.status !== '待审核'" type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
          <el-button :disabled="scope.row.status !== '待审核'" type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
        </template>
        <template #default="scope" v-if="data.user.role !== 'TEACHER'">
          <el-button :disabled="scope.row.status !== '待审核'" type="primary" @click="handleEdit(scope.row)">审核</el-button>
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

  <el-dialog title="科研项目信息" v-model="data.formVisible" width="60%" destroy-on-close>
    <el-form ref="formRef" :model="data.form" label-width="80px" style="padding: 20px 30px 10px 0">
      <el-form-item prop="file" label="项目文件">
        <el-upload
            action="http://localhost:9999/files/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleFileSuccess"
            :show-file-list="true"
            :before-upload="beforeUpload"
        >
          <el-button type="primary">上传文件</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item prop="title" label="项目标题">
        <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入项目标题" />
      </el-form-item>
      <el-form-item prop="title" label="学科方向">
        <el-select
            v-model="data.form.subjectId"
            placeholder="请选择学科方向"
            style="width: 100%"
        >
          <!--              item in data.subjectData循环遍历出来所有的数据-->
          <el-option
              v-for="item in data.subjectData"
              :key="item.id"
              :label="item.title"
              :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="content" label="项目简介">
        <div style="border: 1px solid #ccc; width: 100%">
          <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :mode="mode"
          />
          <Editor
              style="height: 500px; overflow-y: hidden;"
              v-model="data.form.content"
              :mode="mode"
              :defaultConfig="editorConfig"
              @onCreated="handleCreated"
          />
        </div>
      </el-form-item>
      <el-form-item prop="status" label="审核状态" v-if="data.user.role !== 'TEACHER'">
        <el-radio-group v-model="data.form.status">
          <el-radio-button label="待审核" value="待审核" />
          <el-radio-button label="审核通过" value="审核通过" />
          <el-radio-button label="审核拒绝" value="审核拒绝" />
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="reason" label="审核说明" v-if="data.user.role !== 'ADMIN' && data.form.status === '审核拒绝'">
        <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入拒绝说明" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </template>
  </el-dialog>
  <el-dialog title="项目简介" v-model="data.viewVisible" width="60%" destroy-on-close>
    <div v-html="data.content" style="padding: 0 20px"></div>
  </el-dialog>
</template>

<script setup>
import { Search } from "@element-plus/icons-vue";
import { reactive, shallowRef, onBeforeUnmount } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import '@wangeditor/editor/dist/css/style.css' // 引入 css

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  title: null,
  form: {},
  formVisible: false,
  content: null,
  viewVisible: false,
  subjectData: []
})

/* wangEditor5 初始化开始 */
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: 'http://localhost:9999/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file'  // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */

const load = () => {
  request.get('/project/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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

const reset = () => {
  data.title = null
  load()
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleFileSuccess = (res) => {
  data.form.file = res.data
}

const beforeUpload = (file) => {
  const isLt50M = file.size / 1024 / 1024 < 50
  if (!isLt50M) {
    ElMessage.error('文件大小不能超过 50MB!')
  }
  return isLt50M
}

const add = () => {
  request.post('/project/add', data.form).then(res => {
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
  request.put('/project/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/project/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

const save = () => {
  data.form.id ? update() : add()
}

const viewContent = (content) => {
  ElMessageBox.alert(content, '项目简介', { dangerouslyUseHTMLString: true })
}

const loadSubject = () => {
  request.get('/subject/selectAll').then(res => {
    if (res.code === '200') {
      data.subjectData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadSubject()
</script>

<style scoped>
.card {
  background: #fff;
  padding: 15px;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}
</style>
