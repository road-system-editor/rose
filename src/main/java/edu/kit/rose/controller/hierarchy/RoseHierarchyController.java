package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides the functionality to manage the element
 * hierarchy of the roadsystem.
 */
public class RoseHierarchyController extends Controller
    implements HierarchyController,
    SetObserver<Segment, SelectionBuffer> {

  /**
   * The container for selected segments.
   */
  private final ChangeCommandBuffer changeCommandBuffer;
  private final Project project;
  private final Set<SetObserver<Segment, HierarchyController>> observers;
  private final SelectionBuffer selectionBuffer;

  /**
   * Creates a new {@link RoseHierarchyController}.
   *
   * @param storageLock         the coordinator for controller actions
   * @param changeCommandBuffer the buffer for change commands
   * @param selectionBuffer     the container that stores selected segments
   * @param project             the model facade for project data
   */
  public RoseHierarchyController(StorageLock storageLock, ChangeCommandBuffer changeCommandBuffer,
                                 SelectionBuffer selectionBuffer, Project project) {
    super(storageLock);
    this.changeCommandBuffer = changeCommandBuffer;
    this.selectionBuffer = selectionBuffer;
    selectionBuffer.addSubscriber(this);
    this.project = project;
    this.observers = new HashSet<>();
  }

  @Override
  public void createGroup() {
    ChangeCommand createGroupCommand = new CreateGroupCommand(this.project,
            this.selectionBuffer.getSelectedSegments());
    addAndExecute(createGroupCommand);
  }

  @Override
  public void deleteGroup(Group group) {
    ChangeCommand deleteGroupCommand = new DeleteGroupCommand(this.project, group);
    addAndExecute(deleteGroupCommand);
  }

  @Override
  public void addElementToGroup(Element element, Group group) {
    ChangeCommand addElementCommand = new AddElementToGroupCommand(this.project, element, group);
    addAndExecute(addElementCommand);
  }

  @Override
  public void setGroupName(Group group, String name) {
    ChangeCommand setGroupNamCommand = new SetGroupNameCommand(group, name);
    addAndExecute(setGroupNamCommand);
  }

  @Override
  public void addSubscriber(SetObserver<Segment, HierarchyController> observer) {
    this.observers.add(observer);
  }

  @Override
  public void removeSubscriber(SetObserver<Segment, HierarchyController> observer) {
    this.observers.remove(observer);
  }

  @Override
  public void notifySubscribers() {
    observers.forEach(e -> e.notifyChange(this));
  }

  @Override
  public HierarchyController getThis() {
    return this;
  }

  @Override
  public void notifyAddition(Segment unit) {
    observers.forEach(e -> e.notifyAddition(unit));
  }

  @Override
  public void notifyRemoval(Segment unit) {
    observers.forEach(e -> e.notifyRemoval(unit));
  }

  @Override
  public void notifyChange(SelectionBuffer unit) {

  }

  private void addAndExecute(ChangeCommand command) {
    this.changeCommandBuffer.addCommand(command);
    command.execute();
  }
}
