package com.kisman.cc.util.process.web.microsoft;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class PostRequest {
    public HttpURLConnection conn;
    
    public PostRequest(final String uri) throws Exception {
        (this.conn = (HttpURLConnection)new URL(uri).openConnection()).setRequestMethod("POST");
        this.conn.setDoOutput(true);
        this.conn.setDoInput(true);
    }
    
    public PostRequest header(final String key, final String value) {
        this.conn.setRequestProperty(key, value);
        return this;
    }
    
    public void post(final String s) throws IOException {
        byte[] out = s.getBytes(StandardCharsets.UTF_8);
        this.conn.connect();
        OutputStream os = this.conn.getOutputStream();
        os.write(out);
        os.flush();
        os.close();
    }
    
    public void post(final Map<Object, Object> map) throws Exception {
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<Object, Object> entry : map.entrySet()) sj.add(URLEncoder.encode(entry.getKey().toString(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        this.post(sj.toString());
    }
    
    public int response() throws IOException {
        return this.conn.getResponseCode();
    }
    
    public String body() throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader r = new InputStreamReader(this.conn.getInputStream(), StandardCharsets.UTF_8);
        int i;
        while ((i = r.read()) >= 0) sb.append((char)i);
        r.close();
        return sb.toString();
    }
}
