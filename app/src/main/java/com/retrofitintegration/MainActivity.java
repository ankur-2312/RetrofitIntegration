package com.retrofitintegration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Button butGet = findViewById(R.id.butGet);
        Button butPost = findViewById(R.id.butPost);
        Button butPut = findViewById(R.id.butPut);
        Button butPatch = findViewById(R.id.butPatch);
        Button butDelete = findViewById(R.id.butDelete);
        butGet.setOnClickListener(this);
        butPost.setOnClickListener(this);
        butPut.setOnClickListener(this);
        butPatch.setOnClickListener(this);
        butDelete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butGet:
                getData();
                break;

            case R.id.butPost:
                postData();
                break;

            case R.id.butPut:
                putData();
                break;

            case R.id.butPatch:
                patchData();
                break;

            case R.id.butDelete:
                break;
        }


    }

    private void getData() {
        Call<List<example>> call = ApiInstance.getInstance().getApiInstance().getData();
        call.enqueue(new Callback<List<example>>() {
            @Override
            public void onResponse(Call<List<example>> call, Response<List<example>> response) {

                Log.i("my", "hello");
                Log.i("my", "hello" + response.body().size());
            }

            @Override
            public void onFailure(Call<List<example>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("my", "" + t.getMessage());
            }
        });
    }

    private void postData() {

        example ex = new example();
        ex.setBody("sadsad");
        ex.setTitle("qwerrrr");
        ex.setUserId(123);
        Call<example> call1 = ApiInstance.getInstance().getApiInstance().sendData(ex);

        call1.enqueue(new Callback<example>() {
            @Override
            public void onResponse(Call<example> call, Response<example> response) {

                Log.i("my", "hi");
                Log.i("my", "hi" + response.body().getUserId());
                Log.i("my", "hi" + response.body().getId());
                Log.i("my", "hi" + response.body().getBody());
                Log.i("my", "hi" + response.body().getTitle());
            }

            @Override
            public void onFailure(Call<example> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("my", "" + t.getMessage());
            }
        });
    }

    private void putData() {

        Call<example> call1 = ApiInstance.getInstance().getApiInstance().updateData(34, "hello");

        call1.enqueue(new Callback<example>() {
            @Override
            public void onResponse(Call<example> call, Response<example> response) {

                Log.i("my", "hi");
                Log.i("my", "hi" + response.body().getUserId());
                Log.i("my", "hi" + response.body().getId());
                Log.i("my", "hi" + response.body().getBody());
                Log.i("my", "hi" + response.body().getTitle());
            }

            @Override
            public void onFailure(Call<example> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("my", "" + t.getMessage());
            }
        });
    }


    private void patchData() {

        Call<example> call1 = ApiInstance.getInstance().getApiInstance().patchData("title");

        call1.enqueue(new Callback<example>() {
            @Override
            public void onResponse(Call<example> call, Response<example> response) {

                Log.i("my", "hi");
                Log.i("my", "hi" + response.body().getUserId());
                Log.i("my", "hi" + response.body().getId());
                Log.i("my", "hi" + response.body().getBody());
                Log.i("my", "hi" + response.body().getTitle());
            }

            @Override
            public void onFailure(Call<example> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("my", "" + t.getMessage());
            }
        });
    }
}
