package com.cxx.bigevent.service;

import com.cxx.bigevent.dto.ImageInfoResponse;
import com.cxx.bigevent.dto.ImageProcessRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadWithProcess(MultipartFile file, ImageProcessRequest request) throws IOException;

    ImageInfoResponse getImageInfo(String url);

    String processImage(ImageProcessRequest request);

    String extractText(String imageUrl);
}
