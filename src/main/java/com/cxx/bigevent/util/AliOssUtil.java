package com.cxx.bigevent.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.cxx.bigevent.config.AliOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class AliOssUtil {

    @Autowired
    private AliOssProperties aliOssProperties;

    public String uploadFile(String objectName, InputStream in) throws Exception {

        OSS ossClient = new OSSClientBuilder().build(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret()
        );
        String url = "";

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    aliOssProperties.getBucketName(),
                    objectName,
                    in
            );

            PutObjectResult result = ossClient.putObject(putObjectRequest);
            url = "https://" + aliOssProperties.getBucketName() + "." +
                    aliOssProperties.getEndpoint().substring(aliOssProperties.getEndpoint().lastIndexOf("/") + 1) +
                    "/" + objectName;

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}
