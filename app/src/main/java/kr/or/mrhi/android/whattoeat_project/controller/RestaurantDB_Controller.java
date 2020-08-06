package kr.or.mrhi.android.whattoeat_project.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.model.CommentData;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// DB 관리 클래스 : 싱글톤
public class RestaurantDB_Controller extends SQLiteOpenHelper {

    private static final String DB_NAME = "WhatToEatDB";
    private static final int VERSION = 5;

    private Context context;

    private static RestaurantDB_Controller restaurantDb_controller;

    // 생성자
    private RestaurantDB_Controller(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // getInstance
    public static RestaurantDB_Controller getInstance(Context context) {

        if (restaurantDb_controller == null) {
            restaurantDb_controller = new RestaurantDB_Controller(context);
        }
        return restaurantDb_controller;
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {

        // 음식점 정보 DB
        db.execSQL(
                "CREATE TABLE restaurantTBL(" +
                        "brandName VARCHAR(15) PRIMARY KEY," +
                        "category VARCHAR(10)," +
                        "phoneNum VARCHAR(20)," +
                        "address VARCHAR(30)," +
                        "distance INTEGER(5)," +
                        "imgPath VARCHAR(30)," +
                        "starRating FLOAT(2,1),"+
                        "latitude DOUBLE(3,8),"+
                        "longitude DOUBLE(3,8));"
        );

        // 음식점 코멘트 정보 테이블
        db.execSQL(
                "CREATE TABLE commentTBL(" +
                        "brandName VARCHAR(15) PRIMARY KEY," +
                        "imgPath VARCHAR(10)," +
                        "comment VARCHAR(20)," +
                        "date VARCHAR(30)," +
                        "rating FLOAT(2,1));"
        );
    }

    // 버전 바뀌면 테이블 삭제하고 다시 생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists restaurantTBL");
        db.execSQL("drop table if exists commentTBL");
        onCreate(db);
    }

    // 음식점DB Select
    public ArrayList<RestaurantData> selectRestaurantData() {

        ArrayList<RestaurantData> restaurantList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            //쿼리문 입력하고 커서 리턴 받음
            cursor = sqLiteDatabase.rawQuery("select * from restaurantTBL;", null);

            while (cursor.moveToNext()) {
                RestaurantData restaurantData = new RestaurantData(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getFloat(6),
                        cursor.getDouble(7),
                        cursor.getDouble(8)
                );

                restaurantList.add(restaurantData);
            }
        } catch (SQLException e) {
            Log.e("selectData", e.getMessage());

        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }

        return restaurantList;
    }


    // 음식점DB 삽입
    public boolean insertRestaurantData(ArrayList<RestaurantData> restaurantList) {

        boolean retrunValue = false;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
            for (RestaurantData data : restaurantList) {

                String query = "insert into restaurantTBL values("
                        + "'" + data.getBrandName() + "',"
                        + "'" + data.getCategory() + "',"
                        + "'" + data.getPhoneNum() + "',"
                        + "'" + data.getAddress() + "',"
                        + data.getDistance() + ","
                        + "'" + data.getImgPath() + "',"
                        + data.getStarRating()+","
                        + data.getLatitude() +","
                        + data.getLongitude() +")";

                // 쿼리문 작성해서 넘김
                // 예외발생시 SQLException
                sqLiteDatabase.execSQL(query);
            }
            retrunValue = true;

        } catch (SQLException e) {
            Log.e("insertData", e.getMessage());
            retrunValue = false;

        } finally {
            sqLiteDatabase.close();
        }

        return retrunValue;
    }

    // 음식점DB 삭제
    public boolean deleteRestaurantData(RestaurantData restaurantData){

        boolean retrunValue = false;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
            String query = "delete from restaurantTBL where brandName = ?";

            sqLiteDatabase.execSQL(query, new String[]{restaurantData.getBrandName()});

            retrunValue = true;

        }catch (SQLException e){
            Log.e("deleteData", e.getMessage());
            retrunValue = false;

        }finally {
            sqLiteDatabase.close();
        }

        return retrunValue;
    }

    // 코멘트 DB select
    public ArrayList<CommentData> selectCommentDB(String brandName) {

        ArrayList<CommentData> commentList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            //쿼리문 입력하고 커서 리턴 받음
            cursor = sqLiteDatabase.rawQuery("select * from commentTBL where brandName = ?;", new String[]{brandName});

            while (cursor.moveToNext()) {
                CommentData data = new CommentData(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getFloat(4)
                );

                commentList.add(data);
            }
        } catch (SQLException e) {
            Log.e("selectCommentData", e.getMessage());

        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }

        return commentList;
    }

    // 코멘트 DB 삽입
    public boolean insertCommentData(CommentData data) {

        boolean retrunValue = false;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
                String query = "insert into commentTBL values("
                        + "'" + data.getBrandName() + "',"
                        + "'" + data.getImgPath() + "',"
                        + "'" + data.getComment() + "',"
                        + "'" + data.getDate() + "',"
                        + data.getRating() +");";

                // 쿼리문 작성해서 넘김
                // 예외발생시 SQLException
                sqLiteDatabase.execSQL(query);

            retrunValue = true;

        } catch (SQLException e) {
            Log.e("insertCommentData", e.getMessage());
            retrunValue = false;

        } finally {
            sqLiteDatabase.close();
        }

        return retrunValue;
    }

    // 코멘트 DB 삭제
    public boolean deleteCommentData(CommentData commentData){

        boolean retrunValue = false;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
            String query = "delete from commentTBL where brandName = ? AND comment = ? AND date = ? AND rating = ?";

            sqLiteDatabase.execSQL(query, new String[]{commentData.getBrandName(), commentData.getComment(), commentData.getDate(), String.valueOf(commentData.getRating())});

            retrunValue = true;

        }catch (SQLException e){
            Log.e("deleteCommentData", e.getMessage());
            retrunValue = false;

        }finally {
            sqLiteDatabase.close();
        }

        return retrunValue;
    }
}
