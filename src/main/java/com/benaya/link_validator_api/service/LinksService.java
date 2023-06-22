package com.benaya.link_validator_api.service;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.repository.MongodbRepository;
import com.benaya.link_validator_api.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.benaya.link_validator_api.util.UrlUtils.getDomainFromUrl;
import static com.benaya.link_validator_api.util.UrlUtils.isValidUrl;

@Service
@RequiredArgsConstructor
public class LinksService {
    private final MongodbRepository mongodbRepository;
    public boolean isSafeUrlDirectSearch(String url) {
        if (!isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        return !mongodbRepository.existsByUrlsContains(url);
    }

    public boolean isSafeUrlByDomainSearch(String url){
        if (!isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        String domain = getDomainFromUrl(url);
        Domain domainObj = mongodbRepository.findById(Objects.requireNonNull(domain)).orElse(null);
        if (domainObj == null) {
            return true;
        }
        return !domainObj.getUrls().contains(url);
    }
    public boolean isSafeDomain(String url) {
        if (!isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        String domain = UrlUtils.getDomainFromUrl(url);
        return !isUnsafeDomainCached(domain);
    }
    public boolean isUnsafeDomainCached(String domain){
        return mongodbRepository.existsByName(domain);
    }
}
