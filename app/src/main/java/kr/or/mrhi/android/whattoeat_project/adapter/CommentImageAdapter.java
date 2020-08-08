package kr.or.mrhi.android.whattoeat_project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.model.CommentData;

public class CommentImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CommentData> comImageList = new ArrayList<CommentData>();

    public CommentImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return comImageList.size();
    }

    @Override
    public Object getItem(int i) {
        return comImageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.comment_image,null);
        }
        ImageView ivCommentImage = convertView.findViewById(R.id.ivCommentImage);

        CommentData commentData = comImageList.get(i);
        String cImage = commentData.getImgPath();

        //ivCommentImage.setImageBitmap();

        return convertView;
    }
}
