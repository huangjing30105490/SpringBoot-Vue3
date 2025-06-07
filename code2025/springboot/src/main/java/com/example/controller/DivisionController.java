package com.example.controller;

import com.example.common.Result;
import com.example.entity.Division;
import com.example.service.DivisionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/division")
public class DivisionController {

    @Resource
    DivisionService divisionService;

    @PostMapping("/add")
    public Result add(@RequestBody Division division) {  // @RequestBody 接收前端传来的 json参数
        divisionService.add(division);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Division division) {  // @RequestBody 接收前端传来的 json参数
        divisionService.update(division);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        divisionService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/division/selectAll
    public Result selectAll(Division division) {
        List<Division> divisionList = divisionService.selectAll(division);
        return Result.success(divisionList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Division division) {
        PageInfo<Division> pageInfo = divisionService.selectPage(pageNum, pageSize, division);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
