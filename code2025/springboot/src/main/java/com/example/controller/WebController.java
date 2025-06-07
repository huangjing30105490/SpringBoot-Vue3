package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Teacher;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.TeacherService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    @Autowired
    private TeacherService teacherService;

    //  表示这是一个 get请求的接口
    @GetMapping("/") // 接口的路径，全局唯一的
    public Result hello() {
        return Result.success("hello");  // 接口的返回值
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if ("ADMIN".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("USER".equals(account.getRole())) {
            dbAccount = userService.login(account);
        } else if ("TEACHER".equals(account.getRole())) {
            dbAccount = teacherService.login(account);
        } else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);
    }

//    @PostMapping("/register")
//    public Result register(@RequestBody User user) {
//        userService.register(user);
//        return Result.success();
//    }
    @PostMapping("/register")
    public Result register(@RequestBody Teacher teacher) {
        teacherService.register(teacher);
        return Result.success();
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if ("USER".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        if ("TEACHER".equals(account.getRole())) {
            teacherService.updatePassword(account);
        }
        return Result.success();
    }
    /**点击页面没反应怎么办？

    打开F12看看前端控制台有没有报错，有报错就看一下，没有就下一步
    要确定一下有没有调用到后端接口（打断点），如果没有走到那就排查一下接口，如果走到了，跟着断点跟到逻辑那里看看是不是正确的走向。
    再打开F12网络，看看该接口返回的数据。
     */

}
