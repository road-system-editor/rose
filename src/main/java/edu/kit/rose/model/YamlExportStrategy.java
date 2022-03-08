package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the export for the {@link edu.kit.rose.model.roadsystem.RoadSystem} into the
 * YAML-Format.
 * This Format allows the Project to be reopened a Program supporting the YAML-Format.
 */
class YamlExportStrategy extends ExportStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(YamlExportStrategy.class);
  private final Project project;

  YamlExportStrategy(Project project) {
    this.project = project;
  }

  @Override
  boolean exportToFile(File file) {
    var factory = new YAMLFactory()
        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
    var mapper = new ObjectMapper(factory);
    mapper.findAndRegisterModules();
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    var yamlProject = new YamlProject(project);

    try {
      mapper.writeValue(file, yamlProject);
      return true;
    } catch (IOException e) {
      String message = String.format("Could not export project to file %s", file.toPath());
      LOG.error(message, e);
      return false;
    }
  }
}
