package edu.kit.rose.view.panel.roadsystem;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.Project;
import edu.kit.rose.view.commons.FxmlContainer;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The navigator view is a set of buttons which can be used to navigate on the RoadSystemPanel.
 */
public class NavigatorView extends FxmlContainer {

  private static final String NAVIGATE_TOP_ICON_RESOURCE = "TopNavigationIcon.png";
  private static final String NAVIGATE_BOTTOM_ICON_RESOURCE = "BottomNavigationIcon.png";
  private static final String NAVIGATE_LEFT_ICON_RESOURCE = "LeftNavigationIcon.png";
  private static final String NAVIGATE_RIGHT_ICON_RESOURCE = "RightNavigationIcon.png";
  private static final String ZOOM_IN_ICON_RESOURCE = "ZoomInIcon.png";
  private static final String ZOOM_OUT_ICON_RESOURCE = "ZoomOutIcon.png";

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
  private ImageView navigateToTopIcon;
  @FXML
  private Button navigateToBottomButton;
  @FXML
  private ImageView navigateToBottomIcon;
  @FXML
  private Button navigateToLeftButton;
  @FXML
  private ImageView navigateToLeftIcon;
  @FXML
  private Button navigateToRightButton;
  @FXML
  private ImageView navigateToRightIcon;
  @FXML
  private Button zoomInButton;
  @FXML
  private ImageView zoomInIcon;
  @FXML
  private Button zoomOutButton;
  @FXML
  private ImageView zoomOutIcon;


  /**
   * Creates a new NavigatorView.
   */
  public NavigatorView() {
    super("NavigatorView.fxml");

    registerButtonListeners();
    setupIcons();
  }

  private void setupIcons() {
    final var topUrl = getClass().getResource(NAVIGATE_TOP_ICON_RESOURCE);
    final var bottomUrl = getClass().getResource(NAVIGATE_BOTTOM_ICON_RESOURCE);
    final var leftUrl = getClass().getResource(NAVIGATE_LEFT_ICON_RESOURCE);
    final var rightUrl = getClass().getResource(NAVIGATE_RIGHT_ICON_RESOURCE);
    final var zoomInUrl = getClass().getResource(ZOOM_IN_ICON_RESOURCE);
    final var zoomOutUrl = getClass().getResource(ZOOM_OUT_ICON_RESOURCE);
    Image topImage;
    Image bottomImage;
    Image leftImage;
    Image rightImage;
    Image zoomInImage;
    Image zoomOutImage;
    if (topUrl != null && bottomUrl != null && leftUrl != null && rightUrl != null
        && zoomInUrl != null && zoomOutUrl != null) {
      topImage = new Image(topUrl.toString());
      bottomImage = new Image(bottomUrl.toString());
      leftImage = new Image(leftUrl.toString());
      rightImage = new Image(rightUrl.toString());
      zoomInImage = new Image(zoomInUrl.toString());
      zoomOutImage = new Image(zoomOutUrl.toString());
    } else {
      throw new IllegalStateException("image not found.");
    }
    this.navigateToTopIcon.setImage(topImage);
    this.navigateToBottomIcon.setImage(bottomImage);
    this.navigateToLeftIcon.setImage(leftImage);
    this.navigateToRightIcon.setImage(rightImage);
    this.zoomInIcon.setImage(zoomInImage);
    this.zoomOutIcon.setImage(zoomOutImage);
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

  @Override
  protected void updateTranslatableStrings(Language lang) {

  }

  @Override
  protected Collection<FxmlContainer> getSubFxmlContainer() {
    return null;
  }
}
