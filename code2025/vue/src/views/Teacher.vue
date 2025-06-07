<template>
  <div>
    <!-- 使用纵向布局：flex-direction: column -->
    <div style="display: flex; flex-direction: column; height: 100vh;">

      <!-- 顶部导航栏 -->
      <div class="header">

        <!-- 左侧 LOGO 和系统名称 -->
        <div class="logo">
          <img src="@/assets/imgs/logo.png" alt="logo" />
          <span>教师人事档案管理系统</span>
        </div>

        <!-- 中间菜单 -->
        <div class="nav">
          <div
              v-for="item in menus"
              :key="item.path"
              :class="['nav-item', route.path === item.path ? 'active' : '']"
              @click="handleMenuClick(item)"
          >
            {{ item.name }}
          </div>
        </div>

        <!-- 右侧头像和用户信息 -->
        <div class="user-container">
            <div class="user-info">
              <img
                  v-if="data.user?.avatar"
                  :src="data.user.avatar"
                  alt="avatar"
              />
              <img
                  v-else
                  src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                  alt="default avatar"
              />
              <span>{{ data.user?.name || '未登录' }}</span>
            </div>
        </div>
      </div>

      <!-- 下方内容区 -->
      <div style="flex: 1; padding: 10px; background-color: #eeeff1; ">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { reactive, onMounted } from 'vue'

const router = useRouter()
const route = useRoute()

// 示例用户数据（你可以从接口或本地缓存获取）
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}")
})

// 菜单配置
const menus = [
  { name: '首页', path: '/teacher/home' },
  { name: '教学情况', path: '/teacher/teach' },
  { name: '进修培训', path: '/teacher/book' },
  { name: '培训记录', path: '/teacher/record' },
  { name: '科研项目申请', path: '/teacher/project' },
  { name: '发表论文', path: '/teacher/page' },
  { name: '出版书籍', path: '/teacher/books' },
  { name: '获得荣誉', path: '/teacher/honor' },
  // { name: '经费申请', path: '/teacher/apply' },
  // { name: '数据分析', path: '/notice' },
  { name: '个人中心', path: '/teacher/person' },
  { name: '修改密码', path: '/teacher/updatePassword' },
  { name: '退出登录', path: '/login', isLogout: true }
]

// 示例退出方法
const logout = () => {
  // 清除缓存/状态
  localStorage.removeItem('user')
  router.push('/login')
}

// 监听更新用户事件（可选）
const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}

const handleMenuClick = (item) => {
  if (item.isLogout) {
    // 如果是退出登录菜单
    localStorage.removeItem('code_user')
  }
  router.push(item.path)
}
</script>

<style scoped>
.header {
  height: 60px;
  background-color: rgb(24, 163, 227);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 30px;
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  width: 40px;
  margin-right: 10px;
  border-radius: 50%;
}

.logo span {
  font-size: 18px;
  font-weight: bold;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav-item {
  cursor: pointer;
  padding: 5px 10px;
  transition: all 0.3s;
}

.nav-item:hover {
  color: #00cc99;
}

.nav-item.active {
  border-bottom: 2px solid #00cc99;
}

.user-container {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  color: #fff;
  cursor: pointer;
}

.user-info img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-info span {
  margin-left: 8px;
}
</style>
