package com.ht.api.caller.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class JacksonConverterFactory {

    public static Converter.Factory create(ObjectMapper objectMapper) {
        return new Converter.Factory() {

            @Override
            public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
                return new JacksonRequestConverter<>(objectMapper);
            }


            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new JacksonResponseConverter<>(objectMapper, type);
            }
        };
    }
}
