package com.example.runapp;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.runapp.entity.Comment;
import com.example.runapp.entity.RunClass;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.fragment.Myfragment;
import com.example.runapp.fragment.fragment_home;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class find_Run_Activity extends AppCompatActivity {
    FrameLayout frameLayout;
    TextView title,jianjie,position,phone,buttonseed;
    ImageView cllone;
    RelativeLayout wenzhi;
    VideoView playvdeo;
    RecyclerView pinglun_recycle;
    SportsDetail sportsDetail;
    EditText editTextseed;
    MyRecycleViewClassAdapter myRecycleViewClassAdapter;
    //该用户是否收藏标记
    boolean fign=false;
    ArrayList<Comment> data=new ArrayList<>();
    int index=0;
    String [] strings=new String[]{
            "http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3105600238,693167464&fm=26&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=3922344982,423380743&fm=26&gp=0.jpg",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__run_);
        getData();
        title=findViewById(R.id.title);
        jianjie=findViewById(R.id.jianjie);
        position=findViewById(R.id.phone);
        frameLayout=findViewById(R.id.img_freament);
        phone=findViewById(R.id.phone);
        cllone=findViewById(R.id.cllone);
        wenzhi=findViewById(R.id.wenzhi);
        playvdeo=findViewById(R.id.playvdeo);
        pinglun_recycle=findViewById(R.id.pinrling_recycle);
        editTextseed=findViewById(R.id.editpinglun);
        buttonseed=findViewById(R.id.buttonseed);
        init();
    }

    private void init() {
        title.setText(sportsDetail.getTitle());
        jianjie.setText(sportsDetail.getBriefIntroduction());
        position.setText(sportsDetail.getAddress());
        phone.setText(sportsDetail.getPhone());
        if(strings.length>0){
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.img_freament,new Myfragment(strings[index])).commit();
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
                                manager.beginTransaction().replace(R.id.img_freament,new Myfragment(strings[index])).commit();
                            }

                        }else if(x2<x1&&x1-x2>=40&&fg){
                            if(index+1<strings.length) {
                                index++;
                                fg=false;
                                FragmentManager manager= getSupportFragmentManager();
                                manager.beginTransaction().replace(R.id.img_freament,new Myfragment(strings[index])).commit();
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
                    data.add(new Comment(Common_Uitl.getTimeId(),sportsDetail.getId(), Singleton.getInstance().getUser().getId(),seddsmg,new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), Singleton.getInstance().getUser().getName(), Singleton.getInstance().getUser().getUrlimg()));
                    myRecycleViewClassAdapter.notifyDataSetChanged();
                }
            }
        });
        playvdeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playvdeo.setVideoURI(Uri.parse(sportsDetail.getVideourl()));
            }
        });
        if(!fign){
            Glide.with(find_Run_Activity.this).load(R.drawable.love).into(cllone);
        }else{
            Glide.with(find_Run_Activity.this).load(R.drawable.loveed).into(cllone);
        }
        cllone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fign){
                    Glide.with(find_Run_Activity.this).load(R.drawable.love).into(cllone);
                    fign=false;
                }else{
                    Glide.with(find_Run_Activity.this).load(R.drawable.loveed).into(cllone);
                    fign=true;
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

    public void Back(View view) {
        finish();
    }
    public void getData() {
       sportsDetail=new SportsDetail("23","23","34","ad","sd",new String[]{"data:image/jpeg;base64,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"},"23","哈哈哈","dsds","dsds","sdsd");
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
            myViewHolderClass.title.setText(comment.getName());
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

