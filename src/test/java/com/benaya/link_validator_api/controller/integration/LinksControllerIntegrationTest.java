package com.benaya.link_validator_api.controller.integration;

import com.benaya.link_validator_api.model.UrlSafety;
import com.benaya.link_validator_api.service.LinksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class LinksControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinksService linksService;

    @Test
    public void testValidateLinkDirectSearchTrue_WhenUrlExists_ShouldReturnValid() throws Exception {
        String url = "http://safe-url.com";
        when(linksService.isSafeUrlByDirectSearch(url)).thenReturn(true);

        mockMvc.perform(get("/validateLink-url")
                        .param("directSearch", "true")
                        .content(url)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + UrlSafety.VALID.getValue() + "\""));

        verify(linksService, times(1)).isSafeUrlByDirectSearch(url);
    }
    @Test
    public void testValidateLinkDirectSearchTrue_WhenUrlDoesNotExist_ShouldReturnInvalid() throws Exception {
        String url = "http://unsafe-url.com";
        when(linksService.isSafeUrlByDirectSearch(url)).thenReturn(false);

        mockMvc.perform(get("/validateLink-url")
                        .param("directSearch", "true")
                        .content(url)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + UrlSafety.INVALID.getValue() + "\""));

        verify(linksService, times(1)).isSafeUrlByDirectSearch(url);
    }
    @Test
    public void testValidateLinkDirectSearchTrue_WhenUrlIsMalformed_ShouldReturnError() throws Exception {
        String malformedUrl = "htp://unsafe-url.com";

        mockMvc.perform(get("/validateLink-url")
                        .param("directSearch", "true")
                        .content(malformedUrl)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isUnprocessableEntity());
        verify(linksService, times(0)).isSafeUrlByDirectSearch(malformedUrl);
    }
    @Test
    public void testValidateLinkDirectSearchFalse_WhenUrlDoesNotExist_ShouldReturnValid() throws Exception {
        String url = "http://safe-url.com";
        when(linksService.isSafeUrlByDomainSearchThenUrl(url)).thenReturn(true);

        mockMvc.perform(get("/validateLink-url")
                        .param("directSearch", "false")
                        .content(url)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + UrlSafety.VALID.getValue() + "\""));

        verify(linksService, times(1)).isSafeUrlByDomainSearchThenUrl(url);
    }


    @Test
    public void testValidateLinkDirectSearchFalse_WhenUrlExists_ShouldReturnInvalid() throws Exception {
        String url = "http://safe-url.com";
        when(linksService.isSafeUrlByDomainSearchThenUrl(url)).thenReturn(false);

        mockMvc.perform(get("/validateLink-url")
                        .param("directSearch", "false")
                        .content(url)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + UrlSafety.INVALID.getValue() + "\""));

        verify(linksService, times(1)).isSafeUrlByDomainSearchThenUrl(url);
    }
    @Test
    public void testValidateLinkDirectSearchFalse_WhenUrlIsMalformed_ShouldReturnError() throws Exception {
        String malformedUrl = "ttp://unsafe-url";

        mockMvc.perform(get("/validateLink-url")
                        .param("directSearch", "false")
                        .content(malformedUrl)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isUnprocessableEntity());
        verify(linksService, times(0)).isSafeUrlByDomainSearchThenUrl(malformedUrl);
    }

    @Test
    public void testValidateLinkByDomain_WhenDomainExists_ShouldReturnInvalid() throws Exception {
        String url = "http://unsafe-url.com";
        when(linksService.isSafeDomainByUrl(url)).thenReturn(false);

        mockMvc.perform(get("/validateLink-domain")
                        .content(url)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + UrlSafety.INVALID.getValue() + "\""));
        verify(linksService, times(1)).isSafeDomainByUrl(url);
    }
    @Test
    public void testValidateLinkByDomain_WhenDomainDoesNotExist_ShouldReturnValid() throws Exception {
        String url = "http://safe-url.com";
        when(linksService.isSafeDomainByUrl(url)).thenReturn(true);

        mockMvc.perform(get("/validateLink-domain")
                        .content(url)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + UrlSafety.VALID.getValue() + "\""));
        verify(linksService, times(1)).isSafeDomainByUrl(url);
    }
    @Test
    public void testValidateLinkByDomain_WhenUrlIsMalformed_ShouldReturnError() throws Exception {
        String malformedUrl = "http-unsafe-url.com/oidfjdoivfre";

        mockMvc.perform(get("/validateLink-domain")
                        .content(malformedUrl)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isUnprocessableEntity());
        verify(linksService, times(0)).isSafeDomainByUrl(malformedUrl);
    }
}
