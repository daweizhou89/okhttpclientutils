# okhttpclientutils
okhttpclientutils基于OkHttp，RxAndroid, Gson封装, 目的简化http接口请求代码。

## 初始化
在Application中添加
```java
OkHttpClientUtils.config()
                .setUserAgent("daweizhou89/test")   // 设定UserAgent
                .setAssertMainThread(true)          // 打开检测UI线程执行的断言
                .setDebugLog(true);                 // 打开log
```

## Callback方式使用
```java
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
```

## RxJava返回String类型
```java
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
```

## RxJava通过Class指定返回类型
```java
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
```

## RxJava通过Type指定返回类型
```java
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
```