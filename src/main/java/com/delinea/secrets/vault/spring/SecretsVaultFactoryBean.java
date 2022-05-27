package com.delinea.secrets.vault.spring;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

/**
 * Creates an initializes a {@link SecretsVault} object using Spring application
 * properties.
 *
 * <p>
 * The required properties are:
 * <ul>
 * <li>{@code secrets_vault.tenant}
 * <li>{@code secrets_vault.client_id}
 * <li>{@code secrets_vault.client_secret}
 * </ul>
 *
 * <p>
 * The SDK gets these properties from the Spring Boot
 * {@code application.properties} file in {@code src/main/resources} by default.
 *
 */
@Component
public class SecretsVaultFactoryBean implements FactoryBean<SecretsVault>, InitializingBean {
    public static final String DEFAULT_BASE_URL_TEMPLATE = "https://%s.secretsvaultcloud.%s/v1",
            DEFAULT_TLD = "com";

    static class AccessGrant {
        private String accessToken, refreshToken, tokenType;
        private int expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public String getTokenType() {
            return tokenType;
        }
    };

    private static final String GRANT_REQUEST_CLIENT_ID_PROPERTY = "client_id";

    private static final String GRANT_REQUEST_CLIENT_SECRET_PROPERTY = "client_secret";

    private static final String GRANT_REQUEST_GRANT_TYPE_PROPERTY = "grant_type";

    private static final String GRANT_REQUEST_GRANT_TYPE = "client_credentials";

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private static final String AUTHORIZATION_TOKEN_TYPE = "Bearer";

    @Value("${secrets_vault.base_url_template:" + DEFAULT_BASE_URL_TEMPLATE + "}")
    private String baseUrlTemplate;

    @Value("${secrets_vault.tld:" + DEFAULT_TLD + "}")
    private String tld;

    @Value("${secrets_vault.client_id}")
    private String clientId;

    @Value("${secrets_vault.client_secret}")
    private String clientSecret;

    @Value("${secrets_vault.tenant}")
    private String tenant;

    @Autowired(required = false)
    private ClientHttpRequestFactory requestFactory;

    private UriBuilderFactory uriBuilderFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        tld = tld.replaceAll("^\\.*(.*?)\\.*$", "$1"); // .com. -> com
        baseUrlTemplate = baseUrlTemplate.replaceAll("/*$", ""); // .../ -> ...
        uriBuilderFactory = new DefaultUriBuilderFactory(
                fromUriString(String.format(baseUrlTemplate, tenant, tld)));
        if (requestFactory == null)
            requestFactory = new SimpleClientHttpRequestFactory();
    }

    private AccessGrant getAccessGrant() {
        final Map<String, Object> request = new HashMap<String, Object>();
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        request.put(GRANT_REQUEST_GRANT_TYPE_PROPERTY, GRANT_REQUEST_GRANT_TYPE);
        request.put(GRANT_REQUEST_CLIENT_ID_PROPERTY, clientId);
        request.put(GRANT_REQUEST_CLIENT_SECRET_PROPERTY, clientSecret);

        return new RestTemplate().postForObject(String.format(baseUrlTemplate, tenant, tld) + "/token",
                new HttpEntity<Map<String, Object>>(request, headers), AccessGrant.class);
    }

    @Override
    public SecretsVault getObject() throws Exception {
        final SecretsVault secretsVault = new SecretsVault();

        secretsVault.setUriTemplateHandler(uriBuilderFactory);
        secretsVault.setRequestFactory( // Add the 'Authorization: Bearer {accessGrant.accessToken}' HTTP header
                new InterceptingClientHttpRequestFactory(requestFactory, Arrays.asList((request, body, execution) -> {
                    request.getHeaders().add(AUTHORIZATION_HEADER_NAME,
                            String.format("%s %s", AUTHORIZATION_TOKEN_TYPE, getAccessGrant().accessToken));
                    return execution.execute(request, body);
                })));
        return secretsVault;
    }

    @Override
    public Class<?> getObjectType() {
        return SecretsVault.class;
    }

}
