package com.example.mapper;

import com.example.entity.Teacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {

    List<Teacher> selectAll(Teacher teacher);

    void insert(Teacher teacher);

    @Select("select * from `teacher` where username = #{username}")
    Teacher selectByUsername(String username);

    void updateById(Teacher teacher);

    @Delete("delete from `teacher` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `teacher` where id = #{id}")
    Teacher selectById(String id);

}
