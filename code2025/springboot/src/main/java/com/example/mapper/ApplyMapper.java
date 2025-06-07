package com.example.mapper;

import com.example.entity.Apply;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface ApplyMapper {

    List<Apply> selectAll(Apply apply);

    void insert(Apply apply);

    void updateById(Apply apply);

    @Delete("delete from `apply` where id = #{id}")
    void deleteById(Integer id);

}
