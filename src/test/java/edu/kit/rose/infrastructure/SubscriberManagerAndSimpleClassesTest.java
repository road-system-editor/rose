package edu.kit.rose.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link SimpleUnitObservable}, {@link SimpleSetObservable} and
 * {@link SimpleDualSetObservable}. Thus testing the SubscriberManager class as well.
 */
public class SubscriberManagerAndSimpleClassesTest {

  @Test
  public void singleSubscriberSubscribeTestSimpleUnitObservable() {
    var unitObservable = new TestUnitObservable();
    var unitObserver = new TestUnitObserver();

    unitObservable.notifySubscribers();
    Assertions.assertFalse(unitObserver.isNotified);

    unitObservable.addSubscriber(unitObserver);
    Assertions.assertFalse(unitObserver.isNotified);

    unitObservable.removeSubscriber(unitObserver);
    Assertions.assertFalse(unitObserver.isNotified);

    unitObservable.addSubscriber(unitObserver);
    unitObservable.notifySubscribers();
    Assertions.assertTrue(unitObserver.isNotified);

  }

  @Test
  public void multipleSubscribersSubscribeTestSimpleUnitObservable() {
    var unitObservable = new TestUnitObservable();
    var unitObserver1 = new TestUnitObserver();
    var unitObserver2 = new TestUnitObserver();

    unitObservable.notifySubscribers();
    Assertions.assertFalse(unitObserver1.isNotified || unitObserver2.isNotified);

    unitObservable.addSubscriber(unitObserver1);
    unitObservable.addSubscriber(unitObserver2);
    unitObservable.notifySubscribers();
    Assertions.assertTrue(unitObserver1.isNotified && unitObserver2.isNotified);

  }

  @Test
  public void singleSubscriberSubscribeTestSimpleSetObservable() {
    var setObservable = new TestSetObservable();
    var setObserver = new TestSetObserver();

    setObservable.notifySubscribers();
    Assertions.assertFalse(setObserver.isNotified);

    setObservable.addSubscriber(setObserver);
    Assertions.assertFalse(setObserver.isNotified);

    setObservable.removeSubscriber(setObserver);
    Assertions.assertFalse(setObserver.isNotified);

    setObservable.addSubscriber(setObserver);
    setObservable.notifySubscribers();
    Assertions.assertTrue(setObserver.isNotified);

  }

  @Test
  public void multipleSubscribersSubscribeTestSimpleSetObservable() {
    var setObservable = new TestSetObservable();
    var setObserver1 = new TestSetObserver();
    var setObserver2 = new TestSetObserver();

    setObservable.notifySubscribers();
    Assertions.assertFalse(setObserver1.isNotified || setObserver2.isNotified);

    setObservable.addSubscriber(setObserver1);
    setObservable.addSubscriber(setObserver2);
    setObservable.notifySubscribers();
    Assertions.assertTrue(setObserver1.isNotified && setObserver2.isNotified);

  }

  @Test
  public void singleSubscriberSubscribeTestSimpleDualSetObservable() {
    var dualSetObservable = new TestDualSetObservable();
    var dualSetObserver = new TestDualSetObserver();

    dualSetObservable.notifySubscribers();
    Assertions.assertFalse(dualSetObserver.isNotified);

    dualSetObservable.addSubscriber(dualSetObserver);
    Assertions.assertFalse(dualSetObserver.isNotified);

    dualSetObservable.removeSubscriber(dualSetObserver);
    Assertions.assertFalse(dualSetObserver.isNotified);

    dualSetObservable.addSubscriber(dualSetObserver);
    dualSetObservable.notifySubscribers();
    Assertions.assertTrue(dualSetObserver.isNotified);

  }

  @Test
  public void multipleSubscribersSubscribeTestSimpleDualSetObservable() {
    var dualSetObservable = new TestDualSetObservable();
    var dualSetObserver1 = new TestDualSetObserver();
    var dualSetObserver2 = new TestDualSetObserver();

    dualSetObservable.notifySubscribers();
    Assertions.assertFalse(dualSetObserver1.isNotified || dualSetObserver2.isNotified);

    dualSetObservable.addSubscriber(dualSetObserver1);
    dualSetObservable.addSubscriber(dualSetObserver2);
    dualSetObservable.notifySubscribers();
    Assertions.assertTrue(dualSetObserver1.isNotified && dualSetObserver2.isNotified);

  }




  private static class TestUnitObservable extends SimpleUnitObservable<TestUnitObservable> {

    @Override
    public TestUnitObservable getThis() {
      return this;
    }
  }

  private static class TestSetObservable extends SimpleSetObservable<Object, TestSetObservable> {

    @Override
    public TestSetObservable getThis() {
      return this;
    }
  }

  private static class TestDualSetObservable
          extends SimpleDualSetObservable<Object, Object, TestDualSetObservable> {

    @Override
    public TestDualSetObservable getThis() {
      return this;
    }
  }

  private static class TestUnitObserver implements UnitObserver<TestUnitObservable> {

    private boolean isNotified = false;

    @Override
    public void notifyChange(TestUnitObservable unit) {
      isNotified = true;
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
