package com.example.controller;

import com.example.common.Result;
import com.example.entity.Project;
import com.example.service.ProjectService;
import com.example.service.ProjectService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Resource
    ProjectService projectService;

    @PostMapping("/add")
    public Result add(@RequestBody Project project) {  // @RequestBody 接收前端传来的 json参数
        projectService.add(project);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Project project) {  // @RequestBody 接收前端传来的 json参数
        projectService.update(project);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        projectService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/project/selectAll
    public Result selectAll(Project project) {
        List<Project> projectList = projectService.selectAll(project);
        return Result.success(projectList);
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Project project) {
        PageInfo<Project> pageInfo = projectService.selectPage(pageNum, pageSize, project);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}
