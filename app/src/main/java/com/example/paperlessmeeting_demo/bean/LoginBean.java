package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;

/**
 * Created by 梅涛 on 2020/10/20.
 */

public class LoginBean implements Serializable {

        /**
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7Il9pZCI6IjVmNjc3NjAyYmQwYzFmMmQ5MDZkYWQ2MyIsInN0YXR1cyI6IkVOQUJMRSIsImNfaWQiOnsiX2lkIjoiNWY2NzcwOGZiZDBjMWYyZDkwNmRhZDVlIiwibmFtZSI6IuS4reWNj-aZuuiDvSJ9LCJkZXB0X2lkIjp7Il9pZCI6IjVmNjc3M2Y3YmQwYzFmMmQ5MDZkYWQ2MSIsIm5hbWUiOiLlvIDlj5Hpg6gifSwibmFtZSI6IuS4nOaWueS4jei0pSIsImFnZSI6MjUsInNleCI6IueUtyIsInBob25lIjoiMTIzNCIsImVtYWlsIjoiMTIzQDE2OC5jb20iLCJyb2xlIjoiTSIsImxvZ2luX2NvZGUiOiIxMjM0IiwiYXZhdGFyIjoiaHR0cDovLzE5Mi4xNjguMTEuMTM2OjMwMDAvdXBsb2FkL2RlZmF1bHQuanBnIiwiX192IjowfSwiZXhwIjoxNjE2NzIxMDE3LCJpYXQiOjE2MTY2NDA3NTh9.fRk5iNvLqeBIi8Bc42WqOvXKOkFvtw9zAeTaJ0tDfPg
         * user : {"_id":"5f677602bd0c1f2d906dad63","status":"ENABLE","c_id":{"id":"5f67708fbd0c1f2d906dad5e","name":"中协智能"},"dept_id":{"id":"5f6773f7bd0c1f2d906dad61","name":"开发部"},"name":"东方不败","age":"25","sex":"男","phone":"1234","email":"123@168.com","role":"M","login_code":"1234","avatar":"http://192.168.11.136:3000/upload/default.jpg"}
         */

        private String token;
        private UserBean user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * _id : 5f677602bd0c1f2d906dad63
             * status : ENABLE
             * c_id : {"id":"5f67708fbd0c1f2d906dad5e","name":"中协智能"}
             * dept_id : {"id":"5f6773f7bd0c1f2d906dad61","name":"开发部"}
             * name : 东方不败
             * age : 25
             * sex : 男
             * phone : 1234
             * email : 123@168.com
             * role : M
             * login_code : 1234
             * avatar : http://192.168.11.136:3000/upload/default.jpg
             */

            private String _id;
            private String status;
            private CIdBean c_id;
            private DeptIdBean dept_id;
            private String name;
            private String age;
            private String sex;
            private String phone;
            private String email;
            private String role;
            private String login_code;
            private String avatar;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public CIdBean getC_id() {
                return c_id;
            }

            public void setC_id(CIdBean c_id) {
                this.c_id = c_id;
            }

            public DeptIdBean getDept_id() {
                return dept_id;
            }

            public void setDept_id(DeptIdBean dept_id) {
                this.dept_id = dept_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getLogin_code() {
                return login_code;
            }

            public void setLogin_code(String login_code) {
                this.login_code = login_code;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public static class CIdBean {
                /**
                 * id : 5f67708fbd0c1f2d906dad5e
                 * name : 中协智能
                 */

                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class DeptIdBean {
                /**
                 * id : 5f6773f7bd0c1f2d906dad61
                 * name : 开发部
                 */

                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

}
