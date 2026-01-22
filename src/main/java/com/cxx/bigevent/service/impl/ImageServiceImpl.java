package com.cxx.bigevent.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.cxx.bigevent.config.AliOssProperties;
import com.cxx.bigevent.dto.ImageInfoResponse;
import com.cxx.bigevent.dto.ImageProcessRequest;
import com.cxx.bigevent.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private AliOssProperties aliOssProperties;

    @Override
    public String uploadWithProcess(MultipartFile file, ImageProcessRequest request) throws IOException {
        OSS ossClient = new OSSClientBuilder().build(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret()
        );

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + extension;

            String folder = "images/";
            String fileUrl = folder + newFilename;

            InputStream inputStream = file.getInputStream();
            ossClient.putObject(aliOssProperties.getBucketName(), fileUrl, inputStream);

            String resultUrl = "https://" + aliOssProperties.getBucketName() + "." +
                    aliOssProperties.getEndpoint().replace("https://", "") + "/" + fileUrl;

            if (request != null && (request.getCompress() || request.getWatermark() != null)) {
                resultUrl = buildProcessedUrl(resultUrl, request);
            }

            log.info("图片上传成功：{}", resultUrl);
            return resultUrl;

        } catch (OSSException | ClientException e) {
            log.error("图片上传失败：{}", e.getMessage());
            throw new IOException("图片上传失败：" + e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public ImageInfoResponse getImageInfo(String url) {
        ImageInfoResponse info = new ImageInfoResponse();
        info.setUrl(url);

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            info.setSize(conn.getContentLengthLong());
            info.setFormat(conn.getContentType());

            conn.disconnect();
        } catch (Exception e) {
            log.warn("获取图片信息失败：{}", e.getMessage());
        }

        return info;
    }

    @Override
    public String processImage(ImageProcessRequest request) {
        return buildProcessedUrl(request.getUrl(), request);
    }

    @Override
    public String extractText(String imageUrl) {
        return "OCR功能需要集成阿里云OCR或百度OCR API，当前仅返回占位";
    }

    private String buildProcessedUrl(String url, ImageProcessRequest request) {
        StringBuilder params = new StringBuilder();

        if (request.getWidth() != null) {
            params.append(",w_").append(request.getWidth());
        }
        if (request.getHeight() != null) {
            params.append(",h_").append(request.getHeight());
        }
        if (request.getQuality() != null) {
            params.append(",q_").append(request.getQuality());
        }
        if (request.getFormat() != null) {
            params.append(",format_").append(request.getFormat());
        }
        if (Boolean.TRUE.equals(request.getWatermark()) && request.getWatermarkText() != null) {
            try {
                String encodedText = java.net.URLEncoder.encode(request.getWatermarkText(), "UTF-8");
                params.append(",watermark=1&text=").append(encodedText);
            } catch (Exception e) {
                log.warn("水印文本编码失败：{}", e.getMessage());
            }
        }

        if (params.length() > 0) {
            String separator = url.contains("?") ? "&" : "?";
            return url + separator + "x-oss-process=image" + params;
        }

        return url;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
