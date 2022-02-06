package edu.kit.rose.view.commons;

import com.google.inject.Inject;
import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.Movement;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.List;
import java.util.function.Supplier;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

/**
 * An exit segment view is the visual representation of an exit street segment.
 */
public abstract class RampSegmentView<T extends Segment> extends SegmentView<T> {

  private Rotate rotation;
  private ImageView imageView;
  private ConnectorView entryConnectorView;
  private ConnectorView exitConnectorView;
  private ConnectorView rampConnectorView;
  private Image defaultImage;
  private Image selectedImage;

  private Point2D startPos;
  private double posOnSourceX;
  private double posOnSourceY;

  @Inject
  private RoadSystemController roadSystemController;


  /**
   * Creates a new segment view that acts as visual representation of a given segment.
   *
   * @param segment    the segment to create a visual representation for
   * @param controller the road system controller.
   * @param translator the translator.
   */
  public RampSegmentView(T segment,
                         RoadSystemController controller,
                         LocalizedTextProvider translator) {
    super(segment, controller, translator);
    segment.addSubscriber(this);
    setup();
  }

  private void setup() {
    setupRotation();
    setupConnectors();
    setupImage();
    setupDragging();
    updatePosition();
    getChildren().addAll(imageView, exitConnectorView, entryConnectorView, rampConnectorView);

    relocateConnectorView(entryConnectorView);
    relocateConnectorView(exitConnectorView);
    relocateConnectorView(rampConnectorView);
  }

  private void setupConnectors() {
    this.getSegment().getConnectors().forEach(c -> {
      switch (c.getType()) {
        case ENTRY -> entryConnectorView = new ConnectorView(15, c.getPosition());
        case EXIT -> exitConnectorView = new ConnectorView(15, c.getPosition());
        case RAMP_EXIT, RAMP_ENTRY -> rampConnectorView = new ConnectorView(6, c.getPosition());
        default -> throw new IllegalStateException("segment is not a exit segment.");
      }
    });
  }

  private void relocateConnectorView(ConnectorView connectorView) {
    connectorView.setCenterX(connectorView.getCenterX() - getImagePosOffsetX());
    connectorView.setCenterY(connectorView.getCenterY() - getImagePosOffsetY());
  }

  private void setupImage() {
    var defaultUrl = getClass().getResource(getDefaultImageResource());
    var selectedUrl = getClass().getResource(getSelectedImageResource());
    defaultImage = new Image(defaultUrl.toString());
    selectedImage = new Image(selectedUrl.toString());
    imageView = new ImageView(defaultImage);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(getImageWidth());
    imageView.setFitHeight(getImageHeight());
  }

  private void updatePosition() {
    relocate(getSegment().getCenter().getX() + getImagePosOffsetX(),
        getSegment().getCenter().getY() + getImagePosOffsetY());
  }

  private void setupRotation() {
    rotation = new Rotate(getSegment().getRotation());
    rotation.setPivotX(-getImagePosOffsetX());
    rotation.setPivotY(-getImagePosOffsetY());
    getTransforms().add(rotation);
  }

  private void setupDragging() {
    this.setOnMousePressed(mouseEvent -> {
      startPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
    });

    this.setOnDragDetected(mouseEvent -> startFullDrag());

    this.setOnMouseDragged(mouseEvent -> {
      var currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var movement = new Movement(currentPos.getX() - startPos.getX(),
          currentPos.getY() - startPos.getY());
      if (canBeMoved(movement)) {
        getSegment().move(movement);
        startPos = currentPos;
      }
      mouseEvent.consume();
    });

    this.setOnMouseDragReleased(mouseEvent -> {
      var currentPos = localToParent(mouseEvent.getX(), mouseEvent.getY());
      var movement = new Movement(
          currentPos.getX() - startPos.getX(),
          currentPos.getY() - startPos.getY());
      getSegment().move(movement);
    });
  }

  private boolean canBeMoved(Movement movement) {
    final Bounds bounds = this.getLayoutBounds();
    final Point2D topLeft = localToParent(bounds.getMinX(), bounds.getMinY());
    final Point2D topRight = localToParent(bounds.getMaxX(), bounds.getMinY());
    final Point2D bottomLeft = localToParent(bounds.getMinX(), bounds.getMaxY());
    final Point2D bottomRight = localToParent(bounds.getMinX(), bounds.getMinY());
    final Point2D movementVector = new Point2D(movement.getX(), movement.getY());
    final Bounds gridBounds = getParent().getLayoutBounds();
    List<Supplier<Boolean>> checks = List.of(
        () -> gridBounds.contains(topLeft.add(movementVector)),
        () -> gridBounds.contains(topRight.add(movementVector)),
        () -> gridBounds.contains(bottomLeft.add(movementVector)),
        () -> gridBounds.contains(bottomRight.add(movementVector))
    );
    return checks.stream().allMatch(Supplier::get);
  }

  @Override
  public void redraw() {
    updatePosition();
    if (getDrawAsSelected()) {
      imageView.setImage(selectedImage);
    } else {
      imageView.setImage(defaultImage);
    }
    rotation.setPivotX(-getImagePosOffsetX());
    rotation.setPivotY(-getImagePosOffsetY());
    rotation.setAngle(getSegment().getRotation());
  }

  @Override
  public void notifyChange(Element unit) {
    this.draw();
  }

  @Override
  public void notifyAddition(Element unit) {
  }

  @Override
  public void notifyRemoval(Element unit) {
  }

  protected abstract double getImagePosOffsetX();

  protected abstract double getImagePosOffsetY();

  protected abstract String getDefaultImageResource();

  protected abstract String getSelectedImageResource();

  protected abstract int getImageWidth();

  protected abstract int getImageHeight();

}
