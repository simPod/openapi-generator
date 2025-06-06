/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
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

package org.openapitools.generator.gradle.plugin.tasks

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutputFactory
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import org.gradle.util.GradleVersion
import org.openapitools.codegen.CodegenConstants
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.config.CodegenConfigurator
import org.openapitools.codegen.config.GlobalSettings
import org.openapitools.codegen.config.MergedSpecBuilder

/**
 * A task which generates the desired code.
 *
 * Example (CLI):
 *
 * ./gradlew -q openApiGenerate --input=/path/to/file
 *
 * @author Jim Schubert
 */
@Suppress("UnstableApiUsage")
@CacheableTask
open class GenerateTask @Inject constructor(private val objectFactory: ObjectFactory) : DefaultTask() {

    /**
     * The verbosity of generation
     */
    @Optional
    @Input
    val verbose = project.objects.property<Boolean>()

    /**
     * Whether an input specification should be validated upon generation.
     */
    @Optional
    @Input
    val validateSpec = project.objects.property<Boolean>()

    /**
     * The name of the generator which will handle codegen. (see "openApiGenerators" task)
     */
    @Optional
    @Input
    val generatorName = project.objects.property<String>()

    /**
     * This is the configuration for reference paths where schemas for openapi generation are stored
     * The directory which contains the additional schema files
     */
    @Optional
    @InputDirectory
    @PathSensitive(PathSensitivity.ABSOLUTE)
    val schemaLocation = project.objects.property<String>()

    /**
     * The output target directory into which code will be generated.
     */
    @Optional
    @get:OutputDirectory
    val outputDir = project.objects.property<String>()

    @Suppress("unused")
    @set:Option(option = "input", description = "The input specification.")
    @Internal
    var input: String? = null
        set(value) {
            inputSpec.set(value)
        }

    /**
     * The Open API 2.0/3.x specification location.
     *
     * Be default, Gradle will treat the openApiGenerate task as up-to-date based only on this file, regardless of
     * changes to any $ref referenced files. Use the `inputSpecRootDirectory` property to have Gradle track changes to
     * an entire directory of spec files.
     */
    @Optional
    @get:InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    val inputSpec = project.objects.property<String>()

    /**
     * Local root folder with spec files.
     *
     * By default, a merged spec file will be generated based on the contents of the directory. To disable this, set the
     * `inputSpecRootDirectorySkipMerge` property.
     */
    @Optional
    @get:InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val inputSpecRootDirectory = project.objects.property<String>();

    /**
     * Skip bundling all spec files into a merged spec file, if true.
     */
    @Input
    @Optional
    val inputSpecRootDirectorySkipMerge = project.objects.property<Boolean>()

    /**
     * Name of the file that will contain all merged specs
     */
    @Input
    @Optional
    val mergedFileName = project.objects.property<String>();

    /**
     * The remote Open API 2.0/3.x specification URL location.
     */
    @Input
    @Optional
    val remoteInputSpec = project.objects.property<String>()

    /**
     * The template directory holding a custom template.
     */
    @Optional
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val templateDir = project.objects.property<String?>()

    /**
     * Resource path containing template files.
     */
    @Optional
    @Input
    val templateResourcePath = project.objects.property<String?>()

    /**
     * Adds authorization headers when fetching the OpenAPI definitions remotely.
     * Pass in a URL-encoded string of name:header with a comma separating multiple values
     */
    @Optional
    @Input
    val auth = project.objects.property<String>()

    /**
     * Sets specified global properties.
     */
    @Optional
    @Input
    val globalProperties = project.objects.mapProperty<String, String>()

    /**
     * Path to json configuration file.
     * File content should be in a json format { "optionKey":"optionValue", "optionKey1":"optionValue1"...}
     * Supported options can be different for each language. Run config-help -g {generator name} command for language specific config options.
     */
    @Optional
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    val configFile = project.objects.property<String>()

    /**
     * Specifies if the existing files should be overwritten during the generation.
     */
    @Optional
    @Input
    val skipOverwrite = project.objects.property<Boolean?>()

    /**
     * Package for generated classes (where supported)
     */
    @Optional
    @Input
    val packageName = project.objects.property<String>()

    /**
     * Package for generated api classes
     */
    @Optional
    @Input
    val apiPackage = project.objects.property<String>()

    /**
     * Package for generated models
     */
    @Optional
    @Input
    val modelPackage = project.objects.property<String>()

    /**
     * Prefix that will be prepended to all model names. Default is the empty string.
     */
    @Optional
    @Input
    val modelNamePrefix = project.objects.property<String>()

    /**
     * Suffix that will be appended to all model names. Default is the empty string.
     */
    @Optional
    @Input
    val modelNameSuffix = project.objects.property<String>()

    /**
     * Suffix that will be appended to all api names. Default is the empty string.
     */
    @Optional
    @Input
    val apiNameSuffix = project.objects.property<String>()

    /**
     * Sets instantiation type mappings.
     */
    @Optional
    @Input
    val instantiationTypes = project.objects.mapProperty<String, String>()

    /**
     * Sets mappings between OpenAPI spec types and generated code types.
     */
    @Optional
    @Input
    val typeMappings = project.objects.mapProperty<String, String>()

    /**
     * Sets additional properties that can be referenced by the mustache templates in the format of name=value,name=value.
     * You can also have multiple occurrences of this option.
     */
    @Optional
    @Input
    val additionalProperties = project.objects.mapProperty<String, Any>()

    /**
     * Sets server variable for server URL template substitution, in the format of name=value,name=value.
     * You can also have multiple occurrences of this option.
     */
    @Optional
    @Input
    val serverVariables = project.objects.mapProperty<String, String>()

    /**
     * Specifies additional language specific primitive types in the format of type1,type2,type3,type3. For example: String,boolean,Boolean,Double.
     */
    @Optional
    @Input
    val languageSpecificPrimitives = project.objects.listProperty<String>()

    /**
     * Specifies .openapi-generator-ignore list in the form of relative/path/to/file1,relative/path/to/file2. For example: README.md,pom.xml.
     */
    @Optional
    @Input
    val openapiGeneratorIgnoreList = project.objects.listProperty<String>()

    /**
     * Specifies mappings between a given class and the import that should be used for that class.
     */
    @Optional
    @Input
    val importMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between a given schema and the new one.
     */
    @Optional
    @Input
    val schemaMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between the inline scheme name and the new name
     */
    @Optional
    @Input
    val inlineSchemaNameMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies options for inline schemas
     */
    @Optional
    @Input
    val inlineSchemaOptions = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between the property name and the new name
     */
    @Optional
    @Input
    val nameMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between the parameter name and the new name
     */
    @Optional
    @Input
    val parameterNameMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between the model name and the new name
     */
    @Optional
    @Input
    val modelNameMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between the enum name and the new name
     */
    @Optional
    @Input
    val enumNameMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings between the operation id name and the new name
     */
    @Optional
    @Input
    val operationIdNameMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies mappings (rules) in OpenAPI normalizer
     */
    @Optional
    @Input
    val openapiNormalizer = project.objects.mapProperty<String, String>()

    /**
     * Root package for generated code.
     */
    @Optional
    @Input
    val invokerPackage = project.objects.property<String>()

    /**
     * GroupId in generated pom.xml/build.gradle.kts or other build script. Language-specific conversions occur in non-jvm generators.
     */
    @Optional
    @Input
    val groupId = project.objects.property<String>()

    /**
     * ArtifactId in generated pom.xml/build.gradle.kts or other build script. Language-specific conversions occur in non-jvm generators.
     */
    @Optional
    @Input
    val id = project.objects.property<String>()

    /**
     * Artifact version in generated pom.xml/build.gradle.kts or other build script. Language-specific conversions occur in non-jvm generators.
     */
    @Optional
    @Input
    val version = project.objects.property<String>()

    /**
     * Reference the library template (sub-template) of a generator.
     */
    @Optional
    @Input
    val library = project.objects.property<String?>()

    /**
     * Git host, e.g. gitlab.com.
     */
    @Optional
    @Input
    val gitHost = project.objects.property<String?>()

    /**
     * Git user ID, e.g. openapitools.
     */
    @Optional
    @Input
    val gitUserId = project.objects.property<String?>()

    /**
     * Git repo ID, e.g. openapi-generator.
     */
    @Optional
    @Input
    val gitRepoId = project.objects.property<String?>()

    /**
     * Release note, default to 'Minor update'.
     */
    @Optional
    @Input
    val releaseNote = project.objects.property<String?>()

    /**
     * HTTP user agent, e.g. codegen_csharp_api_client, default to 'OpenAPI-Generator/{packageVersion}/{language}'
     */
    @Optional
    @Input
    val httpUserAgent = project.objects.property<String?>()

    /**
     * Specifies how a reserved name should be escaped to.
     */
    @Optional
    @Input
    val reservedWordsMappings = project.objects.mapProperty<String, String>()

    /**
     * Specifies an override location for the .openapi-generator-ignore file. Most useful on initial generation.
     */
    @Optional
    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    val ignoreFileOverride = project.objects.property<String?>()

    /**
     * Remove prefix of operationId, e.g. config_getId => getId
     */
    @Optional
    @Input
    val removeOperationIdPrefix = project.objects.property<Boolean?>()

    /**
     * Remove examples defined in the operation
     */
    @Optional
    @Input
    val skipOperationExample = project.objects.property<Boolean?>()

    /**
     * Defines which API-related files should be generated. This allows you to create a subset of generated files (or none at all).
     *
     * This option enables/disables generation of ALL api-related files.
     *
     * NOTE: Configuring any one of [apiFilesConstrainedTo], [modelFilesConstrainedTo], or [supportingFilesConstrainedTo] results
     *   in others being disabled. That is, OpenAPI Generator considers any one of these to define a subset of generation.
     *   For more control over generation of individual files, configure an ignore file and refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val apiFilesConstrainedTo = project.objects.listProperty<String>()

    /**
     * Defines which model-related files should be generated. This allows you to create a subset of generated files (or none at all).
     *
     * NOTE: Configuring any one of [apiFilesConstrainedTo], [modelFilesConstrainedTo], or [supportingFilesConstrainedTo] results
     *   in others being disabled. That is, OpenAPI Generator considers any one of these to define a subset of generation.
     *   For more control over generation of individual files, configure an ignore file and refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val modelFilesConstrainedTo = project.objects.listProperty<String>()

    /**
     * Defines which supporting files should be generated. This allows you to create a subset of generated files (or none at all).
     *
     * Supporting files are those related to `projects/frameworks` which may be modified
     * by consumers.
     *
     * NOTE: Configuring any one of [apiFilesConstrainedTo], [modelFilesConstrainedTo], or [supportingFilesConstrainedTo] results
     *   in others being disabled. That is, OpenAPI Generator considers any one of these to define a subset of generation.
     *   For more control over generation of individual files, configure an ignore file and refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val supportingFilesConstrainedTo = project.objects.listProperty<String>()

    /**
     * Defines whether model-related _test_ files should be generated.
     *
     * This option enables/disables generation of ALL model-related _test_ files.
     *
     * For more control over generation of individual files, configure an ignore file and
     * refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val generateModelTests = project.objects.property<Boolean>()

    /**
     * Defines whether model-related _documentation_ files should be generated.
     *
     * This option enables/disables generation of ALL model-related _documentation_ files.
     *
     * For more control over generation of individual files, configure an ignore file and
     * refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val generateModelDocumentation = project.objects.property<Boolean>()

    /**
     * Defines whether api-related _test_ files should be generated.
     *
     * This option enables/disables generation of ALL api-related _test_ files.
     *
     * For more control over generation of individual files, configure an ignore file and
     * refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val generateApiTests = project.objects.property<Boolean>()

    /**
     * Defines whether api-related _documentation_ files should be generated.
     *
     * This option enables/disables generation of ALL api-related _documentation_ files.
     *
     * For more control over generation of individual files, configure an ignore file and
     * refer to it via [ignoreFileOverride].
     */
    @Optional
    @Input
    val generateApiDocumentation = project.objects.property<Boolean>()

    /**
     * To write all log messages (not just errors) to STDOUT
     */
    @Optional
    @Input
    val logToStderr = project.objects.property<Boolean>()

    /**
     * To enable the file post-processing hook. This enables executing an external post-processor (usually a linter program).
     * This only enables the post-processor. To define the post-processing command, define an environment variable such as
     * LANG_POST_PROCESS_FILE (e.g. GO_POST_PROCESS_FILE, SCALA_POST_PROCESS_FILE). Please open an issue if your target
     * generator does not support this functionality.
     */
    @Optional
    @Input
    val enablePostProcessFile = project.objects.property<Boolean>()

    /**
     * To skip spec validation. When true, we will skip the default behavior of validating a spec before generation.
     */
    @Optional
    @Input
    val skipValidateSpec = project.objects.property<Boolean>()

    /**
     * To generate alias (array, list, map) as model. When false, top-level objects defined as array, list, or map will result in those
     * definitions generated as top-level Array-of-items, List-of-items, Map-of-items definitions.
     * When true, A model representation either containing or extending the array,list,map (depending on specific generator implementation) will be generated.
     */
    @Optional
    @Input
    val generateAliasAsModel = project.objects.property<Boolean>()

    /**
     * A dynamic map of options specific to a generator.
     */
    @Optional
    @Input
    val configOptions = project.objects.mapProperty<String, String>()

    /**
     * Templating engine: "mustache" (default) or "handlebars" (beta)
     */
    @Optional
    @Input
    val engine = project.objects.property<String?>()

    /**
     * Defines whether the output dir should be cleaned up before generating the output.
     *
     */
    @Optional
    @Input
    val cleanupOutput = project.objects.property<Boolean>()

    /**
     * Defines whether the generator should run in dry-run mode.
     */
    @Optional
    @Input
    val dryRun = project.objects.property<Boolean>()

    private fun <T : Any?> Property<T>.ifNotEmpty(block: Property<T>.(T) -> Unit) {
        if (isPresent) {
            val item: T? = get()
            if (item != null) {
                when (get()) {
                    is String -> if ((get() as String).isNotEmpty()) {
                        block(get())
                    }
                    is String? -> if (true == (get() as String?)?.isNotEmpty()) {
                        block(get())
                    }
                    else -> block(get())
                }
            }
        }
    }

    protected open fun createDefaultCodegenConfigurator(): CodegenConfigurator = CodegenConfigurator()

    private fun createFileSystemManager(): FileSystemManager {
        return if(GradleVersion.current() >= GradleVersion.version("6.0")) {
            objectFactory.newInstance(FileSystemManagerDefault::class.java)
        } else {
            objectFactory.newInstance(FileSystemManagerLegacy::class.java, project)
        }
    }

    @Suppress("unused")
    @TaskAction
    fun doWork() {
        var resolvedInputSpec = ""

        inputSpec.ifNotEmpty { value ->
            resolvedInputSpec = value
        }

        remoteInputSpec.ifNotEmpty { value ->
            resolvedInputSpec = value
        }

        inputSpecRootDirectory.ifNotEmpty { inputSpecRootDirectoryValue ->
            val skipMerge = inputSpecRootDirectorySkipMerge.get()
            val runMergeSpec = !skipMerge
            if (runMergeSpec) {
                run {
                    resolvedInputSpec = MergedSpecBuilder(
                        inputSpecRootDirectoryValue,
                        mergedFileName.getOrElse("merged")
                    ).buildMergedSpec()
                    logger.info("Merge input spec would be used - {}", resolvedInputSpec)
                }
            }
        }

        cleanupOutput.ifNotEmpty { cleanup ->
            if (cleanup) {
                createFileSystemManager().delete(outputDir)
                val out = services.get(StyledTextOutputFactory::class.java).create("openapi")
                out.withStyle(StyledTextOutput.Style.Success)
                out.println("Cleaned up output directory ${outputDir.get()} before code generation (cleanupOutput set to true).")
            }
        }

        val configurator: CodegenConfigurator = if (configFile.isPresent) {
            CodegenConfigurator.fromFile(configFile.get())
        } else createDefaultCodegenConfigurator()

        try {
            if (globalProperties.isPresent) {
                globalProperties.get().forEach { (key, value) ->
                    configurator.addGlobalProperty(key, value)
                }
            }

            if (supportingFilesConstrainedTo.isPresent && supportingFilesConstrainedTo.get().isNotEmpty()) {
                GlobalSettings.setProperty(
                    CodegenConstants.SUPPORTING_FILES,
                    supportingFilesConstrainedTo.get().joinToString(",")
                )
            } else {
                GlobalSettings.clearProperty(CodegenConstants.SUPPORTING_FILES)
            }

            if (modelFilesConstrainedTo.isPresent && modelFilesConstrainedTo.get().isNotEmpty()) {
                GlobalSettings.setProperty(CodegenConstants.MODELS, modelFilesConstrainedTo.get().joinToString(","))
            } else {
                GlobalSettings.clearProperty(CodegenConstants.MODELS)
            }

            if (apiFilesConstrainedTo.isPresent && apiFilesConstrainedTo.get().isNotEmpty()) {
                GlobalSettings.setProperty(CodegenConstants.APIS, apiFilesConstrainedTo.get().joinToString(","))
            } else {
                GlobalSettings.clearProperty(CodegenConstants.APIS)
            }

            if (generateApiDocumentation.isPresent) {
                GlobalSettings.setProperty(CodegenConstants.API_DOCS, generateApiDocumentation.get().toString())
            }

            if (generateModelDocumentation.isPresent) {
                GlobalSettings.setProperty(CodegenConstants.MODEL_DOCS, generateModelDocumentation.get().toString())
            }

            if (generateModelTests.isPresent) {
                GlobalSettings.setProperty(CodegenConstants.MODEL_TESTS, generateModelTests.get().toString())
            }

            if (generateApiTests.isPresent) {
                GlobalSettings.setProperty(CodegenConstants.API_TESTS, generateApiTests.get().toString())
            }

            if (inputSpec.isPresent && remoteInputSpec.isPresent) {
                logger.warn("Both inputSpec and remoteInputSpec is specified. The remoteInputSpec will take priority over inputSpec.")
            }

            configurator.setInputSpec(resolvedInputSpec)

            // now override with any specified parameters
            verbose.ifNotEmpty { value ->
                configurator.setVerbose(value)
            }

            validateSpec.ifNotEmpty { value ->
                configurator.setValidateSpec(value)
            }

            skipOverwrite.ifNotEmpty { value ->
                configurator.setSkipOverwrite(value ?: false)
            }

            generatorName.ifNotEmpty { value ->
                configurator.setGeneratorName(value)
            }

            outputDir.ifNotEmpty { value ->
                configurator.setOutputDir(value)
            }

            auth.ifNotEmpty { value ->
                configurator.setAuth(value)
            }

            templateDir.ifNotEmpty { value ->
                configurator.setTemplateDir(value)
            }

            templateResourcePath.ifNotEmpty { value ->
                templateDir.ifNotEmpty {
                    logger.warn("Both templateDir and templateResourcePath were configured. templateResourcePath overwrites templateDir.")
                }
                configurator.setTemplateDir(value)
            }

            packageName.ifNotEmpty { value ->
                configurator.setPackageName(value)
            }

            apiPackage.ifNotEmpty { value ->
                configurator.setApiPackage(value)
            }

            modelPackage.ifNotEmpty { value ->
                configurator.setModelPackage(value)
            }

            modelNamePrefix.ifNotEmpty { value ->
                configurator.setModelNamePrefix(value)
            }

            modelNameSuffix.ifNotEmpty { value ->
                configurator.setModelNameSuffix(value)
            }

            apiNameSuffix.ifNotEmpty { value ->
                configurator.setApiNameSuffix(value)
            }

            invokerPackage.ifNotEmpty { value ->
                configurator.setInvokerPackage(value)
            }

            groupId.ifNotEmpty { value ->
                configurator.setGroupId(value)
            }

            id.ifNotEmpty { value ->
                configurator.setArtifactId(value)
            }

            version.ifNotEmpty { value ->
                configurator.setArtifactVersion(value)
            }

            library.ifNotEmpty { value ->
                configurator.setLibrary(value)
            }

            gitHost.ifNotEmpty { value ->
                configurator.setGitHost(value)
            }

            gitUserId.ifNotEmpty { value ->
                configurator.setGitUserId(value)
            }

            gitRepoId.ifNotEmpty { value ->
                configurator.setGitRepoId(value)
            }

            releaseNote.ifNotEmpty { value ->
                configurator.setReleaseNote(value)
            }

            httpUserAgent.ifNotEmpty { value ->
                configurator.setHttpUserAgent(value)
            }

            ignoreFileOverride.ifNotEmpty { value ->
                configurator.setIgnoreFileOverride(value)
            }

            removeOperationIdPrefix.ifNotEmpty { value ->
                configurator.setRemoveOperationIdPrefix(value!!)
            }

            skipOperationExample.ifNotEmpty { value ->
                configurator.setSkipOperationExample(value!!)
            }

            logToStderr.ifNotEmpty { value ->
                configurator.setLogToStderr(value)
            }

            enablePostProcessFile.ifNotEmpty { value ->
                configurator.setEnablePostProcessFile(value)
            }

            skipValidateSpec.ifNotEmpty { value ->
                configurator.setValidateSpec(!value)
            }

            generateAliasAsModel.ifNotEmpty { value ->
                configurator.setGenerateAliasAsModel(value)
            }

            engine.ifNotEmpty { value ->
                if ("handlebars".equals(value, ignoreCase = true)) {
                    configurator.setTemplatingEngineName("handlebars")
                } else {
                    configurator.setTemplatingEngineName(value)
                }
            }

            if (globalProperties.isPresent) {
                globalProperties.get().forEach { entry ->
                    configurator.addGlobalProperty(entry.key, entry.value)
                }
            }

            if (instantiationTypes.isPresent) {
                instantiationTypes.get().forEach { entry ->
                    configurator.addInstantiationType(entry.key, entry.value)
                }
            }

            if (importMappings.isPresent) {
                importMappings.get().forEach { entry ->
                    configurator.addImportMapping(entry.key, entry.value)
                }
            }

            if (schemaMappings.isPresent) {
                schemaMappings.get().forEach { entry ->
                    configurator.addSchemaMapping(entry.key, entry.value)
                }
            }

            if (inlineSchemaNameMappings.isPresent) {
                inlineSchemaNameMappings.get().forEach { entry ->
                    configurator.addInlineSchemaNameMapping(entry.key, entry.value)
                }
            }

            if (inlineSchemaOptions.isPresent) {
                inlineSchemaOptions.get().forEach { entry ->
                    configurator.addInlineSchemaOption(entry.key, entry.value)
                }
            }

            if (nameMappings.isPresent) {
                nameMappings.get().forEach { entry ->
                    configurator.addNameMapping(entry.key, entry.value)
                }
            }

            if (parameterNameMappings.isPresent) {
                parameterNameMappings.get().forEach { entry ->
                    configurator.addParameterNameMapping(entry.key, entry.value)
                }
            }

            if (modelNameMappings.isPresent) {
                modelNameMappings.get().forEach { entry ->
                    configurator.addModelNameMapping(entry.key, entry.value)
                }
            }

            if (enumNameMappings.isPresent) {
                enumNameMappings.get().forEach { entry ->
                    configurator.addEnumNameMapping(entry.key, entry.value)
                }
            }

            if (operationIdNameMappings.isPresent) {
                operationIdNameMappings.get().forEach { entry ->
                    configurator.addOperationIdNameMapping(entry.key, entry.value)
                }
            }

            if (openapiNormalizer.isPresent) {
                openapiNormalizer.get().forEach { entry ->
                    configurator.addOpenapiNormalizer(entry.key, entry.value)
                }
            }

            if (typeMappings.isPresent) {
                typeMappings.get().forEach { entry ->
                    configurator.addTypeMapping(entry.key, entry.value)
                }
            }

            if (additionalProperties.isPresent) {
                additionalProperties.get().forEach { entry ->
                    configurator.addAdditionalProperty(entry.key, entry.value)
                }
            }

            if (serverVariables.isPresent) {
                serverVariables.get().forEach { entry ->
                    configurator.addServerVariable(entry.key, entry.value)
                }
            }

            if (languageSpecificPrimitives.isPresent) {
                languageSpecificPrimitives.get().forEach {
                    configurator.addLanguageSpecificPrimitive(it)
                }
            }

            if (openapiGeneratorIgnoreList.isPresent) {
                openapiGeneratorIgnoreList.get().forEach {
                    configurator.addOpenapiGeneratorIgnoreList(it)
                }
            }

            if (reservedWordsMappings.isPresent) {
                reservedWordsMappings.get().forEach { entry ->
                    configurator.addAdditionalReservedWordMapping(entry.key, entry.value)
                }
            }

            var dryRunSetting = false
            dryRun.ifNotEmpty { setting ->
                dryRunSetting = setting
            }

            val clientOptInput = configurator.toClientOptInput()
            val codegenConfig = clientOptInput.config

            if (configOptions.isPresent) {
                val userSpecifiedConfigOptions = configOptions.get()
                codegenConfig.cliOptions().forEach {
                    if (userSpecifiedConfigOptions.containsKey(it.opt)) {
                        clientOptInput.config.additionalProperties()[it.opt] = userSpecifiedConfigOptions[it.opt]
                    }
                }
            }

            try {
                val out = services.get(StyledTextOutputFactory::class.java).create("openapi")
                out.withStyle(StyledTextOutput.Style.Success)

                DefaultGenerator(dryRunSetting).opts(clientOptInput).generate()

                out.println("Successfully generated code to ${outputDir.get()}")
            } catch (e: RuntimeException) {
                throw GradleException("Code generation failed.", e)
            }
        } finally {
            GlobalSettings.reset()
        }
    }
}

internal interface FileSystemManager {

    fun delete(outputDir: Property<String>)

}

internal open class FileSystemManagerLegacy @Inject constructor(private val project: Project): FileSystemManager {

    override fun delete(outputDir: Property<String>) {
        project.delete(outputDir)
    }
}

internal open class FileSystemManagerDefault @Inject constructor(private val fs: FileSystemOperations) : FileSystemManager {

    override fun delete(outputDir: Property<String>) {
        fs.delete { delete(outputDir) }
    }
}
