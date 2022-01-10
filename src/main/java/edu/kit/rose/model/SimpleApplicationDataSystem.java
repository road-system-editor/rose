package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.UnitObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;

import java.nio.file.Path;

/**
 * A standard implementation for the {@link ApplicationDataSystem}.
 * Provided with a global config file it will write changes in the applicationData to the config file in real time.
 */
class SimpleApplicationDataSystem implements ApplicationDataSystem {

    /**
     * Constructor.
     * Needs to be provided with a Path to a config File for global Settings.
     * @param configFilePath the Path to a config File containing global Settings.
     */
    public SimpleApplicationDataSystem(Path configFilePath) {

    }

    @Override
    public Language getLanguage() {
        return null;
    }

    @Override
    public void setLanguage(Language language) {

    }

    @Override
    public CriteriaManager getCriteriaManager() {
        return null;
    }

    /**
     * Imports all {@link edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion} from the File at the given
     * Path. This adds the included Criteria to the currently active Criteria.
     *
     *
     * @param path the path to the File that contains the Criteria.
     */
    public void importCriteriaFromFile(Path path) {

    }

    @Override
    public Box<AttributeType> getShownAttributeTypes() {
        return null;
    }

    @Override
    public void addShownAttributeType(AttributeType attributeType) {

    }

    @Override
    public void removeShownAttributeType(AttributeType attributeType) {

    }

    @Override
    public void exportCriteriaToFile(Path path) {

    }

    @Override
    public void notifySubscribers() {

    }

    @Override
    public void addSubscriber(UnitObserver<ApplicationDataSystem> observer) {

    }

    @Override
    public void removeSubscriber(UnitObserver<ApplicationDataSystem> observer) {

    }

    @Override
    public void notifyChange(CriteriaManager unit) {

    }
}
