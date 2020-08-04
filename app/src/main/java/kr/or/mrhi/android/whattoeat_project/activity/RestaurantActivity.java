package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.RestaurantGalleryAdapter;
import kr.or.mrhi.android.whattoeat_project.adapter.RestaurantListAdapter;
import kr.or.mrhi.android.whattoeat_project.model.CommentData;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvRestName, tvRestAdress, tvRestContact;
    private Button btnRestCall, btnRestLocation, btnAddComment;
    private Gallery gallery;
    private RecyclerView restaurantList;
    private RestaurantListAdapter restaurantListAdapter;
    private RestaurantGalleryAdapter restaurantGalleryAdapter;
    private RestaurantData restaurantData;

    private ArrayList<RestaurantData> restList = new ArrayList<RestaurantData>();
    private ArrayList<CommentData> commentList = new ArrayList<CommentData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        findViewByIdFnc();
    }

    private void findViewByIdFnc() {
        tvRestName = findViewById(R.id.tvRestName);
        tvRestAdress = findViewById(R.id.tvRestAdress);
        tvRestContact = findViewById(R.id.tvRestContact);
        btnRestCall = findViewById(R.id.btnRestCall);
        btnRestLocation = findViewById(R.id.btnRestLocation);
        btnAddComment = findViewById(R.id.btnAddComment);
        gallery = findViewById(R.id.gallery);
        restaurantList = findViewById(R.id.restaurantList);

        btnRestCall.setOnClickListener(this);
        btnRestLocation.setOnClickListener(this);
        btnAddComment.setOnClickListener(this);
    }

    //버튼 이벤트 함수
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //음식점으로 전화연결
            case R.id.btnRestCall :
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:012-3456-7890"));
                startActivity(intent);
                break;
            //음식점 위치 정보
            case R.id.btnRestLocation : break;
            //리뷰 작성 다이얼로그 창
            case R.id.btnAddComment :
                View commentView = View.inflate(view.getContext(),R.layout.add_comment,null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

                dialog.setTitle("리뷰 등록");
                dialog.setView(commentView);

                ImageView ivCommentImage = commentView.findViewById(R.id.ivCommentImage);
                Button btnCommentImage = commentView.findViewById(R.id.btnCommentImage);
                EditText etComment = commentView.findViewById(R.id.etComment);

                //사진 등록 버튼
                btnCommentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();

                break;
        }
    }
}
