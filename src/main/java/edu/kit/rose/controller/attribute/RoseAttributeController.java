package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;


/**
 * Provides methods for setting the values of attribute accessors.
 *
 */
public class RoseAttributeController extends Controller implements AttributeController {

  private final ChangeCommandBuffer changeCommandBuffer;
  private final ApplicationDataSystem applicationDataSystem;
  private final Project project;

  /**
   * Creates a new {@link RoseAttributeController}.
   *
   * @param changeCommandBuffer the buffer for change commands
   * @param storageLock         the coordinator for controller actions
   * @param applicationDataSystem             the model facade for project data
   */
  public RoseAttributeController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                 Project project,
                                 ApplicationDataSystem applicationDataSystem) {
    super(storageLock);
    this.changeCommandBuffer = changeCommandBuffer;
    this.project = project;
    this.applicationDataSystem = applicationDataSystem;
  }

  @Override
  public <T> void setAttribute(AttributeAccessor<T> accessor, T value) {
    var command  = new SetAttributeAccessorCommand<>(project, accessor,
        accessor.getValue(), value);

    command.execute();
    changeCommandBuffer.addCommand(command);
  }

  @Override
  public void addShownAttributeType(AttributeType attributeType) {
    this.applicationDataSystem.addShownAttributeType(attributeType);
  }

  @Override
  public void removeShownAttributeType(AttributeType attributeType) {
    this.applicationDataSystem.removeShownAttributeType(attributeType);
  }
}
