package com.zestmsp.codegen;

import org.openapitools.codegen.*;
import org.openapitools.codegen.model.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.swagger.models.properties.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.meta.features.*;
import static org.openapitools.codegen.utils.StringUtils.camelize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class NodejsExpressServerEsmGenerator extends DefaultCodegen implements CodegenConfig {

  private final Logger LOGGER = LoggerFactory.getLogger(NodejsExpressServerEsmGenerator.class);

  // source folder where to write the files
  protected String sourceFolder = "src";
  protected String apiVersion = "1.0.0";

  protected String implFolder = "services";

  public static final String SERVER_PORT = "serverPort";
  protected String defaultServerPort = "8080";

  /**
   * Configures the type of generator.
   *
   * @return  the CodegenType for this generator
   * @see     org.openapitools.codegen.CodegenType
   */
  public CodegenType getTag() {
    return CodegenType.OTHER;
  }

  /**
   * Configures a friendly name for the generator.  This will be used by the generator
   * to select the library with the -g flag.
   *
   * @return the friendly name for the generator
   */
  public String getName() {
    return "nodejs-express-server-esm";
  }

  /**
   * Provides an opportunity to inspect and modify operation data before the code is generated.
   */
  @Override
  public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {

    // to try debugging your code generator:
    // set a break point on the next line.
    // then debug the JUnit test called LaunchGeneratorInDebugger

    OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);

    OperationMap ops = results.getOperations();
    List<CodegenOperation> opList = ops.getOperation();

    // iterate over the operation and perhaps modify something
    for(CodegenOperation co : opList){
      // example:
      // co.httpMethod = co.httpMethod.toLowerCase();
    }

    return results;
  }

  /**
   * Returns human-friendly help for the generator.  Provide the consumer with help
   * tips, parameters here
   *
   * @return A string value for the help message
   */
  public String getHelp() {
    return "Generates a nodejs-express-server-esm client library.";
  }

//  @Override
//  public String toApiName(String name) {
//    if (name.length() == 0) {
//      return "DefaultApi";
//    }
//    // Super has this
//    // return camelize(apiNamePrefix + "_" + name + "_" + apiNameSuffix);
//    String superName = camelize(apiNamePrefix + "_" + name + "_" + apiNameSuffix);
//    return camelize(name);
//  }
//
//  @Override
//  public String toApiFilename(String name) {
//    return toApiName(name) + "Controller";
//  }
//
//  @Override
//  public String apiFilename(String templateName, String tag, String outputDir) {
//    //String superName = super.apiFilename(templateName, tag, outputDir);
//    String filename = apiFilename(templateName, tag);
//    String suffix = apiTemplateFiles().get(templateName);
//
//    String path = outputDir + File.separator + filename + suffix;
//    return path;
//  }
  @Override
  public String apiFilename(String templateName, String tag, String outputDir) {
    //LOGGER.info("apiFilename templateName is {} and tag is {} ", templateName, tag);
    String result =  super.apiFilename(templateName, tag, outputDir);
    String suffix = apiTemplateFiles().get(templateName);

    if (templateName.contains("service_stub")) {
      result = outputDir + File.separator + "Service_stub" + suffix;
    } else if (templateName.contains("services")) {
      result = outputDir + File.separator + "DefaultApiServices" + suffix;
    } else if (templateName.contains("handlers")) {
      result = outputDir + File.separator + "DefaultHandlers" + suffix;
    } else if (templateName.contains("api_controller")) {
      result = outputDir + File.separator + "DefaultApiController" + suffix;
    } else if (templateName.contains("controllers/index.mustache")) {
      result = outputDir + File.separator + "index" + suffix;
    } else if (templateName.contains("services/index.mustache")) {
      result = outputDir + File.separator + "index" + suffix;
    }

    //LOGGER.info("apiFilename result: {}", result);
    return result;
  }

  public NodejsExpressServerEsmGenerator() {
    super();

    modifyFeatureSet(features -> features
            .includeDocumentationFeatures(
                    DocumentationFeature.Readme,
                    DocumentationFeature.Api
            )
            .wireFormatFeatures(EnumSet.of(WireFormatFeature.JSON))
            .securityFeatures(EnumSet.of(
                    SecurityFeature.OAuth2_Implicit
            ))
            .excludeGlobalFeatures(
                    GlobalFeature.XMLStructureDefinitions,
                    GlobalFeature.Callbacks,
                    GlobalFeature.LinkObjects,
                    GlobalFeature.ParameterStyling
            )
            .excludeSchemaSupportFeatures(
                    SchemaSupportFeature.Polymorphism
            )
            .includeParameterFeatures(
                    ParameterFeature.Cookie
            )
    );

    // set the output folder here
    outputFolder = "generated-code/nodejs-express-server-esm";

    // controllers folder
    apiTemplateFiles.put("controllers/api_controller.mustache", ".js");
    templateOutputDirs.put("controllers/api_controller.mustache", "src/controllers");

    apiTemplateFiles.put("controllers/handlers.mustache", ".js");
    templateOutputDirs.put("controllers/handlers.mustache", "src/controllers");

    apiTemplateFiles.put("controllers/index.mustache", ".js");
    templateOutputDirs.put("controllers/index.mustache", "src/controllers");

    supportingFiles.add(new SupportingFile("controllers/Controller.mustache", "src/controllers", "Controller.js"));

    // Services folder
    apiTemplateFiles.put("services/services.mustache", ".js");
    templateOutputDirs.put("services/services.mustache", "src/services");

    apiTemplateFiles.put("api_impl/api_implementation.mustache", ".js");
    templateOutputDirs.put("api_impl/api_implementation.mustache", "src/api_impl");

//    apiTemplateFiles.put("services/service_stub.mustache", ".js");
//    templateOutputDirs.put("services/service_stub.mustache", "src/services");

    supportingFiles.add(new SupportingFile("services/index.mustache", "src/services", "index.js"));
    supportingFiles.add(new SupportingFile("services/Service.mustache", "src/services", "Service.js"));

//    supportingFiles.add(new SupportingFile("api/api_controllers.mustache", "src/controller/api", "Controller.js"));
//    supportingFiles.add(new SupportingFile("api/services.mustache", "src/controller/api", "Service.js"));
//    supportingFiles.add(new SupportingFile("api/service_stub.mustache", "src/controller/api", "Service_stub.js"));
//    supportingFiles.add(new SupportingFile("api/handlers.mustache", "src/controller/api", "Handlers.js"));

    supportingFiles.add(new SupportingFile("openapi.mustache", "src/api", "openapi.yaml"));

    SupportingFile cfg = new SupportingFile("config.mustache", "src", "config.js");
    supportingFiles.add(cfg);

    supportingFiles.add(new SupportingFile("expressServer.mustache", "src", "expressServer.js"));
    supportingFiles.add(new SupportingFile("server.mustache", "src", "server.js"));
    supportingFiles.add(new SupportingFile("logger.mustache", "src", "logger.js"));
    supportingFiles.add(new SupportingFile("eslintrc.mustache", "", ".eslintrc.json"));

    // utils folder
    supportingFiles.add(new SupportingFile("utils" + File.separator + "openapiRouter.mustache", "src/utils", "openapiRouter.js"));

    // do not overwrite if the file is already present
    SupportingFile pkg = new SupportingFile("package.mustache", "", "package.json");
    pkg.doNotOverwrite();

    SupportingFile readme = new SupportingFile("README.mustache", "", "README.md");
    readme.doNotOverwrite();
    /* End of Added files */

    /**
     * Template Location.  This is the location which templates will be read from.  The generator
     * will use the resource stream to attempt to read the templates.
     */
    templateDir = "nodejs-express-server-esm";

    /**
     * Api Package.  Optional, if needed, this can be used in templates
     */
    //apiPackage = "org.openapitools.api";
    apiPackage = "src/controllers/api";

    /**
     * Model Package.  Optional, if needed, this can be used in templates
     */
    modelPackage = "org.openapitools.model";

    /**
     * Reserved words.  Override this with reserved words specific to your language
     */
//    reservedWords = new HashSet<String> (
//      Arrays.asList(
//        "sample1",  // replace with static values
//        "sample2")
//    );

    /**
     * Additional Properties.  These values can be passed to the templates and
     * are available in models, apis, and supporting files
     */
    additionalProperties.put("apiVersion", apiVersion);
    additionalProperties.put("implFolder", implFolder);

    /**
     * Supporting Files.  You can write single files for the generator with the
     * entire object tree available.  If the input file has a suffix of `.mustache
     * it will be processed by the template engine.  Otherwise, it will be copied
     */
//    supportingFiles.add(new SupportingFile("myFile.mustache",   // the input template or file
//      "",                                                       // the destination folder, relative `outputFolder`
//      "myFile.sample")                                          // the output file
//    );

    /**
     * Language Specific Primitives.  These types will not trigger imports by
     * the client generator
     */
    languageSpecificPrimitives = new HashSet<String>(
      Arrays.asList(
        "Type1",      // replace these with your types
        "Type2")
    );
  }

  @Override
  public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
    generateYAMLSpecFile(objs);

    for (OperationMap operations : getOperations(objs)) {
      List<CodegenOperation> ops = operations.getOperation();

      List<Map<String, Object>> opsByPathList = sortOperationsByPath(ops);
      operations.put("operationsByPath", opsByPathList);
    }
    return super.postProcessSupportingFileData(objs);
  }

  private static List<OperationMap> getOperations(Map<String, Object> objs) {
    List<OperationMap> result = new ArrayList<>();
    ApiInfoMap apiInfo = (ApiInfoMap) objs.get("apiInfo");
    for (OperationsMap api : apiInfo.getApis()) {
      result.add(api.getOperations());
    }
    return result;
  }

  private static List<Map<String, Object>> sortOperationsByPath(List<CodegenOperation> ops) {
    Multimap<String, CodegenOperation> opsByPath = ArrayListMultimap.create();

    for (CodegenOperation op : ops) {
      opsByPath.put(op.path, op);
    }

    List<Map<String, Object>> opsByPathList = new ArrayList<>();
    for (Entry<String, Collection<CodegenOperation>> entry : opsByPath.asMap().entrySet()) {
      Map<String, Object> opsByPathEntry = new HashMap<>();
      opsByPathList.add(opsByPathEntry);
      opsByPathEntry.put("path", entry.getKey());
      opsByPathEntry.put("operation", entry.getValue());
      List<CodegenOperation> operationsForThisPath = Lists.newArrayList(entry.getValue());
    }

    return opsByPathList;
  }

  @Override
  public String removeNonNameElementToCamelCase(String name) {
    return removeNonNameElementToCamelCase(name, "[-:;#]");
  }

  @Override
  public void postProcessFile(File file, String fileType) {
    if (file == null) {
      return;
    }

    String jsPostProcessFile = System.getenv("JS_POST_PROCESS_FILE");
    if (StringUtils.isEmpty(jsPostProcessFile)) {
      return; // skip if JS_POST_PROCESS_FILE env variable is not defined
    }

    // only process files with js extension
    if ("js".equals(FilenameUtils.getExtension(file.toString()))) {
      String command = jsPostProcessFile + " " + file;
      try {
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        int exitValue = p.exitValue();
        if (exitValue != 0) {
          LOGGER.error("Error running the command ({}). Exit code: {}", command, exitValue);
        }
        LOGGER.info("Successfully executed: {}", command);
      } catch (InterruptedException | IOException e) {
        LOGGER.error("Error running the command ({}). Exception: {}", command, e.getMessage());
        // Restore interrupted state
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public GeneratorLanguage generatorLanguage() { return GeneratorLanguage.JAVASCRIPT; }

  /**
   * Escapes a reserved word as defined in the `reservedWords` array. Handle escaping
   * those terms here.  This logic is only called if a variable matches the reserved words
   *
   * @return the escaped term
   */
  @Override
  public String escapeReservedWord(String name) {
    return "_" + name;  // add an underscore to the name
  }

  /**
   * Location to write model files.  You can use the modelPackage() as defined when the class is
   * instantiated
   */
  public String modelFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + modelPackage().replace('.', File.separatorChar);
  }

  /**
   * Location to write api files.  You can use the apiPackage() as defined when the class is
   * instantiated
   */
  @Override
  public String apiFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + apiPackage().replace('.', File.separatorChar);
  }

  public String apiFileFolderNode(String theFolder) {
    String packageName = apiPackage();
    packageName = packageName.replace("controllers", theFolder);
    return outputFolder + File.separator + packageName.replace('.', File.separatorChar);
  }

  /**
   * override with any special text escaping logic to handle unsafe
   * characters so as to avoid code injection
   *
   * @param input String to be cleaned up
   * @return string with unsafe characters removed or escaped
   */
  @Override
  public String escapeUnsafeCharacters(String input) {
    //TODO: check that this logic is safe to escape unsafe characters to avoid code injection
    return input;
  }

  /**
   * Escape single and/or double quote to avoid code injection
   *
   * @param input String to be cleaned up
   * @return string with quotation mark removed or escaped
   */
  public String escapeQuotationMark(String input) {
    //TODO: check that this logic is safe to escape quotation mark to avoid code injection
    return input.replace("\"", "\\\"");
  }
}
