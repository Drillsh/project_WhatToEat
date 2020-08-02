package kr.or.mrhi.android.whattoeat_project.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// DB 관리 클래스 : 싱글톤
public class DB_Controller extends SQLiteOpenHelper {

    private static final String DB_NAME = "WhatToEatDB";
    private static final int VERSION = 1;

    private Context context;

    private static DB_Controller db_controller;

    // 생성자
    private DB_Controller(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // getInstance
    private static DB_Controller getInstance(Context context){

        if (db_controller == null){
            db_controller = new DB_Controller(context);
        }
        return db_controller;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
