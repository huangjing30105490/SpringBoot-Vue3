package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public void add(Admin admin) {
        // 根据新的账号查询数据库  是否存在同样账号的数据
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            throw new CustomerException("账号重复");
        }
        // 默认密码
        if (StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("admin");
        }
        admin.setRole("ADMIN");
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Admin> list) {
        for (Admin admin : list) {
            this.deleteById(admin.getId());
        }
    }

    public Admin selectById(String id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public Admin login(Account account) {
        // 验证账号是否存在
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (dbAdmin == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        // 创建token并返回给前端
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + "ADMIN", dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
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
        Admin admin = adminMapper.selectById(currentUser.getId().toString());
        admin.setPassword(account.getNewPassword());
        adminMapper.updateById(admin);
    }
}
