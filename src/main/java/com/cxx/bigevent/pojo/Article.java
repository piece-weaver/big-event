package com.cxx.bigevent.pojo;


import com.cxx.bigevent.annotation.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Article {

    private Integer id;

    @NotEmpty(message = "文章标题不能为空")
    @Pattern(regexp = "^.{1,100}$", message = "文章标题必须是1-100位字符")
    private String title;

    @NotEmpty(message = "文章内容不能为空")
    private String content;

    @URL(message = "封面图片URL格式不正确")
    private String coverImg;

    @State(message = "文章状态只能是已发布或草稿(0/1)")
    private Integer state;

    @NotNull(message = "文章分类ID不能为空")
    private Integer categoryId;

    @JsonIgnore
    private Integer createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
