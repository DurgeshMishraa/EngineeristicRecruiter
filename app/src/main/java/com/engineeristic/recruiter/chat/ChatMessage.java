package com.engineeristic.recruiter.chat;

/**
 * Created by GleasonK on 7/11/15.
 *
 * ChatMessage is used to hold information that is transmitted using PubNub.
 * A message in this app has a username, message, and timestamp.
 */
public class ChatMessage {
    private String username;
    private String message;
    private long timeStamp;
    private String usr;
    private String userTextColor;
    private String date="";
    private String channelName;

    public ChatMessage(String username, String message, long timeStamp){
        this.username  = username;
        this.message   = message;
        this.timeStamp = timeStamp;
    }

    public String getChannelName() {
  		return channelName;
  	}


  	public void setChannelName(String channelName) {
  		this.channelName = channelName;
  	}
    public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getUserTextColor() {
  		return userTextColor;
  	}


  	public void setUserTextColor(String userTextColor) {
  		this.userTextColor = userTextColor;
  	}

    public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
