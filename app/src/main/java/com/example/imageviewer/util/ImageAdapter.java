package com.example.imageviewer.util;

import androidx.appcompat.app.AppCompatActivity;
import com.example.imageviewer.bean.Image;
import android.content.Context;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建一个图像适配器继承于BaseAdapter适配器，并重写方法
 *
 * */
public class ImageAdapter extends BaseAdapter {
    //定义私有类型的上下文对象
    private Context mContext;
    //定义一个数组用来接收存放图片的数组传值
//    private int[] picture;
    private List picture=new ArrayList<>();
    public ImageAdapter(Context c,List list){
        mContext=c;
        picture=list;
    }
    @Override
    public int getCount() {
        return picture.size();//返回图片数组的长度，即有多少张图片
    }

    @Override
    public Object getItem(int position) {
        return picture.get(position); //获取相应position的item
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position
     * @param convertView 缓存被滚动到界面之外的项目
     * @param parent 父本，存放被加载出来的每一个项目视图
     * @return 返回view
     */
    @Override
    public View getView(int position, View convertView , ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(350,360));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//按比例扩大图片的size，居中显示
        }else{
            imageView=(ImageView) convertView;
        }
        Image bean= (Image) picture.get(position);
        imageView.setImageBitmap(bean.getImage());
//        imageView.setImageResource(bean.getLocation());
        return imageView;
    }

}


