package com.benaya.link_validator_api.service;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.benaya.link_validator_api.util.UrlUtils.getDomainFromUrl;

@Service
@RequiredArgsConstructor
public class LinksService {

    private final CachingService cachingService;
    public boolean isSafeUrlByDirectSearch(String url) {
        return cachingService.isSafeUrlByDirectSearch(url);
    }

    public boolean isSafeUrlByDomainSearchThenUrl(String url){
        String domain = getDomainFromUrl(url);
        Domain domainObj = cachingService.getDomainFromCache(domain);
        if (domainObj == null) {
            return true;
        }
        return !domainObj.getUrls().contains(url);
    }
    public boolean isSafeDomainByUrl(String url) {
        String domain = UrlUtils.getDomainFromUrl(url);
        return !cachingService.isUnsafeDomainCached(domain);
    }

}
