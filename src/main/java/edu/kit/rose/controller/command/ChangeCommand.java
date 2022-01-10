package edu.kit.rose.controller.command;

/**
 * Represents an action that can be undone and redone.
 *
 */
public interface ChangeCommand {

    /**
     * Executes or redoes the defined action.
     */
    void execute();

    /**
     * Undoes the defined action.
     */
    void unexecute();
}
