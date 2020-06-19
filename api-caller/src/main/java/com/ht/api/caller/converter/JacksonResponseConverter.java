package com.ht.api.caller.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class JacksonResponseConverter<T> implements Converter<ResponseBody, T> {
    private ObjectMapper objectMapper;
    private Type type;

    public JacksonResponseConverter(ObjectMapper objectMapper, Type type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }


    @Override
    public T convert(ResponseBody body) throws IOException {
        if (type == String.class) {
            return (T) body.string();
        }
        byte[] bytes = body.bytes();
        body.close();
        return objectMapper.readValue(bytes, new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        });
    }
}
