package com.example.vaporwaveVideo.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater;
import com.example.vaporwaveVideo.entity.EditInfoEdtity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.filter.MagicFilterType;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.ui.VideoEditActivity;

import java.util.ArrayList;

import static com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater.FILTER_MENU;

public class FilterFragment extends Fragment implements OnRecycleListerCall {
   private RecyclerView mFilterRecycleView;
   private ArrayList<EditInfoEdtity> mData=new ArrayList<>();
   private String sourceImg;

    public FilterFragment(String filterThumbnail) {
        this.sourceImg = filterThumbnail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_filter,container,false);
        initView(view);
        initData();
        initShow();
        return view;
    }

    private void initShow() {
        EditMenuSelectAdpater editMenuSelectAdpater=new EditMenuSelectAdpater(FILTER_MENU,getActivity(),R.layout.item_edit_action_select_filter_view,mData);
        mFilterRecycleView.setAdapter(editMenuSelectAdpater);
        editMenuSelectAdpater.setOnRecycleListerCall(this);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mFilterRecycleView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        String filterThumbnail=sourceImg;
        mData.clear();
        mData.add(new EditInfoEdtity(0,"None",filterThumbnail,getResources().getColor(R.color.none)));
        mData.add(new EditInfoEdtity(1,"Warn",filterThumbnail,getResources().getColor(R.color.warn)));
        mData.add(new EditInfoEdtity(2,"Antique",filterThumbnail,getResources().getColor(R.color.antique)));
        mData.add(new EditInfoEdtity(3,"Inkwell",filterThumbnail,getResources().getColor(R.color.inkwell)));
         mData.add(new EditInfoEdtity(4,"Brannan",filterThumbnail,getResources().getColor(R.color.brannan)));
        mData.add(new EditInfoEdtity(5,"N1977",filterThumbnail,getResources().getColor(R.color.n1977)));
        mData.add(new EditInfoEdtity(6,"Freud",filterThumbnail,getResources().getColor(R.color.freud)));
        mData.add(new EditInfoEdtity(7,"Hefe",filterThumbnail,getResources().getColor(R.color.hefe)));
        mData.add(new EditInfoEdtity(8,"Hudson",filterThumbnail,getResources().getColor(R.color.hudson)));
        mData.add(new EditInfoEdtity(9,"Nashville",filterThumbnail,getResources().getColor(R.color.nashville)));
        mData.add(new EditInfoEdtity(10,"Cool",filterThumbnail,getResources().getColor(R.color.cool)));
    }

    private void initView(View view) {
        mFilterRecycleView=view.findViewById(R.id.filter_recycleview);
    }

    @Override
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClickItem(View.OnClickListener onClickListener, int postion) {
        ((VideoEditActivity)getActivity()).videoAddFilter(mData.get(postion));
    }
}
