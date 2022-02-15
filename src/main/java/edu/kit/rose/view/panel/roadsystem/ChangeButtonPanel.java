package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import edu.kit.rose.controller.application.ApplicationController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * The change button panel contains the buttons for undoing and redoing a changeable action.
 */
public class ChangeButtonPanel extends FxmlContainer {

  private static final String UNDO_ICON_RESOURCE = "undo.png";
  private static final String REDO_ICON_RESOURCE = "redo.png";

  @FXML
  private Button undoButton;

  @FXML
  private Button redoButton;

  @FXML
  private ImageView undoButtonIcon;

  @FXML
  private ImageView redoButtonIcon;

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
    setupIcons();
  }

  private void setupIcons() {
    var undoUrl = getClass().getResource(UNDO_ICON_RESOURCE);
    var redoUrl = getClass().getResource(REDO_ICON_RESOURCE);
    Image undoImage;
    Image redoImage;
    if (undoUrl != null && redoUrl != null) {
      undoImage = new Image(undoUrl.toString());
      redoImage = new Image(redoUrl.toString());
    } else {
      throw new IllegalStateException("image not found.");
    }
    this.undoButtonIcon.setImage(undoImage);
    this.redoButtonIcon.setImage(redoImage);
  }


  @Override
  protected void updateTranslatableStrings(Language lang) {
  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
