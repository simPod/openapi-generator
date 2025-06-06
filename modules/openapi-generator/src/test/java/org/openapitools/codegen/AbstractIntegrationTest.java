/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.testutils.IntegrationTestPathsConfig;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.openapitools.codegen.testutils.AssertFile.assertPathEqualsRecursively;

public abstract class AbstractIntegrationTest {

    protected abstract IntegrationTestPathsConfig getIntegrationTestPathsConfig();

    protected abstract CodegenConfig getCodegenConfig();

    protected abstract Map<String, String> configProperties();

    protected Boolean generateMetadata = true;

    protected Map<String, String> globalPropertyOverrides = new HashMap<>();

    // @wing328: ignore for the time being until we fix the error with the integration test
    @Test(enabled = false)
    public void generatesCorrectDirectoryStructure() throws IOException {
        DefaultGenerator codeGen = new DefaultGenerator();
        codeGen.setGenerateMetadata(generateMetadata);
        for (Map.Entry<String, String> propertyOverride : globalPropertyOverrides.entrySet()) {
            codeGen.setGeneratorPropertyDefault(propertyOverride.getKey(), propertyOverride.getValue());
        }

        IntegrationTestPathsConfig integrationTestPathsConfig = getIntegrationTestPathsConfig();

        String specContent = Files.lines(integrationTestPathsConfig.getSpecPath()).collect(Collectors.joining("\n"));
        OpenAPI openAPI = TestUtils.parseContent(specContent);


        CodegenConfig codegenConfig = getCodegenConfig();
        codegenConfig.setOutputDir(integrationTestPathsConfig.getOutputPath().toString());
        codegenConfig.setIgnoreFilePathOverride(integrationTestPathsConfig.getIgnoreFilePath().toFile().toString());
        ClientOptInput opts = new ClientOptInput()
                .config(codegenConfig)
                .openAPI(openAPI);

        codegenConfig.additionalProperties().putAll(configProperties());

        codeGen.opts(opts).generate();

        assertPathEqualsRecursively(integrationTestPathsConfig.getExpectedPath(), integrationTestPathsConfig.getOutputPath());
    }
}
