package com.example.demo.entity;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 自定义的MultipartFile的实现类，主要用于base64上传文件，以下方法都可以根据实际项目自行实现
 *
 * @author zhuzhe
 * @date 2018/10/17 17:19
 * @email zhe.zhu1@outlook.com
 */
public class Base64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String header;
    private final String cardId;
    private final String photoType;

    public Base64DecodedMultipartFile(byte[] imgContent, String header, String cardId, String photoType) {
        this.imgContent = imgContent;
        this.cardId = cardId;
        this.photoType = photoType;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return cardId + "_" + photoType + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return cardId + "_" + photoType + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}
