package com.example.service;

import com.example.entity.Account;
import com.example.entity.Teach;
import com.example.mapper.TeachMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachService {

    @Resource
    TeachMapper teachMapper;

    public void add(Teach teach) {
        teachMapper.insert(teach);
    }

    public void update(Teach teach) {
        Account currentUser = TokenUtils.getCurrentUser();
        teachMapper.updateById(teach);
    }

    public void deleteById(Integer id) {
        teachMapper.deleteById(id);
    }

    public List<Teach> selectAll(Teach teach) {
        return teachMapper.selectAll(teach);
    }

    public PageInfo<Teach> selectPage(Integer pageNum, Integer pageSize, Teach teach) {
        // 开启分页查询
        Account currentUser = TokenUtils.getCurrentUser();
        if ("TEACHER".equals(currentUser.getRole())) {
            teach.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Teach> list = teachMapper.selectAll(teach);
        return PageInfo.of(list);
    }
}
