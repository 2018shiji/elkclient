## HttpClient

HttpClient是Apache Jakarta Common下的子项目，可以用来提供功能丰富的支持HTTP协议的客户端编程工具包。

收发模型：

```java
public class AsyncHttpClientUtil{
    private CloseableHttpAsyncClient asyncClient;
    @Getter
    private CallbackHandler callbackHandler = new CallbackHandler();
    
    private void initAsyncClient() throws IOReactorException               {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();

        IOReactorConfig ioReactorCfg = IOReactorConfig.custom()
                .setIoThreadCount(4)
                .setSoKeepAlive(true)
                .build();

        ConnectingIOReactor connReactor = 
            new DefaultConnectingIOReactor(ioReactorCfg);
        PoolingNHttpClientConnectionManager connManager =
                new PoolingNHttpClientConnectionManager(connReactor);
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(100);

        asyncClient = HttpAsyncClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

    }
    
    public Future<HttpResponse> sendHttpRequestAsyncDisOrder(String remoteUrl, String base64Str, CountDownLatch countDownLatch) {

        Future<HttpResponse> responseFuture = null;

        HttpPost httpPost = new HttpPost(remoteUrl);
        StringEntity stringEntity =
                new StringEntity(
            base64Str, ContentType.create("application/json", "UTF-8"));
        httpPost.setEntity(stringEntity);

        callbackHandler.setCountDownLatch(countDownLatch);

        long begin = System.currentTimeMillis();
        System.out.println("------------before execute------------" +
                new SimpleDateFormat("HH:mm:ss:SSS")
                           .format(Calendar.getInstance().getTime()));
        responseFuture = asyncClient.execute(httpPost, callbackHandler);
        System.out.println("------------after execute------------" + (System.currentTimeMillis() - begin) / 1000.0);

        return responseFuture;

    }

    public Future<HttpResponse> sendHttpRequestAsyncOrder(String remoteUrl, String base64Str) {

        Future<HttpResponse> responseFuture = null;

        HttpPost httpPost = new HttpPost(remoteUrl);

        StringEntity stringEntity =
                new StringEntity(
            base64Str, ContentType.create("application/json", "UTF-8"));
        httpPost.setEntity(stringEntity);

        long begin = System.currentTimeMillis();
        System.out.println("------------before execute------------" +
                new SimpleDateFormat("HH:mm:ss:SSS")
                           .format(Calendar.getInstance().getTime()));
        responseFuture = asyncClient.execute(httpPost, null);
        System.out.println("------------after execute------------" + (System.currentTimeMillis() - begin) / 1000.0);

        return responseFuture;

    }

    public void start(){
        try {
            initAsyncClient();
        }catch (IOReactorException e){e.printStackTrace();}
        this.asyncClient.start();
    }

    public void close(){
        try{
            this.asyncClient.close();
        } catch (IOException e){e.printStackTrace();}
    }

}
```

```java
public class SyncHttpClientUtil<T> {
	private CloseableHttpClient httpClient = HttpClients.createDefault();
    @Setter
    private Class targetClass;
    
    public T getResponsePost(String remoteUrl, String base64Str, CloseableHttpClient httpClient){
        if(targetClass == null){
            System.out.println("!!!!!!! 请先设置好转换后的目标类型  !!!!!!!");
            return null;
        }
        HttpPost httpPost = new HttpPost(remoteUrl);
        StringEntity stringEntity =
                new StringEntity(
            base64Str, ContentType.create("application/json", "UTF-8"));
        httpPost.setEntity(stringEntity);

        PostJsonEntityRH<T> jsonEntityRH = new PostJsonEntityRH<>();
        jsonEntityRH.setTargetClass(targetClass);

        T result = null;
        try{
            long begin = System.currentTimeMillis();
            System.out.println("------------before execute------------" +
                    new SimpleDateFormat("HH:mm:ss:SSS")
                               .format(Calendar.getInstance().getTime()));
            result = httpClient.execute(httpPost, jsonEntityRH);
            System.out.println("------------after execute------------"
                               + (System.currentTimeMillis()-begin)/1000.0
                               + "s Thread:"
                               + Thread.currentThread().getId());
            System.out.println(result);
        } catch (IOException e){e.printStackTrace();}

        return result;
    }
}
```



```java
public class CallbackHandler implements FutureCallback<HttpResponse> {
    @Setter
    private CountDownLatch countDownLatch;
    @Getter
    private List<String> resultStrings = new ArrayList<>();
    
    @Override
    public void completed(HttpResponse result) {
        try {
            System.out.println(result);
            System.out.println("completed once");
            InputStream contentStream = result.getEntity().getContent();
            String contentStr = CharStreams.toString(
                new InputStreamReader(contentStream));
            System.out.println("=============" + contentStr);
            resultStrings.add(contentStr);
            countDownLatch.countDown();

        } catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void failed(Exception e) {
        System.out.println("Failed..." + e.getMessage());
        countDownLatch.countDown();
    }

    @Override
    public void cancelled() {
        countDownLatch.countDown();
    }
}
```

使用案例：

```java
//异步无序版本
public List<ContainerFrontTail> getContainerFrontTails(String remoteUrl, List<String> requestBodyStrs) {
        List<String> frontTailStrings = 
            sendRequestAsyncDisOrder(remoteUrl, requestBodyStrs);
        List<ContainerFrontTail> frontTails = new ArrayList<>();
         
        try{
            for(int i = 0; i < frontTailStrings.size(); i++){
                String resultStr = frontTailStrings.get(i);
                System.out.println("+++++++++++++++++++++++" + resultStr);
                JSONObject jsonObject = JSON.parseObject(resultStr);
                frontTails.add(
                    JSON.toJavaObject(jsonObject, ContainerFrontTail.class));
            }
        } catch (Exception e){e.printStackTrace();}

        return frontTails;
    }

public List<String> sendRequestAsyncDisOrder(String remoteUrl，List<String> requestBodyStrs){
    try {
        CountDownLatch countDownLatch = 
            new CountDownLatch(requestBodyStrs.size);
        asyncClient.start();
        for(int i = 0; i < requestBodyStrs.size(), i++){
            asyncClient.sendHttpRequestAsyncCallback(
                remoteUrl, 
                requestBodyStrws.get(i), 
                countDownLatch);
        }

        countDownLatch.await();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } finally {
        asyncClient.close();
    }
    return asyncClient.getCallbackHandler().getResultStrings();
}
```

```java
//异步有序版
public List<String> sendRe
```

