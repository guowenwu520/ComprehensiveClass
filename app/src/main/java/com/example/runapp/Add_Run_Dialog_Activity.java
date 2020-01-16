package com.example.runapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.util.FileUtilcll;
import com.example.runapp.util.OnClisterItem;
import com.example.runapp.util.Singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 18179 on 2020/1/16.
 */

public class Add_Run_Dialog_Activity   extends AppCompatActivity {
    EditText title,address,time,xiangx;
    TextView addvdeio;
    ImageView videoView;
    RecyclerView recyclerView;
    Spinner classs_ports;
    String videourl;
    String strclassport[] =new String[]{"乒乓球","羽毛球"};
    ArrayList<String> datas =new ArrayList<>();
    public final static int CAMERA_RESULT_CODE=0;
    public final static int ALBUM_RESULT_CODE=1;
    public final static int CROP_RESULT_CODE=2;
    public final static int  REQUEST_PERMISSIONS=3;
    public  final  static  int VIDEO_COND=4;
    public  final  static  int VEDIO_BUTTON_CODE=5;
    File file;
    Add_Run_Dialog_Activity.MyRecycleViewClassAdapter myRecycleViewClassAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_run_view_dialog);
        title=findViewById(R.id.title);
        address=findViewById(R.id.address);
        time=findViewById(R.id.time);
        xiangx=findViewById(R.id.xiangx);
        address=findViewById(R.id.address);
        addvdeio=findViewById(R.id.kkk);
        videoView=findViewById(R.id.playvdieo);
        recyclerView=findViewById(R.id.add_recycle);
        classs_ports=findViewById(R.id.class_sprot);
        getData();
        init();
    }

    private void init() {
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,strclassport);
        classs_ports.setAdapter(stringArrayAdapter);
        addvdeio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideo();
            }
        });
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                vedioPlayer(videourl);
//            }
//        });
        myRecycleViewClassAdapter=new Add_Run_Dialog_Activity.MyRecycleViewClassAdapter();
        recyclerView.setAdapter(myRecycleViewClassAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        myRecycleViewClassAdapter.setOnClisterItem(new OnClisterItem() {
            @Override
            public void onItemLister(View.OnClickListener onClickListener, int postion) {
               if(datas.get(postion).equals("0")){
                   initPermission();
                    opeanImgSelect();
                }
            }
        });
    }
//选择视频文件
    private void openSelectVideo() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=LayoutInflater.from(this).inflate(R.layout.img_select_dialog,null,false);
        TextView camera=view.findViewById(R.id.camera);
        TextView Album=view.findViewById(R.id.Album);
        TextView cancel=view.findViewById(R.id.cancel);
        camera.setText(getResources().getString(R.string.Videoshooting));
        Album.setText(getResources().getString(R.string.Localvideo));
        builder.setView(view);
        final AlertDialog alertDialog=builder.show();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vedioButton();
                alertDialog.dismiss();
            }
        });
        Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideo();
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //视频播放
    public void vedioPlayer(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
       // File file = new File(path);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".FileProvider", new File(path));
        intent.setDataAndType(uri, "video/*");
        startActivity(intent);
    }
    /***
     * 视频录制
     * @return
     */
    public void vedioButton() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        // 将文件存到指定的路径
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = storageDir + timeStamp + ".mp4";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        Uri photoUri = FileProvider.getUriForFile(
                this,
                "com.example.runapp.fileprovider",
                file);
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        // 自定义输出位置，这样可以将视频存在我们指定的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        //设置视频录制的最长时间
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        //设置视频录制的画质
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.2);
          startActivityForResult(intent, VEDIO_BUTTON_CODE);
    }
    private void opeanImgSelect() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=LayoutInflater.from(this).inflate(R.layout.img_select_dialog,null,false);
        TextView camera=view.findViewById(R.id.camera);
        TextView Album=view.findViewById(R.id.Album);
        TextView cancel=view.findViewById(R.id.cancel);
        builder.setView(view);
        final AlertDialog alertDialog=builder.show();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysCamera();
                alertDialog.dismiss();
            }
        });
        Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSysAlbum();
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void Back(View view) {
        finish();
    }

    public void Save(View view) {
    }

    public void getData() {
        datas.add("0");
    }
    //适配器
    class  MyRecycleViewClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        OnClisterItem onClisterItem;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(Add_Run_Dialog_Activity.this).inflate(R.layout.img_view,parent,false);
            return new Add_Run_Dialog_Activity.MyRecycleViewClassAdapter.myViewHolderClass(view,onClisterItem);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String str=datas.get(position);
            myViewHolderClass myViewHolderClass= (MyRecycleViewClassAdapter.myViewHolderClass) holder;
            if(str.equals("0")){
                Glide.with(Add_Run_Dialog_Activity.this).load(R.drawable.add_imgs).into(myViewHolderClass.head);
            }else {
                Glide.with(Add_Run_Dialog_Activity.this).load(str).into(myViewHolderClass.head);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void setOnClisterItem(OnClisterItem onClisterItem) {
            this.onClisterItem = onClisterItem;
        }

        class  myViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener{
            OnClisterItem onClisterItem;
             ImageView head;
            @Override
            public void onClick(View v) {
                onClisterItem.onItemLister(this,getPosition());
            }

            public myViewHolderClass(View itemView,OnClisterItem onClisterItem) {
                super(itemView);
                this.onClisterItem=onClisterItem;
                itemView.setOnClickListener(this);
                  head=itemView.findViewById(R.id.imgs);
            }
        }
    }
    //拍照
    private void openSysCamera() {
        Uri  imgUriOri;;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
//                new File(Environment.getExternalStorageDirectory(), imgName)));
//        File file = new File(Environment.getExternalStorageDirectory(), imgName);
        try {
            file = createOriImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imgUriOri = Uri.fromFile(file);
            } else {
                imgUriOri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriOri);
            startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
        }
    }
    /**
     * 打开系统相册
     */
    private void openSysAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }
    private  void  openVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,VIDEO_COND);
    }
    /**
     * 裁剪图片
     *
     * @param data
     */
    private void cropPic(Uri data) {
        if (data == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(data, "image/*");

        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true");
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 320);
        cropIntent.putExtra("outputY", 320);
        cropIntent.putExtra("scale", true);
        /**
         * return-data
         * 这个属性决定我们在 onActivityResult 中接收到的是什么数据，
         * 如果设置为true 那么data将会返回一个bitmap
         * 如果设置为false，则会将图片保存到本地并将对应的uri返回，当然这个uri得有我们自己设定。
         * 系统裁剪完成后将会将裁剪完成的图片保存在我们所这设定这个uri地址上。我们只需要在裁剪完成后直接调用该uri来设置图片，就可以了。
         */
        cropIntent.putExtra("return-data", true);
        // 当 return-data 为 false 的时候需要设置这句
//        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 图片输出格式
//        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 头像识别 会启动系统的拍照时人脸识别
//        cropIntent.putExtra("noFaceDetection", true);
        startActivityForResult(cropIntent, CROP_RESULT_CODE);
    }
    /**
     * 创建原图像保存的文件
     *
     * @return
     * @throws IOException
     */
    private File createOriImageFile() throws IOException {
        String imgPathOri;
        String imgNameOri = "HomePic_" + new SimpleDateFormat(
                "yyyyMMdd_HHmmss").format(new Date());
        File pictureDirOri = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(
                imgNameOri,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirOri       /* directory */
        );
        imgPathOri = image.getAbsolutePath();
        Log.e("urlsimh",imgPathOri);
        return image;
    }
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_RESULT_CODE:
                cropPic(getImageContentUri(file));
                break;
            case CROP_RESULT_CODE:
                // 裁剪时,这样设置 cropIntent.putExtra("return-data", true); 处理方案如下
                if (data != null) {
                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        Bitmap bitmap = bundle.getParcelable("data");
                       // imageView.setImageBitmap(bitmap);
                        // 把裁剪后的图片保存至本地 返回路径
                        String urlpath = FileUtilcll.saveFile(this, "crop.jpg", bitmap);
                         datas.remove(datas.size()-1);
                        Log.e("url",urlpath);
                         datas.add(urlpath);
                         datas.add("0");
                        myRecycleViewClassAdapter.notifyDataSetChanged();
                    }
                }

                // 裁剪时,这样设置 cropIntent.putExtra("return-data", false); 处理方案如下
//                try {
//                    ivHead.setImageBitmap(BitmapFactory.decodeStream(
// getActivity().getContentResolver().openInputStream(imageUri)));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;
            case ALBUM_RESULT_CODE:
                // 相册
                cropPic(data.getData());
                break;
            case VIDEO_COND:
              //  Log.e("ererd",.getPath());
                final String docId = DocumentsContract.getDocumentId(data.getData());
                final String[] split = docId.split(":");
                Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, "_id=?", new String[] { split[1] }, null);
                cursor.moveToFirst();
                String videoPath = cursor.getString( cursor.getColumnIndexOrThrow("_data"));
                String imgPath = cursor.getString( cursor
                        .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
              //  String path = cursor.getString(1);
                Log.e("ererd2",videoPath);
                Glide.with(Add_Run_Dialog_Activity.this).load(imgPath).into(videoView);
                videourl=videoPath;
//                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                // 将文件存到指定的路径
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String filePath = storageDir + timeStamp + ".mp4";
//                copyFile(videoPath,filePath);
                addvdeio.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
                addvdeio.setEnabled(false);
                break;
            case VEDIO_BUTTON_CODE:
                Uri uri=data.getData();
               // setImgfrist(uri.getPath());
                break;
        }
    }
    /**
     * 初始化相机相关权限
     * 适配6.0+手机的运行时权限
     */
    private void initPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 之前拒绝了权限，但没有点击 不再询问 这个时候让它继续请求权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "用户曾拒绝打开相机权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            } else {
                //注册相机权限
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //成功
                    Toast.makeText(this, "用户授权相机权限", Toast.LENGTH_SHORT).show();
                } else {
                    // 勾选了不再询问
                    Toast.makeText(this, "用户拒绝相机权限", Toast.LENGTH_SHORT).show();
                    /**
                     * 跳转到 APP 详情的权限设置页
                     *
                     * 可根据自己的需求定制对话框，点击某个按钮在执行下面的代码
                     */
                   // Intent intent = Util.getAppDetailSettingIntent(PhotoFromSysActivity.this);
                  //  startActivity(intent);
                }
                break;
        }
    }
    public boolean copyFile(String oldPathName, String newPathName) {
        try {
            File oldFile = new File(oldPathName);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */

            FileInputStream fileInputStream = new FileInputStream(oldPathName);
            FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
