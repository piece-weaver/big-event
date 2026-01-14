package com.cxx.bigevent.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {

    @NotNull(message = "分类ID不能为空")
    private Integer id;

    @NotEmpty(message = "分类名称不能为空")
    private String categoryName;

    @NotEmpty(message = "分类别名不能为空")
    private String categoryAlias;

    @JsonIgnore
    private Integer createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public interface Add {
    }

    public interface Update {
    }
}
