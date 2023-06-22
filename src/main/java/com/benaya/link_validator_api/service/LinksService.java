package com.benaya.link_validator_api.service;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.repository.MongodbRepository;
import com.benaya.link_validator_api.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.benaya.link_validator_api.util.UrlUtils.getDomainFromUrl;
import static com.benaya.link_validator_api.util.UrlUtils.isValidUrl;

@Service
@RequiredArgsConstructor
public class LinksService {
    private final MongodbRepository mongodbRepository;

    @Cacheable(value = "urlCache", key = "#url", unless = "#result == false")
    public boolean isSafeUrlByDirectSearch(String url) {
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
        Domain domainObj = getDomainFromCache(domain);
        if (domainObj == null) {
            return true;
        }
        return !domainObj.getUrls().contains(url);
    }
    public boolean isSafeDomainByUrl(String url) {
        if (!isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        String domain = UrlUtils.getDomainFromUrl(url);
        return !isUnsafeDomainCached(domain);
    }
    @Cacheable(value = "domainCache", key = "#domain", unless = "#result == false")
    public boolean isUnsafeDomainCached(String domain){
        return mongodbRepository.existsByName(domain);
    }
    @Cacheable(value = "domainCache", key = "#domain", unless = "#result == null")
    public Domain getDomainFromCache(String domain){
        return mongodbRepository.findById(domain).orElse(null);
    }
}
