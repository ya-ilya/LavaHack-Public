package com.kisman.cc.util.process.web.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpTools {
    public static boolean ping(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            return true;
        } catch (final MalformedURLException e) {
            throw new IllegalStateException("Bad URL: " + url, e);
        } catch (final IOException e) {
            return false;
        }
    }
}
