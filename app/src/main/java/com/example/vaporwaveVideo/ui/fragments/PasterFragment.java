package com.example.vaporwaveVideo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater;
import com.example.vaporwaveVideo.entity.EditInfoEdtity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.ui.VideoEditActivity;

import java.util.ArrayList;

import static com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater.PASTER_MENU;

public class PasterFragment extends Fragment implements OnRecycleListerCall {
    ArrayList<EditInfoEdtity> mDatas=new ArrayList<>();
    RecyclerView mPasterRecycleView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_paster,container,false);
        initView(view);
        initData();
        initShow();
        return view;
    }

    private void initShow() {
        EditMenuSelectAdpater editMenuSelectAdpater=new EditMenuSelectAdpater(PASTER_MENU,getActivity(),R.layout.item_edit_action_select_paster_view,mDatas);
        mPasterRecycleView.setAdapter(editMenuSelectAdpater);
        editMenuSelectAdpater.setOnRecycleListerCall(this);
        mPasterRecycleView.setLayoutManager(new GridLayoutManager(getContext(),4));
    }

    private void initData() {
        mDatas.clear();
        for (int i=1;i<=62;i++){
            mDatas.add(new EditInfoEdtity(getActivity().getResources().getIdentifier("paster"+i,"drawable",getActivity().getPackageName())));
        }
    }

    private void initView(View view) {
        mPasterRecycleView=view.findViewById(R.id.paster_recycleview);
    }

    @Override
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas) {

    }

    @Override
    public void onClickItem(View.OnClickListener onClickListener, int postion) {
        ((VideoEditActivity)getActivity()).videoAddPaster(mDatas.get(postion).getPasterId());
    }
}
