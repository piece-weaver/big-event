package com.cxx.bigevent.controller;

import com.cxx.bigevent.exception.ErrorCode;
import com.cxx.bigevent.pojo.Result;
import com.cxx.bigevent.util.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "文件上传", description = "文件上传管理接口")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private AliOssUtil aliOssUtil;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传图片文件到阿里云OSS")
    public Result<String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("文件上传失败：文件为空");
            return Result.error(ErrorCode.FILE_UPLOAD_FAILED);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("文件上传失败：文件大小超过限制，当前大小：{} bytes", file.getSize());
            return Result.error(ErrorCode.FILE_UPLOAD_FAILED.getCode(), "文件大小不能超过10MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            log.warn("文件上传失败：文件名为空");
            return Result.error(ErrorCode.FILE_UPLOAD_FAILED);
        }

        String extension;
        try {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        } catch (Exception e) {
            log.warn("文件上传失败：文件名格式错误，文件名：{}", originalFilename);
            return Result.error(ErrorCode.FILE_UPLOAD_FAILED);
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            log.warn("文件上传失败：不支持的文件格式，扩展名：{}", extension);
            return Result.error(ErrorCode.FILE_FORMAT_NOT_SUPPORTED);
        }

        String fileName = UUID.randomUUID().toString() + extension;

        try {
            String url = aliOssUtil.uploadFile(fileName, file.getInputStream());
            log.info("文件上传成功：{}，URL：{}", fileName, url);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败：IO异常，文件名：{}", fileName, e);
            return Result.error(ErrorCode.FILE_UPLOAD_FAILED);
        } catch (Exception e) {
            log.error("文件上传失败：未知异常，文件名：{}", fileName, e);
            return Result.error(ErrorCode.FILE_UPLOAD_FAILED.getCode(), "文件上传失败: " + e.getMessage());
        }
    }
}
