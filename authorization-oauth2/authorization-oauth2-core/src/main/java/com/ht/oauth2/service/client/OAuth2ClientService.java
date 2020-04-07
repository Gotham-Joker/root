package com.ht.oauth2.service.client;

import com.ht.oauth2.exceptions.NoSuchClientException;
import com.ht.oauth2.model.OAuth2Client;

public interface OAuth2ClientService {
    /**
     * 查找客户端，如果不存在就会抛出{@link NoSuchClientException}
     *
     * @param clientId
     * @return
     * @throws NoSuchClientException
     */
    OAuth2Client findById(String clientId) throws NoSuchClientException;
}
