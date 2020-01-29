package com.example.runapp;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.entity.Collection;
import com.example.runapp.entity.Comment;
import com.example.runapp.entity.Imgs;
import com.example.runapp.entity.RunClass;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.fragment.Myfragment;
import com.example.runapp.fragment.fragment_home;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.runapp.Data.DataAccess.URL_IMG_ACCESS;
import static com.example.runapp.Data.DataAccess.URL_VEDIO_ACCESS;
import static com.example.runapp.fragment.fragment_home.adress;

public class find_Run_Activity extends AppCompatActivity {
    FrameLayout frameLayout;
    TextView title,jianjie,position,phone,buttonseed;
    ImageView cllone,phoneNo;
    RelativeLayout wenzhi;
    VideoView playvdeo;
    RecyclerView pinglun_recycle;
    SportsDetail sportsDetail;
    EditText editTextseed;
    MyRecycleViewClassAdapter myRecycleViewClassAdapter;
    Imgs imgs;
    //该用户是否收藏标记
    boolean fign=false;
    ArrayList<Comment> data=new ArrayList<>();
    int index=0;
   ArrayList<Imgs> strings=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__run_);
        title=findViewById(R.id.title);
        jianjie=findViewById(R.id.jianjie);
        position=findViewById(R.id.position);
        phoneNo=findViewById(R.id.phoneNo);
        frameLayout=findViewById(R.id.img_freament);
        phone=findViewById(R.id.phone);
        cllone=findViewById(R.id.cllone);
        wenzhi=findViewById(R.id.wenzhi);
        playvdeo=findViewById(R.id.playvdeo);
        pinglun_recycle=findViewById(R.id.pinrling_recycle);
        editTextseed=findViewById(R.id.editpinglun);
        buttonseed=findViewById(R.id.buttonseed);
        getData();
    }

    private void init() {

        title.setText(sportsDetail.getTitle());
        jianjie.setText(sportsDetail.getBriefIntroduction());
        position.setText(adress);
        phone.setText(sportsDetail.getTiem());
        phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + sportsDetail.getPhone());
                intent.setData(data);
                startActivity(intent);
            }
        });
        if(strings.size()>0){
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.img_freament,new Myfragment(URL_IMG_ACCESS+strings.get(index).getImgname())).commit();
        }
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            float x1,y1,x2,y2;
            boolean fg=false;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_DOWN:
                        fg=true;
                        x1=event.getX();
                        y1=event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2=event.getX();
                        y2=event.getY();
                        if(x2>x1&&x2-x1>=40&&fg){
                            if(index-1>=0) {
                                fg=false;
                                index--;
                                FragmentManager manager= getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.img_freament,new Myfragment(URL_IMG_ACCESS+strings.get(index).getImgname())).commit();
                            }

                        }else if(x2<x1&&x1-x2>=40&&fg){
                            if(index+1<strings.size()) {
                                index++;
                                fg=false;
                                FragmentManager manager= getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.img_freament,new Myfragment(URL_IMG_ACCESS+strings.get(index).getImgname())).commit();
                            }
                        }
                        break;
                }
                return true;
        }
                                       });
        buttonseed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seddsmg=editTextseed.getText().toString().trim();
                if(seddsmg.equals("")){
                    Common_Uitl.showToast(find_Run_Activity.this,"发送消息不能为空");
                }else {
                    final Comment comment=new Comment(Common_Uitl.getTimeId(),sportsDetail.getId(), Singleton.getInstance().getUser().getId(),seddsmg,new   SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), Singleton.getInstance().getUser().getMyname(), Singleton.getInstance().getUser().getUrlimg());
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            DataAccess.insertComment(comment);
                        }
                    }.start();
                    data.add(comment);
                    myRecycleViewClassAdapter.notifyDataSetChanged();
                }
            }
        });
        new AsyncTask<Void,Void,Void>(){
            String url="";
            @Override
            protected Void doInBackground(Void... voids) {
                String photoUrl = URL_VEDIO_ACCESS+imgs.getImgname();   //文件URL地址
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();     //保存目录
                url=saveUrlAs(photoUrl, filePath+"/mymp4");
                        return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("ererer",URL_VEDIO_ACCESS+imgs.getImgname()+"    "+url);
                playvdeo.setVideoURI(Uri.parse(url));
                playvdeo.requestFocus();
                playvdeo.start();
                playvdeo.setMediaController(new MediaController(playvdeo.getContext()));
            }
        }.execute();
           if(!fign){
            Glide.with(find_Run_Activity.this).load(R.drawable.love).into(cllone);
        }else{
            Glide.with(find_Run_Activity.this).load(R.drawable.loveed).into(cllone);
        }
        cllone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fign){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Collection collection=new Collection();
                            collection.setSportsid(sportsDetail.getId());
                            collection.setUserid(Singleton.getInstance().getUser().getId());
                            collection.setId(Common_Uitl.getTimeId());
                            DataAccess.addCollection(collection);
                        }
                    }.start();
                    Glide.with(find_Run_Activity.this).load(R.drawable.loveed).into(cllone);
                    fign=true;
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Collection collection=new Collection();
                            collection.setSportsid(sportsDetail.getId());
                            collection.setUserid(Singleton.getInstance().getUser().getId());
                            collection.setId(Common_Uitl.getTimeId());
                            DataAccess.remoteCollection(collection);
                        }
                    }.start();
                    Glide.with(find_Run_Activity.this).load(R.drawable.love).into(cllone);
                    fign=false;
                }
            }
        });
        wenzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(find_Run_Activity.this, Map_Activity.class);
                intent.putExtra("lat",sportsDetail.getLat());
                intent.putExtra("lon",sportsDetail.getLon());
                startActivity(intent);
            }
        });
        myRecycleViewClassAdapter=new MyRecycleViewClassAdapter();
        pinglun_recycle.setAdapter(myRecycleViewClassAdapter);
        pinglun_recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    public static String  saveUrlAs(String url,String filePath){
        //System.out.println("fileName---->"+filePath);
        //创建不同的文件夹目录
        File file=new File(filePath);
        //判断文件夹是否存在
        if (!file.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath+"/final.mp4");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return filePath+"/final.mp4";

    }
    public void Back(View view) {
        finish();
    }
    public void getData() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                sportsDetail=Singleton.getInstance().getSportsDetail();
                strings= DataAccess.getAssecceIMg(sportsDetail.getId());
                imgs=DataAccess.getAssecceVoide(sportsDetail.getId());
                data=DataAccess.getCommment(sportsDetail.getId());
                Collection collection=new Collection();
                collection.setSportsid(sportsDetail.getId());
                collection.setUserid(Singleton.getInstance().getUser().getId());
                collection.setId(Common_Uitl.getTimeId());
                fign=DataAccess.isCollent(collection);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                init();
            }
        }.execute();

    }

    //适配器
    class  MyRecycleViewClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        OnClisterItem onClisterItem;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(find_Run_Activity.this).inflate(R.layout.commt_view_item,parent,false);
            return new MyRecycleViewClassAdapter.myViewHolderClass(view,onClisterItem);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           Comment comment=data.get(position);
            MyRecycleViewClassAdapter.myViewHolderClass myViewHolderClass= (MyRecycleViewClassAdapter.myViewHolderClass) holder;
            myViewHolderClass.time.setText(comment.getTime());
            myViewHolderClass.msg.setText(comment.getMsg());
            myViewHolderClass.title.setText(comment.getNames());
            Glide.with(find_Run_Activity.this).load(URL_IMG_ACCESS+comment.getImgurl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(myViewHolderClass.head);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setOnClisterItem(OnClisterItem onClisterItem) {
            this.onClisterItem = onClisterItem;
        }

        class  myViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener{
            OnClisterItem onClisterItem;
            TextView title,msg,time;
            ImageView head;
            @Override
            public void onClick(View v) {
                onClisterItem.onItemLister(this,getPosition());
            }

            public myViewHolderClass(View itemView,OnClisterItem onClisterItem) {
                super(itemView);
                this.onClisterItem=onClisterItem;
            //    itemView.setOnClickListener(this);
                title=itemView.findViewById(R.id.title);
                msg=itemView.findViewById(R.id.msg);
                time=itemView.findViewById(R.id.time);
                head=itemView.findViewById(R.id.head);
            }
        }
    }
}

