package com.engineeristic.recruiter.pojo;

/**
 * Created by h1 on 11-Oct-17.
 */

public class UpdateAppData {

    public static String title;
    public static String msg;
    public static String status;
    public static String app_status;
    public static String chat_enable;
    public static String btn_text;

    public UpdateAppData(){}

    public static String getBtn_text() {
        return btn_text;
    }

    public static void setBtn_text(String btn_text) {
        UpdateAppData.btn_text = btn_text;
    }
    public static String getChat_enable() {
        return chat_enable;
    }

    public static void setChat_enable(String chat_enable) {
        UpdateAppData.chat_enable = chat_enable;
    }
    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        UpdateAppData.title = title;
    }

    public static String getMsg() {
        return msg;
    }

    public static void setMsg(String msg) {
        UpdateAppData.msg = msg;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        UpdateAppData.status = status;
    }

    public static String getApp_status() {
        return app_status;
    }

    public static void setApp_status(String app_status) {
        UpdateAppData.app_status = app_status;
    }

}
