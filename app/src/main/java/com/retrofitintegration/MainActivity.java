package com.retrofitintegration;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String Tag = "my";
    private RecyclerViewDataAdapter adapter;
    private ArrayList<PoJo> poJoArrayList;
    private int cacheSize = 10 * 1024 * 1024;

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
        Button butMultiPart=findViewById(R.id.butMultiPart);
        RecyclerView rv = findViewById(R.id.recyclerView);
        butGet.setOnClickListener(this);
        butPost.setOnClickListener(this);
        butPut.setOnClickListener(this);
        butPatch.setOnClickListener(this);
        butDelete.setOnClickListener(this);
        butMultiPart.setOnClickListener(this);
        poJoArrayList = new ArrayList<>();
        adapter = new RecyclerViewDataAdapter(poJoArrayList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rv);
        Cache cache = new Cache(getCacheDir(), cacheSize);
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

            case R.id.butMultiPart:
                startActivity(new Intent(MainActivity.this,MultiPartActivity.class));
                break;
        }


    }



    private void getData() {

         Interceptor onlineInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                int maxAge = 60; // read from cache for 60 seconds even if there is internet connection
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            }
        };

         Interceptor offlineInterceptor= new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkAvailable()) {
                    int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
                    request = request.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return chain.proceed(request);
            }
        };

        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(onlineInterceptor)
                .cache(cache)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        Api api = retrofit.create(Api.class);
        Call<List<PoJo>> call = api.getData();
        call.enqueue(new Callback<List<PoJo>>() {
            @Override
            public void onResponse(Call<List<PoJo>> call, Response<List<PoJo>> response) {

                Log.i(Tag, "Get Response Successful  "+response.code());


                if (response.raw().cacheResponse() != null) {
                    Log.i(Tag, "Get Response Successful cache  "+response.raw().cacheResponse());
                    // true: response was served from cache
                }

                if (response.raw().networkResponse() != null) {
                    Log.i(Tag, "Get Response Successful network "+response.raw().networkResponse());
                    // true: response was served from network/server
                }

                if(response.body()!=null) {
                    // assert response.body() != null;
                    poJoArrayList.clear();
                    poJoArrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
