package com.example.demo.util;

import com.example.demo.entity.Base64DecodedMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;

public class Base64Util {

    public static MultipartFile base64ToMultipart(String base64, String cardId, String photoType) {
        try {
            String[] baseStr = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStr[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new Base64DecodedMultipartFile(b, baseStr[0], cardId, photoType);
        } catch (IOException e) {
            return null;
        }
    }
}
