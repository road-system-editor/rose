package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.DualSetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.nio.file.Path;

/**
 * ApplicationData is all data that has to be saved independently of the project.
 * It includes the current language setting as well as the {@link CriteriaManager}
 * as these are to be configured on a project independent level.
 * Is to be observed by the {@link edu.kit.rose.view} package.
 * Observes the {@link CriteriaManager} in order to be informed of changes to the criteria held
 * within, as the ApplicationDataSystem needs to write these changes to the config file.
 */
public interface ApplicationDataSystem extends DualSetObservable<AttributeType, Path,
    ApplicationDataSystem>, SetObserver<PlausibilityCriterion, CriteriaManager> {

  /**
   * Returns the currently selected {@link Language}.
   *
   * @return the currently selected {@link Language}.
   */
  Language getLanguage();

  /**
   * Sets a {@link Language}.
   *
   * @param language the {@link Language} to set.
   */
  void setLanguage(Language language);

  /**
   * Gives the {@link CriteriaManager}.
   *
   * @return the {@link CriteriaManager} that is saved in this ApplicationDataSystem.
   */
  CriteriaManager getCriteriaManager();

  /**
   * Imports all {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}
   * from the File at the given Path.
   * This adds the included Criteria to the currently active Criteria.
   *
   * @param path the path to the File that contains the Criteria.
   * @return {@code true} if the import was successful and {@code false} otherwise.
   */
  boolean importCriteriaFromFile(Path path);

  /**
   * Exports all {@link edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion}
   * into a File at the given Path.
   *
   * @param path the path giving the location of where to save the new File.
   * @return {@code true} if the import was successful and {@code false} otherwise.
   */
  boolean exportCriteriaToFile(Path path);

  /**
   * Returns the {@link AttributeType}s that are currently chosen to be rendered
   * onto street segments.
   *
   * @return the selected AttributeTypes
   */
  Box<AttributeType> getShownAttributeTypes();

  /**
   * Adds an {@link AttributeType} to the currently chosen.
   *
   * @param attributeType the AttributeType that is to be added.
   */
  void addShownAttributeType(AttributeType attributeType);

  /**
   * Removes an {@link AttributeType} from the currently chosen.
   *
   * @param attributeType the AttributeType that is to be removed.
   */
  void removeShownAttributeType(AttributeType attributeType);

  /**
   * Returns paths of recently opened {@link Project}s.
   */
  Box<Path> getRecentProjectPaths();

  /**
   * Adds a path to the list of recently opened projects.
   */
  void addRecentProjectPath(Path recentProjectPath);
}
