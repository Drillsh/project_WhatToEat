package kr.or.mrhi.android.whattoeat_project.model;

import android.graphics.Bitmap;

// 코멘트 정보를 담는 모델
public class CommentData {

    private String brandName;
    private String imgPath;
    private String comment;
    private String date;
    private float rating;

    public CommentData(String brandName, String imgPath, String comment, String date, float rating) {
        this.brandName = brandName;
        this.imgPath = imgPath;
        this.comment = comment;
        this.date = date;
        this.rating = rating;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
