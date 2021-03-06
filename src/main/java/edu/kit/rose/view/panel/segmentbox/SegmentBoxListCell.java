package edu.kit.rose.view.panel.segmentbox;

import edu.kit.rose.controller.roadsystem.RoadSystemController;
import edu.kit.rose.infrastructure.language.LocalizedTextProvider;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import edu.kit.rose.view.commons.EnumLocalizationUtility;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;


/**
 * A {@link SegmentBoxListCell} represents a list item that gets
 * displayed in the {@link javafx.scene.control.ListView} of
 * the {@link SegmentBoxPanel}.
 */
public class SegmentBoxListCell extends ListCell<SegmentType> {
  private final RoadSystemController roadSystemController;

  private final LocalizedTextProvider translator;

  public SegmentBoxListCell(
          RoadSystemController roadSystemController, LocalizedTextProvider translator) {
    this.roadSystemController = roadSystemController;
    this.translator = translator;
  }

  @Override
  protected void updateItem(SegmentType item, boolean empty) {
    super.updateItem(item, empty);

    if (item == null || empty) {
      setText(null);
      setGraphic(null);
    } else {
      SegmentBlueprint segmentBlueprint
              = new SegmentBlueprint(this.translator, this.roadSystemController, item);
      setGraphic(segmentBlueprint);
      String title = EnumLocalizationUtility.localizeSegmentTypeTitle(this.translator, item);
      setTooltip(new Tooltip(title));
    }
  }
}
