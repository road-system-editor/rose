package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * The change button panel contains the buttons for undoing and redoing a changeable action.
 */
public class ChangeButtonPanel extends FxmlContainer {

  @FXML
  private Button undoButton;

  @FXML
  private Button redoButton;

  @Inject
  private ApplicationController controller;

  /**
   * Creates a new FXMLPanel and immediately mounts the components
   * specified in the given FXML file ({@code fxmlResourceName}.
   */
  public ChangeButtonPanel() {
    super("ChangeButtonPanel.fxml");
    undoButton.setOnMouseClicked(mouseEvent -> controller.undo());
    redoButton.setOnMouseClicked(mouseEvent -> controller.redo());
  }


  @Override
  protected void updateTranslatableStrings(Language lang) {
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
