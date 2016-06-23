/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of manager for HttpClients
 *
 * @author it060587las
 */
public class HttpClientManagerImpl {

    private static Logger logger = LoggerFactory.getLogger(HttpClientManagerImpl.class);

    private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
    };

    private HttpClient client = null;

    HttpClientBuilder manager = null;

    public HttpClientManagerImpl() throws Exception {
        HttpClientBuilder mgr = this.getConnectionManager(1000);
        RequestConfig requestConf = this.getRequestConfiguration(5000, 5000);
        HttpClient httpclient = mgr.setDefaultRequestConfig(requestConf)
                .setDefaultRequestConfig(requestConf)
                .build();
        manager = mgr;
        client = httpclient;
    }

    /**
     * Configuration for connection manager
     *
     * @param maxPerRoute
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private HttpClientBuilder getConnectionManager(int maxPerRoute) throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, UNQUESTIONING_TRUST_MANAGER, null);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setSSLContext(sc).setMaxConnTotal(maxPerRoute);
        builder.setSSLContext(sc).setMaxConnPerRoute(maxPerRoute);
        //PoolingHttpClientConnectionManager p=new PoolingHttpClientConnectionManager();
        return builder;
    }

    /**
     *
     * @param connectTimeout
     * @param connRequestTimeout
     * @return
     */
    private RequestConfig getRequestConfiguration(int connectTimeout, int connRequestTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connRequestTimeout)
                .build();

        return requestConfig;
    }

    public HttpClient getClient() {
        return client;
    }

    
}
