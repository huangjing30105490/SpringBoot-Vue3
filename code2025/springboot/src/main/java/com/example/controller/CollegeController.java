package com.example.controller;

import com.example.common.Result;
import com.example.entity.College;
import com.example.service.CollegeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Resource
    CollegeService collegeService;

    @PostMapping("/add")
    public Result add(@RequestBody College college) {  // @RequestBody 接收前端传来的 json参数
        collegeService.add(college);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody College college) {  // @RequestBody 接收前端传来的 json参数
        collegeService.update(college);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        collegeService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/college/selectAll
    public Result selectAll(College college) {
        List<College> collegeList = collegeService.selectAll(college);
        return Result.success(collegeList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             College college) {
        PageInfo<College> pageInfo = collegeService.selectPage(pageNum, pageSize, college);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
