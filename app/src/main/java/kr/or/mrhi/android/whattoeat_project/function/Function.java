package kr.or.mrhi.android.whattoeat_project.function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import kr.or.mrhi.android.whattoeat_project.kakaologin.LoginActivity;

public interface Function {

    @RequiresApi(api = Build.VERSION_CODES.N)
    static void settingToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
