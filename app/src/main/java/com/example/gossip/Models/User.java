package com.example.gossip.Models;

public class User
{
    private String uid,name,PhoneNumber,ProfileImage,token;
public User(){

}
    public User(String uid, String name, String phoneNumber, String profileImage) {
        this.uid = uid;
        this.name = name;
        PhoneNumber = phoneNumber;
        ProfileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
