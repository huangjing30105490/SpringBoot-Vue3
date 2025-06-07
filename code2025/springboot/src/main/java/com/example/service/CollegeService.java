package com.example.service;

import com.example.entity.College;
import com.example.mapper.CollegeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    @Resource
    CollegeMapper collegeMapper;

    public void add(College college) {
        collegeMapper.insert(college);
    }

    public void update(College college) {
        collegeMapper.updateById(college);
    }

    public void deleteById(Integer id) {
        collegeMapper.deleteById(id);
    }

    public List<College> selectAll(College college) {
        return collegeMapper.selectAll(college);
    }

    public PageInfo<College> selectPage(Integer pageNum, Integer pageSize, College college) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<College> list = collegeMapper.selectAll(college);
        return PageInfo.of(list);
    }
}
