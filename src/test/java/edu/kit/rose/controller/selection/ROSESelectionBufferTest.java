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
 * Unit test for {@link ROSESelectionBuffer}
 */
class ROSESelectionBufferTest {
    private static Segment base;
    private static Segment exit;
    private ROSESelectionBuffer selectionBuffer;

    @BeforeAll
    static void init() {
        ROSESelectionBufferTest.base = new Base();
        ROSESelectionBufferTest.exit = new Exit();
    }

    @BeforeEach
    void setUp() {
        this.selectionBuffer = new ROSESelectionBuffer();
    }

    @Test
    void addSegmentSelection() {
        this.selectionBuffer.addSegmentSelection(ROSESelectionBufferTest.base);
        this.selectionBuffer.addSegmentSelection(ROSESelectionBufferTest.exit);

        Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.base));
        Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.exit));

    }

    @Test
    void removeSegmentSelection() {
        this.selectionBuffer.addSegmentSelection(ROSESelectionBufferTest.base);
        this.selectionBuffer.addSegmentSelection(ROSESelectionBufferTest.exit);
        this.selectionBuffer.removeSegmentSelection(ROSESelectionBufferTest.exit);

        Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.base));
        Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.exit));
    }

    @Test
    void toggleSegmentSelection() {
        this.selectionBuffer.addSegmentSelection(ROSESelectionBufferTest.base);

        Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.base));
        Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.exit));

        this.selectionBuffer.toggleSegmentSelection(ROSESelectionBufferTest.base);
        this.selectionBuffer.toggleSegmentSelection(ROSESelectionBufferTest.exit);

        Assertions.assertFalse(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.base));
        Assertions.assertTrue(this.selectionBuffer.isSegmentSelected(ROSESelectionBufferTest.exit));
    }

    @Test
    void notifyTest() {
        Observer observer = new Observer();

        this.selectionBuffer.addSubscriber(observer);
        this.selectionBuffer.addSegmentSelection(ROSESelectionBufferTest.base);

        Assertions.assertTrue(observer.getAddNotified());
        Assertions.assertFalse(observer.removeNotified);

        this.selectionBuffer.removeSegmentSelection(ROSESelectionBufferTest.base);

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