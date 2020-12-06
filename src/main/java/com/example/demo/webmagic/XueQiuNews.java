package com.example.demo.webmagic;

import java.util.List;

/**
 * @author cai
 */
public class XueQiuNews {

    private List<StatusesBean> statuses;

    public List<StatusesBean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusesBean> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return "XueQiuNews{" +
                "statuses=" + statuses +
                '}';
    }

    public  class StatusesBean {
        private String title;
        private long created_at;
        private String description;
        private String text;
        private String source;

        public RetweetedStatus retweeted_status;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public RetweetedStatus getRetweeted_status() {
            return retweeted_status;
        }

        public void setRetweeted_status(RetweetedStatus retweeted_status) {
            this.retweeted_status = retweeted_status;
        }

        @Override
        public String toString() {
            return "StatusesBean{" +
                    "title='" + title + '\'' +
                    ", created_at=" + created_at +
                    ", description='" + description + '\'' +
                    ", text='" + text + '\'' +
                    ", source='" + source + '\'' +
                    '}';
        }
    }


    public  class RetweetedStatus{
        private String title;
        private long created_at;
        private String description;
        private String text;
        private String source;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
