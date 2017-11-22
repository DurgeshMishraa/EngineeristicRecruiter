package com.engineeristic.recruiter.model;

/**
 * Created by dell on 9/22/2016.
 */
public class JobRefreshModel {

    private String status;
    private String refresh_status;
    private String error_msg;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefresh_status() {
        return refresh_status;
    }

    public void setRefresh_status(String refresh_status) {
        this.refresh_status = refresh_status;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }


}
