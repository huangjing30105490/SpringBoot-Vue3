package com.example.controller;

import com.example.common.Result;
import com.example.entity.Subject;
import com.example.service.SubjectService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Resource
    SubjectService subjectService;

    @PostMapping("/add")
    public Result add(@RequestBody Subject subject) {  // @RequestBody 接收前端传来的 json参数
        subjectService.add(subject);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Subject subject) {  // @RequestBody 接收前端传来的 json参数
        subjectService.update(subject);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        subjectService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/subject/selectAll
    public Result selectAll(Subject subject) {
        List<Subject> subjectList = subjectService.selectAll(subject);
        return Result.success(subjectList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Subject subject) {
        PageInfo<Subject> pageInfo = subjectService.selectPage(pageNum, pageSize, subject);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
