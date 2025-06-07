package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Teacher;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.TeacherMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Resource
    TeacherMapper teacherMapper;

    public void add(Teacher teacher) {
        // 根据新的账号查询数据库  是否存在同样账号的数据
        Teacher dbTeacher = teacherMapper.selectByUsername(teacher.getUsername());
        if (dbTeacher != null) {
            throw new CustomerException("账号重复");
        }
        // 默认密码
        if (StrUtil.isBlank(teacher.getPassword())) {
            teacher.setPassword("teacher");
        }
        teacher.setRole("TEACHER");
        teacherMapper.insert(teacher);
    }

    public void update(Teacher teacher) {
        teacherMapper.updateById(teacher);
    }

    public void deleteById(Integer id) {
        teacherMapper.deleteById(id);
    }

    public void deleteBatch(List<Teacher> list) {
        for (Teacher teacher : list) {
            this.deleteById(teacher.getId());
        }
    }

    public Teacher selectById(String id) {
        return teacherMapper.selectById(id);
    }

    public List<Teacher> selectAll(Teacher teacher) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            teacher.setId(currentUser.getId());
        }
        return teacherMapper.selectAll(teacher);
    }

    public PageInfo<Teacher> selectPage(Integer pageNum, Integer pageSize, Teacher teacher) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            teacher.setId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> list = teacherMapper.selectAll(teacher);
        return PageInfo.of(list);
    }

    public Teacher login(Account account) {
        // 验证账号是否存在
        Teacher dbTeacher = teacherMapper.selectByUsername(account.getUsername());
        if (dbTeacher == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbTeacher.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        // 创建token并返回给前端
        String token = TokenUtils.createToken(dbTeacher.getId() + "-" + "TEACHER", dbTeacher.getPassword());
        dbTeacher.setToken(token);
        return dbTeacher;
    }

    public void register(Teacher teacher) {
        this.add(teacher);
    }

    public void updatePassword(Account account) {
        // 先判断新密码和确认密码是否一致
        if (!account.getNewPassword().equals(account.getNew2Password())) {
            throw new CustomerException("500", "您两次输入的密码不一致");
        }
        // 校验一下原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if (!account.getPassword().equals(currentUser.getPassword())) {
            throw new CustomerException("500", "原密码输入错误");
        }
        // 开始更新密码了
        Teacher teacher = teacherMapper.selectById(currentUser.getId().toString());
        teacher.setPassword(account.getNewPassword());
        teacherMapper.updateById(teacher);
    }
}
