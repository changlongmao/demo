package com.example.demo.entity;


public class Constant {

    /**
     * 用户信息注入标识
     */
    public static final String AUTH_REQUEST = "userInfo";

    /**
     * Jwt头
     */
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";


    public static final String SUPER_ADMIN = "1";

    public static final String SUPER_ADMIN_ORG = "01";

    public static final String DEFAULT_PW = "123456";


    public static final int EXPIRE = 3600 * 24;


    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";


    public final static String SMS_CONFIG_KEY = "SMS_CONFIG_KEY";


    public static final String SESSION = "SESSION:";


    public static final String SYS_CACHE = "SYS_CACHE:";


    public static final String MTM_CACHE = "MTM_CACHE:";

    public static final String STR_ZERO = "0";
    public static final String STR_ONE = "1";
    public static final String STR_TWO = "2";
    public static final String STR_THREE = "3";
    public static final String STR_FOUR = "4";

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;


    public static final int UNSUBSCRIBE = 0;

    public static final int SUBSCRIBE = 1;

    public static final String BLANK = "";

    public static final String SLASH = "/";

    public static final String COMMA = ",";

    public static final String DOT = ".";

    public static final String COLON = ":";

    public static final String UNDERSCORE = "_";

    public static final String LINE_BREAK = "\\r\\n";

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    public static final String BPMN20 = ".bpmn20.xml";

    public static final String IMAGE = "image";

    public static final String XML = "xml";
    public static final String PNG = "png";
    public static final String BAR = "bar";
    public static final String ZIP = "zip";
    public static final String BPMN = "bpmn";


    public static final String CATEGORY_ITEMS = "categoryItems";
    public static final String SEARCH_KEY = "searchKey";
    public static final String SORT = "sort";

    public static final int SYS_SEND = 0;

    public static final int one = 1;


    public enum MenuType {

        CATALOG(0),

        MENU(1),

        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public enum ScheduleStatus {

        NORMAL(0),

        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public enum CloudService {

        QINIU(1),

        ALIYUN(2),

        QCLOUD(3),

        DISCK(4),

        FAST_DFS(5);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
