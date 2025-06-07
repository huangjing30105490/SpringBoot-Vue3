package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Books;
import com.example.entity.Subject;
import com.example.mapper.BooksMapper;
import com.example.mapper.SubjectMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Resource
    BooksMapper booksMapper;
    @Resource
    SubjectMapper subjectMapper;
    @Resource
    UserMapper userMapper;

    public void add(Books books) {
        Account currentUser = TokenUtils.getCurrentUser();
        books.setTeacherId(currentUser.getId());
        books.setTime(DateUtil.now());
        books.setStatus("待审核");
        booksMapper.insert(books);
    }

    public void update(Books books) {
        Account currentUser = TokenUtils.getCurrentUser();
        booksMapper.updateById(books);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        booksMapper.deleteById(id);
    }

    public List<Books> selectAll(Books books) {
        return booksMapper.selectAll(books);
    }

    public PageInfo<Books> selectPage(Integer pageNum, Integer pageSize, Books books) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("TEACHER".equals(currentUser.getRole())) {
            books.setTeacherId(currentUser.getId());
        }

        if ("USER".equals(currentUser.getRole())) {
            books.setTeacherId(currentUser.getId());
        }
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Books> list = booksMapper.selectAll(books);
        for(Books dbBooks : list){
            Integer subjectId = dbBooks.getSubjectId();
            Subject subject = subjectMapper.selectById(subjectId);
            if(ObjectUtil.isNotNull(subject)){
                dbBooks.setSubjectTitle(subject.getTitle());
            }
        }
        // 这个list里面存储了旅游攻略的原始数据（只有分类id，categoryId）
        /*for (Introduction dbIntroduction : list) {
            // 先拿到categoryId
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            // 通过categoryId从category表里通过主键查询出分类数据
            Category category = categoryMapper.selectById(categoryId);
            User user = userMapper.selectById(userId.toString());
            if (ObjectUtil.isNotEmpty(category)) {
                // 把分类的title赋值给categoryTitle
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if (ObjectUtil.isNotEmpty(user)) {
                dbIntroduction.setUserName(user.getName());
            }
        }*/
        return PageInfo.of(list);
    }

}
