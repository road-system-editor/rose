package edu.kit.rose.controller.selection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
  private static Segment segment1;
  private static Segment segment2;
  private RoseSelectionBuffer selectionBuffer;

  @BeforeAll
  static void init() {
    segment1 = new Base();
    segment2 = new Exit();
  }

  @BeforeEach
  void setUp() {
    this.selectionBuffer = new RoseSelectionBuffer();
  }

  @Test
  void addSegmentSelection() {
    this.selectionBuffer.addSegmentSelection(segment1);
    this.selectionBuffer.addSegmentSelection(segment2);

    Assertions.assertEquals(2, this.selectionBuffer.getSelectedSegments().size());
    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(segment1));
    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(segment2));
  }

  @Test
  void removeSegmentSelection() {
    this.selectionBuffer.addSegmentSelection(segment1);
    this.selectionBuffer.addSegmentSelection(segment2);
    this.selectionBuffer.removeSegmentSelection(segment1);

    Assertions.assertEquals(1, this.selectionBuffer.getSelectedSegments().size());
    Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(segment1));
    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(segment2));
  }

  @Test
  void toggleSegmentSelection() {
    this.selectionBuffer.addSegmentSelection(segment1);

    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(segment1));
    Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(segment2));

    this.selectionBuffer.toggleSegmentSelection(segment1);
    this.selectionBuffer.toggleSegmentSelection(segment2);

    Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(segment1));
    Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(segment2));
  }

  @Test
  void removeAllSelectionsTest() {
    this.selectionBuffer.addSegmentSelection(segment1);
    this.selectionBuffer.addSegmentSelection(segment2);
    this.selectionBuffer.removeAllSelections();

    Assertions.assertEquals(0, this.selectionBuffer.getSelectedSegments().size());

  }

  @Test
  void notifyTest() {
    SetObserver<Segment, SelectionBuffer> observer = mock(SetObserver.class);

    this.selectionBuffer.addSubscriber(observer);
    this.selectionBuffer.addSegmentSelection(segment1);
    verify(observer, times(1)).notifyAddition(segment1);

    this.selectionBuffer.removeSegmentSelection(segment1);
    verify(observer, times(1)).notifyRemoval(segment1);

    this.selectionBuffer.removeSubscriber(observer);
    this.selectionBuffer.addSegmentSelection(segment1);
    verify(observer, times(1)).notifyAddition(segment1);
  }

  @Test
  void getThisTest() {
    Assertions.assertSame(this.selectionBuffer, this.selectionBuffer.getThis());
  }
}