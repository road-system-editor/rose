package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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

    var serialized = new SerializedProject(project);

    try { //TODO
      mapper.writeValue(file, serialized);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static ObjectMapper createObjectMapper() {
    var factory = new JsonFactory();
    var mapper = new ObjectMapper(factory)
        .enable(SerializationFeature.INDENT_OUTPUT);
    mapper.findAndRegisterModules();
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    return mapper;
  }

  public static void importToProject(Project project, File file) {
    var mapper = createObjectMapper();

    SerializedProject serialized;
    try {
      serialized = mapper.readValue(file, SerializedProject.class);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    serialized.populateProject(project);
  }
}
