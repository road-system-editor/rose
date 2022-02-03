package edu.kit.rose.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseSetObservable;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * A standard implementation for the {@link ApplicationDataSystem}.
 * Provided with a global config file it will write changes in the applicationData to the
 * config file in real time.
 */
class RoseApplicationDataSystem extends RoseSetObservable<AttributeType, ApplicationDataSystem>
    implements ApplicationDataSystem {
  private final ObjectMapper serializationObjectMapper = new ObjectMapper(new JsonFactory())
      .enable(SerializationFeature.INDENT_OUTPUT);

  private final Path configFilePath;
  private final CriteriaManager criteriaManager;
  private final Set<AttributeType> shownAttributeTypes;
  private Language language = Language.DEFAULT;

  /**
   * Constructor.
   * Needs to be provided with a Path to a config File for global Settings.
   *
   * @param configFilePath the Path to a config File containing global Settings.
   */
  public RoseApplicationDataSystem(Path configFilePath) {
    this.configFilePath = configFilePath;
    this.criteriaManager = new CriteriaManager();
    this.shownAttributeTypes = new HashSet<>(); //fill with standard AttributeTypes

    // or get from config file
    if (configFilePath.toFile().exists()) {
      load();
    }
  }

  @Override
  public Language getLanguage() {
    return this.language;
  }

  @Override
  public void setLanguage(Language language) {
    this.language = language;
    notifySubscribers();
  }

  @Override
  public CriteriaManager getCriteriaManager() {
    return this.criteriaManager;
  }

  @Override
  public Box<AttributeType> getShownAttributeTypes() {
    return new RoseBox<>(shownAttributeTypes);
  }

  @Override
  public void addShownAttributeType(AttributeType attributeType) {
    boolean added = this.shownAttributeTypes.add(attributeType);

    if (added) {
      getSubscriberIterator().forEachRemaining(sub -> sub.notifyAddition(attributeType));
    }
  }

  @Override
  public void removeShownAttributeType(AttributeType attributeType) {
    boolean removed = this.shownAttributeTypes.remove(attributeType);

    if (removed) {
      getSubscriberIterator().forEachRemaining(sub -> sub.notifyRemoval(attributeType));
    }
  }

  @Override
  public void importCriteriaFromFile(Path path) {
    SerializableCriteria export;
    try {
      export = serializationObjectMapper.readValue(path.toFile(), SerializableCriteria.class);
    } catch (IOException e) {
      e.printStackTrace();
      // TODO proper exception handling
      return;
    }

    export.populateCriteriaManager(this.getCriteriaManager());
  }

  @Override
  public void exportCriteriaToFile(Path path) {
    SerializableCriteria export = new SerializableCriteria(criteriaManager);
    try {
      serializationObjectMapper.writeValue(path.toFile(), export);
    } catch (IOException e) {
      e.printStackTrace();
      // TODO proper exception handling
    }
  }

  @Override
  public ApplicationDataSystem getThis() {
    return this;
  }

  @Override
  public void notifyChange(CriteriaManager unit) {
    save();
  }

  @Override
  public void notifyAddition(PlausibilityCriterion unit) {
    save();
  }

  @Override
  public void notifyRemoval(PlausibilityCriterion unit) {
    save();
  }

  private void save() {
    //TODO
  }

  private void load() {
    //TODO
  }
}
