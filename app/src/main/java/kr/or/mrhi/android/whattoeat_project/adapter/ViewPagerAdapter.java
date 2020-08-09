package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

//뷰페이저 어댑터 클래스
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<RestaurantData> arrayList;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.main_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int index = position % arrayList.size();

        RestaurantData restaurantData = arrayList.get(index);

        //지정폴더에서  path값으로 비트맵을 만든다.
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        if (arrayList.size() != 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(restaurantData.getImgPath(), bfo);
            holder.ivBrandImg.setImageBitmap(bitmap);
        } else {
            holder.ivBrandImg.setImageDrawable(context.getResources().getDrawable(R.drawable.pika));
        }

        holder.tvBrandName.setText(restaurantData.getBrandName());
        holder.tvCategory.setText(restaurantData.getCategory());
        holder.tvDistance.setText(String.valueOf(restaurantData.getDistance()));
        holder.tvPhoneNum.setText(restaurantData.getPhoneNum());
        holder.tvAddress.setText(restaurantData.getAddress());
        holder.starRating.setRating(restaurantData.getStarRating());

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBrandImg;
        TextView tvBrandName;
        TextView tvCategory;
        TextView tvDistance;
        TextView tvPhoneNum;
        TextView tvAddress;
        RatingBar starRating;

        public MyViewHolder(@NonNull View view) {
            super(view);
            ivBrandImg = view.findViewById(R.id.ivImage);
            tvBrandName = view.findViewById(R.id.tvBrandName);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvDistance = view.findViewById(R.id.tvDesc);
            tvPhoneNum = view.findViewById(R.id.tvPhoneNum);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvAddress.setSelected(true);
            starRating = view.findViewById(R.id.starRating);
        }
    }

    //get, set
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<RestaurantData> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<RestaurantData> arrayList) {
        this.arrayList = arrayList;
    }
}
