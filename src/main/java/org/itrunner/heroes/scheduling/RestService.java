package org.itrunner.heroes.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.Map;

@Service
@Slf4j
public class RestService {
    private static final String TOKEN_URL_FORMAT = "%s/realms/%s/protocol/openid-connect/token";

    @Value("${api.offline-token}")
    private String offlineToken;

    private final KeycloakSpringBootProperties keycloakSpringBootProperties;

    private RestTemplate restTemplate;

    public RestService(KeycloakSpringBootProperties keycloakSpringBootProperties) {
        this.keycloakSpringBootProperties = keycloakSpringBootProperties;

        initRestTemplate();
    }

    public <T> ResponseEntity<T> requestForEntity(String uri, HttpMethod requestMethod, Class<T> responseType, Map<String, ?> uriVariables) {
        if (uriVariables == null) {
            return requestForEntity(uri, requestMethod, responseType);
        }

        return restTemplate.exchange(uri, requestMethod, getRequestEntityWithBearerHeader(), responseType, uriVariables);
    }

    public <T> ResponseEntity<T> requestForEntity(String uri, HttpMethod requestMethod, Class<T> responseType) {
        return restTemplate.exchange(uri, requestMethod, getRequestEntityWithBearerHeader(), responseType);
    }

    private void initRestTemplate() {
        createSslRestTemplate();
    }

    private void createSslRestTemplate() {
        try {
            SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true).build();
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"SSLv3", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

            HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            this.restTemplate = new RestTemplate(requestFactory);
        } catch (Exception e) {
            log.error("Error occurred while creating SSLContext", e);
            this.restTemplate = new RestTemplate();
        }
    }

    private HttpEntity getRequestEntityWithBearerHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + getAccessToken());
        return new HttpEntity(httpHeaders);
    }

    private String getAccessToken() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", keycloakSpringBootProperties.getResource());
        map.add("refresh_token", offlineToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, requestHeaders);

        String tokenUrl = String.format(TOKEN_URL_FORMAT, keycloakSpringBootProperties.getAuthServerUrl(), keycloakSpringBootProperties.getRealm());
        Map<String, String> response = restTemplate.postForObject(tokenUrl, requestEntity, Map.class);
        return response.get("access_token");
    }

}