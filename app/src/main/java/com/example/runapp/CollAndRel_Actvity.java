package com.example.runapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.entity.User;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.util.ArrayList;

import static com.example.runapp.Data.DataAccess.URL_IMG_ACCESS;

public class CollAndRel_Actvity extends AppCompatActivity {
   private  int index;
   ImageView addimg;
   TextView top;
   RecyclerView recyclerView;
    ArrayList<SportsDetail> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coll_and_rel);
        index=getIntent().getIntExtra("index",0);
        addimg=findViewById(R.id.add_image);
        top=findViewById(R.id.top);
        recyclerView=findViewById(R.id.recyclle_details);
        getData();

    }
    public void getData() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                User user=Singleton.getInstance().getUser();
                if(index==1){
                       data=DataAccess.getCollteions(user.getId());
                }else{
                     data= DataAccess.getSprtsDetailUser(user.getId());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                init();
            }
        }.execute();

    }

    private void init() {
        //收藏
         if(index==1){
             addimg.setVisibility(View.INVISIBLE);
             top.setText(getResources().getString(R.string.mycollection));
             //发布
         }else{
             addimg.setVisibility(View.VISIBLE);
             top.setText(getResources().getString(R.string.myrelease));
         }
         addimg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               Intent intent=new Intent(CollAndRel_Actvity.this,Add_Run_Dialog_Activity.class);
               startActivity(intent);
             }
         });
        CollAndRel_Actvity.MyRecycleViewClassAdapter myRecycleViewClassAdapter=new CollAndRel_Actvity.MyRecycleViewClassAdapter();
        recyclerView.setAdapter(myRecycleViewClassAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecycleViewClassAdapter.setOnClisterItem(new OnClisterItem() {
            @Override
            public void onItemLister(View.OnClickListener onClickListener, int postion) {
                Intent intent=new Intent(CollAndRel_Actvity.this, find_Run_Activity.class);
                Singleton.getInstance().setSportsDetail(data.get(postion));
                startActivity(intent);
            }
        });
    }

    public void Back(View view) {
        finish();
    }
    //适配器
    class  MyRecycleViewClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        OnClisterItem onClisterItem;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(CollAndRel_Actvity.this).inflate(R.layout.sports_details_view_item,parent,false);
            return new CollAndRel_Actvity.MyRecycleViewClassAdapter.myViewHolderClass(view,onClisterItem);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SportsDetail runClass=data.get(position);
            CollAndRel_Actvity.MyRecycleViewClassAdapter.myViewHolderClass myViewHolderClass= (CollAndRel_Actvity.MyRecycleViewClassAdapter.myViewHolderClass) holder;
            Glide.with(CollAndRel_Actvity.this).load(URL_IMG_ACCESS+runClass.getStrings().get(0).getImgname()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(myViewHolderClass.head);
            myViewHolderClass.title.setText(runClass.getTitle());
            myViewHolderClass.briefIntroduction.setText(runClass.getBriefIntroduction());
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
            TextView briefIntroduction,title;
            ImageView head;
            @Override
            public void onClick(View v) {
                onClisterItem.onItemLister(this,getPosition());
            }

            public myViewHolderClass(View itemView,OnClisterItem onClisterItem) {
                super(itemView);
                this.onClisterItem=onClisterItem;
                itemView.setOnClickListener(this);
                briefIntroduction=itemView.findViewById(R.id.briefIntroduction);
                title=itemView.findViewById(R.id.title);
                head=itemView.findViewById(R.id.img);
            }
        }
    }
}
