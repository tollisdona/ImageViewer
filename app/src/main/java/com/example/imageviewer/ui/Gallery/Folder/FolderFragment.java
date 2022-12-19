package com.example.imageviewer.ui.Gallery.Folder;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.imageviewer.R;
import com.example.imageviewer.bean.ImageFolders;
import com.example.imageviewer.util.folderAdapter;
import com.example.imageviewer.ui.Gallery.Image.GalleryFragment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private List<ImageFolders> folders;
    private FragmentManager fragmentManager;

    public FolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getParentFragmentManager();
        folders=getImageFolders();
        //展示已获取的文件夹
        Log.i("Tag","onCreate:Info   "+folders);
        setHasOptionsMenu(true);
        System.out.println(folders);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder,container,false);

        ListView listView =view.findViewById(R.id.listview);
        folderAdapter adapter= new folderAdapter(this.getActivity(),folders);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //准备Fragment
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                GalleryFragment galleryFragment=new GalleryFragment();
                //传送数据
                String folder_name = adapter.getFolder(position).getName();
                String folder_path = adapter.getFolder(position).getDir();
                Bundle bundle = new Bundle();
                bundle.putString("name",folder_name);
                bundle.putString("path",folder_path);
                galleryFragment.setArguments(bundle);
                //切换fragment
                transaction.replace(R.id.nav_host_fragment_content_main,galleryFragment);
                transaction.commit();
            }
        });
        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.main,menu);
    }

    /**
     * 得到图片文件夹集合
     */
    public List<ImageFolders> getImageFolders() {
        List<ImageFolders> folders = new ArrayList<ImageFolders>();
        // 扫描图片
        Cursor c = null;

        try {
            /**
             * c 获取provider---imagee.media的所有内容，筛选条件为imagejpeg or imagepng
             * 循环c.moveToNext()
             * path 图片的path
             * 找到图片，查看有无父文件夹，没有则跳过一次循环，否则 dir 为父文件夹的绝对路径
             * mDirs 保存父文件夹的路径 添加父文件夹
             *
             */
            c = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            List<String> mDirs = new ArrayList<String>();//用于保存已经添加过的文件夹目录
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));// 路径
                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                    continue;
                String dir = parentFile.getAbsolutePath();
                if (mDirs.contains(dir))//如果已经添加过
                    continue;
                mDirs.add(dir);//添加到保存目录的集合中
                ImageFolders folderBean = new ImageFolders();
                folderBean.setDir(dir);
                folderBean.setFistImgPath(path);
                if (parentFile.list() == null)
                    continue;
                FilenameFilter filenameFilter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                };
//              int count =parentFile.list(filenameFilter).length; 可能会有空指针异常
//                返回一个字符串数组，字符串制定此抽象路径名表示的目录中的文件和目录
                int count = Objects.requireNonNull(parentFile.list(filenameFilter)).length;
                folderBean.setCount(count);
                folders.add(folderBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return folders;
    }

}