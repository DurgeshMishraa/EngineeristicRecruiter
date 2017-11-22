package com.engineeristic.recruiter.chat;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public interface ChatApiCallback {	

	void onPostResponse(String result, int apiCall);
	void herenNowCallback(Set<String> onlineNow);
	void wherenNowCallback();
	void subscribeCallback(ChatMessage chatMsg);
	void loadhistorymessageCallback(List<ChatMessage> chatMsgs, boolean count);
	void presenceSubscribeCallback(JSONObject json);
	void historyCallback(List<ChatMessage> chatMsgs, boolean count);
}
