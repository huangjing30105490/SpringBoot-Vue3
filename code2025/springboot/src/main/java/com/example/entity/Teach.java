package com.example.entity;

/**
 * 图书信息
 */
public class Teach {

    private Integer id;
    private String img;
    private String name;
    private String price;
    private Integer num;
    private Integer teacherId;
    private Integer divisionId;

    /**
     * 不是数据库里面的字段，而是用来存储数据库里category_id关联category表查询出来的title字段
     */
    private String divisionTitle;
    /**
     * 不是数据库里面的字段，而是用来存储数据库里user_id关联user表查询出来的name字段
     */

    private String teacherName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionTitle() {
        return divisionTitle;
    }

    public void setDivisionTitle(String divisionTitle) {
        this.divisionTitle = divisionTitle;
    }


}
