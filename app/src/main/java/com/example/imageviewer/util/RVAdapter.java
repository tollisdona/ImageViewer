package com.example.imageviewer.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imageviewer.R;
import com.example.imageviewer.bean.Image;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVHolder> {

    public Context context;
    private List<Image> imageList;
    private ImageView imageView;

    public RVAdapter(Context context,List<Image> imageList){
        this.context=context;
        this.imageList=imageList;
    }

    /**
     * 这个自定义ViewHolder可以是外部类，但是为了方便，将其作为ChatAdaprer的内部类
     * 需要在自定义的ViewHolder中绑定视图文件
     */
    class RVHolder extends RecyclerView.ViewHolder{
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.SpiderImg);
        }
    }


    /**
     * 返回我们的内部类MyViewHolder ，此处为将我们的item布局文件和adapter绑定。
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.download_layout,parent,false);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, int position) {
        Log.d("TAG","Element"+position+"set.");
        Glide.with(context)
                .load(imageList.get(position).getLocation())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_failed)
//                .override(500,500)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }



}
