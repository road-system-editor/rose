package edu.kit.rose.controller.selection;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.ArrayList;
import java.util.List;

/**
 * A selection buffer is a container that stores selected segments
 * and notifies its observer when  the selection state
 * of one of the segments changes.
 * Segments that are stored in the selection buffer are implicitly assumed to be selected.
 */
public class RoseSelectionBuffer implements SelectionBuffer {

  private final List<Segment> segmentList = new ArrayList<>();
  private final List<SetObserver<Segment, SelectionBuffer>> observers = new ArrayList<>();

  @Override
  public void addSegmentSelection(Segment segment) {
    if (!this.segmentList.contains(segment)) {
      segmentList.add(segment);
      observers.forEach(e -> e.notifyAddition(segment));
      notifySubscribers();
    }
  }

  @Override
  public void removeSegmentSelection(Segment segment) {
    if (this.segmentList.contains(segment)) {
      this.segmentList.remove(segment);
      observers.forEach(e -> e.notifyRemoval(segment));
      notifySubscribers();
    }
  }

  @Override
  public void toggleSegmentSelection(Segment segment) {
    if (segmentList.contains(segment)) {
      removeSegmentSelection((segment));
      observers.forEach(e -> e.notifyRemoval(segment));
      notifySubscribers();
    } else {
      addSegmentSelection(segment);
      observers.forEach(e -> e.notifyAddition(segment));
      notifySubscribers();
    }
  }

  @Override
  public boolean isSegmentSelected(Segment segment) {
    return this.segmentList.contains(segment);
  }

  @Override
  public List<Element> getSelectedSegments() {
    return new ArrayList<>(this.segmentList);
  }

  @Override
  public void addSubscriber(SetObserver<Segment, SelectionBuffer> observer) {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  @Override
  public void removeSubscriber(SetObserver<Segment, SelectionBuffer> observer) {
    observers.remove(observer);
  }

  @Override
  public void notifySubscribers() {
    observers.forEach(e -> e.notifyChange(this));
  }

  @Override
  public SelectionBuffer getThis() {
    return this;
  }
}
