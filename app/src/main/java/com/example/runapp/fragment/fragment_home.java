package com.example.runapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Add_Run_Dialog_Activity;
import com.example.runapp.CollAndRel_Actvity;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.MainHome;
import com.example.runapp.Map_Activity;
import com.example.runapp.R;
import com.example.runapp.Sports_Details_Activity;
import com.example.runapp.entity.RunClass;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.util.ArrayList;

/**
 * Created by 18179 on 2020/1/14.
 */

public class fragment_home extends Fragment {
    RelativeLayout relativeLayout;
    TextView postion;
    RecyclerView recyclerView;
    ImageView addImage;
   public  static  double latv,lonv;
   public static String adress;
   ArrayList<RunClass> data=new ArrayList<>();
   ArrayList<SportsDetail> sportsDetails=new ArrayList<>();
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_home,container,false);
         relativeLayout=view.findViewById(R.id.wenzhi);
         recyclerView=view.findViewById(R.id.recycle_runclass);
         postion=view.findViewById(R.id.position);
         addImage=view.findViewById(R.id.add_image);
         getData();
         initPostion();
        return view;
    }

    private void initPostion() {
        MainHome.isShowTop(true);
//初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
//异步获取定位结果
        final AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                   Log.e("er",amapLocation.getErrorInfo());
                    if (amapLocation.getErrorCode() == 0) {
                        postion.setText(amapLocation.getAddress());
                        adress=amapLocation.getAddress();
                        latv=amapLocation.getLatitude();
                        lonv=amapLocation.getLongitude();
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
        //获取位置信息
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Map_Activity.class);
                intent.putExtra("lat",latv);
                intent.putExtra("lon",lonv);
                startActivity(intent);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Add_Run_Dialog_Activity.class);
                startActivity(intent);
            }
        });
    }

    public void getData() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
               data= DataAccess.getRunClass();
                sportsDetails=DataAccess.getSprtsDetail();
                for (int j=0;j<data.size();j++) {
                    int k=0;
                    for (int i = 0; i < sportsDetails.size(); i++) {
                        if (sportsDetails.get(i).getClassid().equals(data.get(j).getId())) {
                         k++;
                        }
                    }
                    data.get(j).setClassnumber(k+"");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //渲染数据
                MyRecycleViewClassAdapter myRecycleViewClassAdapter=new MyRecycleViewClassAdapter();
                recyclerView.setAdapter(myRecycleViewClassAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                Singleton.getInstance().setSportsDetails(sportsDetails);
                myRecycleViewClassAdapter.setOnClisterItem(new OnClisterItem() {
                    @Override
                    public void onItemLister(View.OnClickListener onClickListener, int postion) {

                        Intent intent=new Intent(getActivity(), Sports_Details_Activity.class);
                        intent.putExtra("id",data.get(postion).getId());
                        intent.putExtra("name",data.get(postion).getClasss());
                        startActivity(intent);
                    }
                });
            }
        }.execute();

        Glide.with(getActivity()).load(R.drawable.addimg).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(addImage);
    }

    //适配器
    class  MyRecycleViewClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        OnClisterItem onClisterItem;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getContext()).inflate(R.layout.yunapp_class_view,parent,false);
            return new myViewHolderClass(view,onClisterItem);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RunClass runClass=data.get(position);
            myViewHolderClass myViewHolderClass= (MyRecycleViewClassAdapter.myViewHolderClass) holder;
            myViewHolderClass.classnumber.setText("活动数:"+runClass.getClassnumber());
            myViewHolderClass.classname.setText(runClass.getClassname());
            myViewHolderClass.classs.setText(runClass.getClasss());
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
            TextView classs,classname,classnumber;
            @Override
            public void onClick(View v) {
                onClisterItem.onItemLister(this,getPosition());
            }

            public myViewHolderClass(View itemView,OnClisterItem onClisterItem) {
                super(itemView);
                this.onClisterItem=onClisterItem;
                itemView.setOnClickListener(this);
                classs=itemView.findViewById(R.id.classs);
                classname=itemView.findViewById(R.id.classtitle);
                classnumber=itemView.findViewById(R.id.classtiom);
            }
        }
    }
}
