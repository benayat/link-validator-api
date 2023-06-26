package com.benaya.link_validator_api.controller;

import com.benaya.link_validator_api.model.UrlSafety;
import com.benaya.link_validator_api.service.LinksService;
import com.benaya.link_validator_api.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinksController {
    private final LinksService linksService;

    @GetMapping("/validateLink-url")
    public UrlSafety validateLink(@RequestBody String link, @RequestParam("directSearch") boolean isDirectSearch) {
        UrlUtils.validateUrl(link);
        if (isDirectSearch) {
            return linksService.isSafeUrlByDirectSearch(link) ? UrlSafety.VALID : UrlSafety.INVALID;
        }else{
            return linksService.isSafeUrlByDomainSearchThenUrl(link) ? UrlSafety.VALID : UrlSafety.INVALID;
        }
    }
    @GetMapping("/validateLink-domain")
    public UrlSafety validateLinkByDomain(@RequestBody String link) {
        UrlUtils.validateUrl(link);
        return linksService.isSafeDomainByUrl(link) ? UrlSafety.VALID : UrlSafety.INVALID;
    }
}
