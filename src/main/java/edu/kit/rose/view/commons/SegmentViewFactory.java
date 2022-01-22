package edu.kit.rose.view.commons;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Entrance;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * The segment view factory can create a {@link SegmentView} for a given
 * {@link Segment}.
 */
public class SegmentViewFactory {
  private final LocalizedTextProvider translator;
  private final RoadSystemController controller;

  /**
   * Creates a new segment view factory.
   */
  public SegmentViewFactory(LocalizedTextProvider translator, RoadSystemController controller) {
    this.translator = translator;
    this.controller = controller;
  }

  /**
   * Creates a segment view for a given segment.
   *
   * @param segment the segment to create a view for.
   * @return the created view.
   */
  public SegmentView<? extends Segment> createForSegment(Segment segment) {
    switch (segment.getSegmentType()) {
      case BASE:
        return new BaseSegmentView((Base) segment, this.controller, this.translator);
      case ENTRANCE:
        return new EntranceSegmentView((Entrance) segment, this.controller, this.translator);
      case EXIT:
        return new ExitSegmentView((Exit) segment, this.controller, this.translator);
      default:
        return null;
    }
  }
}
