package com.thycotic.secrets.vault.spring;

import static java.lang.String.format;

import org.springframework.web.client.RestTemplate;

/**
 * A <a href="https://spring.io/projects/spring-framework">Spring Framework</a>
 * <a href=
 * "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html">RestTemplate</a>
 * with convenience methods specific to the Thycotic DevOps Secrets Vault REST
 * API.
 *
 * <p>
 * Use the {@link SecretsVaultFactoryBean} to create and initialize it.
 */
public class SecretsVault extends RestTemplate {
	private static final String SECRET_PATH_URI = "/secrets/%s";

	/**
	 * Fetch and return a {@link Secret} from Thycotic DevOps Secrets Vault.
	 *
	 * @param path - the path of the secret to be fetched
	 * @return a {@link Secret} object
	 */
	public Secret getSecret(final String path) {
		return getForObject(format(SECRET_PATH_URI, path.replaceAll("^/+", "")), Secret.class);
	}
}
