package com.benaya.link_validator_api.service.integration;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.repository.DomainRepository;
import com.benaya.link_validator_api.service.CachingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@Slf4j
public class CachingServiceIntegrationTest {
    private static final int CONTAINER_PORT = 27017;
    private static final String UNSAFE_URL = "https://a.tb52ebklo.repl.co/service/https:/www.facebook.com/unsupporte";
    private static final String UNSAFE_DOMAIN = "a.tb52ebklo.repl.co";
    private static final String SAFE_URL = "https://www.facebook.com";
    private static final String SAFE_DOMAIN = "www.facebook.com";
    //    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017)
//            .withCopyFileToContainer(MountableFile.forClasspathResource("phishing_collection.domains.json"), "/tmp/phishing_collection.domains.json")
//            .withCommand("bash -c 'mongod --bind_ip_all --fork --logpath /var/log/mongod.log && mongoimport --host localhost --port 27017 --db phishing_collection --collection domains --type json --file /tmp/phishing_collection.domains.json --jsonArray'")
            .waitingFor(Wait.forLogMessage(".*waiting for connections on port 27017.*\\n", 1));

    static {
        mongoDBContainer.start();
        int port = mongoDBContainer.getMappedPort(CONTAINER_PORT);
        String host = mongoDBContainer.getHost();
        System.setProperty("spring.data.mongodb.uri", "mongodb://" + host + ":" + port + "/phishing_collection");
    }

    @Autowired
    private CachingService cachingService;

    @SpyBean
    private DomainRepository repository;

    @BeforeEach
    public void setUp() {
        repository.save(Domain.builder().name(UNSAFE_DOMAIN).urls(List.of(UNSAFE_URL)).build());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
        cachingService.cacheEvict();
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlExist_ShouldBeCached() {
        // Prepare
        AtomicInteger unsafeCounter = new AtomicInteger(0);
        // Act
        IntStream.range(0, 100).forEach(i -> unsafeCounter
                .updateAndGet(value -> cachingService
                        .isSafeUrlByDirectSearch(UNSAFE_URL) ? value : value + 1));
        // Assert
        Assertions.assertEquals(unsafeCounter.get(), 100);
        verify(repository, times(1)).existsByUrlsContains(UNSAFE_URL);
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlDoesNotExist_ShouldNotBeCached() {
        // Prepare
        AtomicInteger safeCounter = new AtomicInteger(0);
        // Act
        IntStream.range(0, 10).forEach(i -> safeCounter
                .updateAndGet(value -> cachingService
                        .isSafeUrlByDirectSearch(SAFE_URL) ? value + 1 : value));
        // Assert
        Assertions.assertEquals(safeCounter.get(), 10);
        verify(repository, times(10)).existsByUrlsContains(SAFE_URL);
    }

    @Test
    public void testGetDomainFromCache_WhenDomainExists_ShouldBeCached() {

        // Prepare
        AtomicInteger unsafeCounter = new AtomicInteger(0);
        Domain domain = Domain.builder().name(UNSAFE_DOMAIN).urls(List.of(UNSAFE_URL)).build();
        // Act
        IntStream.range(0, 100).forEach(i -> unsafeCounter
                .updateAndGet(value -> cachingService
                        .getDomainFromCache(UNSAFE_DOMAIN).equals(domain) ? value + 1 : value));
        // Assert
        Assertions.assertEquals(unsafeCounter.get(), 100);
        verify(repository, times(1)).findById(UNSAFE_DOMAIN);
    }

    @Test
    public void testGetDomainFromCache_WhenDomainDoesNotExists_ShouldNotBeCached() {
        // Prepare
        AtomicInteger safeCounter = new AtomicInteger(0);
        // Act
        IntStream.range(0, 100).forEach(i -> safeCounter
                .updateAndGet(value -> cachingService
                        .getDomainFromCache(SAFE_DOMAIN) == null ? value + 1 : value));
        // Assert
        Assertions.assertEquals(safeCounter.get(), 100);
        verify(repository, times(100)).findById(SAFE_DOMAIN);
    }

    @Test
    public void testIsUnsafeDomainCached_WhenDomainExist_ShouldBeCached() {
        // Prepare
        AtomicInteger unsafeCounter = new AtomicInteger(0);

        // Act
        IntStream.range(0, 100).forEach(i -> unsafeCounter
                .updateAndGet(value -> cachingService
                        .isUnsafeDomainCached(UNSAFE_DOMAIN) ? value + 1 : value));
        // Assert
        Assertions.assertEquals(unsafeCounter.get(), 100);
        verify(repository, times(1)).existsByName(UNSAFE_DOMAIN);
    }

    @Test
    public void testIsUnsafeDomainCached_WhenDomainDoesNotExist_ShouldNotBeCached() {
        // Prepare
        AtomicInteger safeCounter = new AtomicInteger(0);
        // Act
        IntStream.range(0, 100).forEach(i -> safeCounter
                .updateAndGet(value -> cachingService
                        .isUnsafeDomainCached(SAFE_DOMAIN) ? value : value + 1));
        // Assert
        Assertions.assertEquals(safeCounter.get(), 100);
        verify(repository, times(100)).existsByName(SAFE_DOMAIN);
    }
}
