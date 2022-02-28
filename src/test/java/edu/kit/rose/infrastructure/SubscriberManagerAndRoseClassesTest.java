package edu.kit.rose.infrastructure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link RoseUnitObservable}, {@link RoseSetObservable} and
 * {@link RoseDualSetObservable}. Thus testing the SubscriberManager class as well.
 */
public class SubscriberManagerAndRoseClassesTest {
  @Test
  void testAddSameSubscriberIsIgnored() {
    var unitObservable = new TestUnitObservable();
    UnitObserver<TestUnitObservable> unitObserver = mockUnitObserver();

    unitObservable.addSubscriber(unitObserver);
    unitObservable.addSubscriber(unitObserver);

    unitObservable.notifySubscribers();

    // the unit observer should be added exactly one time
    verify(unitObserver, times(1)).notifyChange(unitObservable);
  }

  @Test
  void testAddSubscriberNull() {
    var observable = new TestUnitObservable();
    assertThrows(NullPointerException.class, () -> observable.addSubscriber(null));
  }

  @SuppressWarnings("unchecked") // mock is allowed to be of generic type T
  private <T> UnitObserver<T> mockUnitObserver() {
    return mock(UnitObserver.class);
  }

  @Test
  public void singleSubscriberSubscribeTestRoseUnitObservable() {
    var unitObservable = new TestUnitObservable();
    UnitObserver<TestUnitObservable> unitObserver = mockUnitObserver();

    unitObservable.notifySubscribers();
    verify(unitObserver, never()).notifyChange(any());

    unitObservable.addSubscriber(unitObserver);
    verify(unitObserver, never()).notifyChange(any());

    unitObservable.notifySubscribers();
    verify(unitObserver, times(1)).notifyChange(unitObservable);

    unitObservable.removeSubscriber(unitObserver);
    verify(unitObserver, times(1)).notifyChange(any());

    unitObservable.notifySubscribers();
    verify(unitObserver, times(1)).notifyChange(any());
  }

  @Test
  public void multipleSubscribersSubscribeTestRoseUnitObservable() {
    var unitObservable = new TestUnitObservable();
    UnitObserver<TestUnitObservable> unitObserver1 = mockUnitObserver();
    UnitObserver<TestUnitObservable> unitObserver2 = mockUnitObserver();

    unitObservable.notifySubscribers();
    verify(unitObserver1, never()).notifyChange(any());
    verify(unitObserver2, never()).notifyChange(any());

    unitObservable.addSubscriber(unitObserver1);
    unitObservable.addSubscriber(unitObserver2);
    unitObservable.notifySubscribers();
    verify(unitObserver1, times(1)).notifyChange(unitObservable);
    verify(unitObserver2, times(1)).notifyChange(unitObservable);

  }

  @Test
  public void singleSubscriberSubscribeTestRoseSetObservable() {
    var setObservable = new TestSetObservable();
    var setObserver = new TestSetObserver();

    setObservable.notifySubscribers();
    assertFalse(setObserver.isNotified);

    setObservable.addSubscriber(setObserver);
    assertFalse(setObserver.isNotified);

    setObservable.removeSubscriber(setObserver);
    assertFalse(setObserver.isNotified);

    setObservable.addSubscriber(setObserver);
    setObservable.notifySubscribers();
    Assertions.assertTrue(setObserver.isNotified);

  }

  @Test
  public void multipleSubscribersSubscribeTestRoseSetObservable() {
    var setObservable = new TestSetObservable();
    var setObserver1 = new TestSetObserver();
    var setObserver2 = new TestSetObserver();

    setObservable.notifySubscribers();
    assertFalse(setObserver1.isNotified || setObserver2.isNotified);

    setObservable.addSubscriber(setObserver1);
    setObservable.addSubscriber(setObserver2);
    setObservable.notifySubscribers();
    Assertions.assertTrue(setObserver1.isNotified && setObserver2.isNotified);

  }

  @Test
  public void singleSubscriberSubscribeTestRoseDualSetObservable() {
    var dualSetObservable = new TestDualSetObservable();
    var dualSetObserver = new TestDualSetObserver();

    dualSetObservable.notifySubscribers();
    assertFalse(dualSetObserver.isNotified);

    dualSetObservable.addSubscriber(dualSetObserver);
    assertFalse(dualSetObserver.isNotified);

    dualSetObservable.removeSubscriber(dualSetObserver);
    assertFalse(dualSetObserver.isNotified);

    dualSetObservable.addSubscriber(dualSetObserver);
    dualSetObservable.notifySubscribers();
    Assertions.assertTrue(dualSetObserver.isNotified);

  }

  @Test
  public void multipleSubscribersSubscribeTestRoseDualSetObservable() {
    var dualSetObservable = new TestDualSetObservable();
    var dualSetObserver1 = new TestDualSetObserver();
    var dualSetObserver2 = new TestDualSetObserver();

    dualSetObservable.notifySubscribers();
    assertFalse(dualSetObserver1.isNotified || dualSetObserver2.isNotified);

    dualSetObservable.addSubscriber(dualSetObserver1);
    dualSetObservable.addSubscriber(dualSetObserver2);
    dualSetObservable.notifySubscribers();
    Assertions.assertTrue(dualSetObserver1.isNotified && dualSetObserver2.isNotified);

  }




  private static class TestUnitObservable extends RoseUnitObservable<TestUnitObservable> {

    @Override
    public TestUnitObservable getThis() {
      return this;
    }
  }

  private static class TestSetObservable extends RoseSetObservable<Object, TestSetObservable> {

    @Override
    public TestSetObservable getThis() {
      return this;
    }
  }

  private static class TestDualSetObservable
          extends RoseDualSetObservable<Object, Object, TestDualSetObservable> {

    @Override
    public TestDualSetObservable getThis() {
      return this;
    }
  }

  private static class TestSetObserver implements SetObserver<Object, TestSetObservable> {

    private boolean isNotified = false;

    @Override
    public void notifyAddition(Object unit) {

    }

    @Override
    public void notifyRemoval(Object unit) {

    }

    @Override
    public void notifyChange(TestSetObservable unit) {
      isNotified = true;
    }
  }

  private static class TestDualSetObserver
          implements DualSetObserver<Object, Object, TestDualSetObservable> {

    private boolean isNotified = false;

    @Override
    public void notifyAdditionSecond(Object unit) {

    }

    @Override
    public void notifyRemovalSecond(Object unit) {

    }

    @Override
    public void notifyAddition(Object unit) {

    }

    @Override
    public void notifyRemoval(Object unit) {

    }

    @Override
    public void notifyChange(TestDualSetObservable unit) {
      isNotified = true;
    }
  }

}
