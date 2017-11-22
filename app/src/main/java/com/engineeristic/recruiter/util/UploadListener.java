package com.engineeristic.recruiter.util;

import org.json.JSONObject;

/**
 * Created by dell on 11/2/2016.
 */

public interface UploadListener {

    public void UploadSuccess(JSONObject jsonObject);
    public void UploadFails(String errorMessage);
}
