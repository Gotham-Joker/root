package com.ht.api.caller.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.api.caller.api.RetrofitService;
import com.ht.api.caller.config.RetrofitProperties;
import com.ht.api.caller.converter.JacksonConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import java.util.concurrent.TimeUnit;

public class RetrofitApiFactory {

    private Retrofit retrofit;

    public RetrofitApiFactory(RetrofitProperties retrofitProperties) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        initRetrofit(retrofitProperties, objectMapper);
    }

    private void initRetrofit(RetrofitProperties retrofitProperties, ObjectMapper objectMapper) {
        this.retrofit = new Retrofit.Builder()
                .client(defaultClient(retrofitProperties))
                .baseUrl(retrofitProperties.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(new SyncAdapterFactory())
                .build();
    }

    public RetrofitApiFactory(RetrofitProperties retrofitProperties, ObjectMapper objectMapper) {
        initRetrofit(retrofitProperties, objectMapper);
    }

    private OkHttpClient defaultClient(RetrofitProperties retrofitProperties) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(retrofitProperties.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(retrofitProperties.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(retrofitProperties.getTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (Boolean.TRUE.equals(retrofitProperties.getTrustAllCert())) {
            builder.sslSocketFactory(TrustAllCertFactory.createSSLSocketFactory(), new TrustAllCertFactory.TrustAllManager())
                    .hostnameVerifier(new TrustAllCertFactory.TrustAllHostnameVerifier());
        }

        return builder.build();
    }

    public <T extends RetrofitService> T create(Class<T> service) {
        return this.retrofit.create(service);
    }

}
