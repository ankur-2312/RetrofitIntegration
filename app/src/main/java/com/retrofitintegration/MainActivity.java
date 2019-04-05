package com.retrofitintegration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String Tag = "my";
    private RecyclerViewDataAdapter adapter;
    private ArrayList<PoJo> poJoArrayList;

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
        RecyclerView rv = findViewById(R.id.recyclerView);
        butGet.setOnClickListener(this);
        butPost.setOnClickListener(this);
        butPut.setOnClickListener(this);
        butPatch.setOnClickListener(this);
        butDelete.setOnClickListener(this);
        poJoArrayList = new ArrayList<>();
        adapter = new RecyclerViewDataAdapter(poJoArrayList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rv);
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
                deleteData();
                break;
        }


    }

    private void getData() {
        Call<List<PoJo>> call = ApiInstance.getInstance().getApiInstance().getData();
        call.enqueue(new Callback<List<PoJo>>() {
            @Override
            public void onResponse(Call<List<PoJo>> call, Response<List<PoJo>> response) {

                Log.i(Tag, "Get Response Successful  "+response.code());
                assert response.body() != null;
                poJoArrayList.clear();
                poJoArrayList.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PoJo>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(Tag, "" + t.getMessage());
            }
        });
    }

    private void postData() {

        PoJo ex = new PoJo();
        ex.setBody("Title");
        ex.setTitle("Body");
        ex.setUserId(123);
        Call<PoJo> call = ApiInstance.getInstance().getApiInstance().postData(ex);

        call.enqueue(new Callback<PoJo>() {
            @Override
            public void onResponse(Call<PoJo> call, Response<PoJo> response) {

                Log.i(Tag, "Post Response Successful "+response.code());
                assert response.body() != null;
                poJoArrayList.clear();
                poJoArrayList.add(response.body());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<PoJo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(Tag, "" + t.getMessage());
            }
        });
    }

    private void putData() {

        Call<PoJo> call = ApiInstance.getInstance().getApiInstance().putData(34, "title");

        call.enqueue(new Callback<PoJo>() {
            @Override
            public void onResponse(Call<PoJo> call, Response<PoJo> response) {

                Log.i(Tag, "Put Response Successful  "+response.code());
                if(response.body() != null) {
                    poJoArrayList.clear();
                    poJoArrayList.add(response.body());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<PoJo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(Tag, "" + t.getMessage());
            }
        });
    }


    private void patchData() {

        Call<PoJo> call = ApiInstance.getInstance().getApiInstance().patchData("title");

        call.enqueue(new Callback<PoJo>() {
            @Override
            public void onResponse(Call<PoJo> call, Response<PoJo> response) {

                Log.i(Tag, "Patch Response Successful  "+response.code());
                assert response.body() != null;
                poJoArrayList.clear();
                poJoArrayList.add(response.body());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PoJo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(Tag, "" + t.getMessage());
            }
        });
    }

    private void deleteData() {

        Call<ResponseBody> call = ApiInstance.getInstance().getApiInstance().deleteData(2);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.i(Tag, "Delete Response Successful  "+response.code());
                poJoArrayList.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i(Tag, "" + t.getMessage());
            }
        });
    }
}
