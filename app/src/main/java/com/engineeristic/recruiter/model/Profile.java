package com.engineeristic.recruiter.model;

public class Profile
{
    private String email;

    private String tt;

    private String name;

    private String organisation;

    private String image;
    private String designation;

    private String usr;

    private String usrr;

    private String recruiter_id;   
    

    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRecruiter_id() {
		return recruiter_id;
	}

	public void setRecruiter_id(String recruiter_id) {
		this.recruiter_id = recruiter_id;
	}

	public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getTt ()
    {
        return tt;
    }

    public void setTt (String tt)
    {
        this.tt = tt;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getOrganisation ()
    {
        return organisation;
    }

    public void setOrganisation (String organisation)
    {
        this.organisation = organisation;
    }

   

    public String getDesignation ()
    {
        return designation;
    }

    public void setDesignation (String designation)
    {
        this.designation = designation;
    }

    public String getUsr ()
    {
        return usr;
    }

    public void setUsr (String usr)
    {
        this.usr = usr;
    }

    public String getUsrr ()
    {
        return usrr;
    }

    public void setUsrr (String usrr)
    {
        this.usrr = usrr;
    }

   

    @Override
    public String toString()
    {
        return "ClassPojo [email = "+email+", tt = "+tt+", name = "+name+", organisation = "+organisation+", image = "+image+", designation = "+designation+", usr = "+usr+", usrr = "+usrr+", recruiter_id = "+recruiter_id+"]";
    }
}