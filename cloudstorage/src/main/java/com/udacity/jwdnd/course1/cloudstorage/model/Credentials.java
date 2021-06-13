package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credentials {

    private int userid;
    private String url;
    private String username;
    private String key;
    private String password;
    private Integer credentialid;
    private String encpassword;

    public Credentials(int userid, String url, String username, String key, String password, Integer credentialid) {
        this.userid = userid;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.credentialid = credentialid;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Integer getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public String getEncpassword() {
        return encpassword;
    }

    public void setEncpassword(String encpassword) {
        this.encpassword = encpassword;
    }
}
