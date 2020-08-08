package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.CommentImageAdapter;
import kr.or.mrhi.android.whattoeat_project.adapter.CommentListAdapter;
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
    private CommentImageAdapter commentImageAdapter;

    private ArrayList<RestaurantData> restList = new ArrayList<>();
    private ArrayList<CommentData> commentArrayList = new ArrayList<>();
    RestaurantDB_Controller commentDB = RestaurantDB_Controller.getInstance(RestaurantActivity.this);

    RestaurantData restaurantData;

    private final int GET_GALLERY_IMAGE = 1;
    private String gImgPath;
    private String fileName;
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

        //GalleryView
        commentImageAdapter = new CommentImageAdapter(getApplicationContext());
        gallery.setAdapter(commentImageAdapter);

        rbTotal.setEnabled(false);
    }

    //업체 평균 별점을 구하는 함수

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void restaurantAvgRating() {
        float totalRating = 0.0f;
        float avgRating = 0.0f;

        if(!commentArrayList.isEmpty()) {
            //리스트에서 하나씩 가져와서 계속 더해준다
            for (CommentData commentData : commentArrayList) {

                totalRating += commentData.getRating();

            }
            //위에서 더해준 총 별점을 등록된 리스트 사이즈로 나눠서 평균값을 구한다
            avgRating = totalRating / commentArrayList.size();
        }

        rbTotal.setRating(avgRating);

        boolean returnValue = commentDB.updateRestaurantRating(restaurantData,avgRating);
        if (returnValue) {
            Function.settingToast(getApplicationContext(), "데이터 갱신 성공");

        } else {
            Function.settingToast(getApplicationContext(), "데이터 갱신 실패");
        }

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
                //선택된 업체의 위치정보를 건내줌
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

                        /*String downPath = getSaveFolder().getAbsolutePath();
                        Log.d("다운로드 패스",downPath);*/
                        getSaveFolder();
                        copyImageFile();

                        //작성한 리뷰,별점,이미지,작성날짜를 받아서 DB에 저장
                        String comment = etComment.getText().toString();
                        float rating = rbRecommed.getRating();
                        String commetImage = getSaveFolder().getAbsolutePath()+"/"+fileName;
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
            fileName = getImageNameToUri(uri);
            Log.d("사진 파일명","파일명: "+fileName+"이미지 path: "+gImgPath+"uri 값: "+uri.toString());
            setImage(uri);
        }
    }
    //이미지뷰에 uri를 통해 비트맵생성후 세팅
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                CommentData commentData = commentArrayList.get(position);
                commentDB.deleteCommentData(commentData);
                commentArrayList.remove(position);
                restaurantAvgRating();
                deleteImageFile(commentData);
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

    //Uri에서 파일명을 추출하는 함수
    public String getImageNameToUri(Uri data){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data,proj,null,null,null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String imgPath = cursor.getString(index);
        gImgPath = imgPath;
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }

    //사진을 저장할 폴더를 만드는 함수 ///storage/emulated/0:commentImageFolder
    public File getSaveFolder(){
        String folderName = "commentImageFolder";
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folderName);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    //사진을 저장 시키는 함수
    public void copyImageFile(){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(gImgPath);
            fos = new FileOutputStream("/storage/emulated/0/commentImageFolder/"+fileName);
            int data = 0;

            while((data = fis.read())!= -1){
                fos.write(data);
            }

        } catch (FileNotFoundException e) {
            Log.d("FileNotError",e.getMessage());
        } catch (IOException e) {
            Log.d("fis read",e.getMessage());
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                Log.d("반납오류",e.getMessage());
            }
        }
    }
    //저장된 사진을 지우는 함수
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteImageFile(CommentData commentData){
        String fPath = null;
        //commentData 안에 이미지 파일 경로설정
        fPath = commentData.getImgPath();
        File f = new File(fPath);

        if(f.delete()){
            Function.settingToast(getApplicationContext(),"삭제 성공");
        }
        else{
            Function.settingToast(getApplicationContext(),"삭제 실패");
        }
    }
}
