# The Thycotic Secrets Java SDK

The [Thycotic](https://thycotic.com/)
[DevOps Secrets Vault](https://thycotic.com/products/devops-secrets-vault-password-management/)
(DSV) Java SDK contains classes that interact with the DSV via the REST API.

The SDK contains an API based the [Spring Framework](https://spring.io/projects/spring-framework)
[RestTemplate](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html),
and a simple application based on [Spring Boot](https://spring.io/projects/spring-boot),
that calls the API.

## Build

### Prerequisites

The SDK builds and runs on Java 8 or later.

Apache [Maven](https://maven.apache.org/) is also required to build the SDK.

Maven runs unit and integration tests during the build so the settings in
`src/main/resources/application.properties` must be configured before the build
will succeed.

### Settings

The API authenticates to DSV using a `client_id` and `client_secret`.
The SDK application gets a secret from DSV by it's _path_.

```ini
secrets_vault.tenant = mytenant
secrets_vault.client_id = 359f8c9f-d555-40ff-a036-ce95432e708b
secrets_vault.client_secret = xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
secret.path = path/to/secret
```

## Run the jar

After the SDK application settings are configured the jar can be built:

```bash
mvn package
```

The build runs the SDK application, however, the it also produces an executable
jar capable of accepting properties set via the command-line.

For example:

```bash
java -jar target/dsv-sdk-java-1.0-SNAPSHOT-exec.jar --secret.path=/path/to/a/secret
```

## Use the API

Configure the `SecretsVaultFactoryBean` in the Spring
[ApplicationContext](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)
then inject a `SecretsVault` where required.

```java
@Autowired
private SecretsVault secretsVault;

public static void main(final String[] args) {
    final Secret secret = secretsVault.getSecret("/path/to/secret");

    System.out.println(String.format("The password is %s", secret.getData().get("password")));
}
```
