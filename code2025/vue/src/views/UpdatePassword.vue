<template>
  <div class="card" style="width: 50%">
    <div style="font-size: 16px">修改密码</div>
<!--    :model：绑定表单数据对象（数据驱动）。 :rules：定义字段验证逻辑（解耦校验规则）。 ref：通过脚本控制表单（校验、重置等）。 prop：关联数据字段与验证规则（关键桥梁）-->
    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 20px 30px 10px 0">
<!--      关联数据字段（prop）。 显示标签文本（label）。 集成表单验证功能rule-->
      <el-form-item prop="password" label="原密码">
        <el-input show-password v-model="data.user.password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item prop="newPassword" label="新密码">
        <el-input show-password v-model="data.user.newPassword" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item prop="new2Password" label="确认密码">
        <el-input show-password v-model="data.user.new2Password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" style="padding: 20px 30px" @click="updatePassword">保存</el-button>
    </div>
  </div>
</template>
<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  rules: {
    password: [
      { required: true, message: '请填写原密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请填写新密码', trigger: 'blur' }
    ],
    new2Password: [
      { required: true, message: '请确认新密码', trigger: 'blur' }
    ],
  }
})

const formRef = ref()

const updatePassword = () => {
  // validate	方法	触发表单整体验证，通过回调或 Promise 返回结果。valid	布尔值	表示表单是否通过验证（true=通过，false=失败）。
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('修改成功')
          setInterval(() => {
            localStorage.removeItem('code_user')
            location.href = '/login'
          }, 500)
          // 修改成功直接跳转肉眼察觉不到，给500ms的延时 setInterval
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>