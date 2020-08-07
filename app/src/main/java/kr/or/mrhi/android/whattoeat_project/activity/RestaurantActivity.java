package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.CommentListAdapter;
import kr.or.mrhi.android.whattoeat_project.adapter.RestaurantGalleryAdapter;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.function.Function;
import kr.or.mrhi.android.whattoeat_project.model.CommentData;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener, CommentListAdapter.OnLongClickListener{
    private TextView tvRestName, tvRestAdress, tvRestContact;
    private Button btnRestCall, btnRestLocation, btnAddComment;
    private RatingBar rbTotal;
    private Gallery gallery;
    private RecyclerView commentList;
    private CommentListAdapter commentListAdapter;
    private RestaurantGalleryAdapter restaurantGalleryAdapter;

    private ArrayList<RestaurantData> restList = new ArrayList<>();
    private ArrayList<CommentData> commentArrayList = new ArrayList<>();
    RestaurantDB_Controller commentDB = RestaurantDB_Controller.getInstance(RestaurantActivity.this);

    RestaurantData restaurantData;

    private final int GET_GALLERY_IMAGE = 1;
    Uri uri;
    ImageView ivCommentImage;
    Button btnCommentImage;
    EditText etComment;
    RatingBar rbRecommed;
    int position;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        //View 아이디 찾기
        findViewByIdFnc();

        //MainFrag 리스트에서 사용자가 선택한 리스트의 위치정보 받아오기
        Intent intent = getIntent();
        position = intent.getIntExtra("nearbyList",0);

        //DB에 저장된 업체정보를 가져와서 ArrayList에 저장
        restList = commentDB.selectRestaurantData();


        //ArrayList에 저장된 업체 중 MainFrag 리스트에서 선택된 업체정보를 받아옴
        restaurantData = restList.get(position);

        //받아온 정보를 각 텍스트뷰에 적용
        tvRestName.setText(restaurantData.getBrandName());
        tvRestAdress.setText(restaurantData.getAddress());

        tvRestAdress.setSelected(true);     //텍스트수가 일정수를 넘어가면 흐르게하는 효과
        tvRestContact.setText(restaurantData.getPhoneNum());
        rbTotal.setRating(restaurantData.getStarRating());

        // 상호명으로 음식점의 코멘트 정보 가져옴
        commentArrayList.clear();
        commentArrayList.addAll(commentDB.selectCommentDB(restaurantData.getBrandName()));

        //recyclerview
        commentListAdapter = new CommentListAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        commentListAdapter.setCommentList(commentArrayList);
        commentListAdapter.notifyDataSetChanged();

        commentList.setAdapter(commentListAdapter);
        commentList.setLayoutManager(linearLayoutManager);

        commentListAdapter.setOnLongClickListener(this);

        rbTotal.setEnabled(false);
    }

    //업체 평균 별점을 구하는 함수

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void restaurantAvgRating() {
        float totalRating = 0.0f;
        for(CommentData commentData :commentArrayList){

            totalRating += commentData.getRating();

        }

        float avgRating = totalRating / commentArrayList.size();

        rbTotal.setRating(avgRating);

        boolean returnValue = commentDB.updateRestaurantRating(restaurantData,avgRating);
        if (returnValue) {
            Function.settingToast(getApplicationContext(), "데이터 갱신 성공");

        } else {
            Function.settingToast(getApplicationContext(), "데이터 갱신 실패");
        }

        Function.settingToast(getApplicationContext(),String.valueOf(restaurantData.getStarRating()));

    }

    private void findViewByIdFnc() {
        tvRestName = findViewById(R.id.tvRestName);
        tvRestAdress = findViewById(R.id.tvRestAdress);
        tvRestContact = findViewById(R.id.tvRestContact);
        btnRestCall = findViewById(R.id.btnRestCall);
        btnRestLocation = findViewById(R.id.btnRestLocation);
        btnAddComment = findViewById(R.id.btnAddComment);
        gallery = findViewById(R.id.gallery);
        commentList = findViewById(R.id.commentList);
        rbTotal = findViewById(R.id.rbTotal);

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
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + restaurantData.getPhoneNum()));
                startActivity(intent);
                break;
            //음식점 위치 정보
            case R.id.btnRestLocation :
                Intent mapIntent = new Intent(getApplicationContext(),MapActivity.class);
                mapIntent.putExtra("restLocation",position);
                startActivity(mapIntent);
                break;

            //리뷰 작성 다이얼로그 창
            case R.id.btnAddComment :
                View commentView = View.inflate(view.getContext(),R.layout.add_comment,null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

                dialog.setTitle("리뷰 등록");
                dialog.setView(commentView);

                ivCommentImage = commentView.findViewById(R.id.ivCommentImage);
                btnCommentImage = commentView.findViewById(R.id.btnCommentImage);
                etComment = commentView.findViewById(R.id.etComment);
                rbRecommed = commentView.findViewById(R.id.rbRecommed);

                //사진 등록 버튼
                btnCommentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GET_GALLERY_IMAGE);

                    }
                });

                dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //작성한 리뷰,별점,이미지,작성날짜를 받아서 DB에 저장
                        String comment = etComment.getText().toString();
                        float rating = rbRecommed.getRating();
                        String commetImage = uri.toString();
                        Date today = Calendar.getInstance().getTime();
                        String date = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일", Locale.getDefault()).format(today);

                        CommentData commentData = new CommentData(restaurantData.getBrandName(),commetImage,comment,date,rating);
                        commentArrayList.add(commentData);

                        boolean returnValue = commentDB.insertCommentData(commentData);

                        if (returnValue) {
                            Function.settingToast(getApplicationContext(), "데이터 삽입 성공");

                        } else {
                            Function.settingToast(getApplicationContext(), "데이터 삽입 실패");
                        }

                        //위에서 저장한 리뷰를 리스트로 뿌려줌
                        commentListAdapter.setCommentList(commentArrayList);
                        commentList.setAdapter(commentListAdapter);
                        commentListAdapter.notifyDataSetChanged();

                        //해당업체 리뷰 평균 별점
                        restaurantAvgRating();
                    }
                });

                dialog.setNegativeButton("취소", null);
                dialog.show();

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE) {
            uri = data.getData();
            setImage(uri);
        }
    }

    private void setImage(Uri uri){
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            ivCommentImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //리스트 롱클릭 이벤트 처리 함수
    @Override
    public void onItemLongClick(View view, int position) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RestaurantActivity.this);
        builder.setTitle("목록에서 삭제하시겠습니까?");
        //"예"누를시 적용되는 이벤트 처리
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                CommentData commentData = commentArrayList.get(position);
                commentDB.deleteCommentData(commentData);
                commentArrayList.remove(position);
                commentListAdapter.notifyDataSetChanged();
            }
        });
        //"아니요" 누를 시 적용되는 이벤트 등록 처리
        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
