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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.entity.Imgs;
import com.example.runapp.entity.RunClass;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.fragment.fragment_home;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.util.ArrayList;

import static com.example.runapp.Data.DataAccess.URL_IMG_ACCESS;

public class Sports_Details_Activity extends AppCompatActivity {
    TextView topclass;
    RecyclerView recyclerView;
    ImageView addimg;
    String id,name;
    ArrayList<Imgs> strings=new ArrayList<>();
    ArrayList<SportsDetail> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports__details);
        topclass=findViewById(R.id.topclass);
        recyclerView=findViewById(R.id.recycle_runclass_detailes);
        addimg=findViewById(R.id.add_image);
        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        getData();

    }

    private void init() {
        topclass.setText(name);
        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sports_Details_Activity.this,Add_Run_Dialog_Activity.class);
                startActivity(intent);
            }
        });
        MyRecycleViewClassAdapter myRecycleViewClassAdapter=new MyRecycleViewClassAdapter();
        recyclerView.setAdapter(myRecycleViewClassAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecycleViewClassAdapter.setOnClisterItem(new OnClisterItem() {
            @Override
            public void onItemLister(View.OnClickListener onClickListener, int postion) {
                Intent intent=new Intent(Sports_Details_Activity.this, find_Run_Activity.class);
                Singleton.getInstance().setSportsDetail(data.get(postion));
                startActivity(intent);
            }
        });
    }
    public void Back(View view) {
        finish();
    }

    public void getData() {
        final ArrayList<SportsDetail> detailArrayList=new ArrayList<>();
        new AsyncTask<Void,Void,Void>(){
                     @Override
                     protected void onPostExecute(Void aVoid) {
                         super.onPostExecute(aVoid);
                         data=detailArrayList;
                         init();
                     }

                     @Override
                     protected Void doInBackground(Void... voids) {
                         ArrayList<SportsDetail> detailArrayAll=Singleton.getInstance().getSportsDetails();
                         for(int i=0;i<detailArrayAll.size();i++){
                             if(detailArrayAll.get(i).getClassid().equals(id)){
                         strings= DataAccess.getAssecceIMg( detailArrayAll.get(i).getId());
                                 detailArrayAll.get(i).setStrings(strings);
                                 detailArrayList.add(detailArrayAll.get(i));
                             }
                         }
                         return  null;
                     }
                 }.execute();





    }



    //适配器
    class  MyRecycleViewClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        OnClisterItem onClisterItem;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(Sports_Details_Activity.this).inflate(R.layout.sports_details_view_item,parent,false);
            return new MyRecycleViewClassAdapter.myViewHolderClass(view,onClisterItem);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SportsDetail runClass=data.get(position);
            MyRecycleViewClassAdapter.myViewHolderClass myViewHolderClass= (MyRecycleViewClassAdapter.myViewHolderClass) holder;
            Glide.with(Sports_Details_Activity.this).load(URL_IMG_ACCESS+runClass.getStrings().get(0).getImgname()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(myViewHolderClass.head);
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
