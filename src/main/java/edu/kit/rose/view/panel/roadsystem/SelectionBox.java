package edu.kit.rose.view.panel.roadsystem;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 * Represents a {@link SelectionBox} that can be displayed on
 * the {@link Grid} class and allows the user to select content.
 */
public class SelectionBox extends Pane {

  private static final double INITIAL_WIDTH = 0;
  private static final double INITIAL_HEIGHT = 0;
  private static final double OPACITY = 0.5;

  private final Point2D startingPoint;
  private Point2D lastMousePosition;

  /**
   * Creates a new instance of the {@link SelectionBox} class with
   * a given starting point.
   *
   * @param startingPoint the starting point
   */
  public SelectionBox(Point2D startingPoint) {
    this.startingPoint = startingPoint;
    this.lastMousePosition = startingPoint;

    init();
  }

  private void init() {
    this.setLayoutX(startingPoint.getX());
    this.setLayoutY(startingPoint.getY());
    this.setPrefWidth(INITIAL_WIDTH);
    this.setPrefHeight(INITIAL_HEIGHT);
    this.setOpacity(OPACITY);
    this.setBackground(
        new Background(
            new BackgroundFill(
                Paint.valueOf("#009682"),
                CornerRadii.EMPTY,
                Insets.EMPTY)));
  }

  /**
   * Updates positioning and size of the {@link SelectionBox},
   * based on the starting position and the current mouse position.
   *
   * @param currentMousePosition the current mouse position
   */
  public void update(Point2D currentMousePosition) {
    if (currentMousePosition == null) {
      return;
    }

    this.lastMousePosition = currentMousePosition;

    if (startingPoint.getX() > currentMousePosition.getX()) {
      this.setLayoutX(currentMousePosition.getX());
      this.setPrefWidth(startingPoint.getX() - currentMousePosition.getX());
    } else {
      this.setLayoutX(startingPoint.getX());
      this.setPrefWidth(currentMousePosition.getX() - startingPoint.getX());
    }

    if (startingPoint.getY() > currentMousePosition.getY()) {
      this.setLayoutY(currentMousePosition.getY());
      this.setPrefHeight(startingPoint.getY() - currentMousePosition.getY());
    } else {
      this.setLayoutY(startingPoint.getY());
      this.setPrefHeight(currentMousePosition.getY() - startingPoint.getY());
    }
  }

  /**
   * Returns the starting point of the {@link SelectionBox}.
   *
   * @return starting point
   */
  public Point2D getStartingPoint() {
    return startingPoint;
  }

  /**
   * Return the last captured mouse position.
   *
   * @return last mouse position
   */
  public Point2D getLastMousePosition() {
    return lastMousePosition;
  }
}
