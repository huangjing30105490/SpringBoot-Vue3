package com.example.mapper;

import com.example.entity.Teach;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeachMapper {

    List<Teach> selectAll(Teach teach);

    void insert(Teach teach);

    void updateById(Teach teach);

    @Delete("delete from `teach` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `teach` where id = #{id}")
    Teach selectById(Integer id);

}
