package com.example.controller;

import com.example.common.Result;
import com.example.entity.Page;
import com.example.service.PageService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/page")
public class PageController {

    @Resource
    PageService pageService;

    @PostMapping("/add")
    public Result add(@RequestBody Page page) {  // @RequestBody 接收前端传来的 json参数
        pageService.add(page);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Page page) {  // @RequestBody 接收前端传来的 json参数
        pageService.update(page);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        pageService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/page/selectAll
    public Result selectAll(Page page) {
        List<Page> pageList = pageService.selectAll(page);
        return Result.success(pageList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Page page) {
        PageInfo<Page> pageInfo = pageService.selectPage(pageNum, pageSize, page);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
