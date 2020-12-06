package com.example.demo.webmagic;

import java.util.List;

/**
 * @author cai
 */
public class TestNews {

    private List<TestNew> list;


    public List<TestNew> getList() {
        return list;
    }

    public void setList(List<TestNew> list) {
        this.list = list;
    }



    public class TestNew {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
