package com.si.siapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.si.siapiclientsdk.requestParams.RequestParamsFactory;

import java.util.HashMap;
import java.util.Map;

import static com.si.siapiclientsdk.utils.SignUtils.genSign;


public class siApiClient {

    private String accessKey;

    private String secretKey;

    private static final String GATEWAY_HOST = "http://tapi.tleon.top:8090";

    public siApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }

    private Map<String, String> getHeaderMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(accessKey, secretKey));
        return hashMap;
    }

    public String invoke(String url, String method) {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.valueOf(method), GATEWAY_HOST + url);
        HttpResponse httpResponse = httpRequest.addHeaders(getHeaderMap()).execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    public String invoke(String url, String method, String requestParams) throws Exception {
        RequestParamsFactory factory = getFactory(url);
        String json = factory.create(requestParams);
        HttpRequest httpRequest = HttpUtil.createRequest(Method.valueOf(method), GATEWAY_HOST + url);
        HttpResponse httpResponse = httpRequest.addHeaders(getHeaderMap())
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());// 获取响应码
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    private RequestParamsFactory getFactory(String url) throws Exception {
        String[] pathSegments = url.split("/");
        String typeName = StrUtil.upperFirst(pathSegments[2]) + "RequestParamsFactory";
        String factoryClassName = "com.si.siapiclientsdk.requestParams." + typeName;
        RequestParamsFactory factory = (RequestParamsFactory) Class.forName(factoryClassName).newInstance();
        return factory;
    }
}
