package com.benaya.link_validator_api.repository;

import com.benaya.link_validator_api.model.Domain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends MongoRepository<Domain, String> {
    boolean existsByName(String domainName);
    Domain getByName(String domainName);
    Optional<Domain> findFirstBy();
    boolean existsByUrlsContains(String url);
}
