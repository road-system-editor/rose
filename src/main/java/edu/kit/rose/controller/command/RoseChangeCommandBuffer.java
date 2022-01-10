package edu.kit.rose.controller.command;

/**
 * Implements a container that stores ChangeCommand in
 * the order they get added. This container is responsible for
 * executing the stored ChangeCommand in the correct change order
 * when it comes to undo/redo requests.
 *
 * @author ROSE Team
 */
public class RoseChangeCommandBuffer implements ChangeCommandBuffer {
    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public void addCommand(ChangeCommand changeCommand) {

    }

    @Override
    public void clear() {

    }
}
