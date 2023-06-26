//package com.benaya.link_validator_api.service.integration;
//
//import com.benaya.link_validator_api.repository.MongodbRepository;
//import com.benaya.link_validator_api.service.LinksService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.MongoDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@SpringBootTest
//@Testcontainers
//public class LinksServiceIntegrationTest {
//
//    @Container
//    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6")
//            .withExposedPorts(27017);
//
//    @Container
//    public static GenericContainer redisContainer = new GenericContainer("redis:6.2.5")
//            .withExposedPorts(6379);
//
//    @Autowired
//    private LinksService service;
//
//    @Autowired
//    private MongodbRepository repository;
//
//    @Test
//    public void testCaching() {
//        // Arrange
//        String url = "http://safe-url.com";
//        when(repository.findByUrl(url)).thenReturn(new Domain());
//
//        // Act
//        boolean firstCallResult = service.isSafeUrlByDirectSearch(url);
//        boolean secondCallResult = service.isSafeUrlByDirectSearch(url);
//
//        // Assert
//        assertTrue(firstCallResult);
//        assertTrue(secondCallResult);
//        verify(repository, times(1)).findByUrl(url); // The repository should only be hit once due to caching
//    }
//}