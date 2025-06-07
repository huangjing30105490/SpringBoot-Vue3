<template>
<!--  <div style="display: flex; justify-content: center;" >-->
    <div class="card" style="width: 50%" >
      <div style="font-size: 15px">个人中心</div>
      <el-form ref="formRef" :model="data.user" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.user.username" autocomplete="off" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.user.name" autocomplete="off" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.user.phone" autocomplete="off" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.user.email" autocomplete="off" placeholder="请输入邮箱" />
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
      <div style="text-align: center">
        <el-button type="primary" style="padding: 18px 35px" @click="update">保存</el-button>
      </div>
    </div>
<!--  </div>-->

</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
})

const handleFileSuccess = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])
// emit 是一个用于 触发自定义事件 的函数
// emit，子组件中触发父组件监听的事件。更新manager的数据，数据联通
const update = () => {
  //自己定义一个后判断是管理员还是用户
  let url
  if (data.user.role === 'ADMIN') {
    url = '/admin/update'
  }
  if (data.user.role === 'USER') {
    url = '/user/update'
  }
  if(data.user.role === 'TEACHER') {
    url = '/teacher/update'
  }
  // 发送请求，更新是put请求
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      // 更新成功之后从数据库拿数据更新
      localStorage.setItem("code_user", JSON.stringify(data.user))
      emit('updateUser')
    }
  })
}
</script>