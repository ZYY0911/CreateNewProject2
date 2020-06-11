package com.example.createnewproject2.bean;

import java.util.List;

/**
 * Create by 张瀛煜 on 2020-06-08 ：）
 */
public class Images {

    /**
     * ROWS_DETAIL : [{"id":1,"image":"http://112.224.2.83:8080/MyCreate/images/icon_单行框组.png"},{"id":2,"image":"http://112.224.2.83:8080/MyCreate/images/icon_日期选择.png"},{"id":3,"image":"http://112.224.2.83:8080/MyCreate/images/icon_时间选择.png"},{"id":4,"image":"http://112.224.2.83:8080/MyCreate/images/icon_文字.png"}]
     * RESULT : S
     */

    private String RESULT;
    private List<ROWSDETAILBean> ROWS_DETAIL;

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<ROWSDETAILBean> getROWS_DETAIL() {
        return ROWS_DETAIL;
    }

    public void setROWS_DETAIL(List<ROWSDETAILBean> ROWS_DETAIL) {
        this.ROWS_DETAIL = ROWS_DETAIL;
    }

    public static class ROWSDETAILBean {
        /**
         * id : 1
         * image : http://112.224.2.83:8080/MyCreate/images/icon_单行框组.png
         */

        private int id;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
