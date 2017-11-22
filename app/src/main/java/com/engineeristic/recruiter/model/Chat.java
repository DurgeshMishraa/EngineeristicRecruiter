package com.engineeristic.recruiter.model;

public class Chat
{
    private String id;

    private String channelname;

    private String email;

    private String name;

    private String seekerimage_url;

    private String notify;

    private String current_organization;

    private String current_designation;

    private String reciver;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getChannelname ()
    {
        return channelname;
    }

    public void setChannelname (String channelname)
    {
        this.channelname = channelname;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

  

    public String getSeekerimage_url() {
		return seekerimage_url;
	}

	public void setSeekerimage_url(String seekerimage_url) {
		this.seekerimage_url = seekerimage_url;
	}

	public String getNotify ()
    {
        return notify;
    }

    public void setNotify (String notify)
    {
        this.notify = notify;
    }

    public String getCurrent_organization ()
    {
        return current_organization;
    }

    public void setCurrent_organization (String current_organization)
    {
        this.current_organization = current_organization;
    }

    public String getCurrent_designation ()
    {
        return current_designation;
    }

    public void setCurrent_designation (String current_designation)
    {
        this.current_designation = current_designation;
    }

    public String getReciver ()
    {
        return reciver;
    }

    public void setReciver (String reciver)
    {
        this.reciver = reciver;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", channelname = "+channelname+", email = "+email+", name = "+name+", seekerimage_url = "+seekerimage_url+", notify = "+notify+", current_organization = "+current_organization+", current_designation = "+current_designation+", reciver = "+reciver+"]";
    }
}
