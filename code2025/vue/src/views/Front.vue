<template>
  <div style="background-color: #f6f9fc; min-height: 1000px">
    <el-menu
        :default-active="1"
        class="el-menu-demo"
        mode="horizontal"
    >
      <el-menu-item index="1">系统首页</el-menu-item>
      <el-menu-item @click="logout">退出</el-menu-item>
    </el-menu>

    <div style="margin-bottom: 20px">
      <el-carousel height="350px">
<!--        <el-carousel-item v-for="item in data.carouselData" :key="item">-->
<!--          <img :src="item" alt="" style="height: 350px; width: 100%">-->
<!--        </el-carousel-item>-->
        <el-carousel-item v-for="item in data.introductionData" :key="item">
          <img :src="item.img" alt="" style="cursor: pointer; height: 350px; width: 100%" @click="navTo('/front/introductionDetail?id=' + item.id)">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="width: 80%; margin: 20px auto">
      <div style="font-size: 20px; border-left: 5px solid #2fbd67; padding-left: 10px; height: 30px; line-height: 30px">旅游攻略</div>
      <div style="margin-top: 20px; display: flex; grid-gap: 20px" v-for="item in data.introductionData">
        <div style="flex: 1">
          <img @click="navTo('/front/introductionDetail?id=' + item.id)" :src="item.img" alt="" style="width: 100%; height: 220px; display: block; border-radius: 5px; cursor: pointer">
        </div>
        <div style="flex: 3">
          <div style="font-size: 20px; font-weight: bold; cursor: pointer" @click="navTo('/front/introductionDetail?id=' + item.id)">{{ item.title }}</div>
          <div style="margin-top: 10px; font-size: 15px; color: #666666; line-height: 25px; height: 125px; text-align: justify" class="line5">{{ item.description }}</div>
          <div style="display: flex; align-items: center; margin-top: 10px; grid-gap: 10px">
            <img :src="item.userAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
            <div style="font-size: 15px">{{ item.userName }}</div>
            <div style="color: #666666">{{ item.time }}</div>
          </div>
        </div>
      </div>
    </div>

    <div style="width: 80%; margin: 20px auto">
      <div style="font-size: 20px; border-left: 5px solid #2fbd67; padding-left: 10px; height: 30px; line-height: 30px">旅游攻略</div>
      <div style="margin-top: 20px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.introductionData" style="margin-bottom: 20px">
            <img :src="item.img" alt="" style="width: 100%; height: 250px; border-radius: 5px">
            <div style="font-size: 17px; font-weight: bold; margin-top: 10px">{{ item.title }}</div>
            <div style="display: flex; align-items: center; margin-top: 10px; grid-gap: 10px">
              <img :src="item.userAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
              <div style="font-size: 15px">{{ item.userName }}</div>
              <div style="color: #666666">{{ item.time }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

  </div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import lun1 from '@/assets/imgs/lun-1.jpg';
import lun2 from '@/assets/imgs/lun-2.jpg';
import lun3 from '@/assets/imgs/lun-3.jpg';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionData: [],
  carouselData: [lun1, lun2, lun3]
})

const loadIntroduction = () => {
  request.get('/introduction/selectAll').then(res => {
    if (res.code === '200') {
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadIntroduction()

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}
const navTo = (url) => {
  location.href = url
}
</script>
<style>
.line5 {
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5; /* 超出几行省略 */
  overflow: hidden;
}
</style>