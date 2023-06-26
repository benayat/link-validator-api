package com.benaya.link_validator_api.service.unit;

import com.benaya.link_validator_api.model.Domain;
import com.benaya.link_validator_api.service.CachingService;
import com.benaya.link_validator_api.service.LinksService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinksServiceTest {
    @Mock
    private CachingService cachingService;

    @InjectMocks
    private LinksService service;

    private static List<String> urls;
    private static final String urlToSearchFromList = "http://safe-url.com/oascijmsdoifdjoisdj";
    private static final String urlToSearchNotFromList = "http://unsafe-url.com/oascijmsdoifdjoisdj";
    private static final String domainFromList = "safe-url.com";
    private static final String domainNotFromList = "unsafe-url.com";
    @BeforeAll
    public static void init() {
        urls = List.of("http://safe-url.com/oascijmsdoifdjoisdj",
                "http://bafybeiatjc366ulydhzw767hbng7crkrfhtodd5lstcihaveyqvffnvexu.ipfs.dweb.link/jd04691_onedriveoe7w.html",
                "https://objectstorage.sa-saopaulo-1.oraclecloud.com/n/grcrplvrg8aa/b/bucket-20230608-0554/o/index.html");
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlExistsInDatabase() {
        // Arrange
        when(cachingService.isSafeUrlByDirectSearch(anyString())).thenReturn(false);

        // Act
        boolean result = service.isSafeUrlByDirectSearch(urlToSearchFromList);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeUrlByDirectSearch_WhenUrlDoesNotExistInDatabase() {
        // Arrange
        when(cachingService.isSafeUrlByDirectSearch(anyString())).thenReturn(true);
        // Act
        boolean result = service.isSafeUrlByDirectSearch(urlToSearchNotFromList);
        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSafeUrlByDomainSearch_WhenDomainExistsInDatabase() {
        // Arrange
        when(cachingService.getDomainFromCache(anyString())).thenReturn(Domain.builder().name(domainFromList).urls(urls).build());
        // Act
        boolean result = service.isSafeUrlByDomainSearchThenUrl(urlToSearchFromList);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeUrlByDomainSearch_WhenDomainDoesNotExistInDatabase() {
        // Arrange
        when(cachingService.getDomainFromCache(anyString())).thenReturn(Domain.builder().name(domainNotFromList).urls(urls).build());

        // Act
        boolean result = service.isSafeUrlByDomainSearchThenUrl(urlToSearchNotFromList);

        // Assert
        assertTrue(result);
    }
    @Test
    public void testIsSafeUrlByDomainSearch_WhenDomainExistsInDatabaseButUrlDoesNotExist() {
        // Arrange
        when(cachingService.getDomainFromCache(anyString())).thenReturn(Domain.builder().name(domainFromList).urls(urls).build());

        // Act
        boolean result = service.isSafeUrlByDomainSearchThenUrl(urlToSearchNotFromList);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsSafeDomain_WhenDomainExistsInDatabase() {
        // Arrange
        when(cachingService.isUnsafeDomainCached(anyString())).thenReturn(true);
        // Act
        boolean result = service.isSafeDomainByUrl(urlToSearchFromList);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsSafeDomain_WhenDomainDoesNotExistInDatabase() {
        // Arrange
        when(cachingService.isUnsafeDomainCached(anyString())).thenReturn(false);
        // Act
        boolean result = service.isSafeDomainByUrl(urlToSearchNotFromList);
        // Assert
        assertTrue(result);
    }
}
