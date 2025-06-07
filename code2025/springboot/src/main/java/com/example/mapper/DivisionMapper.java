package com.example.mapper;

import com.example.entity.Division;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DivisionMapper {

    List<Division> selectAll(Division division);

    void insert(Division division);

    void updateById(Division division);

    @Delete("delete from `division` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from division where id = #{id}")
    Division selectById(Integer id);
}
