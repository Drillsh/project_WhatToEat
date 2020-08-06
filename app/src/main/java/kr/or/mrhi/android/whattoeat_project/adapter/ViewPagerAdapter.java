package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;
//뷰페이저 어댑터 클래스
public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<RestaurantData> arrayList;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }
    //데이터 리스트에서 인자로 넘어온 position에 해당하는 아이템 항목에 대한 페이지를 생성
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.main_item,null);

        ImageView ivBrandImg = view.findViewById(R.id.ivImage);
        TextView tvBrandName = view.findViewById(R.id.tvBrandName);
        TextView tvCategory = view.findViewById(R.id.tvCategory);
        TextView tvDistance = view.findViewById(R.id.tvDesc);
        TextView tvPhoneNum = view.findViewById(R.id.tvPhoneNum);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        tvAddress.setSelected(true);
        RatingBar starRating = view.findViewById(R.id.starRating);

        RestaurantData restaurantData = arrayList.get(position);
        //ivBrandImg;
        tvBrandName.setText(restaurantData.getBrandName());
        tvCategory.setText(restaurantData.getCategory());
        tvDistance.setText(String.valueOf(restaurantData.getDistance()));
        tvPhoneNum.setText(restaurantData.getPhoneNum());
        tvAddress.setText(restaurantData.getAddress());
        starRating.setRating(restaurantData.getStarRating());

        container.addView(view);

        return view;
    }
    //Adapter가 관리하는 데이터 리스트의 총개수
    @Override
    public int getCount() {
        return arrayList.size();
    }
    //페이지가 특정 키와 연관되는지 체크
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
    //Adapter가 관리하는 데이터 리스트에서 인자로 넘어온 position에 해당하는
    //데이터 항목을 생성된 페이지를 제거
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
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
