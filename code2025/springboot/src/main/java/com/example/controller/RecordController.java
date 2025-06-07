package com.example.controller;

import com.example.common.Result;
import com.example.entity.Record;
import com.example.service.RecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    RecordService recordService;

    @PostMapping("/add")
    public Result add(@RequestBody Record record) {  // @RequestBody 接收前端传来的 json参数
        recordService.add(record);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Record record) {  // @RequestBody 接收前端传来的 json参数
        recordService.update(record);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        recordService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/record/selectAll
    public Result selectAll(Record record) {
        List<Record> recordList = recordService.selectAll(record);
        return Result.success(recordList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Record record) {
        PageInfo<Record> pageInfo = recordService.selectPage(pageNum, pageSize, record);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
