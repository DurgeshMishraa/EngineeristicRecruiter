package com.engineeristic.recruiter.chat;

import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

import android.util.Log;

/**
 * Created by GleasonK on 7/15/15.
 */
public class BasicCallback extends Callback {

    public BasicCallback(){

    }

    @Override
    public void successCallback(String channel, Object response) {
        Log.d("PUBNUB", "Success: " + response.toString());
    }

    @Override
    public void connectCallback(String channel, Object message) {
        Log.d("PUBNUB", "Connect: " + message.toString());
    }

    @Override
    public void errorCallback(String channel, PubnubError error) {
        Log.d("PUBNUB", "Error: " + error.toString());
    }
}
