package com.chenbaiyu.web.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.qiniu.common.Zone;


import java.util.UUID;

@Component
public class FileUploadUtil {
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.rtValue}")
    private String rtValue;

    public String upload(MultipartFile multipartFile) throws Exception {
        String img = "";
        try{
            String originalFilename = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-","").toUpperCase();
            String fileName= uuid+"_"+originalFilename;
            Configuration cfg = new Configuration(Region.region2());
            //...其他参数参考类注释
            //上传管理器
            UploadManager uploadManager = new UploadManager(cfg);
            //身份认证
            Auth auth = Auth.create(accessKey, secretKey);
            //指定覆盖上传
            String upToken = auth.uploadToken(bucket,fileName);
            //上传
            Response response = uploadManager.put(multipartFile.getBytes(), fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            img = rtValue+"/"+fileName;
        } catch (QiniuException ex) {
            System.err.println(ex.getMessage());
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
            }
        }
        return img;
    }
}
