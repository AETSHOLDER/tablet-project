package com.example.paperlessmeeting_demo.bean;

import com.example.paperlessmeeting_demo.tool.Constants;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.UserUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoteListBean implements Serializable {

    private ArrayList<VoteBean> data;

    public ArrayList<VoteListBean.VoteBean> getData() {
        return data;
    }

    public void setData(ArrayList<VoteListBean.VoteBean> data) {
        this.data = data;
    }

    public static class VoteBean {

        /**
         * options : ["同意","不同意"]
         * user_list : [{"choose":[],"status":"APPLY","meeting_vote_id":"5f968df1dfa33a25b8f98261","user_id":"5f677602bd0c1f2d906dad63","user_name":"东方不败","user_avtar":"","vote_time":""},{"choose":[],"status":"APPLY","meeting_vote_id":"5f968df1dfa33a25b8f98261","user_id":"5f67767ebd0c1f2d906dad66","user_name":"江小鱼","user_avtar":"","vote_time":""}]
         * _id : 5f968df1dfa33a25b8f98261
         * status : APPLY
         * c_id : 5f67708fbd0c1f2d906dad5e
         * meeting_record_id : 5f968cfcdfa33a25b8f98258
         * type : 0
         * topic : 开发部每日10分钟晨会
         * end_time : Wed Sep 23 2020 19:00:00 GMT+0800 (GMT+08:00)
         * anonymity : 0
         * from : {"_id":"5f677602bd0c1f2d906dad63","name":"东方不败","role":"M","avatar":"upload/default.jpg","phone":"1234","dept_id":"5f6773f7bd0c1f2d906dad61"}
         * create_time : Mon Oct 26 2020 16:50:00 GMT+0800 (GMT+08:00)
         */

        private String _id;
        private String status;
        private String c_id;
        private String meeting_record_id;
        private String type;
        private String topic;
        private String end_time;
        private String anonymity;
        private FromBean from;
        private String create_time;
        private List<String> options;
        private List<UserListBean> user_list;
        private String flag;
        private   List<TemporBean> temporBeanList;

        public List<TemporBean> getTemporBeanList() {
            return temporBeanList;
        }

        public void setTemporBeanList(List<TemporBean> temporBeanList) {
            this.temporBeanList = temporBeanList;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        //  手动新增加,判断我当前的投票状态，用这个来判断，不要用上面的status
        private int mvoteStatus;

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
            boolean isvote = false;
            this.status = status;
            if(user_list.size()<1){
                mvoteStatus = Constants.VoteStatusEnum.hasStartUnVote;
            }else {

                for (UserListBean bean : user_list) {
                    if (UserUtil.isTempMeeting) {
                        if (bean.user_id.equals(FLUtil.getMacAddress()) && bean.status.equals("FINISH")) { //  自己已投票
                            isvote = true;
                        }
                    } else {
                        if (bean.user_id.equals(UserUtil.user_id) && bean.status.equals("FINISH")) { //  自己已投票
                            isvote = true;
                        }
                    }

                }
            }
            if(status.equals("ENABLE")){
                if(isvote){
                    mvoteStatus = Constants.VoteStatusEnum.hasVotedNoResult;  //  已投票未结束
                }else {
                    mvoteStatus = Constants.VoteStatusEnum.hasStartUnVote;     //  未投票
                }
            }else {
                mvoteStatus = Constants.VoteStatusEnum.hasFinshed;  //  已结束
            }

        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getMeeting_record_id() {
            return meeting_record_id;
        }

        public void setMeeting_record_id(String meeting_record_id) {
            this.meeting_record_id = meeting_record_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getAnonymity() {
            return anonymity;
        }

        public void setAnonymity(String anonymity) {
            this.anonymity = anonymity;
        }

        public FromBean getFrom() {
            return from;
        }

        public void setFrom(FromBean from) {
            this.from = from;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public int getMvoteStatus() {
            return mvoteStatus;
        }

        public void setMvoteStatus(int mvoteStatus) {
            this.mvoteStatus = mvoteStatus;
        }

        public static class FromBean {
            /**
             * _id : 5f677602bd0c1f2d906dad63
             * name : 东方不败
             * role : M
             * avatar : upload/default.jpg
             * phone : 1234
             * dept_id : 5f6773f7bd0c1f2d906dad61
             */

            private String _id;
            private String name;
            private String role;
            private String avatar;
            private String phone;
            private String dept_id;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDept_id() {
                return dept_id;
            }

            public void setDept_id(String dept_id) {
                this.dept_id = dept_id;
            }
        }
        //临时存放选项状态及选项
        public static class TemporBean {

            private String content;
            private boolean isChecked;
            private String userName;
            private String choosWhich;
            private String  orderNumb;
            private String  fileName;
           private String votePath;

            public String getVotePath() {
                return votePath;
            }

            public void setVotePath(String votePath) {
                this.votePath = votePath;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getOrderNumb() {
                return orderNumb;
            }

            public void setOrderNumb(String orderNumb) {
                this.orderNumb = orderNumb;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getChoosWhich() {
                return choosWhich;
            }

            public void setChoosWhich(String choosWhich) {
                this.choosWhich = choosWhich;
            }


        }
        public static class UserListBean {
            /**
             * choose : []
             * status : APPLY
             * meeting_vote_id : 5f968df1dfa33a25b8f98261
             * user_id : 5f677602bd0c1f2d906dad63
             * user_name : 东方不败
             * user_avtar :
             * vote_time :
             */

            private String status;
            private String meeting_vote_id;
            private String user_id;
            private String user_name;
            private String user_avtar;
            private String vote_time;
            private List<String> choose;
            private  List<ChoseBean> choseBeanList;
            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMeeting_vote_id() {
                return meeting_vote_id;
            }

            public void setMeeting_vote_id(String meeting_vote_id) {
                this.meeting_vote_id = meeting_vote_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_avtar() {
                return user_avtar;
            }

            public void setUser_avtar(String user_avtar) {
                this.user_avtar = user_avtar;
            }

            public String getVote_time() {
                return vote_time;
            }

            public void setVote_time(String vote_time) {
                this.vote_time = vote_time;
            }

            public List<String> getChoose() {
                return choose;
            }

            public void setChoose(List<String> choose) {
                this.choose = choose;
            }


            public List<ChoseBean> getChoseBeanList() {
                return choseBeanList;
            }

            public void setChoseBeanList(List<ChoseBean> choseBeanList) {
                this.choseBeanList = choseBeanList;
            }
            public static class ChoseBean{
                private String content;
                private boolean isChecked;
                private String userName;
                private String choosWhich;
                private String  orderNumb;
               private String votePath;

                public String getVotePath() {
                    return votePath;
                }

                public void setVotePath(String votePath) {
                    this.votePath = votePath;
                }

                public String getOrderNumb() {
                    return orderNumb;
                }

                public void setOrderNumb(String orderNumb) {
                    this.orderNumb = orderNumb;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public boolean isChecked() {
                    return isChecked;
                }

                public void setChecked(boolean checked) {
                    isChecked = checked;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getChoosWhich() {
                    return choosWhich;
                }

                public void setChoosWhich(String choosWhich) {
                    this.choosWhich = choosWhich;
                }
            }
        }
    }
}


