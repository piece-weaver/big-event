package com.cxx.bigevent.controller;

import com.cxx.bigevent.dto.ImageInfoResponse;
import com.cxx.bigevent.dto.ImageProcessRequest;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@Tag(name = "图片处理", description = "图片上传、处理和分析接口")
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    @Operation(summary = "上传图片", description = "上传图片并支持压缩处理")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "true") Boolean compress,
            @RequestParam(required = false, defaultValue = "85") Integer quality) throws IOException {

        ImageProcessRequest request = new ImageProcessRequest();
        request.setCompress(compress);
        request.setQuality(quality);

        String url = imageService.uploadWithProcess(file, request);
        return Result.success(url);
    }

    @GetMapping("/info")
    @Operation(summary = "获取图片信息", description = "获取图片的尺寸、格式等详细信息")
    public Result<ImageInfoResponse> info(@RequestParam String url) {
        ImageInfoResponse info = imageService.getImageInfo(url);
        return Result.success(info);
    }

    @PostMapping("/process")
    @Operation(summary = "处理图片", description = "对图片进行压缩、格式转换等处理")
    public Result<String> process(@RequestBody ImageProcessRequest request) {
        String processedUrl = imageService.processImage(request);
        return Result.success(processedUrl);
    }

    @PostMapping("/compress")
    @Operation(summary = "压缩图片", description = "对图片进行压缩处理")
    public Result<String> compress(@RequestBody ImageProcessRequest request) {
        request.setCompress(true);
        String processedUrl = imageService.processImage(request);
        return Result.success(processedUrl);
    }

    @PostMapping("/watermark")
    @Operation(summary = "添加水印", description = "为图片添加文字水印")
    public Result<String> watermark(@RequestBody ImageProcessRequest request) {
        String processedUrl = imageService.processImage(request);
        return Result.success(processedUrl);
    }

    @PostMapping("/ocr")
    @Operation(summary = "图片OCR", description = "从图片中提取文字内容")
    public Result<String> ocr(@RequestParam String url) {
        String text = imageService.extractText(url);
        return Result.success(text);
    }
}
