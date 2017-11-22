package com.engineeristic.recruiter.chat;

public class MYChatListBO {

	private String id;
	private String channelname;
	private String name;
	private String email;
	private String current_designation;
	private String current_organization;
	private String seekerimage_url;
	private String notify;
	private String reciver;
	
	
	public String getSeekerimage_url() {
		return seekerimage_url;
	}
	public void setSeekerimage_url(String seekerimage_url) {
		this.seekerimage_url = seekerimage_url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCurrent_designation() {
		return current_designation;
	}
	public void setCurrent_designation(String current_designation) {
		this.current_designation = current_designation;
	}
	public String getCurrent_organization() {
		return current_organization;
	}
	public void setCurrent_organization(String current_organization) {
		this.current_organization = current_organization;
	}
	
	public String getNotify() {
		return notify;
	}
	public void setNotify(String notify) {
		this.notify = notify;
	}
	public String getReciver() {
		return reciver;
	}
	public void setReciver(String reciver) {
		this.reciver = reciver;
	}
	
	
	
}
