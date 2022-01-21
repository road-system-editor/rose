package edu.kit.rose.controller.attribute;

import edu.kit.rose.controller.command.ChangeCommandBuffer;
import edu.kit.rose.controller.commons.Controller;
import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.roadsystem.attributes.AttributeAccessor;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;

/**
 * Standard implementation of the {@link AttributeController} interface.
 *
 * @author ROSE Team
 */
public class RoseAttributeController extends Controller implements AttributeController {

  /**
   * Creates a new {@link RoseAttributeController}.
   *
   * @param changeCommandBuffer the buffer for change commands
   * @param storageLock         the coordinator for controller actions
   * @param project             the model facade for project data
   */
  public RoseAttributeController(ChangeCommandBuffer changeCommandBuffer, StorageLock storageLock,
                                 Project project) {
    super(changeCommandBuffer, storageLock);
  }

  @Override
  public <T> void setAttribute(AttributeAccessor<T> accessor, T value) {


  }

  @Override
  public void addShownAttributeType(AttributeType attributeType) {

  }

  @Override
  public void removeShownAttributeType(AttributeType attributeType) {

  }
}
