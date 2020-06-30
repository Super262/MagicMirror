package com.qilu.ec.main.sample.user_profile;

public class UserProfile_Data_Data {
    int UserID;
    String UserName;
    String Signature;
    String Phone;
    String Avatar;
    int FollowingNum;
    int FollowersNum;

    public UserProfile_Data_Data(int userID,
                                 String userName,
                                 String signature,
                                 String phone,
                                 String avatar,
                                 int followingNum,
                                 int followersNum) {
        UserID = userID;
        UserName = userName;
        Signature = signature;
        Phone = phone;
        Avatar = avatar;
        FollowingNum = followingNum;
        FollowersNum = followersNum;
    }

    public String getUserName() {
        return UserName;
    }

    public String getAvatar() {
        return Avatar;
    }

}
