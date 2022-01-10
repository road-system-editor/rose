package edu.kit.rose.controller.selection;

import edu.kit.rose.infrastructure.SetObservable;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * A selection buffer is a container that stores selected segments and notifies its observer when  the selection state
 * of one of the segments changes. Segments that are stored in the selection buffer are implicitly assumed to be selected.
 */
public interface SelectionBuffer extends SetObservable<Segment, SelectionBuffer> {

    /**
     * Adds a segment to the selection buffer and marks it as selected.
     * @param segment the segment to add
     */
    void addSegmentSelection(Segment segment);

    /**
     * Removes a segment from the selection buffer and marks it as unselected.
     * @param segment the segment to remove
     */
    void removeSegmentSelection(Segment segment);

    /**
     * Toggle the selection status of a given segment.
     * @param segment the segment to toggle its selection status
     */
    void toggleSegmentSelection(Segment segment);

    /**
     * Determines if the given segment is selected/in the selection buffer.
     * @param segment the segment to check if it is validated
     * @return boolean value that indicates if the segment is selected
     */
    boolean isSegmentSelected(Segment segment);
}
