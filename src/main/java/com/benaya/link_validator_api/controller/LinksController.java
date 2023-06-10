package com.benaya.link_validator_api.controller;

import com.benaya.link_validator_api.service.MongodbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinksController {
    private final MongodbService mongodbService;
    @GetMapping("/validateLink-url")
    public boolean validateLink(@RequestBody String link) {
        return mongodbService.notExistsByUrl(link);
    }
    @GetMapping("/validateLink-domain")
    public boolean validateLinkByDomain(@RequestBody String link) {
        return mongodbService.notExistsByDomain(link);
    }
}
