package com.example.mapper;

import com.example.entity.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PageMapper {

    List<Page> selectAll(Page page);

    void insert(Page page);

    void updateById(Page page);

    @Delete("delete from `page` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from page where id = #{id}")
    Page selectById(Integer id);
}
