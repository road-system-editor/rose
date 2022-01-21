package edu.kit.rose.view.commons;

import edu.kit.rose.infrastructure.Observable;
import edu.kit.rose.infrastructure.UnitObserver;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * This is a utility class that allows executing {@link Runnable}s in the event that a JavaFX node
 * is removed from its parent.
 */
public final class UnmountUtility {
  public static <T> void subscribeUntilUnmount(Node node, UnitObserver<T> observer,
                                               Observable<UnitObserver<T>, T> observable) {
    observable.addSubscriber(observer);
    runOnUnmount(node, () -> observable.removeSubscriber(observer));
  }

  public static void runOnUnmount(Node node, Runnable listener) {
    new ListenerAdapter(node, listener).register();
  }

  private static class ListenerAdapter implements ListChangeListener<Node> {
    private final Node node;
    private final Runnable listener;
    private ObservableList<Node> subscribedTo;

    public ListenerAdapter(Node node, Runnable listener) {
      this.node = node;
      this.listener = listener;
    }

    @Override
    public void onChanged(Change<? extends Node> c) {
      if (c.getRemoved().contains(this.node)) {
        this.listener.run();
        this.unregister();
      }
    }

    public void register() {
      this.subscribedTo = node.getParent().getChildrenUnmodifiable();
      this.subscribedTo.addListener(this);
    }

    public void unregister() {
      if (this.subscribedTo != null) {
        this.subscribedTo.removeListener(this);
        this.subscribedTo = null;
      }
    }
  }
}
