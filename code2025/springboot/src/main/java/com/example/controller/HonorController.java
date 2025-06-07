package com.example.controller;

import com.example.common.Result;
import com.example.entity.Honor;
import com.example.service.HonorService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/honor")
public class HonorController {

    @Resource
    HonorService honorService;

    @PostMapping("/add")
    public Result add(@RequestBody Honor honor) {  // @RequestBody 接收前端传来的 json参数
        honorService.add(honor);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Honor honor) {  // @RequestBody 接收前端传来的 json参数
        honorService.update(honor);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        honorService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/honor/selectAll
    public Result selectAll(Honor honor) {
        List<Honor> honorList = honorService.selectAll(honor);
        return Result.success(honorList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Honor honor) {
        PageInfo<Honor> pageInfo = honorService.selectPage(pageNum, pageSize, honor);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
