package com.example.controller;

import com.example.common.Result;
import com.example.entity.Teach;
import com.example.service.TeachService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teach")
public class TeachController {

    @Resource
    TeachService teachService;

    @PostMapping("/add")
    public Result add(@RequestBody Teach teach) {  // @RequestBody 接收前端传来的 json参数
        teachService.add(teach);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Teach teach) {  // @RequestBody 接收前端传来的 json参数
        teachService.update(teach);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        teachService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/teach/selectAll
    public Result selectAll(Teach teach) {
        List<Teach> teachList = teachService.selectAll(teach);
        return Result.success(teachList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Teach teach) {
        PageInfo<Teach> pageInfo = teachService.selectPage(pageNum, pageSize, teach);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
