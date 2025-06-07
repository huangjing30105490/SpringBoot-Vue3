package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.*;
import com.example.mapper.CategoryMapper;
import com.example.mapper.ProjectMapper;
import com.example.mapper.SubjectMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Resource
    ProjectMapper projectMapper;
    @Resource
    SubjectMapper subjectMapper;
    @Resource
    UserMapper userMapper;

    public void add(Project project) {
        Account currentUser = TokenUtils.getCurrentUser();
        project.setTeacherId(currentUser.getId());
        project.setTime(DateUtil.now());
        project.setStatus("待审核");
        projectMapper.insert(project);
    }

    public void update(Project project) {
        Account currentUser = TokenUtils.getCurrentUser();
        projectMapper.updateById(project);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        projectMapper.deleteById(id);
    }

    public List<Project> selectAll(Project project) {
        return projectMapper.selectAll(project);
    }

    public PageInfo<Project> selectPage(Integer pageNum, Integer pageSize, Project project) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("TEACHER".equals(currentUser.getRole())) {
            project.setTeacherId(currentUser.getId());
        }

        if ("USER".equals(currentUser.getRole())) {
            project.setTeacherId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Project> list = projectMapper.selectAll(project);
        for(Project dbProject : list){
            Integer subjectId = dbProject.getSubjectId();
            Subject subject = subjectMapper.selectById(subjectId);
            if(ObjectUtil.isNotNull(subject)){
                dbProject.setSubjectTitle(subject.getTitle());
            }
        }
        // 这个list里面存储了旅游攻略的原始数据（只有分类id，categoryId）
        /*for (Introduction dbIntroduction : list) {
            // 先拿到categoryId
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            // 通过categoryId从category表里通过主键查询出分类数据
            Category category = categoryMapper.selectById(categoryId);
            User user = userMapper.selectById(userId.toString());
            if (ObjectUtil.isNotEmpty(category)) {
                // 把分类的title赋值给categoryTitle
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if (ObjectUtil.isNotEmpty(user)) {
                dbIntroduction.setUserName(user.getName());
            }
        }*/
        return PageInfo.of(list);
    }

    public List<Map<String, Object>> getCountGroupByDate(Date start, Date end, String pattern) {
        return projectMapper.countGroupByDate(start, end, pattern);
    }
}
