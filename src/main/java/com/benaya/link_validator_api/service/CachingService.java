package com.benaya.link_validator_api.service;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CachingService {
    private final DomainRepository domainRepository;

    @Cacheable(value = "urlCache", key = "#url", unless = "#result == true")
    public boolean isSafeUrlByDirectSearch(String url) {
        return !domainRepository.existsByUrlsContains(url);
    }
    @Cacheable(value = "domainCache", key = "#domain", unless = "#result == null")
    public Domain getDomainFromCache(String domain){
        return domainRepository.findById(domain).orElse(null);
    }
    @Cacheable(value = "domainCacheExist", key = "#domain", unless = "#result == false")
    public boolean isUnsafeDomainCached(String domain){
        return domainRepository.existsByName(domain);
    }

    @Scheduled(fixedDelay = 5, timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    @CacheEvict(value = {"urlCache, domainCacheExist, domainCache"}, allEntries = true)
    public void cacheEvict() {
    }
}
