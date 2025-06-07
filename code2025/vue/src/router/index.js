      import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager/home' },
    { path: '/manager', component: () => import('../views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '主页' }, component: () => import('../views/Home.vue'),},
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('../views/Admin.vue'),},
        { path: 'user', meta: { name: '普通用户信息' }, component: () => import('../views/User.vue'),},
        { path: 'person', meta: { name: '个人信息' }, component: () => import('../views/Person.vue'),},
        { path: 'updatePassword', meta: { name: '修改密码' }, component: () => import('../views/UpdatePassword.vue'),},
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('../views/Notice.vue'),},
        { path: 'introduction', meta: { name: '旅游攻略' }, component: () => import('../views/Introduction.vue'),},
        { path: 'category', meta: { name: '攻略分类' }, component: () => import('../views/Category.vue'),},
        { path: 'apply', meta: { name: '请假申请' }, component: () => import('../views/Apply.vue'),},
        { path: 'book', meta: { name: '图书信息' }, component: () => import('../views/Book.vue'),},
        { path: 'record', meta: { name: '借阅信息' }, component: () => import('../views/Record.vue'),},
        { path: 'project', meta: { name: '科研项目' }, component: () => import('../views/Project.vue'),},
        { path: 'subject', meta: { name: '学科方向' }, component: () => import('../views/Subject.vue'),},
        { path: 'college', meta: { name: '院系部门' }, component: () => import('../views/College.vue'),},
        { path: 'teachers', meta: { name: '教师信息' }, component: () => import('../views/Teachers.vue'),},
        { path: 'page', meta: { name: '教师信息' }, component: () => import('../views/Page.vue'),},
        { path: 'books', meta: { name: '出版书籍' }, component: () => import('../views/Books.vue'),},
        { path: 'honor', meta: { name: '荣誉' }, component: () => import('../views/Honor.vue'),},
        { path: 'teach', meta: { name: '教学情况' }, component: () => import('../views/Teach.vue'),},
        { path: 'division', meta: { name: '院系部门' }, component: () => import('../views/Division.vue'),},
      ]
    },
    { path: '/teacher', component: () => import('../views/Teacher.vue'),
      children: [
        { path: 'home', meta: { name: '主页' }, component: () => import('../views/Home.vue'),},
        { path: 'person', meta: { name: '个人信息' }, component: () => import('../views/Person.vue'),},
        { path: 'updatePassword', meta: { name: '修改密码' }, component: () => import('../views/UpdatePassword.vue'),},
        { path: 'project', meta: { name: '科研项目' }, component: () => import('../views/Project.vue'),},
        { path: 'apply', meta: { name: '请假申请' }, component: () => import('../views/Apply.vue'),},
        { path: 'page', meta: { name: '论文发表' }, component: () => import('../views/Page.vue'),},
        { path: 'books', meta: { name: '出版书籍' }, component: () => import('../views/Books.vue'),},
        { path: 'honor', meta: { name: '荣誉' }, component: () => import('../views/Honor.vue'),},
        { path: 'book', meta: { name: '进修培训' }, component: () => import('../views/Book.vue'),},
        { path: 'record', meta: { name: '培训记录' }, component: () => import('../views/Record.vue'),},
        { path: 'teach', meta: { name: '教学情况' }, component: () => import('../views/Teach.vue'),},
      ]
    },
    { path: '/front/home', component: import('../views/Front.vue'),},
    { path: '/front/introductionDetail', component: import('../views/IntroductionDetail.vue'),},
    { path: '/login', component: import('../views/Login.vue'),},
    { path: '/register', component: import('../views/Register.vue'),},
    { path: '/teacher', component: import('../views/Teacher.vue'),},
    { path: '/notFound', component: import('../views/404.vue'),},
    { path: '/:pathMatch(.*)', redirect: '/notFound' }
  ],
})

export default router
