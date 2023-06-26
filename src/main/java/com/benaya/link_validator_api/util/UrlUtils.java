package com.benaya.link_validator_api.util;

import com.benaya.link_validator_api.exception.BadUrlException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtils {
    public static String getDomainFromUrl(String url) {
        try {
            URL urlObj = new URL(url);
            return urlObj.getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    public static void validateUrl(String url) {
        try {
            new URL(url).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new BadUrlException(e);
        }
    }
}
