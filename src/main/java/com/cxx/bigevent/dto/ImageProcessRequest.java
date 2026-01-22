package com.cxx.bigevent.dto;

import lombok.Data;

@Data
public class ImageProcessRequest {
    private String url;
    private Boolean compress = true;
    private Integer quality = 85;
    private Integer width;
    private Integer height;
    private String format;
    private Boolean watermark = false;
    private String watermarkText;
}
