package com.example.mapper;

import com.example.entity.College;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollegeMapper {

    List<College> selectAll(College college);

    void insert(College college);

    void updateById(College college);

    @Delete("delete from `college` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from college where id = #{id}")
    College selectById(Integer id);
}
