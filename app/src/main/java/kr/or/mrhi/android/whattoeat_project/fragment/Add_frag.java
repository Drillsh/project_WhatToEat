package kr.or.mrhi.android.whattoeat_project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapView;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;

public class Add_frag extends Fragment {

    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_add, container, false);

        MapView mapView = new MapView(mainActivity);
        LinearLayout map_view= view.findViewById(R.id.mapView);
        map_view.addView(mapView);

        return view;
    }
}
