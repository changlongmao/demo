package com.example.demo.webmagic;

/**
 * @author cai
 */
public class ResultNews {
    private String title;
    private String text;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ResultNews{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
