package com.example.mapper;

import com.example.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper {
//Ctrl + r,全选表中的同一个单词
    List<Notice> selectAll(Notice notice);

    void insert(Notice notice);

    void updateById(Notice notice);

    @Delete("delete from `notice` where id = #{id}")
    void deleteById(Integer id);

}
