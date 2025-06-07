package com.example.mapper;

import com.example.entity.Books;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BooksMapper {

    List<Books> selectAll(Books books);

    void insert(Books books);

    void updateById(Books books);

    @Delete("delete from `books` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from books where id = #{id}")
    Books selectById(Integer id);
}
