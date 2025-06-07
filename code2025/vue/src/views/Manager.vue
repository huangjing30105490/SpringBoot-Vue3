<template>
  <div>
    <!-- 头部区域开始 -->
    <div style="height: 60px; display: flex;">
      <div style="width: 280px; display: flex; align-items: center; padding-left: 20px; background-color: #3a456b">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/logo.png" alt="">
        <span style="font-size: 20px; font-weight: bold; color: #f1f1f1; margin-left: 5px">高校教师业务档案管理</span>
      </div>
      <div style="flex: 1; display: flex; align-items: center; padding-left: 20px; border-bottom: 1px solid #ddd">
        <span style="margin-right: 5px; cursor: pointer" @click="router.push('/manager/home')">首页</span> / <span style="margin-left: 5px">{{ router.currentRoute.value.meta.name }}</span>
      </div>
      <div style="width: fit-content; padding-right: 20px; display: flex; align-items: center; border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex; align-items: center">
            <img v-if="data.user?.avatar" style="width: 40px; height: 40px; border-radius: 50%" :src="data.user?.avatar" />
            <img v-else style="width: 40px; height: 40px; border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
            <span style="margin-left: 5px">{{ data.user?.name }}</span>
<!--            ？的作用  如果 data.user 是 null 或 undefined，表达式会直接返回 undefined，而不会抛出错误。  有些人没有设置。<span>主要用于包裹文本或其他行内元素行内元素默认不会换行，与其他行内元素在同一行内排列-->
          </div>
          <template #dropdown>
            <el-dropdown-menu>
<!--              跳转网址-->
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!--  头部区域结束 -->

    <!--  下方区域开始 -->
    <div style="display: flex">
      <!--  菜单区域开始 -->
      <div style="width: 280px;">
        <el-menu router :default-openeds="['1', '2', '3', '4']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>院系部门</span>
            </template>
            <el-menu-item index="/manager/college">院校管理</el-menu-item>
            <el-menu-item index="/manager/division">班级管理</el-menu-item>
            <el-menu-item index="/manager/subject">学科方向</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>项目管理</span>
            </template>
            <el-menu-item index="/manager/project">科研项目</el-menu-item>
            <el-menu-item index="/manager/page">学术论文</el-menu-item>
            <el-menu-item index="/manager/books">出版书籍</el-menu-item>
            <el-menu-item index="/manager/honor">获得荣誉</el-menu-item>
<!--            <el-menu-item index="/manager/apply">项目经费申请</el-menu-item>-->
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">系统公告</el-menu-item>
            <el-menu-item index="/manager/notice" v-else>公告信息</el-menu-item>
<!--            <el-menu-item index="/manager/category">攻略分类</el-menu-item>-->
<!--            <el-menu-item index="/manager/introduction">旅游攻略</el-menu-item>-->
            <el-menu-item index="/manager/book">培训信息</el-menu-item>
            <el-menu-item index="/manager/record">培训记录</el-menu-item>
            <el-menu-item index="/manager/teach">教学情况</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">院级管理员</el-menu-item>
            <el-menu-item index="/manager/teachers">教师信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <!--  菜单区域结束 -->

      <!--  数据渲染区域开始 -->
      <div style="flex: 1; width: 0; padding: 10px; background-color: #f2f4ff">
<!--        如果路由有嵌套结构（如 /user/profile），需要在父路由组件中再次使用 <RouterView>：-->
        <RouterView @updateUser="updateUser"/>
<!--        @updateUser用于监听是否调用了，调用了就触发自己的函数-->
      </div>
      <!--  数据渲染区域结束 -->

    </div>
    <!--  下方区域结束 -->

  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}")
})

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}

// if (!data.user?.id) {
//   location.href = '/login'
// }
</script>

<style>
.el-menu {
  background-color: #3a456b;
  border: none;
}
.el-sub-menu__title {
  background-color: #3a456b;
  color: #ddd;
}
.el-menu-item {
  height: 50px;
  color: #ddd;
}
.el-menu .is-active {
  background-color: #537bee;
  color: #fff;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}
.el-menu-item:not(.is-active):hover {
  background-color: #7a9fff;
  color: #333;
}
.el-dropdown {
  cursor: pointer;
}
.el-tooltip__trigger {
  outline: none;
}
.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}
</style>