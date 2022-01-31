package edu.kit.rose.controller.hierarchy;

import edu.kit.rose.controller.command.ChangeCommand;
import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.controller.selection.SelectionBuffer;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.elements.Element;
import edu.kit.rose.model.roadsystem.elements.Group;
import edu.kit.rose.model.roadsystem.elements.Segment;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Provides the functionality to manage the element
 * hierarchy of the roadsystem.
 */
public class RoseHierarchyController extends Controller
    implements HierarchyController,
    SetObserver<Segment, SelectionBuffer> {

  private static final Boolean SELECTED = true;
  private static final Boolean UNSELECTED = false;

  /**
   * The container for selected segments.
   */
  private final ChangeCommandBuffer changeCommandBuffer;
  private final Project project;
  private final SelectionBuffer selectionBuffer;
  private final Set<BiConsumer<Segment, Boolean>> consumers;

  /**
   * Creates a new {@link RoseHierarchyController}.
   *
   * @param storageLock         the coordinator for controller actions
   * @param changeCommandBuffer the buffer for change commands
   * @param selectionBuffer     the container that stores selected segments
   * @param project             the model facade for project data
   * @param navigator           the navigator for the controller
   */
  public RoseHierarchyController(StorageLock storageLock, ChangeCommandBuffer changeCommandBuffer,
                                 SelectionBuffer selectionBuffer, Project project,
                                 Navigator navigator) {
    super(storageLock, navigator);
    this.changeCommandBuffer = changeCommandBuffer;
    this.selectionBuffer = selectionBuffer;
    selectionBuffer.addSubscriber(this);
    this.project = project;
    this.consumers = new HashSet<>();
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
  public void toggleSegmentSelection(Segment segment) {
    this.selectionBuffer.toggleSegmentSelection(segment);
  }


  @Override
  public void addSubscription(BiConsumer<Segment, Boolean> consumer) {
    this.consumers.add(consumer);
  }

  @Override
  public void removeSubscription(BiConsumer<Segment, Boolean> consumer) {
    this.consumers.remove(consumer);
  }

  @Override
  public void notifyAddition(Segment unit) {
    consumers.forEach(e -> e.accept(unit, SELECTED));
  }

  @Override
  public void notifyRemoval(Segment unit) {
    consumers.forEach(e -> e.accept(unit, UNSELECTED));
  }

  @Override
  public void notifyChange(SelectionBuffer unit) {

  }

  private void addAndExecute(ChangeCommand command) {
    this.changeCommandBuffer.addCommand(command);
    command.execute();
  }

}
