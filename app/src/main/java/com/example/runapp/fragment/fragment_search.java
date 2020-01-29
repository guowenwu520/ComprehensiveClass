package com.example.runapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.MainHome;
import com.example.runapp.R;
import com.example.runapp.Sports_Details_Activity;
import com.example.runapp.find_Run_Activity;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.util.ArrayList;

import static com.example.runapp.Data.DataAccess.URL_IMG_ACCESS;

/**
 * Created by 18179 on 2020/1/14.
 */

public class fragment_search extends Fragment {
    RecyclerView recyclerView;
    EditText editserch;
    ImageView search,kong;
    ArrayList<SportsDetail> data=new ArrayList<>();
    MyRecycleViewClassAdapter myRecycleViewClassAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);
        MainHome.isShowTop(true);
        recyclerView=view.findViewById(R.id.search_recycle);
        editserch=view.findViewById(R.id.editserch);
        search=view.findViewById(R.id.searchimg);
        kong=view.findViewById(R.id.kong);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strserch=editserch.getText().toString().trim();
                if(!strserch.equals("")){
                    getData(strserch);
                }else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    kong.setVisibility(View.VISIBLE);
                    Common_Uitl.showToast(getContext(),"请输入");
                }
            }
        });
        recyclerView.setVisibility(View.INVISIBLE);
        kong.setVisibility(View.VISIBLE);
        myRecycleViewClassAdapter=new MyRecycleViewClassAdapter();
        recyclerView.setAdapter(myRecycleViewClassAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycleViewClassAdapter.setOnClisterItem(new OnClisterItem() {
            @Override
            public void onItemLister(View.OnClickListener onClickListener, int postion) {
                Intent intent=new Intent(getActivity(), find_Run_Activity.class);
                Singleton.getInstance().setSportsDetail(data.get(postion));
                startActivity(intent);
            }
        });
        return view;
    }

    public void getData(final String str) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                data= DataAccess.getSprtsDetailLike(str);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(data.size()<=0){
                    recyclerView.setVisibility(View.INVISIBLE);
                    kong.setVisibility(View.VISIBLE);
                    Common_Uitl.showToast(getContext(),"为搜索到相关内容");
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    kong.setVisibility(View.INVISIBLE);
                    myRecycleViewClassAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    //适配器
    class  MyRecycleViewClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        OnClisterItem onClisterItem;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.sports_details_view_item,parent,false);
            return new MyRecycleViewClassAdapter.myViewHolderClass(view,onClisterItem);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SportsDetail runClass=data.get(position);
            MyRecycleViewClassAdapter.myViewHolderClass myViewHolderClass= (MyRecycleViewClassAdapter.myViewHolderClass) holder;
            Glide.with(getActivity()).load(URL_IMG_ACCESS+runClass.getStrings().get(0).getImgname()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(myViewHolderClass.head);
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
