package com.example.finalproject_tylernguyen;

public class Job {
    private String job_location;
    private String position_type;
    private String logo_url;
    private String job_title;

    public Job(){}
    public Job(String job_title, String position_type
            , String job_location, String logo_url){
        //send these String to the object
        this.job_title = job_title;
        this.position_type = position_type;
        this.job_location = job_location;
        this.logo_url = logo_url;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getPosition_type() {
        return position_type;
    }

    public void setPosition_type(String position_type) {
        this.position_type = position_type;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }
}
