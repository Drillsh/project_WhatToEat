package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<RestaurantData> brandList;

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    // 생성자
    public BrandListAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public BrandListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item, viewGroup, false);
        BrandListAdapter.CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrandListAdapter.CustomViewHolder customViewHolder, int position) {

        customViewHolder.tvBrandName.setText(brandList.get(position).getBrandName());
        customViewHolder.tvCategory.setText(brandList.get(position).getCategory());
        customViewHolder.tvPhoneNum.setText(brandList.get(position).getPhoneNum());
        customViewHolder.tvAddress.setText(brandList.get(position).getAddress());
        customViewHolder.tvDistance.setText(brandList.get(position).getDistance()+"m");

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

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvBrandName;
        TextView tvCategory;
        TextView tvDistance;
        TextView tvPhoneNum;
        TextView tvAddress;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBrandName = (TextView) itemView.findViewById(R.id.tvBrandName);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvPhoneNum = (TextView) itemView.findViewById(R.id.tvPhoneNum);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);

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
