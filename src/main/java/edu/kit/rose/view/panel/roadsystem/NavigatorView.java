package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ZoomSetting;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * The navigator view is a set of buttons which can be used to navigate on the RoadSystemPanel.
 */
public class NavigatorView extends FxmlContainer {

  private static final int UPDATE_EDITOR_POSITION_OFFSET = 10;
  private static final int UPDATE_EDITOR_POSITION_IGNORE_DIMENSION = 0;

  private Runnable onUp = () -> {};
  private Runnable onDown = () -> {};
  private Runnable onLeft = () -> {};
  private Runnable onRight = () -> {};
  private Runnable onIn = () -> {};
  private Runnable onOut = () -> {};

  @Inject
  private RoadSystemController roadSystemController;

  @Inject
  private Project project;

  @FXML
  private Button navigateToTopButton;
  @FXML
  private Button navigateToBottomButton;
  @FXML
  private Button navigateToLeftButton;
  @FXML
  private Button navigateToRightButton;
  @FXML
  private Button zoomInButton;
  @FXML
  private Button zoomOutButton;


  /**
   * Creates a new NavigatorView.
   */
  public NavigatorView() {
    super("NavigatorView.fxml");

    registerButtonListeners();
  }

  public void setOnUp(Runnable onUp) {
    this.onUp = onUp;
  }

  public void setOnDown(Runnable onDown) {
    this.onDown = onDown;
  }

  public void setOnLeft(Runnable onLeft) {
    this.onLeft = onLeft;
  }

  public void setOnRight(Runnable onRight) {
    this.onRight = onRight;
  }

  public void setOnIn(Runnable onIn) {
    this.onIn = onIn;
  }

  public void setOnOut(Runnable onOut) {
    this.onOut = onOut;
  }

  private void registerButtonListeners() {
    navigateToTopButton.setOnMouseClicked(mouseEvent -> onUp.run());
    navigateToBottomButton.setOnMouseClicked(mouseEvent -> onDown.run());
    navigateToLeftButton.setOnMouseClicked(mouseEvent -> onLeft.run());
    navigateToRightButton.setOnMouseClicked(mouseEvent -> onRight.run());
    zoomInButton.setOnMouseClicked(mouseEvent -> onIn.run());
    zoomOutButton.setOnMouseClicked(mouseEvent -> onOut.run());
  }

  private void updateEditorPosition(int offsetX, int offsetY) {
    Position newCenter = new Position(
        project.getZoomSetting().getCenterOfView().getX() + offsetX,
        project.getZoomSetting().getCenterOfView().getX() + offsetY);

    roadSystemController.setEditorPosition(newCenter);
  }

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
