package com.example.demo.controller;

import cn.hutool.core.date.DateUtil;
import com.example.demo.util.Base64Util;
import com.example.demo.util.ImageCompressUtil;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/doUpload")
    public Map<String, Object> doUpload(@RequestParam("file") MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();

        if (file == null) {
            return new HashMap(0);
        }
        Map map = new HashMap(2);
        // 原文件名称
        String filename = file.getOriginalFilename();
        // 创建临时文件
        File tempFile = File.createTempFile("tem", null);
        file.transferTo(tempFile);
        tempFile.deleteOnExit();
        // 文件输入流
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] buffer = new byte[(int)tempFile.length()];
        inputStream.read(buffer);
        inputStream.close();
        // 转换为base64编码格式
        String base64 = Base64Utils.encodeToString(buffer);
        // 上面方法中获得的base64编码中，包含有换行符，统一全部替换掉
        base64 = base64.replaceAll("[\\s*\t\n\r]", "");
        // 返回值
        map.put("filename", filename);
        map.put("base64", base64);

        Long endTime = System.currentTimeMillis();
        System.out.println("编码base64共用时：" + (endTime - startTime) + "ms");
        return map;
    }

//    @PostMapping("/upload3")
//    public List<String> upload3(String base64) throws IOException {
//         //TODO BASE64 方式的 格式和名字需要自己控制（如 png 图片编码后前缀就会是 data:image/png;base64,）
//        MultipartFile file = Base64Util.base64ToMultipart(base64);
//        return this.upload(file);
//    }

    public List<String> upload(MultipartFile file){
        List<String> fileIds = new ArrayList<String>();
        Date date = new Date();
            try {
                String originalFilename = file.getOriginalFilename();
                String originalPath = "C://upload"  + File.separator ;
                String fileId = "123";
                ImageCompressUtil.writeFile(originalPath, fileId, file.getInputStream());
                fileIds.add(fileId);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        return fileIds;
    }

    /*@RequestMapping(value = "/download/{fileId}",method= RequestMethod.GET)
    public void download(@PathVariable("fileId") String fileId, HttpServletResponse response) throws IOException{
        downLoadFile(fileId, response);
    }

    private void downLoadFile(String fileId, HttpServletResponse response)  throws IOException {
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + URLEncoder.encode("123.jpg", "utf-8") + "\";filename*=utf-8''" + URLEncoder.encode("123.jpg", "utf-8"));

            response.setContentType("image/jpeg");
            response.setCharacterEncoding("UTF-8");
            response.flushBuffer();
            ServletOutputStream os = response.getOutputStream();
            InputStream is = null;
            try{
                is = new FileInputStream("C://upload" + File.separator + fileId);
                byte[] buffer = new byte[512 * 1024];
                int length;
                while ((length = is.read(buffer)) != -1){
                    os.write(buffer, 0, length);
                    os.flush();
                }
            }finally{
                if(is!=null)
                    is.close();
                if(os!=null)
                    os.close();
            }
            return;
    }*/
}
