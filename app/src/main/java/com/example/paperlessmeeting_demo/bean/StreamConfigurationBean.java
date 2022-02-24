package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2021/7/20.
 */

public class StreamConfigurationBean implements Serializable {

    /**
     * code : 1
     * data : {"auto":false,"type":"rtmp"}
     */

        /**
         * auto : false
         * type : rtmp
         */

        private boolean auto;
        private String type;

        public boolean isAuto() {
            return auto;
        }

        public void setAuto(boolean auto) {
            this.auto = auto;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

}
