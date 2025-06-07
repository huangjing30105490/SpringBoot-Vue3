package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    public void add(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("500", "您的角色暂无权限执行该操作");
        }
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public void update(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("500", "您的角色暂无权限执行该操作");
        }
        noticeMapper.updateById(notice);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            throw new CustomerException("500", "您的角色暂无权限执行该操作");
        }
        noticeMapper.deleteById(id);
    }

    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }
}
