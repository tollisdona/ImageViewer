package com.example.imageviewer.ui.Spider;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imageviewer.R;
import com.example.imageviewer.bean.Image;
import com.example.imageviewer.util.JsoupUtil;
import com.example.imageviewer.util.RVAdapter;

import java.io.IOException;
import java.util.List;

public class SpiderFragment extends Fragment {

    // TODO: Rename and change types of parameters
    //全局变量存储imageList
    private List<Image> imageList;
    private RecyclerView recyclerView;

    private Handler handler = new Handler(Looper.getMainLooper()){
        public void handleMessage(@NonNull Message msg){
            switch (msg.what) {
                case 1:
                    imageList = (List<Image>) msg.obj;
                    //打印日志，检查图片信息是否正常
                    Log.i("TAG", "返回的imglist: " +imageList);
                    if (imageList==null){
                        Log.i("TAG", "JsoupUtil:   未请求到图片");
                        Toast.makeText(getActivity(), "未请求到图片", Toast.LENGTH_SHORT).show();
                    }else{
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(new RVAdapter(getActivity(),imageList));
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        EditText editText = view.findViewById(R.id.input_url);
        Button button = view.findViewById(R.id.btn_start);
        recyclerView=view.findViewById(R.id.v_recycle);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = String.valueOf(editText.getText());
                //域名检验
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(getContext(),"URL 不能为空~", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.WEB_URL.matcher(url.toString()).matches()) {
                    Toast.makeText(getContext(),"URL 非法，请输入有效的URL链接:"+url, Toast.LENGTH_SHORT).show();
                } else{
                    if(imageList!=null){
                        imageList.clear();
                    }
                    sendRequestWithHttpURLConnection(url);
                }
            }
        });
        return view;
    }

    private void sendRequestWithHttpURLConnection(String url) {
        new Thread(new Runnable(){
            @Override
            public void run(){
                List<Image> list;
                try {
                    list=new JsoupUtil().DownloadPic(url);
                        Log.i("TAG", "JsoupUtil:   已经获取");
                        Message message = handler.obtainMessage();
                        message.what=1;
                        message.obj=list;
                        handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}