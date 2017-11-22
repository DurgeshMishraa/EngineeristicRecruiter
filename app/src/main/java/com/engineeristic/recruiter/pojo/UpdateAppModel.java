package com.engineeristic.recruiter.pojo;



/**
 * Created by h1 on 11-Oct-17.
 */

public class UpdateAppModel {

    private String update_title;
    private String error;
    private String status;
    private String update_detail;
    private String update_app_msg;
    private String error_msg;
    private Version version;
    private String message;
    private String chat_log_url;
    private String update_app;
    private String notify;
    private String success;
    private String button_text;
    private String ChatEnable;

    public String getUpdate_title ()
    {
        return update_title;
    }

    public void setUpdate_title (String update_title)
    {
        this.update_title = update_title;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getUpdate_detail ()
    {
        return update_detail;
    }

    public void setUpdate_detail (String update_detail)
    {
        this.update_detail = update_detail;
    }

    public String getUpdate_app_msg ()
    {
        return update_app_msg;
    }

    public void setUpdate_app_msg (String update_app_msg)
    {
        this.update_app_msg = update_app_msg;
    }

    public String getError_msg ()
    {
        return error_msg;
    }

    public void setError_msg (String error_msg)
    {
        this.error_msg = error_msg;
    }

    public Version getVersion ()
    {
        return version;
    }

    public void setVersion (Version version)
    {
        this.version = version;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }


    public String getChatEnable ()
    {
        return ChatEnable;
    }

    public void setChatEnable (String ChatEnable)
    {
        this.ChatEnable = ChatEnable;
    }

    public String getChat_log_url ()
    {
        return chat_log_url;
    }

    public void setChat_log_url (String chat_log_url)
    {
        this.chat_log_url = chat_log_url;
    }

    public String getUpdate_app ()
    {
        return update_app;
    }

    public void setUpdate_app (String update_app)
    {
        this.update_app = update_app;
    }

    public String getNotify ()
    {
        return notify;
    }

    public void setNotify (String notify)
    {
        this.notify = notify;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public String getButton_text ()
    {
        return button_text;
    }

    public void setButton_text (String button_text)
    {
        this.button_text = button_text;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [update_title = "+update_title+", error = "+error+", status = "+status+", update_detail = "+update_detail+", update_app_msg = "+update_app_msg+", error_msg = "+error_msg+", version = "+version+", message = "+message+", ChatEnable = "+ChatEnable+", chat_log_url = "+chat_log_url+", update_app = "+update_app+", notify = "+notify+", success = "+success+", button_text = "+button_text+"]";
    }

}
