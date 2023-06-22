package com.benaya.link_validator_api.repository;

import com.benaya.link_validator_api.model.Domain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongodbRepository extends MongoRepository<Domain, String> {
    boolean existsByName(String domainName);
    Domain getByName(String domainName);

    boolean existsByUrlsContains(String url);
}
