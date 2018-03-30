package com.example.manzoorhussain.myoutbox.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GeeksEra on 3/30/2018.
 */

public class User implements Serializable {

    private List<Sheet1Object> Sheet1;

    public static User objectFromData(String str) {

        return new com.google.gson.Gson().fromJson(str, User.class);
    }

    public List<Sheet1Object> getSheet1() {
        return Sheet1;
    }

    public void setSheet1(List<Sheet1Object> Sheet1) {
        this.Sheet1 = Sheet1;
    }

    public static class Sheet1Object {
        /**
         * name : Ali
         * mobile_no : 3001234567
         */

        private String name;
        private long mobile_no;
        private String Mesasege = "";

        public String getMesasege() {
            return Mesasege;
        }

        public void setMesasege(String mesasege) {
            Mesasege = mesasege;
        }

        public static Sheet1Object objectFromData(String str) {

            return new com.google.gson.Gson().fromJson(str, Sheet1Object.class);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(long mobile_no) {
            this.mobile_no = mobile_no;
        }
    }
}
