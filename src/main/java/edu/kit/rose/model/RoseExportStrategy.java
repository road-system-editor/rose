package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;

/**
 * Implements the export for the whole Project into the ROSE-Format.
 * This Format allows the Project to be reopened in the ROSE-Program.
 */
class RoseExportStrategy extends ExportStrategy {
  private final Project project;

  RoseExportStrategy(Project project) {
    this.project = project;
  }

  @Override
  void exportToFile(File file) {
    var mapper = createObjectMapper();

    var jsonProject = new SerializedRoadSystem(project.getRoadSystem());

    try { //TODO
      mapper.writeValue(file, jsonProject);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static ObjectMapper createObjectMapper() {
    var factory = new YAMLFactory()
        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
    var mapper = new ObjectMapper(factory);
    mapper.findAndRegisterModules();
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    return mapper;
  }

  public static void importToProject(Project project, File file) {
    var mapper = createObjectMapper();

    SerializedRoadSystem serialized;
    try { //TODO
      serialized = mapper.readValue(file, SerializedRoadSystem.class);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    serialized.populateRoadSystem(project.getRoadSystem());
  }
}
