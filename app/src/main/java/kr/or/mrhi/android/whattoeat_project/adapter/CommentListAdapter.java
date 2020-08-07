package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.model.CommentData;

// 코멘트 데이터 어댑터
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<CommentData> commentList = new ArrayList<>();

//    private OnItemClickListener mListener = null;
    private OnLongClickListener mLongListener = null;

    //생성자
    public CommentListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CommentListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_list, viewGroup, false);
        CommentListAdapter.CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.CustomViewHolder customViewHolder, int position) {

        Bitmap bitmap = null;
        /*Uri uri1 = Uri.parse(commentList.get(position).getImgPath());

        try {
            InputStream in = context.getContentResolver().openInputStream(uri1);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        //지정폴더에서  path값으로 비트맵을 만든다.
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(commentList.get(position).getImgPath(),bfo);

        customViewHolder.tvDate.setText(commentList.get(position).getDate());
        customViewHolder.tvEditComment.setText(commentList.get(position).getComment());
        customViewHolder.rbListRating.setRating(commentList.get(position).getRating());
        customViewHolder.imageView2.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }


    // 리사이클러뷰 클릭 이벤트를 위한 인터페이스
    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    // 리사이클러뷰 롱클릭 이벤트 인터페이스
    public interface OnLongClickListener {
        void onItemLongClick(View view, int position);
    }

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {

    }
    // OnLongClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnLongClickListener(OnLongClickListener longListener) {
        this.mLongListener = longListener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvEditComment;
        TextView tvComment;
        ImageView imageView2;
        RatingBar rbListRating;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvEditComment = itemView.findViewById(R.id.tvEditComment);
            tvComment = itemView.findViewById(R.id.tvComment);
            imageView2 = itemView.findViewById(R.id.imageView2);
            rbListRating = itemView.findViewById(R.id.rbListRating);



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
//
//                        mListener.onItemClick(view, pos);
//                    }
//                }
//            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mLongListener.onItemLongClick(view,pos);
                    }
                    return true;
                }
            });
        }
    }

    public ArrayList<CommentData> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<CommentData> commentList) {
        this.commentList = commentList;
    }

//    public OnItemClickListener getmListener() {
//        return mListener;
//    }
//
//    public void setmListener(OnItemClickListener mListener) {
//        this.mListener = mListener;
//    }

}
