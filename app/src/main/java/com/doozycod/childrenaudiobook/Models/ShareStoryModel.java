package com.doozycod.childrenaudiobook.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShareStoryModel {

    @SerializedName("status")
    @Expose
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message message;

    public class Message {

        public String getSend_to() {
            return send_to;
        }

        public void setSend_to(String send_to) {
            this.send_to = send_to;
        }

        @SerializedName("to")
        @Expose
        private String send_to;

        public Notification getNotification() {
            return notification;
        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }

        public Notification notification;

        public class Notification {



            @SerializedName("body")
            @Expose
            private String body;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("icon")
            @Expose
            private String icon;

            public String getBody() {
                return body;
            }

            public String getTitle() {
                return title;
            }

            public String getIcon() {
                return icon;
            }

        }

    }

}
