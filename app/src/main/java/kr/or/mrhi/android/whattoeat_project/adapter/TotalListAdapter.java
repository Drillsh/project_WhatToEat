package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.ListActivity;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.model.CommentData;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class TotalListAdapter extends RecyclerView.Adapter<TotalListAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<RestaurantData> brandList;
    private ArrayList<CommentData> commentDataList;
    private RestaurantDB_Controller restaurantDB_controller;
    private ListActivity listActivity = new ListActivity();

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLongListener = null;

    // 생성자
    public TotalListAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public TotalListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_food, viewGroup, false);
        TotalListAdapter.CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TotalListAdapter.CustomViewHolder customViewHolder, int position) {
        restaurantDB_controller = RestaurantDB_Controller.getInstance(listActivity);
        Bitmap bitmap = null;
        commentDataList = restaurantDB_controller.selectCommentDB(brandList.get(position).getBrandName());

        //지정폴더에서  path값으로 비트맵을 만든다.
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        if(commentDataList.size() != 0){
            bitmap = BitmapFactory.decodeFile(commentDataList.get(0).getImgPath(),bfo);
            customViewHolder.ivFoodPicture.setImageBitmap(bitmap);
        }else{
            customViewHolder.ivFoodPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.chefpikachu));
        }

        customViewHolder.tvEateryName.setText(brandList.get(position).getBrandName());
        customViewHolder.tvEateryName.setSelected(true);
        customViewHolder.tvFoodMenu.setText(brandList.get(position).getCategory());
        customViewHolder.tvDistance.setText(String.valueOf(brandList.get(position).getDistance())+"M");
        customViewHolder.tvCallNumber.setText("☎ "+brandList.get(position).getPhoneNum());
        customViewHolder.tvAddress.setText(brandList.get(position).getAddress());
        customViewHolder.tvAddress.setSelected(true);
        customViewHolder.ratingBar.setRating(brandList.get(position).getStarRating());
        customViewHolder.ratingBar.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return brandList != null ? brandList.size() : 0;
    }

    // 리사이클러뷰 클릭 이벤트를 위한 인터페이스
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View v, int pos);
    }

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    // OnItemLongClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemLongClickListener(OnItemLongClickListener LongListener){
        this.mLongListener = LongListener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodPicture;
        TextView tvEateryName;
        TextView tvFoodMenu;
        TextView tvDistance;
        TextView tvCallNumber;
        TextView tvAddress;
        RatingBar ratingBar;

        int pos = 0;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodPicture = (ImageView) itemView.findViewById(R.id.ivFoodPicture);
            tvEateryName = (TextView) itemView.findViewById(R.id.tvEateryName);
            tvFoodMenu = (TextView) itemView.findViewById(R.id.tvFoodMenu);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDesc);
            tvCallNumber = (TextView) itemView.findViewById(R.id.tvCallNumber);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            // 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        mListener.onItemClick(view, pos);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mLongListener.onItemLongClick(view,pos);
                    }
                    return true;
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

    public ArrayList<CommentData> getCommentDataList() {
        return commentDataList;
    }

    public void setCommentDataList(ArrayList<CommentData> commentDataList) {
        this.commentDataList = commentDataList;
    }
}
