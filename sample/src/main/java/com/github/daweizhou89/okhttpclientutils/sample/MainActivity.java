package com.github.daweizhou89.okhttpclientutils.sample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.daweizhou89.okhttpclientutils.OkHttpClientUtils;
import com.github.daweizhou89.okhttpclientutils.RequestParams;
import com.github.daweizhou89.okhttpclientutils.ResponseCallback;
import com.github.daweizhou89.okhttpclientutils.sample.databinding.ActivityMainBinding;
import com.github.daweizhou89.okhttpclientutils.sample.model.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 *
 * Created by daweizhou89 on 2017/2/22.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.toString();

    private static final String URL = "http://sugg.us.search.yahoo.net/gossip-gl-location/?appid=weather";

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.getByCallback.setOnClickListener(this);
        mBinding.getByRxString.setOnClickListener(this);
        mBinding.getByRxClass.setOnClickListener(this);
        mBinding.getByRxType.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_by_callback:
                performGetByCallback();
                break;
            case R.id.get_by_rx_string:
                performGetByRxString();
                break;
            case R.id.get_by_rx_class:
                performGetByRxClass();
                break;
            case R.id.get_by_rx_type:
                performGetByRxType();
                break;
        }
    }

    private void e(Throwable throwable) {
        Log.e(TAG, "", throwable);
    }

    private void performGetByCallback() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("output", "json");
        requestParams.put("command", "广");
        OkHttpClientUtils.get(URL, requestParams, new ResponseCallback() {
            @Override
            public void onSuccess(String url, String response) {
                mBinding.response.setText("callback:\n" + response);
            }

            @Override
            public void onFailure(String url, Throwable throwable) {
                e(throwable);
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performGetByRxString() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("output", "json");
        requestParams.put("command", "广");
        OkHttpClientUtils.get(URL, requestParams)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        mBinding.response.setText("rx string:\n" + response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        e(throwable);
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void performGetByRxClass() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("output", "json");
        requestParams.put("command", "广");
        OkHttpClientUtils.get(URL, requestParams, Response.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        mBinding.response.setText("rx class:\n" + new Gson().toJson(response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        e(throwable);
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void performGetByRxType() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("output", "json");
        requestParams.put("command", "广");
        Observable<Response> observable = OkHttpClientUtils.get(URL, requestParams, new TypeToken<Response>() {}.getType());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        mBinding.response.setText("rx type:\n" + new Gson().toJson(response));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        e(throwable);
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
