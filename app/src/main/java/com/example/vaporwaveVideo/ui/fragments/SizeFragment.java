package com.example.vaporwaveVideo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater;
import com.example.vaporwaveVideo.entity.EditInfoEdtity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.ui.VideoEditActivity;

import java.util.ArrayList;

import static com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater.SIZE_MENU;

public class SizeFragment extends Fragment implements OnRecycleListerCall {
    ArrayList<EditInfoEdtity> mDatas = new ArrayList<>();
    RecyclerView mSizeRecycleView;
    EditMenuSelectAdpater editMenuSelectAdpater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_size, container, false);
        initView(view);
        initData();
        initShow();
        return view;
    }

    private void initShow() {
        editMenuSelectAdpater = new EditMenuSelectAdpater(SIZE_MENU, getActivity(), R.layout.item_edit_action_select_size_view, mDatas);
        mSizeRecycleView.setAdapter(editMenuSelectAdpater);
        editMenuSelectAdpater.setOnRecycleListerCall(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mSizeRecycleView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        mDatas.clear();
       EditInfoEdtity editInfoEdtity =new EditInfoEdtity(1, 1);
       editInfoEdtity.setSizeIsSelect(true);
        mDatas.add(editInfoEdtity);
        mDatas.add(new EditInfoEdtity(3, 4));
        mDatas.add(new EditInfoEdtity(4, 3));
        mDatas.add(new EditInfoEdtity(9, 16));
        mDatas.add(new EditInfoEdtity(16, 9));
    }

    private void initView(View view) {
        mSizeRecycleView = view.findViewById(R.id.size_recycleview);
    }

    @Override
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas) {

    }

    @Override
    public void onClickItem(View.OnClickListener onClickListener, int postion) {
        for (int i = 0; i < mDatas.size(); i++) {
            if(i==postion) {
                mDatas.get(i).setSizeIsSelect(true);
                ((VideoEditActivity)getActivity()).videoAddSize(mDatas.get(postion));
            }else {
                mDatas.get(i).setSizeIsSelect(false);
            }

        }
        editMenuSelectAdpater.setDataRefresh(mDatas);
    }
}
