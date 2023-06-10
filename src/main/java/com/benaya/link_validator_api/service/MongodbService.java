package com.benaya.link_validator_api.service;

import com.benaya.link_validator_api.repository.MongodbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class MongodbService {
    private final MongodbRepository mongodbRepository;
    public boolean notExistsByUrl(String url) {
        if (!isValidUrl(url)) {
            return false;
        }
        return !mongodbRepository.existsByUrl(url);
    }
    public boolean notExistsByDomain(String url) {
        if (!isValidUrl(url)) {
            return false;
        }
        String domain = url.split("/")[2];
        return !mongodbRepository.existsByDomain(domain);
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
