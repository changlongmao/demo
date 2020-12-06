package com.example.demo.webmagic;

/**
 * @author cai
 */
public class TestUser {
    private String author;
    // 提交时间
    private String time;
    // 内容
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "author='" + author + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void setContent(String content) {
        this.content = content;
    }



}
