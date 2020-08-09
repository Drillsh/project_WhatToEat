package kr.or.mrhi.android.whattoeat_project.function;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//RecyclerView아이템들의 구분선을 정하는 클래스
public class RecyclerDecoration extends RecyclerView.ItemDecoration {
     private int divHeight;

    public RecyclerDecoration(int divHeight) {
        this.divHeight = divHeight;
    }
    //아래쪽 사이즈 결정한다.
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
            outRect.bottom = divHeight;
        //출처: https://thepassion.tistory.com/298 [좋은향기's 프로그램 블로그]
    }
}
