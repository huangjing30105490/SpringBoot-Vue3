package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Page;
import com.example.entity.Subject;
import com.example.mapper.PageMapper;
import com.example.mapper.SubjectMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Resource
    PageMapper pageMapper;
    @Resource
    SubjectMapper subjectMapper;
    @Resource
    UserMapper userMapper;

    public void add(Page page) {
        Account currentUser = TokenUtils.getCurrentUser();
        page.setTeacherId(currentUser.getId());
        page.setTime(DateUtil.now());
        page.setStatus("待审核");
        pageMapper.insert(page);
    }

    public void update(Page page) {
        Account currentUser = TokenUtils.getCurrentUser();
        pageMapper.updateById(page);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        pageMapper.deleteById(id);
    }

    public List<Page> selectAll(Page page) {
        return pageMapper.selectAll(page);
    }

    public PageInfo<Page> selectPage(Integer pageNum, Integer pageSize, Page page) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("TEACHER".equals(currentUser.getRole())) {
            page.setTeacherId(currentUser.getId());
        }

        if ("USER".equals(currentUser.getRole())) {
            page.setTeacherId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Page> list = pageMapper.selectAll(page);
        for(Page dbPage : list){
            Integer subjectId = dbPage.getSubjectId();
            Subject subject = subjectMapper.selectById(subjectId);
            if(ObjectUtil.isNotNull(subject)){
                dbPage.setSubjectTitle(subject.getTitle());
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

}
