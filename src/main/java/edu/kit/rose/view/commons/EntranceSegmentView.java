package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/**
 * An entrance segment view is the visual representation of an entrance street segment.
 */
public class EntranceSegmentView extends SegmentView<Entrance> {
  /**
   * Creates a new entrance segment view for a given entrance {@code segment}.
   *
   * @param segment the segment to display.
   * @param controller the controller to use for handling position updates.
   * @param translator the translator to use for localizing label.
   */
  public EntranceSegmentView(Entrance segment, RoadSystemController controller,
                             LocalizedTextProvider translator) {
    super(segment, controller, translator);
    setWidth(100);
    setHeight(50);
  }

  @Override
  protected void redraw(GraphicsContext graphicsContext) {
    graphicsContext.setFill(Paint.valueOf("#000000"));
    graphicsContext.fillRect(0, 0, 100, 50);
  }

  private void drawCircle(GraphicsContext graphicsContext) {
    graphicsContext.setFill(Paint.valueOf("#ff0000"));
    graphicsContext.fillOval(50, 10, 30, 30);
    graphicsContext.strokeOval(50, 10, 30, 30);

    graphicsContext.setFill(Paint.valueOf("#ffffff"));
    graphicsContext.fillOval(55, 15, 20, 20);
    graphicsContext.strokeOval(55, 15, 20, 20);

    graphicsContext.setFill(Paint.valueOf("#000000"));
    graphicsContext.setTextAlign(TextAlignment.CENTER);
    graphicsContext.setTextBaseline(VPos.CENTER);
    graphicsContext.fillText("130", 65, 25);
  }

  private void drawArrow(GraphicsContext graphicsContext) {
    graphicsContext.beginPath();
    graphicsContext.setStroke(Paint.valueOf("#ffffff"));
    graphicsContext.setLineWidth(6);
    graphicsContext.moveTo(20, 25);
    graphicsContext.lineTo(60, 25);
    graphicsContext.stroke();
    graphicsContext.closePath();

    double[] xpoints = new double[] { 60, 60, 75};
    double[] ypoints = new double[] { 16, 34, 25};

    graphicsContext.setFill(Paint.valueOf("#ffffff"));
    graphicsContext.fillPolygon(xpoints, ypoints, 3);
    graphicsContext.setLineWidth(2);
    graphicsContext.strokePolygon(xpoints, ypoints, 3);
  }

  @Override
  public void notifyChange(Element unit) {

  }
}
