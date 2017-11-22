package com.engineeristic.recruiter.model;

import java.io.Serializable;

/**
 * Created by dell on 10/5/2016.
 */
public class ProfileModel implements Serializable{
    private String phone;

    private String location_name;

    private String notification_type;

    private String organisation;

    private String turl;

    private String notification_type_text;

    private String designation;

    private String about;

    private String image;

    private String recname;

    private String furl;

    private String public_url;

    private String type_id;

    private String location_id;

    private String last_login;

    private String company_url;

    private String id;

    private String email;

    private String views;

    private String type_name;

    private String lurl;

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getLocation_name ()
    {
        return location_name;
    }

    public void setLocation_name (String location_name)
    {
        this.location_name = location_name;
    }

    public String getNotification_type ()
    {
        return notification_type;
    }

    public void setNotification_type (String notification_type)
    {
        this.notification_type = notification_type;
    }

    public String getOrganisation ()
    {
        return organisation;
    }

    public void setOrganisation (String organisation)
    {
        this.organisation = organisation;
    }

    public String getTurl ()
    {
        return turl;
    }

    public void setTurl (String turl)
    {
        this.turl = turl;
    }

    public String getNotification_type_text ()
    {
        return notification_type_text;
    }

    public void setNotification_type_text (String notification_type_text)
    {
        this.notification_type_text = notification_type_text;
    }

    public String getDesignation ()
    {
        return designation;
    }

    public void setDesignation (String designation)
    {
        this.designation = designation;
    }

    public String getAbout ()
    {
        return about;
    }

    public void setAbout (String about)
    {
        this.about = about;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getRecname ()
    {
        return recname;
    }

    public void setRecname (String recname)
    {
        this.recname = recname;
    }

    public String getFurl ()
    {
        return furl;
    }

    public void setFurl (String furl)
    {
        this.furl = furl;
    }

    public String getPublic_url ()
    {
        return public_url;
    }

    public void setPublic_url (String public_url)
    {
        this.public_url = public_url;
    }

    public String getType_id ()
    {
        return type_id;
    }

    public void setType_id (String type_id)
    {
        this.type_id = type_id;
    }

    public String getLocation_id ()
    {
        return location_id;
    }

    public void setLocation_id (String location_id)
    {
        this.location_id = location_id;
    }

    public String getLast_login ()
    {
        return last_login;
    }

    public void setLast_login (String last_login)
    {
        this.last_login = last_login;
    }

    public String getCompany_url ()
    {
        return company_url;
    }

    public void setCompany_url (String company_url)
    {
        this.company_url = company_url;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getViews ()
    {
        return views;
    }

    public void setViews (String views)
    {
        this.views = views;
    }

    public String getType_name ()
    {
        return type_name;
    }

    public void setType_name (String type_name)
    {
        this.type_name = type_name;
    }

    public String getLurl ()
    {
        return lurl;
    }

    public void setLurl (String lurl)
    {
        this.lurl = lurl;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [phone = "+phone+", location_name = "+location_name+", notification_type = "+notification_type+", organisation = "+organisation+", turl = "+turl+", notification_type_text = "+notification_type_text+", designation = "+designation+", about = "+about+", image = "+image+", recname = "+recname+", furl = "+furl+", public_url = "+public_url+", type_id = "+type_id+", location_id = "+location_id+", last_login = "+last_login+", company_url = "+company_url+", id = "+id+", email = "+email+", views = "+views+", type_name = "+type_name+", lurl = "+lurl+"]";
    }
}
