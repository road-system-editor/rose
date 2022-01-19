package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;

/**
 * Provides the functionality to manage the element
 * hierarchy of the roadsystem.
 */
public class RoseHierarchyController extends Controller
    implements HierarchyController,
    SetObserver<Segment, SelectionBuffer> {

  /**
   * The container for selected segments
   */
  private SelectionBuffer selectionBuffer;


  /**
   * Creates a new {@link RoseHierarchyController}.
   *
   * @param changeCommandBuffer the buffer for change commands
   * @param storageLock         the coordinator for controller actions
   * @param selectionBuffer     the container that stores selected segments
   * @param project             the model facade for project data
   */
  public RoseHierarchyController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                 SelectionBuffer selectionBuffer, Project project) {
    super(changeCommandBuffer, storageLock);
    this.selectionBuffer = selectionBuffer;
  }

  @Override
  public void createGroup() {

  }

  @Override
  public void deleteGroup(Group group) {

  }

  @Override
  public void addElementToGroup(Element element, Group group) {

  }

  @Override
  public void setGroupName(Group group, String name) {

  }

  @Override
  public void addSubscriber(SetObserver<Segment, HierarchyController> observer) {

  }

  @Override
  public void removeSubscriber(SetObserver<Segment, HierarchyController> observer) {

  }

  @Override
  public void notifySubscribers() {

  }

  @Override
  public HierarchyController getThis() {
    return this;
  }

  @Override
  public void notifyAddition(Segment unit) {

  }

  @Override
  public void notifyRemoval(Segment unit) {

  }

  @Override
  public void notifyChange(SelectionBuffer unit) {

  }
}
