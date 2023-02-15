#!/usr/bin/env python
import os
from time import time

import apicurioregistryclient
from apicurioregistryclient.api import artifacts_api
from dotenv import load_dotenv

from artifact_validator import RegistryArtifactValidator

load_dotenv()

configuration = apicurioregistryclient.Configuration(
    host=os.environ['SERVICE_REGISTRY_URL']
)

# Enable debug mode that prints the request and response
# configuration.debug = True

# Change if your registry instance using different group
# We recommend to use the same group for your artifact types
group_id = "assist"
artifact_id = "drivers_personal_info"


valid_object = {
    "age": 99,
    "name": "ravi",
    "email": "rab4@engineer.com",
    "created_on": "2018-11-13T20:20:39+00:00",
    "mobile_number": "123456789"
}

invalid_object = {
    "age": 110,
    "name": "ravi",
    "email": "rab4u@engineer.com",
    "created_on": "2018-11-13T20:20:39+00:00",
    "mobile_number": "12345678"
}

with apicurioregistryclient.ApiClient(configuration) as api_client:
    # Create an instance of the API class
    api_instance = artifacts_api.ArtifactsApi(api_client)

    # Validate schema
    validator = RegistryArtifactValidator(group_id)
    # Initialize the cache from API
    validator.build_artifacts_cache(api_instance)

    print("\nValidating valid json")
    errors = validator.validate_json_schema(artifact_id, valid_object)
    print("Errors from validator for %s" % valid_object)
    print(errors)

    print("\nValidating invalid json")
    errors = validator.validate_json_schema(artifact_id, invalid_object)
    print("Errors from validator for %s" % invalid_object)
    print(errors)