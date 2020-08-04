package kr.or.mrhi.android.whattoeat_project.model;

public class RestaurantData {

    private String brandName;
    private String category;
    private String phoneNum;
    private String address;
    private int distance;

    public RestaurantData(String brandName, String category, String phoneNum, String address, int distance) {
        this.brandName = brandName;
        this.category = category;
        this.phoneNum = phoneNum;
        this.address = address;
        this.distance = distance;
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
}
