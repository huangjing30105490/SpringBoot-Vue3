package com.example.mapper;

import com.example.entity.Record;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface RecordMapper {

    List<Record> selectAll(Record record);

    void insert(Record record);

    void updateById(Record record);

    @Delete("delete from `record` where id = #{id}")
    void deleteById(Integer id);

}
