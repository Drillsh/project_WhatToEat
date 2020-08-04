package kr.or.mrhi.android.whattoeat_project.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// DB 관리 클래스 : 싱글톤
public class RestaurantDB_Controller extends SQLiteOpenHelper {

    private static final String DB_NAME = "WhatToEatDB";
    private static final int VERSION = 1;

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

        db.execSQL(
                "CREATE TABLE restaurantTBL(" +
                        "brandName VARCHAR(15) PRIMARY KEY," +
                        "category VARCHAR(10)," +
                        "phoneNum VARCHAR(20)," +
                        "address VARCHAR(30)," +
                        "distance INTEGER(5)," +
                        "imgPath VARCHAR(30) );"
        );
    }

    // 버전 바뀌면 테이블 삭제하고 다시 생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists restaurantTBL");
        onCreate(db);
    }

    // DB Select
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
                        cursor.getString(5));

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


    // DB 삽입
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
                        + "'" + data.getImgPath() + "');";

                // 쿼리문 작성해서 넘김
                // 예외발생시 SQLException
                sqLiteDatabase.execSQL(query);
            }
            retrunValue = true;

        } catch (SQLException e) {
            Log.e("selectData", e.getMessage());
            retrunValue = false;

        } finally {
            sqLiteDatabase.close();
        }

        return retrunValue;
    }

    // DB 삭제
    public boolean deleteRestaurantData(RestaurantData restaurantData){

        boolean retrunValue = false;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
            String query = "delete from restaurantTBL where brandName = ?";

            sqLiteDatabase.execSQL(query, new String[]{restaurantData.getBrandName()});

            retrunValue = true;

        }catch (SQLException e){
            Log.e("selectData", e.getMessage());
            retrunValue = false;

        }finally {
            sqLiteDatabase.close();
        }

        return retrunValue;
    }


}
