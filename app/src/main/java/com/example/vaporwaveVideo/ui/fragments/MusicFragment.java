package com.example.vaporwaveVideo.ui.fragments;

import android.content.Intent;
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

import static com.example.vaporwaveVideo.adapter.EditMenuSelectAdpater.MUSIC_MENU;
import static com.example.vaporwaveVideo.ui.VideoEditActivity.TYPE_MUSIC;

public class MusicFragment  extends Fragment implements OnRecycleListerCall {
    ArrayList<EditInfoEdtity> mDatas=new ArrayList<>();
    RecyclerView mMusicRecyclerView;
    EditMenuSelectAdpater editMenuSelectAdpater;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_music,container,false);
        initView(view);
        initData();
        initShow();
        return view;
    }

    private void initShow() {
       editMenuSelectAdpater=new EditMenuSelectAdpater(MUSIC_MENU,getActivity(),R.layout.item_edit_action_select_music_view,mDatas);
        mMusicRecyclerView.setAdapter(editMenuSelectAdpater);
        editMenuSelectAdpater.setOnRecycleListerCall(this);
       LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
       linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mMusicRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initData() {
        mDatas.clear();
        mDatas.add(new EditInfoEdtity());
        mDatas.add(new EditInfoEdtity());
    }

    public void setData(ArrayList<EditInfoEdtity> mDatas){
            editMenuSelectAdpater.setDataRefresh(mDatas);
    }

    private void initView(View view) {
        mMusicRecyclerView=view.findViewById(R.id.music_recycleview);
    }

    @Override
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas) {

    }

    @Override
    public void onClickItem(View.OnClickListener onClickListener, int postion) {
         if(postion==1){
             ((VideoEditActivity)getActivity()).stopPlay();
             Intent intent = new Intent();
              intent.setType("audio/*"); //选择音频
             intent.setAction(Intent.ACTION_GET_CONTENT);
             /* 取得相片后返回本画面 */
             getActivity().startActivityForResult(intent, TYPE_MUSIC);
         }
    }
}
