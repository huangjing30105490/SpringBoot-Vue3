package com.example.controller;

import com.example.common.Result;
import com.example.entity.Books;
import com.example.service.BooksService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Resource
    BooksService booksService;

    @PostMapping("/add")
    public Result add(@RequestBody Books books) {  // @RequestBody 接收前端传来的 json参数
        booksService.add(books);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Books books) {  // @RequestBody 接收前端传来的 json参数
        booksService.update(books);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        booksService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/books/selectAll
    public Result selectAll(Books books) {
        List<Books> booksList = booksService.selectAll(books);
        return Result.success(booksList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Books books) {
        PageInfo<Books> pageInfo = booksService.selectPage(pageNum, pageSize, books);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
