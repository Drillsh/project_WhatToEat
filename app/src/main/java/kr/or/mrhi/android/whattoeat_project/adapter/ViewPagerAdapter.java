package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

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
        return super.instantiateItem(container, position);
    }
    //Adapter가 관리하는 데이터 리스트의 총개수
    @Override
    public int getCount() {
        return 0;
    }
    //페이지가 특정 키와 연관되는지 체크
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
    //Adapter가 관리하는 데이터 리스트에서 인자로 넘어온 position에 해당하는
    //데이터 항목을 생성된 페이지를 제거
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
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
