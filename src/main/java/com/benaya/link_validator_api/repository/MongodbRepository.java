package com.benaya.link_validator_api.repository;

import com.benaya.link_validator_api.model.PhishDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongodbRepository extends MongoRepository<PhishDetails, String> {
    boolean existsByUrl(String url);
    boolean existsByDomain(String domain);
}
