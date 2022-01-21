package edu.kit.rose.controller.selection;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.elements.Base;
import edu.kit.rose.model.roadsystem.elements.Exit;
import edu.kit.rose.model.roadsystem.elements.Segment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link RoseSelectionBuffer}.
 */
class RoseSelectionBufferTest {
  private static Segment base;
  private static Segment exit;
  private RoseSelectionBuffer selectionBuffer;

  @BeforeAll
  static void init() {
    RoseSelectionBufferTest.base = new Base();
    RoseSelectionBufferTest.exit = new Exit();
  }

  @BeforeEach
  void setUp() {
    this.selectionBuffer = new RoseSelectionBuffer();
  }

  @Test
  void addSegmentSelection() {
    this.selectionBuffer.addSegmentSelection(RoseSelectionBufferTest.base);
    this.selectionBuffer.addSegmentSelection(RoseSelectionBufferTest.exit);

    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.base));
    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.exit));

  }

  @Test
  void removeSegmentSelection() {
    this.selectionBuffer.addSegmentSelection(RoseSelectionBufferTest.base);
    this.selectionBuffer.addSegmentSelection(RoseSelectionBufferTest.exit);
    this.selectionBuffer.removeSegmentSelection(RoseSelectionBufferTest.exit);

    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.base));
    Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.exit));
  }

  @Test
  void toggleSegmentSelection() {
    this.selectionBuffer.addSegmentSelection(RoseSelectionBufferTest.base);

    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.base));
    Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.exit));

    this.selectionBuffer.toggleSegmentSelection(RoseSelectionBufferTest.base);
    this.selectionBuffer.toggleSegmentSelection(RoseSelectionBufferTest.exit);

    Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.base));
    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(RoseSelectionBufferTest.exit));
  }

  @Test
  void notifyTest() {
    Observer observer = new Observer();

    this.selectionBuffer.addSubscriber(observer);
    this.selectionBuffer.addSegmentSelection(RoseSelectionBufferTest.base);

    Assertions.assertTrue(observer.getAddNotified());
    Assertions.assertFalse(observer.removeNotified);

    this.selectionBuffer.removeSegmentSelection(RoseSelectionBufferTest.base);

    Assertions.assertTrue(observer.getRemoveNotified());
  }

  private static class Observer implements SetObserver<Segment, SelectionBuffer> {
    private boolean addNotified = false;
    private boolean removeNotified = false;

    @Override
    public void notifyAddition(Segment unit) {
      this.addNotified = true;
    }

    @Override
    public void notifyRemoval(Segment unit) {
      this.removeNotified = true;
    }

    @Override
    public void notifyChange(SelectionBuffer unit) {

    }

    public boolean getAddNotified() {
      return this.addNotified;
    }

    public boolean getRemoveNotified() {
      return this.removeNotified;
    }
  }
}