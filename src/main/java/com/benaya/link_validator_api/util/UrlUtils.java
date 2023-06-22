package com.benaya.link_validator_api.util;

import java.net.MalformedURLException;
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
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
