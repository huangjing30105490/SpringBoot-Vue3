package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Apply;
import com.example.exception.CustomerException;
import com.example.mapper.ApplyMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {

    @Resource
    ApplyMapper applyMapper;

    public void add(Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        apply.setUserId(currentUser.getId());
        apply.setTime(DateUtil.now());
        apply.setStatus("待审核");
        applyMapper.insert(apply);
    }

    public void update(Apply apply) {
        applyMapper.updateById(apply);
    }

    public void deleteById(Integer id) {
        applyMapper.deleteById(id);
    }

    public List<Apply> selectAll(Apply apply) {
        return applyMapper.selectAll(apply);
    }

    public PageInfo<Apply> selectPage(Integer pageNum, Integer pageSize, Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            apply.setUserId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
        return PageInfo.of(list);
    }
}
