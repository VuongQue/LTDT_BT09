package com.example.exuploadfiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAvatarActivity extends AppCompatActivity {
    public static String[] storge_permissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO
    };
    Button btnChoose, btnUpload;
    CircleImageView imgAvatar;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = UploadAvatarActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

        btnChoose = findViewById(R.id.btnChooseFile);
        btnUpload = findViewById(R.id.btnUpload);
        imgAvatar = findViewById(R.id.imgAvatar);


        mProgressDialog = new ProgressDialog(UploadAvatarActivity.this);
        mProgressDialog.setMessage("Please wait upload....");

//bắt sự kiện nút chọn ảnh
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission(); //checks quyền
                //chooseImage();
            }
        });

//bắt sự kiện upload ảnh
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    UploadImage1();
                }
            }
        });
    }
    private void UploadImage1() {

        mProgressDialog.show();

        // create RequestBody instance from file
        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.e("ffff", IMAGE_PATH);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send the actual file name
        MultipartBody.Part partbodyavatar =
                MultipartBody.Part.createFormData(Const.MY_IMAGES, file.getName(), requestFile);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), "5");


        ServiceAPI.serviceapi.upload(id, partbodyavatar).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                mProgressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", response.body().toString());
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getResult() != null)
                        Glide.with(UploadAvatarActivity.this)
                                .load(apiResponse.getResult().get(0).getImages())
                                .into(imgAvatar);
                        Toast.makeText(UploadAvatarActivity.this, "Thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UploadAvatarActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(UploadAvatarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                btnUpload.setText(t.getMessage());
                Log.e("UploadError", "Lỗi: " + t.getMessage());
            }
        });


    }
    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }
    private void CheckPermission() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if(checkSelfPermission (android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }else{
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // request code
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}
