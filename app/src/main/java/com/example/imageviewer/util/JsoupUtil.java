package com.example.imageviewer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.example.imageviewer.bean.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class JsoupUtil {

    public List<Image> DownloadPic(String url) throws IOException {
        Log.i("TAG","DownloadPic    "+"进入jsoup");
        Document document = null;
        List<Image> imageList=new ArrayList<>();
        try {
            Log.i("TAG","onCreate    "+"url"+url);
            document = Jsoup.connect(url)
                    .timeout(5000)
                    .userAgent("User-Agent : Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36Edge/13.10586")
                    .get();
            Log.i("TAG","onCreate    "+"获取连接"+document);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        if(document==null){
           return null;
        }else{
            Elements pngs = document.select("img[src~=(?i)\\.(png|jpe?g)]");
            for(Element element :pngs){
                //获取图片url，存到Image.location属性中，后续用Glide加载
                String src=element.attr("src");
                String imageName=src.substring(src.lastIndexOf("/")+1,src.length());
                Image image=new Image();
                image.setLocation(src);
                image.setName(imageName);
                imageList.add(image);
            }
            return imageList;
        }
    }

}
