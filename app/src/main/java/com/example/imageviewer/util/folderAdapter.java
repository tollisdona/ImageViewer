package com.example.imageviewer.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.imageviewer.R;
import com.example.imageviewer.bean.ImageFolders;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class folderAdapter extends BaseAdapter {
    private List<ImageFolders> folders;
    private Context context;

    //获取参数
    public folderAdapter(Context context,List<ImageFolders> folders) {
        this.folders = folders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        return folders.get(position);
    }

    public ImageFolders getFolder(int position){
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageFolders folder = folders.get(position);
        View view ;
        ViewHolder holder;
        if(convertView==null){
            view= LayoutInflater.from(context).inflate(R.layout.folder_item,null);
            /* 缓存控件对象*/
            holder = new ViewHolder();
            holder.img_folder=(ImageView)view.findViewById(R.id.img_folder);
            holder.tx_folder=(TextView) view.findViewById(R.id.tx_folder);
            holder.path_folder=(TextView) view.findViewById(R.id.path_folder);
            view.setTag(holder);
        }
        else{
            view=convertView;
            holder=(ViewHolder) view.getTag();
        }
        holder.img_folder.setImageResource(R.drawable.ic_baseline_folder_24);
        holder.tx_folder.setText(folder.getName());
        holder.path_folder.setText(folder.getDir());

        return view;
    }

    class ViewHolder{
        ImageView img_folder;
        TextView tx_folder;
        TextView path_folder;
    }
}
