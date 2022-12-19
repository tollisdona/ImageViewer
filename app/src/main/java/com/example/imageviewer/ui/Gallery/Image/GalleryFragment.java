package com.example.imageviewer.ui.Gallery.Image;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.imageviewer.util.ImageAdapter;
import com.example.imageviewer.R;
import com.example.imageviewer.bean.Image;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GalleryFragment extends Fragment {

    private String folder_name;
    private String folder_path;
    private List<Image> imageList = new ArrayList<>();
    public GalleryFragment() {
        // Required empty public constructor
    }
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        folder_name=bundle.getString("name");
        folder_path=bundle.getString("path");
        // 根据路径获取该路径下的所有文件夹（unused）
        List<String> imgpath= getImgListByDir(folder_path);
        Log.i("Tag","onCreate:Info   "+imgpath);
        // TODO: 获取imglist
        imageList = getImages(folder_path);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_gallery, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.girdView);
        gridView.setAdapter(new ImageAdapter(this.getActivity(),imageList));
        //点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = imageList.get(position);
                //获取图片bitmap
                bigImageLoader(image.getImage());
            }
        });
        return view;
    }


    public List<String> getImgListByDir(String dir) {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (directory == null || !directory.exists()) {
            Log.i("Tag","getImgListByDir:Info    file为空");
            return imgPaths;
        }
        //返回一个抽象路径名数组，这些路径表示此抽象路径名表示的目录中的文件
        File[] files = directory.listFiles();
        for (File f : files) {
            System.out.println(f);
            String path = f.getAbsolutePath().toLowerCase();
            if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
                imgPaths.add(path);
            }
        }
        return imgPaths;
    }

    //查询图片信息
    private List<Image> getImages(String folder_path) {
        /* count变量用于控制显示出的图片数目，后续可实现分页加载 */
        int count = 0;
        imageList = new ArrayList();
        Context ctx = this.getActivity();
        Log.i("Tag","onCreate:Info   指定目录下的folder    "+folder_path);
        String selection = MediaStore.Images.Media.DATA + " like ?";
        String[] selectionArgs={folder_path.toLowerCase(Locale.ROOT)+"%"};
        Log.i("Tag","onCreate:Info   指定目录下的folder小写    "+folder_path);

        @SuppressLint("Recycle") Cursor cursor = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, selection, selectionArgs, null);
        Log.i("Tag","onCreate:Info   是否查询成功：  "+  cursor);
        while (cursor.moveToNext()) {
            //获取图片的名称
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
            Log.d("ImgActivity: ", "initImages: " + "imageName: " + name);

            //获取图片的路径
            byte[] data = cursor.getBlob(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            String location = new String(data, 0, data.length - 1);
            Log.d("ImgActivity: ", "initImages: " + "imageLocation: " + location);
            //根据路径获取图片
            Bitmap bm = getImgBitFromPath(location);

            //获取图片的详细信息
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION));
            Log.d("ImgActivity", "initImages: " + "ImageDesc: " + desc);

            //获取图片修改时间
            String time = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
            Log.i("TAG","onCreate:INFO    modified_time    "+time);
            Image image = new Image(bm, name, location);
            imageList.add(image);

            count++;
            if (count >= 100) break;
        }
        Log.d("ImgActivity: ", "initImage: " + "imageList.size: " + imageList.size());
        return imageList;
    }

    //根据路径获取bitmap
    private Bitmap getImgBitFromPath(String path) {
        Bitmap bm = null;
        File file = new File(path);

        if (file.exists()) {
            bm = BitmapFactory.decodeFile(path);
        } else {
            Toast toast = Toast.makeText(this.getActivity(), "该图片不存在！", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("ImgActivity ", "getImgFromDesc: 该图片不存在！");
        }
        return bm;
    }
    //方法里直接实例化一个imageView不用xml文件，传入bitmap设置图片
    private void bigImageLoader(Bitmap bitmap) {
        Context context = this.getActivity();
        final Dialog dialog = new Dialog(context);
        PhotoView image = new PhotoView(context);
        image.setImageBitmap(bitmap);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setOnMatrixChangeListener(rect -> Log.e("Matrix", rect.toString()));
        dialog.setContentView(image);
        //将dialog周围的白块设置为透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //显示
        dialog.show();
        //点击图片取消
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

}
