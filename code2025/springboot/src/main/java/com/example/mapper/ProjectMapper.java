package com.example.mapper;

import com.example.entity.Project;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProjectMapper {

    List<Project> selectAll(Project project);

    void insert(Project project);

    void updateById(Project project);

    @Delete("delete from `project` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from project where id = #{id}")
    Project selectById(Integer id);

    // 用于按时间分组统计
    List<Map<String, Object>> countGroupByDate(@Param("start") Date start,
                                               @Param("end") Date end,
                                               @Param("pattern") String pattern);
}
