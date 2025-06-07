<template>
  <div>
    <div class="card">你好同学！欢迎您使用本系统，祝您毕业顺利！</div>
    <div class="card" style="margin-top: 10px;" v-if="data.user.role === 'TEACHER'">
      <div style="font-size: 18px; margin-bottom: 20px;">系统公告</div>
      <div style="display: flex; gap: 20px;">
        <div style="flex: 1;">
          <el-timeline style="max-width: 600px">
            <el-timeline-item
                :timestamp="item.time"
                color="#0bbd87"
                placement="top"
                v-for="item in data.noticeData"
            >
              <h4>{{ item.title }}</h4>
              <p>{{ item.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div style="flex: 1; height: 400px;" id="radar"></div>
      </div>
    </div>

    <div v-else>
      <div style="display: flex; grid-gap: 10px">
        <div class="card" style="height: 400px; width: 50%" id="pie"></div>
        <div class="card" style="height: 400px; width: 50%" id="bar"></div>
      </div>

      <!-- 新增下拉框：选择统计周期 -->
      <div style="margin: 10px 0;">
        <el-select v-model="lineRange" placeholder="选择统计周期" @change="loadLine">
          <el-option label="最近一周" value="week" />
          <el-option label="最近一个月" value="month" />
          <el-option label="最近一年" value="year" />
        </el-select>
      </div>

      <div class="card" style="height: 400px; width: 100%;" id="line"></div>
    </div>
  </div>
</template>


<script setup>
import { reactive, onMounted, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import * as echarts from "echarts";
import { nextTick } from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem("code_user") || "{}"),
  noticeData: []
});

const lineRange = ref("week"); // 折线图统计周期

const loadNotice = () => {
  request.get("/notice/selectAll").then((res) => {
    if (res.code === "200") {
      data.noticeData = res.data.slice(0, 3);
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const loadPie = () => {
  request.get("/echarts/pie").then((res) => {
    if (res.code === "200") {
      let myChart = echarts.init(document.getElementById("pie"));
      pieOptions.series[0].data = res.data;
      myChart.setOption(pieOptions);
    }
  });
};

const loadBar = () => {
  request.get("/echarts/bar").then((res) => {
    if (res.code === "200") {
      let myChart = echarts.init(document.getElementById("bar"));
      barOptions.xAxis.data = res.data.xAxis;
      barOptions.series[0].data = res.data.yAxis;
      myChart.setOption(barOptions);
    }
  });
};

const loadLine = () => {
  request.get("/echarts/line", {
    params: { range: lineRange.value }
  }).then((res) => {
    if (res.code === "200") {
      nextTick(() => {
        const dom = document.getElementById("line");
        echarts.dispose(dom); // 清除之前实例，防止重复绘图
        const myChart = echarts.init(dom);

        lineOptions.title.subtext = `统计维度：${
            {
              week: "最近一周",
              month: "最近一个月",
              year: "最近一年"
            }[lineRange.value]
        }`;
        lineOptions.xAxis.data = res.data.xAxis;
        lineOptions.series[0].data = res.data.yAxis;

        myChart.setOption(lineOptions, true); // 强制刷新
      });
    }
  });
};


const loadRadar = () => {
  request.get("/echarts/radar").then((res) => {
    if (res.code === "200") {
      let myChart = echarts.init(document.getElementById("radar"));
      radarOptions.radar.indicator = res.data.indicator;
      radarOptions.series[0].data = res.data.seriesData;
      myChart.setOption(radarOptions);
    }
  });
};

onMounted(() => {
  loadNotice();
  loadPie();
  loadBar();
  loadLine();
  loadRadar();
});

// 平滑折线图
let lineOptions = {
  title: {
    text: '教师发布项目数量趋势',
    subtext: '统计维度：最近一周',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    formatter: function(params) {
      return params[0].axisValue + '<br/>项目数量: ' + params[0].data;
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [],
    axisLabel: {
      rotate: 30
    }
  },
  yAxis: {
    type: 'value',
    name: '项目数量'
  },
  series: [{
    name: '项目数量',
    type: 'line',
    smooth: true,
    data: [],
    areaStyle: {
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
        offset: 0,
        color: 'rgba(58, 77, 233, 0.8)'
      }, {
        offset: 1,
        color: 'rgba(58, 77, 233, 0.1)'
      }])
    },
    markPoint: {
      data: [
        { type: 'max', name: '最大值' },
        { type: 'min', name: '最小值' }
      ]
    },
    markLine: {
      data: [{ type: 'average', name: '平均值' }]
    }
  }]
};
// 柱状图
let barOptions = {
  title: {
    text: '不同用户发布项目数量Top5', // 主标题
    subtext: '统计维度：用户昵称', // 副标题
    left: 'center'
  },
  grid : {   // ---------------------------增加这个属性，bottom就是距离底部的距离
    top: '20%',
    bottom : '20%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
    name: '用户昵称',
    axisLabel: {
      show: true, // 是否显示刻度标签，默认显示
      interval: 0, // 坐标轴刻度标签的显示间隔，在类目轴中有效；默认会采用标签不重叠的策略间隔显示标签；可以设置成0强制显示所有标签；如果设置为1，表示『隔一个标签显示一个标签』，如果值为2，表示隔两个标签显示一个标签，以此类推。
      rotate: -60, // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠；旋转的角度从-90度到90度
      inside: false, // 刻度标签是否朝内，默认朝外
      margin: 6, // 刻度标签与轴线之间的距离
    },
  },
  yAxis: {
    type: 'value',
    name: '攻略数量',
  },
  tooltip: {
    trigger: 'item',
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () {
            return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);
          }
        },
      },
    }
  ]
};

// 饼图
let pieOptions = {
  title: {
    text: '不同分类下用户申请项目的数量', // 主标题
    subtext: '统计维度：学科方向', // 副标题
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比', // 鼠标移上去显示内容
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        // {value: 1048, name: '瑞幸咖啡'}, // 示例数据：name表示维度，value表示对应的值
        // {value: 735, name: '雀巢咖啡'},
        // {value: 580, name: '星巴克咖啡'},
        // {value: 484, name: '栖巢咖啡'},
        // {value: 300, name: '小武哥咖啡'}
      ]
    }
  ]
};

let radarOptions = {
  title: {
    text: '教师科研能力分析雷达图',
    subtext: '统计维度：各项科研成果',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    data: ['当前教师数据', '全校平均数据'],
    orient: 'vertical',
    left: 'left'
  },
  radar: {
    // shape: 'circle',
    indicator: [
      { name: '项目申请', max: 10 },
      { name: '论文发表', max: 10 },
      { name: '书籍出版', max: 10 },
      { name: '培训情况', max: 10 },
      { name: '荣誉获取', max: 10 }
    ]
  },
  series: [
    {
      name: '科研能力分析',
      type: 'radar',
      data: [
        {
          value: [4, 3, 2, 5, 3],
          name: '当前教师数据'
        },
        {
          value: [3, 2, 1, 3, 2],
          name: '全校平均数据'
        }
      ]
    }
  ]
};
</script>