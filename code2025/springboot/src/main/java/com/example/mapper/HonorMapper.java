package com.example.mapper;

import com.example.entity.Honor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HonorMapper {

    List<Honor> selectAll(Honor honor);

    void insert(Honor honor);

    void updateById(Honor honor);

    @Delete("delete from `honor` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from honor where id = #{id}")
    Honor selectById(Integer id);
}
