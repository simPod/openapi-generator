---
title: Documentation for the java-play-framework Generator
---

## METADATA

| Property | Value | Notes |
| -------- | ----- | ----- |
| generator name | java-play-framework | pass this to the generate command after -g |
| generator stability | STABLE | |
| generator type | SERVER | |
| generator language | Java | |
| generator default templating engine | mustache | |
| helpTxt | Generates a Java Play Framework Server application. | |

## CONFIG OPTIONS
These options may be applied as additional-properties (cli) or configOptions (plugins). Refer to [configuration docs](https://openapi-generator.tech/docs/configuration) for more details.

| Option | Description | Values | Default |
| ------ | ----------- | ------ | ------- |
|additionalEnumTypeAnnotations|Additional annotations for enum type(class level annotations)| |null|
|additionalModelTypeAnnotations|Additional annotations for model type(class level annotations). List separated by semicolon(;) or new line (Linux or Windows)| |null|
|additionalOneOfTypeAnnotations|Additional annotations for oneOf interfaces(class level annotations). List separated by semicolon(;) or new line (Linux or Windows)| |null|
|allowUnicodeIdentifiers|boolean, toggles whether unicode identifiers are allowed in names or not, default is false| |false|
|apiPackage|package for generated api classes| |controllers|
|artifactDescription|artifact description in generated pom.xml| |OpenAPI Java|
|artifactId|artifactId in generated pom.xml. This also becomes part of the generated library's filename| |openapi-java-playframework|
|artifactUrl|artifact URL in generated pom.xml| |https://github.com/openapitools/openapi-generator|
|artifactVersion|artifact version in generated pom.xml. This also becomes part of the generated library's filename. If not provided, uses the version from the OpenAPI specification file. If that's also not present, uses the default value of the artifactVersion option.| |1.0.0|
|basePackage|base package for generated code| |org.openapitools|
|bigDecimalAsString|Treat BigDecimal values as Strings to avoid precision loss.| |false|
|booleanGetterPrefix|Set booleanGetterPrefix| |get|
|camelCaseDollarSign|Fix camelCase when starting with $ sign. when true : $Value when false : $value| |false|
|configPackage|configuration package for generated code| |org.openapitools.configuration|
|containerDefaultToNull|Set containers (array, set, map) default to null| |false|
|controllerOnly|Whether to generate only API interface stubs without the server files.| |false|
|dateLibrary|Option. Date library to use|<dl><dt>**joda**</dt><dd>Joda (for legacy app only)</dd><dt>**legacy**</dt><dd>Legacy java.util.Date</dd><dt>**java8-localdatetime**</dt><dd>Java 8 using LocalDateTime (for legacy app only)</dd><dt>**java8**</dt><dd>Java 8 native JSR310 (preferred for jdk 1.8+)</dd></dl>|java8|
|developerEmail|developer email in generated pom.xml| |team@openapitools.org|
|developerName|developer name in generated pom.xml| |OpenAPI-Generator Contributors|
|developerOrganization|developer organization in generated pom.xml| |OpenAPITools.org|
|developerOrganizationUrl|developer organization URL in generated pom.xml| |http://openapitools.org|
|disableHtmlEscaping|Disable HTML escaping of JSON strings when using gson (needed to avoid problems with byte[] fields)| |false|
|disallowAdditionalPropertiesIfNotPresent|If false, the 'additionalProperties' implementation (set to true by default) is compliant with the OAS and JSON schema specifications. If true (default), keep the old (incorrect) behaviour that 'additionalProperties' is set to false by default.|<dl><dt>**false**</dt><dd>The 'additionalProperties' implementation is compliant with the OAS and JSON schema specifications.</dd><dt>**true**</dt><dd>Keep the old (incorrect) behaviour that 'additionalProperties' is set to false by default.</dd></dl>|true|
|discriminatorCaseSensitive|Whether the discriminator value lookup should be case-sensitive or not. This option only works for Java API client| |true|
|ensureUniqueParams|Whether to ensure parameter names are unique in an operation (rename parameters that are not).| |true|
|enumPropertyNaming|Naming convention for enum properties: 'MACRO_CASE' and 'legacy'| |MACRO_CASE|
|enumUnknownDefaultCase|If the server adds new enum cases, that are unknown by an old spec/client, the client will fail to parse the network response.With this option enabled, each enum will have a new case, 'unknown_default_open_api', so that when the server sends an enum case that is not known by the client/spec, they can safely fallback to this case.|<dl><dt>**false**</dt><dd>No changes to the enum's are made, this is the default option.</dd><dt>**true**</dt><dd>With this option enabled, each enum will have a new case, 'unknown_default_open_api', so that when the enum case sent by the server is not known by the client/spec, can safely be decoded to this case.</dd></dl>|false|
|generateBuilders|Whether to generate builders for models| |false|
|generateConstructorWithAllArgs|whether to generate a constructor for all arguments| |false|
|groupId|groupId in generated pom.xml| |org.openapitools|
|handleExceptions|Add a 'throw exception' to each controller function. Add also a custom error handler where you can put your custom logic| |true|
|hideGenerationTimestamp|Hides the generation timestamp when files are generated.| |false|
|ignoreAnyOfInEnum|Ignore anyOf keyword in enum| |false|
|implicitHeaders|Skip header parameters in the generated API methods using @ApiImplicitParams annotation.| |false|
|implicitHeadersRegex|Skip header parameters that matches given regex in the generated API methods using @ApiImplicitParams annotation. Note: this parameter is ignored when implicitHeaders=true| |null|
|invokerPackage|root package for generated code| |org.openapitools.api|
|legacyDiscriminatorBehavior|Set to false for generators with better support for discriminators. (Python, Java, Go, PowerShell, C# have this enabled by default).|<dl><dt>**true**</dt><dd>The mapping in the discriminator includes descendent schemas that allOf inherit from self and the discriminator mapping schemas in the OAS document.</dd><dt>**false**</dt><dd>The mapping in the discriminator includes any descendent schemas that allOf inherit from self, any oneOf schemas, any anyOf schemas, any x-discriminator-values, and the discriminator mapping schemas in the OAS document AND Codegen validates that oneOf and anyOf schemas contain the required discriminator and throws an error if the discriminator is missing.</dd></dl>|true|
|licenseName|The name of the license| |Unlicense|
|licenseUrl|The URL of the license| |http://unlicense.org|
|modelPackage|package for generated models| |apimodels|
|openApiNullable|Enable OpenAPI Jackson Nullable library. Not supported by `microprofile` library.| |true|
|parentArtifactId|parent artifactId in generated pom N.B. parentGroupId, parentArtifactId and parentVersion must all be specified for any of them to take effect| |null|
|parentGroupId|parent groupId in generated pom N.B. parentGroupId, parentArtifactId and parentVersion must all be specified for any of them to take effect| |null|
|parentVersion|parent version in generated pom N.B. parentGroupId, parentArtifactId and parentVersion must all be specified for any of them to take effect| |null|
|prependFormOrBodyParameters|Add form or body parameters to the beginning of the parameter list.| |false|
|scmConnection|SCM connection in generated pom.xml| |scm:git:git@github.com:openapitools/openapi-generator.git|
|scmDeveloperConnection|SCM developer connection in generated pom.xml| |scm:git:git@github.com:openapitools/openapi-generator.git|
|scmUrl|SCM URL in generated pom.xml| |https://github.com/openapitools/openapi-generator|
|serializableModel|boolean - toggle &quot;implements Serializable&quot; for generated models| |false|
|snapshotVersion|Uses a SNAPSHOT version.|<dl><dt>**true**</dt><dd>Use a SnapShot Version</dd><dt>**false**</dt><dd>Use a Release Version</dd></dl>|null|
|sortModelPropertiesByRequiredFlag|Sort model properties to place required parameters before optional parameters.| |true|
|sortParamsByRequiredFlag|Sort method arguments to place required parameters before optional parameters.| |true|
|sourceFolder|source folder for generated code| |/app|
|supportAsync|Support Async operations| |false|
|testOutput|Set output folder for models and APIs tests| |${project.build.directory}/generated-test-sources/openapi|
|title|server title name or client service name| |openapi-java-playframework|
|useBeanValidation|Use BeanValidation API annotations| |true|
|useInterfaces|Makes the controllerImp implements an interface to facilitate automatic completion when updating from version x to y of your spec| |true|
|useJakartaEe|whether to use Jakarta EE namespace instead of javax| |false|
|useOneOfInterfaces|whether to use a java interface to describe a set of oneOf options, where each option is a class that implements the interface| |false|
|useSwaggerUI|Add a route to /api which show your documentation in swagger-ui. Will also import needed dependencies| |true|
|withXml|whether to include support for application/xml content type and include XML annotations in the model (works with libraries that provide support for JSON and XML)| |false|
|wrapCalls|Add a wrapper to each controller function to handle things like metrics, response modification, etc..| |true|

## SUPPORTED VENDOR EXTENSIONS

| Extension name | Description | Applicable for | Default value |
| -------------- | ----------- | -------------- | ------------- |
|x-discriminator-value|Used with model inheritance to specify value for discriminator that identifies current model|MODEL|
|x-implements|Ability to specify interfaces that model must implements|MODEL|empty array
|x-setter-extra-annotation|Custom annotation that can be specified over java setter for specific field|FIELD|When field is array & uniqueItems, then this extension is used to add `@JsonDeserialize(as = LinkedHashSet.class)` over setter, otherwise no value
|x-tags|Specify multiple swagger tags for operation|OPERATION|null
|x-accepts|Specify custom value for 'Accept' header for operation|OPERATION|null
|x-content-type|Specify custom value for 'Content-Type' header for operation|OPERATION|null
|x-class-extra-annotation|List of custom annotations to be added to model|MODEL|null
|x-field-extra-annotation|List of custom annotations to be added to property|FIELD, OPERATION_PARAMETER|null


## IMPORT MAPPING

| Type/Alias | Imports |
| ---------- | ------- |
|Array|java.util.List|
|ArrayList|java.util.ArrayList|
|BigDecimal|java.math.BigDecimal|
|Date|java.util.Date|
|DateTime|org.joda.time.*|
|File|java.io.File|
|HashMap|java.util.HashMap|
|LinkedHashSet|java.util.LinkedHashSet|
|List|java.util.*|
|LocalDate|org.joda.time.*|
|LocalDateTime|org.joda.time.*|
|LocalTime|org.joda.time.*|
|Map|java.util.Map|
|Set|java.util.*|
|Timestamp|java.sql.Timestamp|
|URI|java.net.URI|
|UUID|java.util.UUID|


## INSTANTIATION TYPES

| Type/Alias | Instantiated By |
| ---------- | --------------- |
|array|ArrayList|
|map|HashMap|
|set|LinkedHashSet|


## LANGUAGE PRIMITIVES

<ul class="column-ul">
<li>Boolean</li>
<li>Double</li>
<li>Float</li>
<li>Integer</li>
<li>Long</li>
<li>Object</li>
<li>String</li>
<li>boolean</li>
<li>byte[]</li>
</ul>

## RESERVED WORDS

<ul class="column-ul">
<li>_</li>
<li>abstract</li>
<li>apiclient</li>
<li>apiexception</li>
<li>apiresponse</li>
<li>assert</li>
<li>boolean</li>
<li>break</li>
<li>byte</li>
<li>case</li>
<li>catch</li>
<li>char</li>
<li>class</li>
<li>configuration</li>
<li>const</li>
<li>continue</li>
<li>default</li>
<li>do</li>
<li>double</li>
<li>else</li>
<li>enum</li>
<li>extends</li>
<li>file</li>
<li>final</li>
<li>finally</li>
<li>float</li>
<li>for</li>
<li>goto</li>
<li>if</li>
<li>implements</li>
<li>import</li>
<li>instanceof</li>
<li>int</li>
<li>interface</li>
<li>list</li>
<li>localdate</li>
<li>localreturntype</li>
<li>localtime</li>
<li>localvaraccept</li>
<li>localvaraccepts</li>
<li>localvarauthnames</li>
<li>localvarcollectionqueryparams</li>
<li>localvarcontenttype</li>
<li>localvarcontenttypes</li>
<li>localvarcookieparams</li>
<li>localvarformparams</li>
<li>localvarheaderparams</li>
<li>localvarpath</li>
<li>localvarpostbody</li>
<li>localvarqueryparams</li>
<li>long</li>
<li>native</li>
<li>new</li>
<li>null</li>
<li>object</li>
<li>offsetdatetime</li>
<li>package</li>
<li>private</li>
<li>protected</li>
<li>public</li>
<li>return</li>
<li>short</li>
<li>static</li>
<li>strictfp</li>
<li>stringutil</li>
<li>super</li>
<li>switch</li>
<li>synchronized</li>
<li>this</li>
<li>throw</li>
<li>throws</li>
<li>transient</li>
<li>try</li>
<li>void</li>
<li>volatile</li>
<li>while</li>
</ul>

## FEATURE SET


### Client Modification Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|BasePath|✓|ToolingExtension
|Authorizations|✗|ToolingExtension
|UserAgent|✗|ToolingExtension
|MockServer|✗|ToolingExtension

### Data Type Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|Custom|✗|OAS2,OAS3
|Int32|✓|OAS2,OAS3
|Int64|✓|OAS2,OAS3
|Float|✓|OAS2,OAS3
|Double|✓|OAS2,OAS3
|Decimal|✓|ToolingExtension
|String|✓|OAS2,OAS3
|Byte|✓|OAS2,OAS3
|Binary|✓|OAS2,OAS3
|Boolean|✓|OAS2,OAS3
|Date|✓|OAS2,OAS3
|DateTime|✓|OAS2,OAS3
|Password|✓|OAS2,OAS3
|File|✓|OAS2
|Uuid|✗|
|Array|✓|OAS2,OAS3
|Null|✗|OAS3
|AnyType|✗|OAS2,OAS3
|Object|✓|OAS2,OAS3
|Maps|✓|ToolingExtension
|CollectionFormat|✓|OAS2
|CollectionFormatMulti|✓|OAS2
|Enum|✓|OAS2,OAS3
|ArrayOfEnum|✓|ToolingExtension
|ArrayOfModel|✓|ToolingExtension
|ArrayOfCollectionOfPrimitives|✓|ToolingExtension
|ArrayOfCollectionOfModel|✓|ToolingExtension
|ArrayOfCollectionOfEnum|✓|ToolingExtension
|MapOfEnum|✓|ToolingExtension
|MapOfModel|✓|ToolingExtension
|MapOfCollectionOfPrimitives|✓|ToolingExtension
|MapOfCollectionOfModel|✓|ToolingExtension
|MapOfCollectionOfEnum|✓|ToolingExtension

### Documentation Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|Readme|✓|ToolingExtension
|Model|✓|ToolingExtension
|Api|✓|ToolingExtension

### Global Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|Host|✓|OAS2,OAS3
|BasePath|✓|OAS2,OAS3
|Info|✓|OAS2,OAS3
|Schemes|✗|OAS2,OAS3
|PartialSchemes|✓|OAS2,OAS3
|Consumes|✓|OAS2
|Produces|✓|OAS2
|ExternalDocumentation|✓|OAS2,OAS3
|Examples|✓|OAS2,OAS3
|XMLStructureDefinitions|✗|OAS2,OAS3
|MultiServer|✗|OAS3
|ParameterizedServer|✗|OAS3
|ParameterStyling|✗|OAS3
|Callbacks|✗|OAS3
|LinkObjects|✗|OAS3

### Parameter Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|Path|✓|OAS2,OAS3
|Query|✓|OAS2,OAS3
|Header|✓|OAS2,OAS3
|Body|✓|OAS2
|FormUnencoded|✓|OAS2
|FormMultipart|✓|OAS2
|Cookie|✓|OAS3

### Schema Support Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|Simple|✓|OAS2,OAS3
|Composite|✓|OAS2,OAS3
|Polymorphism|✗|OAS2,OAS3
|Union|✗|OAS3
|allOf|✗|OAS2,OAS3
|anyOf|✗|OAS3
|oneOf|✗|OAS3
|not|✗|OAS3

### Security Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|BasicAuth|✓|OAS2,OAS3
|ApiKey|✓|OAS2,OAS3
|OpenIDConnect|✗|OAS3
|BearerToken|✓|OAS3
|OAuth2_Implicit|✓|OAS2,OAS3
|OAuth2_Password|✗|OAS2,OAS3
|OAuth2_ClientCredentials|✗|OAS2,OAS3
|OAuth2_AuthorizationCode|✗|OAS2,OAS3
|SignatureAuth|✗|OAS3
|AWSV4Signature|✗|ToolingExtension

### Wire Format Feature
| Name | Supported | Defined By |
| ---- | --------- | ---------- |
|JSON|✓|OAS2,OAS3
|XML|✓|OAS2,OAS3
|PROTOBUF|✗|ToolingExtension
|Custom|✗|OAS2,OAS3
