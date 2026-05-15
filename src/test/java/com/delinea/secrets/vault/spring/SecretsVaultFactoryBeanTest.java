package com.delinea.secrets.vault.spring;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SecretsVaultFactoryBeanTest {

    private SecretsVaultFactoryBean configured(String tld, String baseUrlTemplate) throws Exception {
        SecretsVaultFactoryBean factory = new SecretsVaultFactoryBean();
        ReflectionTestUtils.setField(factory, "tenant", "mytenant");
        ReflectionTestUtils.setField(factory, "clientId", "test-id");
        ReflectionTestUtils.setField(factory, "clientSecret", "test-secret");
        ReflectionTestUtils.setField(factory, "tld", tld);
        ReflectionTestUtils.setField(factory, "baseUrlTemplate", baseUrlTemplate);
        factory.afterPropertiesSet();
        return factory;
    }

    @Test
    void afterPropertiesSet_trimsDotWrappedTld() throws Exception {
        SecretsVaultFactoryBean factory = configured(".com.", SecretsVaultFactoryBean.DEFAULT_BASE_URL_TEMPLATE);
        assertEquals("com", ReflectionTestUtils.getField(factory, "tld"));
    }

    @Test
    void afterPropertiesSet_preservesCleanTld() throws Exception {
        SecretsVaultFactoryBean factory = configured("eu", SecretsVaultFactoryBean.DEFAULT_BASE_URL_TEMPLATE);
        assertEquals("eu", ReflectionTestUtils.getField(factory, "tld"));
    }

    @Test
    void afterPropertiesSet_stripsTrailingSlashFromBaseUrl() throws Exception {
        SecretsVaultFactoryBean factory = configured("com", "https://%s.secretsvaultcloud.%s/v1/");
        String url = (String) ReflectionTestUtils.getField(factory, "baseUrlTemplate");
        assertFalse(url.endsWith("/"));
    }

    @Test
    void getObjectType_returnsSecretsVaultClass() {
        assertEquals(SecretsVault.class, new SecretsVaultFactoryBean().getObjectType());
    }
}
