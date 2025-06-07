package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.*;
import com.example.entity.Record;
import com.example.service.*;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    public SubjectService subjectService;
    @Resource
    public ProjectService projectService;
    @Resource
    public TeacherService teacherService;
    @Resource
    public PageService pageService;
    @Resource
    public BooksService booksService;
    @Resource
    public HonorService hornorService;
    @Autowired
    private HonorService honorService;
    @Resource
    private RecordService recordService;


    @GetMapping("/pie")
    public Result pie() {
        List<Map<String, Object>> list = new ArrayList<>();
        // 查询出所有的分类信息
        List<Subject> subjects = subjectService.selectAll(new Subject());
        // 查询出所有的帖子信息
        List<Project> projects = projectService.selectAll(new Project());
        for (Subject subject : subjects) {
            long count = projects.stream().filter(x -> subject.getId().equals(x.getSubjectId())).count();
            Map<String, Object> map = new HashMap<>();
            map.put("name", subject.getTitle());
            map.put("value", count);
            list.add(map);
        }
        return Result.success(list);
    }

    @GetMapping("/bar")
    public Result bar() {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> xList = new ArrayList<>();
        List<Long> yList = new ArrayList<>();

        Map<String, Long> map = new HashMap<>();

        // 查询所有的用户
        List<Teacher> teachers = teacherService.selectAll(new Teacher());
        // 查询出所有的帖子信息
        List<Project> projects = projectService.selectAll(new Project());
        for (Teacher teacher : teachers) {
            long count = projects.stream().filter(x -> teacher.getId().equals(x.getTeacherId())).count();
            map.put(teacher.getName(), count);
        }

        // 对map进行排序，按照value来倒序
        LinkedHashMap<String, Long> collectMap = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (String key : collectMap.keySet()) {
            xList.add(key);
            yList.add(collectMap.get(key));
        }

        // top几的的问题，截断一下
        if (xList.size() > 5 && yList.size() > 5) {
            xList = xList.subList(0, 5);
            yList = yList.subList(0, 5);
        }

        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);
        return Result.success(resultMap);
    }
    @GetMapping("/line")
    public Result line(@RequestParam(defaultValue = "week") String range) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Long> yList = new ArrayList<>();

        // 获取当前时间
        Date today = new Date();
        DateTime start;

        // 根据 range 参数决定开始时间
        switch (range) {
            case "month":
                start = DateUtil.offsetMonth(today, -1);
                break;
            case "year":
                start = DateUtil.offsetMonth(today, -12);
                break;
            default: // 默认 week
                start = DateUtil.offsetDay(today, -7);
        }

        // 生成日期列表（按天）
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR)
                .stream().map(DateUtil::formatDate).toList();

        // 查出所有项目
        List<Project> projects = projectService.selectAll(new Project());

        // 每天统计数量
        for (String day : xList) {
            long count = projects.stream()
                    .filter(x -> ObjectUtil.isNotEmpty(x.getTime()) && x.getTime().contains(day))
                    .count();
            yList.add(count);
        }

        resultMap.put("xAxis", xList);
        resultMap.put("yAxis", yList);
        return Result.success(resultMap);
    }

    @GetMapping("/radar")
    public Result radar() {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> indicators = new ArrayList<>();
        List<Map<String, Object>> seriesData = new ArrayList<>();

        // Get current user
        Account currentUser = TokenUtils.getCurrentUser();

        // 查询所有教师，用于后续计算
        List<Teacher> allTeachers = teacherService.selectAll(new Teacher());
        int teacherCount = allTeachers.size() > 0 ? allTeachers.size() : 1; // 避免除以零错误

        // 计算各类数据的最大值，用于设置雷达图的max值
        int maxProjectCount = 0;
        int maxThesisCount = 0;
        int maxBooksCount = 0;
        int maxResearchCount = 0;
        int maxHonorCount = 0;

        // 计算当前教师各项数据的总和，用于计算平均值
        int totalProjectCount = 0;
        int totalThesisCount = 0;
        int totalBooksCount = 0;
        int totalResearchCount = 0;
        int totalHonorCount = 0;

        // 遍历所有教师，计算所有教师的数据最大值和总和
        for (Teacher teacher : allTeachers) {
            // 设置查询参数
            Integer teacherId = teacher.getId();

            Project projectQuery = new Project();
            projectQuery.setTeacherId(teacherId);
            int projectCount = projectService.selectAll(projectQuery).size();
            maxProjectCount = Math.max(maxProjectCount, projectCount);
            totalProjectCount += projectCount;

            Page pageQuery = new Page();
            pageQuery.setTeacherId(teacherId);
            int thesisCount = pageService.selectAll(pageQuery).size();
            maxThesisCount = Math.max(maxThesisCount, thesisCount);
            totalThesisCount += thesisCount;

            Books booksQuery = new Books();
            booksQuery.setTeacherId(teacherId);
            int booksCount = booksService.selectAll(booksQuery).size();
            maxBooksCount = Math.max(maxBooksCount, booksCount);
            totalBooksCount += booksCount;

            Record researchQuery = new Record();
            researchQuery.setTeacherId(teacherId);
            int researchCount = recordService.selectAll(researchQuery).size();
            maxResearchCount = Math.max(maxResearchCount, researchCount);
            totalResearchCount += researchCount;

            Honor honorQuery = new Honor();
            honorQuery.setTeacherId(teacherId);
            int honorCount = honorService.selectAll(honorQuery).size();
            maxHonorCount = Math.max(maxHonorCount, honorCount);
            totalHonorCount += honorCount;
        }

        // 确保最大值至少为1，避免除零错误
        maxProjectCount = Math.max(maxProjectCount, 1);
        maxThesisCount = Math.max(maxThesisCount, 1);
        maxBooksCount = Math.max(maxBooksCount, 1);
        maxResearchCount = Math.max(maxResearchCount, 1);
        maxHonorCount = Math.max(maxHonorCount, 1);

        // 计算平均值
        int avgProjectCount = totalProjectCount / teacherCount;
        int avgThesisCount = totalThesisCount / teacherCount;
        int avgBooksCount = totalBooksCount / teacherCount;
        int avgResearchCount = totalResearchCount / teacherCount;
        int avgHonorCount = totalHonorCount / teacherCount;

        // 设置雷达图指标，使用实际最大值的1.2倍作为上限以留出空间
        Map<String, Object> projectIndicator = new HashMap<>();
        projectIndicator.put("name", "项目申请");
        projectIndicator.put("max", (int)(maxProjectCount * 1.2));
        indicators.add(projectIndicator);

        Map<String, Object> thesisIndicator = new HashMap<>();
        thesisIndicator.put("name", "论文发表");
        thesisIndicator.put("max", (int)(maxThesisCount * 1.2));
        indicators.add(thesisIndicator);

        Map<String, Object> booksIndicator = new HashMap<>();
        booksIndicator.put("name", "书籍出版");
        booksIndicator.put("max", (int)(maxBooksCount * 1.2));
        indicators.add(booksIndicator);

        Map<String, Object> researchIndicator = new HashMap<>();
        researchIndicator.put("name", "培训情况");
        researchIndicator.put("max", (int)(maxResearchCount * 1.2));
        indicators.add(researchIndicator);

        Map<String, Object> honorIndicator = new HashMap<>();
        honorIndicator.put("name", "荣誉获取");
        honorIndicator.put("max", (int)(maxHonorCount * 1.2));
        indicators.add(honorIndicator);

        // 获取当前用户数据
        Integer currentTeacherId = currentUser.getId();

        Project currentProjectQuery = new Project();
        currentProjectQuery.setTeacherId(currentTeacherId);
        int currentProjectCount = projectService.selectAll(currentProjectQuery).size();

        Page currentPageQuery = new Page();
        currentPageQuery.setTeacherId(currentTeacherId);
        int currentThesisCount = pageService.selectAll(currentPageQuery).size();

        Books currentBooksQuery = new Books();
        currentBooksQuery.setTeacherId(currentTeacherId);
        int currentBooksCount = booksService.selectAll(currentBooksQuery).size();

        Record currentResearchQuery = new Record();
        currentResearchQuery.setTeacherId(currentTeacherId);
        int currentResearchCount = recordService.selectAll(currentResearchQuery).size();

        Honor currentHonorQuery = new Honor();
        currentHonorQuery.setTeacherId(currentTeacherId);
        int currentHonorCount = honorService.selectAll(currentHonorQuery).size();

        // 当前用户数据
        Map<String, Object> currentUserData = new HashMap<>();
        currentUserData.put("name", "当前教师数据");
        List<Integer> values = new ArrayList<>();
        values.add(currentProjectCount);
        values.add(currentThesisCount);
        values.add(currentBooksCount);
        values.add(currentResearchCount);
        values.add(currentHonorCount);
        currentUserData.put("value", values);
        seriesData.add(currentUserData);

        // 平均数据
        Map<String, Object> averageData = new HashMap<>();
        averageData.put("name", "全校平均数据");
        List<Integer> avgValues = new ArrayList<>();
        avgValues.add(avgProjectCount);
        avgValues.add(avgThesisCount);
        avgValues.add(avgBooksCount);
        avgValues.add(avgResearchCount);
        avgValues.add(avgHonorCount);
        averageData.put("value", avgValues);
        seriesData.add(averageData);

        // 添加指标和系列数据到结果
        resultMap.put("indicator", indicators);
        resultMap.put("seriesData", seriesData);

        return Result.success(resultMap);
    }
}