package com.ht.api.caller.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

import java.io.IOException;

public class JacksonRequestConverter<T> implements Converter<T, RequestBody> {
    private ObjectMapper objectMapper;

    public JacksonRequestConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        String content = objectMapper.writeValueAsString(value);
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), content);
    }
}
