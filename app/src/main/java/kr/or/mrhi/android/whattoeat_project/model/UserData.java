package kr.or.mrhi.android.whattoeat_project.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {
    private String userNickName;
    private String email;
    private String profileImagePath;

    public UserData(String userNickName, String email, String profileImagePath) {
        this.userNickName = userNickName;
        this.email = email;
        this.profileImagePath = profileImagePath;
    }

    protected UserData(Parcel in) {
        userNickName = in.readString();
        email = in.readString();
        profileImagePath = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userNickName);
        parcel.writeString(email);
        parcel.writeString(profileImagePath);
    }
}
