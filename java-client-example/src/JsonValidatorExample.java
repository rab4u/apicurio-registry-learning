
import java.util.*;


import io.apicurio.registry.resolver.SchemaResolverConfig;
import io.apicurio.registry.resolver.strategy.ArtifactReference;
import io.apicurio.registry.rest.client.RegistryClient;
import io.apicurio.registry.rest.client.RegistryClientFactory;
import io.apicurio.schema.validation.json.JsonValidationResult;
import io.apicurio.schema.validation.json.JsonValidator;


public class JsonValidatorExample {

    RegistryClient client;
    String registryUrl;

    public JsonValidatorExample() {
        registryUrl = System.getenv("SERVICE_REGISTRY_URL");
        client = RegistryClientFactory.create(registryUrl);
    }


    public JsonValidator createJsonValidator(ArtifactReference artifactReference) {
        Map<String, Object> props = new HashMap<>();

        // Configure Service Registry location
        props.putIfAbsent(SchemaResolverConfig.REGISTRY_URL, registryUrl);

        return new JsonValidator(props, Optional.ofNullable(artifactReference));
    }


    public static void main(String [] args) throws Exception {

        String groupId = "assist";
        String artifactId = "drivers_personal_info";

        ArtifactReference artifactReference = ArtifactReference.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .build();

        JsonValidatorExample jve = new JsonValidatorExample();
        JsonValidator validator = jve.createJsonValidator(artifactReference);

        MessageBean valid_msg = new MessageBean(
                "ravi",
                99,
                "rab4@engineer.com",
                "2018-11-13T20:20:39+00:00",
                "123456789"
        );


        JsonValidationResult result = validator.validateByArtifactReference(valid_msg);
        System.out.println("Validation result: " + result);
        System.out.println();

        MessageBean invalid_msg = new MessageBean(
                "ravi",
                101,
                "rab4@engineer.com",
                "2018-11-13T20:20:39+00:00",
                "12345678"
        );

        result = validator.validateByArtifactReference(invalid_msg);
        System.out.println("Validation result: " + result);
        System.out.println();

    }
}