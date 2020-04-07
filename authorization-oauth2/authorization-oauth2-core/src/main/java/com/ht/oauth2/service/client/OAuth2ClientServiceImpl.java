package com.ht.oauth2.service.client;

import com.ht.oauth2.exceptions.NoSuchClientException;
import com.ht.oauth2.repository.Oauth2ClientRepository;
import com.ht.oauth2.model.OAuth2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OAuth2ClientServiceImpl implements OAuth2ClientService {
    @Autowired
    private Oauth2ClientRepository oauth2ClientRepository;

    @Override
    public OAuth2Client findById(String clientId) throws NoSuchClientException {
        Optional<OAuth2Client> optional = oauth2ClientRepository.findById(clientId);
        return optional.orElseThrow(() -> new NoSuchClientException("no such client"));
    }
}
