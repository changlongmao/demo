package com.example.demo.testMain;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * string内存储使用Unicode编码，正编码为\u6b63，即27491，转换为utf-8编码为，e6ada3，
 * @author ChangLF 2022-03-12
 */
public class TestStringUnicode {

    public static final String EMPTY = "";
    public static final String HEX_PREFIX = "0x";
    public static final String HEX_STRING =  "0123456789abcdef";
    /**
     * 该数组的下标值，正好等于该位置的二进制字符串值
     */
    public static final String[] BINARY_ARRAY =
            {"0000","0001","0010","0011",
                    "0100","0101","0110","0111",
                    "1000","1001","1010","1011",
                    "1100","1101","1110","1111"};

    /**
     * 不同版本Java，都只支持Unicode字符集的一个特定版本。
     * 以下测试代码运行在JDK=1.8的环境中。
     *
     * 以下Unicode编码字符并非文字，详细信息请根据编码值，查看Unicode码表。
     * https://www.unicode.org/Public/UCD/latest/charts/CodeCharts.pdf
     *
     * Unicode编码"262F"对应字符为：太极图案 ☯ ️
     * Unicode编码"262FF"对应文字为：上面一个横“目”，下面一个“曹”字（java8目前不支持，我的环境中无法显示，所以只能描述）
     *
     * 输出结果为：
     * ☯ ️
     * ☯F -->并没有输出 \u262FF 对应的文字，只是输出了前4位（两个字节2626）的内容在Unicode码表中的对应符号☯，原样输出最后一个F️
     *
     * 得出结论：Java8并不支持2字节以上的Unicode字符。{@link Character#BYTES} 为2，也正好说明了该事实。
     *
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        //Unicode = 262F表示太极符号：☯
        System.out.println("\u262F");
        //Unicode = 262FF表示上面一个横“目”，下面一个“曹”字.实际输出 ☯F
        System.out.println("\u262F");
        String value = "严";
        //输出 6c49
        System.out.println(unicodeToString(value));
        //输出”汉“字的Unicode编码为：6c49
        System.out.println(value+"字的Unicode编码为："+stringToUnicode(value,false));
        //输出”汉“字的Unicode编码为（带前缀）：\u6c49
        System.out.println(value+"字的Unicode编码为（带前缀）："+stringToUnicode(value));
        //输出”汉“字的UTF-8编码为：e6b189
        System.out.println(value+"字的UTF-8编码为："+stringToUtf8HexCode(value));
        System.out.println(new String("中".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        int[] arr = new int[10];
        long l = 1000_00_000L;
        System.out.println(0x50);
        System.out.println(Arrays.toString("a".getBytes()));
        System.out.println(Arrays.toString("严".getBytes()));
        System.out.println(Arrays.toString("严".getBytes(StandardCharsets.UTF_8)));
        System.out.println((byte)(56 + 127));
        System.out.println((byte)(Byte.MAX_VALUE + 1));
        System.out.println((byte) (128+256+512));
        System.out.println('\u9fa5');
    }

    /**
     * unicode 转字符串
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);// 转换出每一个代码点
            string.append((char) data);// 将一个int型强制转化成字符char，并追加
        }
        return string.toString();
    }

    /**
     * 输出给定字符串的UTF-8编码
     * @param value
     * @return
     */
    public static String stringToUtf8HexCode(String value){
        try {
            if(value != null) {
                byte[] utf8Bytes = value.getBytes("UTF-8");
                return toHexString(utf8Bytes);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *功能描述：将字节数组转化成对应的十六进制字符串
     * @param array
     * @return
     */
    public static String toHexString(byte[] array) {
        StringBuilder sb = null;
        if(array != null) {
            for(byte b : array) {
                sb = toHexString(sb, b);
            }
        }
        return sb == null ? EMPTY : sb.toString();
    }

    /**
     *
     * 相比{@link Integer#toBinaryString(int)}不会转换出多余字节。
     * @param sb : 提供StringBuilder参数，有助于在循环体中调用该方法的执行效率。
     * @param b
     * @return
     */
    public static StringBuilder toHexString(StringBuilder sb, byte b) {
        if(sb == null) {
            sb = new StringBuilder();
        }
        //处理正负数的逻辑不一样，负数的值需要先将补码还原成原码
        //负数补码还原，其换算规则为： 补码-->减1-->取反 = 原码数值

        //处理高四位，高四位10进制值如下，可以用做 binaryArray的下标
        byte index = (byte) ((b & 0xF0) >> 4);
        sb.append(HEX_STRING.charAt(index));
        //处理低四位，低四位10进制值如下，可以用做 binaryArray的下标
        index = (byte) (b & 0x0F);
        sb.append(HEX_STRING.charAt(index));

        return sb;
    }

    /**
     * 返回给定字符串的Unicode编码
     * 中文Unicode编码参考：http://www.chi2ko.com/tool/CJK.htm
     * @param value
     * @return
     */
    public static String stringToUnicode(String value) {
        return stringToUnicode(value,true);
    }

    /**
     * 返回给定字符串的Unicode编码，是否添加前缀 "\\u" 由于addPrefix决定
     * @param value
     * @param addPrefix
     * @return
     */
    public static String stringToUnicode(String value,boolean addPrefix) {
        StringBuffer sb = new StringBuffer();
        char[] charArray = value.toCharArray();
        String unicode = null;
        for (int i=0;i<charArray.length;i++) {
            unicode = Integer.toHexString(charArray[i]);
            if (unicode.length() <= 2) {
                unicode = "00" + unicode;
            }
            if(addPrefix) {
                sb.append("\\u");
            }
            sb.append(unicode);
        }
        return sb.toString();
    }
}
