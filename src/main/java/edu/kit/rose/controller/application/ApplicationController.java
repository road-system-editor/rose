package edu.kit.rose.controller.application;

import edu.kit.rose.infrastructure.language.Language;

/**
 * Provides functionality for application settings
 * and other GUI related methods
 *
 * @author  ROSE Team
 */
public interface ApplicationController {

    /**
     * Updates the applications displaying language to the given language.
     *
     * @param language the language to be set
     */
    void setLanguage(Language language);

    /**
     * Shows a help-window to user
     */
    void showHelp();

    /**
     * Undoes the last undoable action of the user
     */
    void undo();

    /**
     * Redoes the last undoable action of the user
     */
    void redo();
}
