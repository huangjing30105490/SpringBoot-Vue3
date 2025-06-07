package com.example.service;

import com.example.entity.Subject;
import com.example.mapper.SubjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Resource
    SubjectMapper subjectMapper;

    public void add(Subject subject) {
        subjectMapper.insert(subject);
    }

    public void update(Subject subject) {
        subjectMapper.updateById(subject);
    }

    public void deleteById(Integer id) {
        subjectMapper.deleteById(id);
    }

    public List<Subject> selectAll(Subject subject) {
        return subjectMapper.selectAll(subject);
    }

    public PageInfo<Subject> selectPage(Integer pageNum, Integer pageSize, Subject subject) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Subject> list = subjectMapper.selectAll(subject);
        return PageInfo.of(list);
    }
}
