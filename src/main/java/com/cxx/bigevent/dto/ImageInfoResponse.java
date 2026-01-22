package com.cxx.bigevent.dto;

import lombok.Data;

@Data
public class ImageInfoResponse {
    private String url;
    private Long size;
    private Integer width;
    private Integer height;
    private String format;
    private String md5;
}
