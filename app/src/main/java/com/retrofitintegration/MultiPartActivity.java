package com.retrofitintegration;


import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultiPartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    private static String Tag = "my";
    private ImageDataAdapter adapter;
    private ArrayList<Bitmap> uriList;
    private Uri uri;

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_part);
        init();
    }

    //Method to initialize and setting listener to the buttons and set recycler view with adapter
    private void init() {
        Button butPickImage = findViewById(R.id.butGetImage);
        Button butUploadImage = findViewById(R.id.butUploadImage);
        RecyclerView recyclerview = findViewById(R.id.rv);
        uriList = new ArrayList<>();
        adapter = new ImageDataAdapter(uriList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        butPickImage.setOnClickListener(this);
        butUploadImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //Button to pick single image from gallery
            case R.id.butGetImage:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        pickFromGallerySingle();
                    }

                } else {
                    pickFromGallerySingle();

                }
                break;


            case R.id.butUploadImage:
                if (uri != null) {

                    Log.i(Tag, "" +uri.getPath());
                    File file = new File(getRealPathFromURI(uri));
                    final RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);

                    Call<ResponseBody> call = ApiInstance.getInstance().getApiInstance1().uploadImage(part);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.i(Tag, "" + response.code());
                            Toast.makeText(MultiPartActivity.this, "Response  " + response.code(), Toast.LENGTH_SHORT).show();

                            try {
                                String str = response.body().string();
                                Log.i(Tag, "" + str);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i(Tag, "" + t.getMessage());
                        }
                    });
                }
                break;
        }

    }

    //This method get callback when other activity is started with startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:

                    uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    uriList.clear();
                    uriList.add(bitmap);
                    adapter.notifyDataSetChanged();
                    break;


                default:
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallerySingle();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            openSettingsForPermission();
                        } else {
                            giveSingleImagePermissionsDetailsToUser();
                        }
                    }
                }
                break;


            default:

        }
    }

    //Method to pick single image from gallery
    private void pickFromGallerySingle() {
        Intent captureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(captureIntent, GALLERY_REQUEST_CODE);
    }


    //This method shows the dialog box to go to settings
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MultiPartActivity.this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.CANCEL, null)
                .create()
                .show();
    }

    //Method to open settings so that user can give permission
    private void openSettingsForPermission() {
        showMessageOKCancel(getString(R.string.settings),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        startActivity(viewIntent);
                    }
                });
    }

    //Method to give permission requirement to the user
    private void giveSingleImagePermissionsDetailsToUser() {
        showMessageOKCancel(getString(R.string.permissionRequirement),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }

                    }
                });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        Log.i(Tag, "custom  " + result);
        cursor.close();
        return result;
    }


}

