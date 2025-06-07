package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Honor;
import com.example.entity.Subject;
import com.example.mapper.HonorMapper;
import com.example.mapper.SubjectMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HonorService {

    @Resource
    HonorMapper honorMapper;
    @Resource
    SubjectMapper subjectMapper;
    @Resource
    UserMapper userMapper;

    public void add(Honor honor) {
        Account currentUser = TokenUtils.getCurrentUser();
        honor.setTeacherId(currentUser.getId());
        honor.setTime(DateUtil.now());
        honor.setStatus("待审核");
        honorMapper.insert(honor);
    }

    public void update(Honor honor) {
        Account currentUser = TokenUtils.getCurrentUser();
        honorMapper.updateById(honor);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        honorMapper.deleteById(id);
    }

    public List<Honor> selectAll(Honor honor) {
        return honorMapper.selectAll(honor);
    }

    public PageInfo<Honor> selectPage(Integer pageNum, Integer pageSize, Honor honor) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("TEACHER".equals(currentUser.getRole())) {
            honor.setTeacherId(currentUser.getId());
        }

        if ("USER".equals(currentUser.getRole())) {
            honor.setTeacherId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Honor> list = honorMapper.selectAll(honor);
        for(Honor dbHonor : list){
            Integer subjectId = dbHonor.getSubjectId();
            Subject subject = subjectMapper.selectById(subjectId);
            if(ObjectUtil.isNotNull(subject)){
                dbHonor.setSubjectTitle(subject.getTitle());
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
