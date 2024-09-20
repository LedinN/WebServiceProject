package com.nick.webserviceproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    @Value("${apiKey}")
    private String apiKey;

    public String getApiKey() {return apiKey;}
}
