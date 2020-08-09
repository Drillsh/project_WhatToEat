package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.model.CommentData;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// 음식점 리스트 어댑터
public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<RestaurantData> brandList;
    private RestaurantDB_Controller restaurantDB_controller;
    private MainActivity mainActivity = new MainActivity();
    private ArrayList<CommentData> commentDataList;
    private boolean todayBrandPick;

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    // 생성자
    public BrandListAdapter(Context context, boolean todayBrandPick) {
        this.context = context;
        this.todayBrandPick = todayBrandPick;
    }

    @NonNull
    @Override
    public BrandListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item, viewGroup, false);
        BrandListAdapter.CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrandListAdapter.CustomViewHolder customViewHolder, int position) {

        restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);
        Bitmap bitmap = null;
        commentDataList = restaurantDB_controller.selectCommentDB(brandList.get(position).getBrandName());

        //지정폴더에서  path값으로 비트맵을 만든다.
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        if(commentDataList.size() != 0){
            bitmap = BitmapFactory.decodeFile(commentDataList.get(0).getImgPath(),bfo);
            customViewHolder.ivImage.setImageBitmap(bitmap);
        }else{
            customViewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.chefpikachu));
        }

        if (!brandList.isEmpty()) {
            customViewHolder.tvBrandName.setText(brandList.get(position).getBrandName());
            customViewHolder.tvCategory.setText(brandList.get(position).getCategory());
            customViewHolder.tvPhoneNum.setText(brandList.get(position).getPhoneNum());
            customViewHolder.tvAddress.setText(brandList.get(position).getAddress());
            customViewHolder.tvDistance.setText(brandList.get(position).getDistance() + "m");
            customViewHolder.startRating.setRating(brandList.get(position).getStarRating());

            // 글자 흘러가게 하기
            // singleLine = true, ellipsize = marquee 처리도 함께
            customViewHolder.tvAddress.setSelected(true);
        }
    }

    @Override
    public int getItemCount() { return brandList != null ? brandList.size() : 0; }

    // 리사이클러뷰 클릭 이벤트를 위한 인터페이스
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvBrandName;
        TextView tvCategory;
        TextView tvDistance;
        TextView tvPhoneNum;
        TextView tvAddress;
        RatingBar startRating;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage= (ImageView) itemView.findViewById(R.id.ivImage);
            tvBrandName = (TextView) itemView.findViewById(R.id.tvBrandName);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDesc);
            tvPhoneNum = (TextView) itemView.findViewById(R.id.tvPhoneNum);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            startRating = (RatingBar) itemView.findViewById(R.id.starRating);

            // 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        mListener.onItemClick(view, pos);
                    }
                }
            });
        }
    }

    // -------------------Getters, Setters ------------------------

    public ArrayList<RestaurantData> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<RestaurantData> brandList) {
        this.brandList = brandList;
    }

    public OnItemClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
}
