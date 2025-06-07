package com.example.service;

import com.example.entity.Division;
import com.example.mapper.DivisionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivisionService {

    @Resource
    DivisionMapper divisionMapper;

    public void add(Division division) {
        divisionMapper.insert(division);
    }

    public void update(Division division) {
        divisionMapper.updateById(division);
    }

    public void deleteById(Integer id) {
        divisionMapper.deleteById(id);
    }

    public List<Division> selectAll(Division division) {
        return divisionMapper.selectAll(division);
    }

    public PageInfo<Division> selectPage(Integer pageNum, Integer pageSize, Division division) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Division> list = divisionMapper.selectAll(division);
        return PageInfo.of(list);
    }
}
