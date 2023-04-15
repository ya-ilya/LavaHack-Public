package com.kisman.cc.util.api.pastebin;

import com.kisman.cc.util.api.pastebin.exception.PasteBinBufferedReaderException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PasteBinAPI {
    public String url;

    public PasteBinAPI(String url) {
        this.url = url;
    }

    public List<String> get() {
        List<String> list = new ArrayList<>();
        try {
            final URL url = new URL(this.url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                list.add(inputLine);
            }
        } catch(Exception e) {
            throw new PasteBinBufferedReaderException("Reading URL(" + url + ") failed!");
        }
        return list;
    }
}
