package com.example.runapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.entity.User;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.FileUtilcll;
import com.example.runapp.util.Singleton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.runapp.Add_Run_Dialog_Activity.ALBUM_RESULT_CODE;
import static com.example.runapp.Add_Run_Dialog_Activity.CAMERA_RESULT_CODE;
import static com.example.runapp.Add_Run_Dialog_Activity.CROP_RESULT_CODE;
import static com.example.runapp.Add_Run_Dialog_Activity.REQUEST_PERMISSIONS;
import static com.example.runapp.Data.DataAccess.URL_IMG_ACCESS;

public class Setting_Activity extends AppCompatActivity {
     ImageView imageView;
     EditText nam,nickname,pass,phone;
     String imgurl;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        imageView=findViewById(R.id.imgchange);
        nam=findViewById(R.id.namechange);
        nickname=findViewById(R.id.nicknange);
        pass=findViewById(R.id.passchange);
        phone=findViewById(R.id.phonename);
        init();
    }

    private void init() {
        initPermission();
        imgurl= Singleton.getInstance().getUser().getUrlimg();
        User user=Singleton.getInstance().getUser();
        if(imgurl!=null&&!imgurl.equals("0")){
            Glide.with(Setting_Activity.this).load(URL_IMG_ACCESS+Singleton.getInstance().getUser().getUrlimg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }else{
            Glide.with(this).load(R.drawable.defulthead).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }
        if(!Common_Uitl.IsEmptyString(user.getMyname())&!user.getMyname().equals("0")){
            nam.setText(user.getMyname());
        }
        if(!Common_Uitl.IsEmptyString(user.getPass())){
            pass.setText(user.getPass());
        }
        if(!Common_Uitl.IsEmptyString(user.getPhone())){
            phone.setText(user.getPhone());
        }
        if(!Common_Uitl.IsEmptyString(user.getNickname())){
            nickname.setText(user.getNickname());
        }
        //拍照或者选择
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opeanImgSelect();
            }
        });
    }
    //拍照
    private void openSysCamera() {
        Uri imgUriOri;;
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
    private void opeanImgSelect() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.img_select_dialog,null,false);
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
    private void openSysAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }
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
    public void Back(View view) {
        finish();
    }

    public void Save(View view) {
        final String namev=nam.getText().toString().trim();
        final String nicknamev=nickname.getText().toString().trim();
        final String phonev=phone.getText().toString().trim();
        final String passv=pass.getText().toString().trim();
         if(imgurl!=null) {
             final File file = new File(imgurl);
             new Thread() {
                 @Override
                 public void run() {
                     super.run();
                     DataAccess.uploadFile(file, "head"+Singleton.getInstance().getUser().getId()+".png", namev, nicknamev, phonev, passv, Singleton.getInstance().getUser().getId());

                 }
             }.start();
         }
             Common_Uitl.showToast(this, getResources().getString(R.string.saveed));
             finish();

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
                        imgurl=urlpath;
                        Glide.with(Setting_Activity.this).load(imgurl).into(imageView);
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
}
