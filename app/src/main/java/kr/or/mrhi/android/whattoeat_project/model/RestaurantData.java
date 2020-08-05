package kr.or.mrhi.android.whattoeat_project.model;

import android.os.Parcel;
import android.os.Parcelable;

// 음식점 정보 모델
public class RestaurantData implements Parcelable {

    private String brandName;
    private String category;
    private String phoneNum;
    private String address;
    private int distance;
    private String imgPath;
    private float starRating;

    protected RestaurantData(Parcel in) {
        brandName = in.readString();
        category = in.readString();
        phoneNum = in.readString();
        address = in.readString();
        distance = in.readInt();
        imgPath = in.readString();
        starRating = in.readFloat();
    }

    public RestaurantData(String brandName, String category, String phoneNum, String address, int distance, String imgPath, float starRating) {
        this.brandName = brandName;
        this.category = category;
        this.phoneNum = phoneNum;
        this.address = address;
        this.distance = distance;
        this.imgPath = imgPath;
        this.starRating = starRating;
    }

    // ---------------getters, setters -------------------

    public static final Creator<RestaurantData> CREATOR = new Creator<RestaurantData>() {
        @Override
        public RestaurantData createFromParcel(Parcel in) {
            return new RestaurantData(in);
        }

        @Override
        public RestaurantData[] newArray(int size) {
            return new RestaurantData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeString(category);
        dest.writeString(phoneNum);
        dest.writeString(address);
        dest.writeInt(distance);
        dest.writeString(imgPath);
        dest.writeFloat(starRating);
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }
}
