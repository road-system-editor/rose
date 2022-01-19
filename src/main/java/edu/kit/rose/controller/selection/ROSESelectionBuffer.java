package edu.kit.rose.controller.selection;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.elements.Segment;

import java.util.ArrayList;
import java.util.List;

public class ROSESelectionBuffer implements SelectionBuffer {

  private List<Segment> segmentList = new ArrayList<>();
  private List<SetObserver<Segment, SelectionBuffer>> observers = new ArrayList<>();

  @Override
  public void addSegmentSelection(Segment segment) {
    segmentList.add(segment);
    observers.forEach(e -> e.notifyAddition(segment));
    notifySubscribers();
  }

  @Override
  public void removeSegmentSelection(Segment segment) {
    observers.forEach(e -> e.notifyRemoval(segment));
    notifySubscribers();
  }

  @Override
  public void toggleSegmentSelection(Segment segment) {
    if (segmentList.contains(segment)) {
      removeSegmentSelection((segment));
    } else {
      addSegmentSelection(segment);
    }
  }

  @Override
  public boolean isSegmentSelected(Segment segment) {
    return false;
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
