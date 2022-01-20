package edu.kit.rose.view.panel.roadsystem;

import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.view.commons.SegmentView;
import java.util.List;
import javafx.scene.layout.Pane;

/**
 * Represents a background surface that shows a grid, on which segment views can be drawn.
 */
public class Grid extends Pane {

  /**
   * Contains all displayed segment views on grid.
   */
  private List<SegmentView<? extends Segment>> segments;

  /**
   * Adds a segment view and displays it on the grid.
   *
   * @param segmentView the segment view to add
   */
  public void addSegmentView(SegmentView<? extends Segment> segmentView) {
  }

  /**
   * Removes a segment view from the grid.
   *
   * @param segmentView the segment view to remove
   */
  public void removeSegmentView(SegmentView<? extends Segment> segmentView) {
  }
}
