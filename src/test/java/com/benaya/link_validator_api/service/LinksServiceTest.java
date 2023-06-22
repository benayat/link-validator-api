package com.benaya.link_validator_api.service;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.repository.MongodbRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinksServiceTest {

    @Mock
    private MongodbRepository repository;

    @InjectMocks
    private LinksService service;

    private static List<String> urls;
    private static final String urlToSearchFromList = "http://safe-url.com/oascijmsdoifdjoisdj";
    private static final String urlToSearchNotFromList = "http://unsafe-url.com/oascijmsdoifdjoisdj";
    private static final String domainFromList = "safe-url.com";
    private static final String domainNotFromList = "unsafe-url.com";
    @BeforeAll
    public static void init() {
        String url1 = "http://safe-url.com/oascijmsdoifdjoisdj";
        String url2 = "http://bafybeiatjc366ulydhzw767hbng7crkrfhtodd5lstcihaveyqvffnvexu.ipfs.dweb.link/jd04691_onedriveoe7w.html";
        String url3 = "https://objectstorage.sa-saopaulo-1.oraclecloud.com/n/grcrplvrg8aa/b/bucket-20230608-0554/o/index.html";
        String url4 = "https://www.youtube.com/watch?v=6n3pFFPSlW4";
        urls = List.of(url1, url2, url3, url4);
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlExistsInDatabase() {
        // Arrange
        when(repository.existsByUrlsContains(anyString())).thenReturn(true);

        // Act
        boolean result = service.isSafeUrlByDirectSearch(urlToSearchFromList);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlDoesNotExistInDatabase() {
        // Arrange
        when(repository.existsByUrlsContains(anyString())).thenReturn(false);


        // Act
        boolean result = service.isSafeUrlByDirectSearch(urlToSearchNotFromList);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlExistsInCache() {
        // Arrange
        when(repository.existsByUrlsContains(anyString())).thenReturn(true);

        // Act - create a loop of 10 calls to the method, add results to list
        boolean result = service.isSafeUrlByDirectSearch(urlToSearchFromList);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeUrlByDomainSearch_WhenDomainExistsInDatabase() {
        // Arrange
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(Domain.builder().name(domainFromList).urls(urls).build()));
        // Act
        boolean result = service.isSafeUrlByDomainSearch(urlToSearchFromList);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeUrlByDomainSearch_WhenDomainDoesNotExistInDatabase() {
        // Arrange
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(Domain.builder().name(domainNotFromList).urls(urls).build()));

        // Act
        boolean result = service.isSafeUrlByDomainSearch(urlToSearchNotFromList);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSafeDomain_WhenDomainExistsInDatabase() {
        // Arrange
        when(repository.existsByName(anyString())).thenReturn(true);
        // Act
        boolean result = service.isSafeDomainByUrl(urlToSearchFromList);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeDomain_WhenDomainDoesNotExistInDatabase() {
        // Arrange
        when(repository.existsByName(anyString())).thenReturn(false);
        // Act
        boolean result = service.isSafeDomainByUrl(urlToSearchNotFromList);
        // Assert
        assertTrue(result);
    }
}
