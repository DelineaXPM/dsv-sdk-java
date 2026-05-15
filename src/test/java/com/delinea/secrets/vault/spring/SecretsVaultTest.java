package com.delinea.secrets.vault.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class SecretsVaultTest {

    private static final String BASE_URL = "https://mytenant.secretsvaultcloud.com/v1";
    private static final String SECRET_JSON =
            "{\"path\":\"/test/secret\",\"version\":1," +
            "\"data\":{\"username\":\"admin\",\"password\":\"s3cr3t\"},\"attributes\":{}}";

    private SecretsVault vault;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        vault = new SecretsVault();
        vault.setUriTemplateHandler(new DefaultUriBuilderFactory(BASE_URL));
        server = MockRestServiceServer.createServer(vault);
    }

    @Test
    void getSecret_stripsLeadingSlash() {
        server.expect(requestTo(BASE_URL + "/secrets/test/secret"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess(SECRET_JSON, MediaType.APPLICATION_JSON));

        Secret secret = vault.getSecret("/test/secret");

        assertNotNull(secret);
        server.verify();
    }

    @Test
    void getSecret_withoutLeadingSlash() {
        server.expect(requestTo(BASE_URL + "/secrets/test/secret"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess(SECRET_JSON, MediaType.APPLICATION_JSON));

        Secret secret = vault.getSecret("test/secret");

        assertNotNull(secret);
        server.verify();
    }

    @Test
    void getSecret_mapsDataFields() {
        server.expect(requestTo(BASE_URL + "/secrets/test/secret"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess(SECRET_JSON, MediaType.APPLICATION_JSON));

        Secret secret = vault.getSecret("test/secret");

        assertEquals("admin", secret.getData().get("username"));
        assertEquals("s3cr3t", secret.getData().get("password"));
    }
}
