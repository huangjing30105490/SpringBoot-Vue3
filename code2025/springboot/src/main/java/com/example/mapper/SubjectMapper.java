package com.example.mapper;

import com.example.entity.Subject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubjectMapper {

    List<Subject> selectAll(Subject subject);

    void insert(Subject subject);

    void updateById(Subject subject);

    @Delete("delete from `subject` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from subject where id = #{id}")
    Subject selectById(Integer id);
}
