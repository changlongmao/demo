package com.example.demo.util;

import com.example.demo.entity.Base64DecodedMultipartFile;
import jodd.util.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class Base64Util {

    public static MultipartFile base64ToMultipart(String base64, String cardId, String photoType) {
        String[] baseStr = base64.split(",");

        byte[] b = Base64.decode(baseStr[1]);

        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }

        return new Base64DecodedMultipartFile(b, baseStr[0], cardId, photoType);
    }
}
