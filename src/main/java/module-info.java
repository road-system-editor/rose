module edu.kit.rose {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires com.google.guice;
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.dataformat.yaml;
  requires org.jgrapht.core;
  requires com.google.common;
  requires org.apache.commons.collections4;
  requires java.desktop;
  requires org.slf4j;

  opens edu.kit.rose.model to com.fasterxml.jackson.databind;
  opens edu.kit.rose.model.plausibility.criteria to com.fasterxml.jackson.databind;
  opens edu.kit.rose.model.plausibility.criteria.validation to com.fasterxml.jackson.databind;
  opens edu.kit.rose.model.roadsystem.attributes to com.fasterxml.jackson.databind;
  opens edu.kit.rose.model.roadsystem.elements to com.fasterxml.jackson.databind;
  opens edu.kit.rose.infrastructure.language to com.fasterxml.jackson.databind;
  opens edu.kit.rose.view to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.commons to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.criterion to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.hierarchy to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.measurement to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.violation to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.roadsystem to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.segment to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.panel.segmentbox to javafx.fxml, com.google.guice;
  opens edu.kit.rose.view.window to javafx.fxml, com.google.guice;
  exports edu.kit.rose.view;
}