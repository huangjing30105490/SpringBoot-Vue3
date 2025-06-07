package com.example.controller;

import com.example.common.Result;
import com.example.entity.Book;
import com.example.service.BookService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    BookService bookService;

    @PostMapping("/add")
    public Result add(@RequestBody Book book) {  // @RequestBody 接收前端传来的 json参数
        bookService.add(book);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Book book) {  // @RequestBody 接收前端传来的 json参数
        bookService.update(book);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        bookService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/book/selectAll
    public Result selectAll(Book book) {
        List<Book> bookList = bookService.selectAll(book);
        return Result.success(bookList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Book book) {
        PageInfo<Book> pageInfo = bookService.selectPage(pageNum, pageSize, book);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
