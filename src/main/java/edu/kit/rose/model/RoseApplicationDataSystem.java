package edu.kit.rose.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.kit.rose.infrastructure.Box;
import edu.kit.rose.infrastructure.RoseBox;
import edu.kit.rose.infrastructure.RoseDualSetObservable;
import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * A standard implementation for the {@link ApplicationDataSystem}.
 * Provided with a global config file it will write changes in the applicationData to the
 * config file in real time.
 */
class RoseApplicationDataSystem extends RoseDualSetObservable<AttributeType,
    Path, ApplicationDataSystem> implements ApplicationDataSystem {
  /**
   * This adapter calls {@link #save()} once a plausibility criterion changes.
   */
  private final SetObserver<SegmentType, PlausibilityCriterion> criterionChangedSaveAdapter =
      new SetObserver<>() {
        @Override
        public void notifyAddition(SegmentType unit) {
          RoseApplicationDataSystem.this.save();
        }

        @Override
        public void notifyRemoval(SegmentType unit) {
          RoseApplicationDataSystem.this.save();
        }

        @Override
        public void notifyChange(PlausibilityCriterion unit) {
          RoseApplicationDataSystem.this.save();
        }
      };

  private final Path configFilePath;
  private final ObjectMapper serializationObjectMapper = new ObjectMapper(new JsonFactory())
      .enable(SerializationFeature.INDENT_OUTPUT);
  private final CriteriaManager criteriaManager;
  private final Set<AttributeType> shownAttributeTypes;
  private final Set<Path> recentProjectPaths;
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
    this.criteriaManager.addSubscriber(this);
    this.shownAttributeTypes = new HashSet<>(); //fill with standard AttributeTypes
    this.recentProjectPaths = new TreeSet<>();

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
    this.save();
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
      this.save();
    }
  }

  @Override
  public void removeShownAttributeType(AttributeType attributeType) {
    boolean removed = this.shownAttributeTypes.remove(attributeType);

    if (removed) {
      getSubscriberIterator().forEachRemaining(sub -> sub.notifyRemoval(attributeType));
      this.save();
    }
  }

  @Override
  public Box<Path> getRecentProjectPaths() {
    return new RoseBox<>(this.recentProjectPaths);
  }

  @Override
  public void addRecentProjectPath(Path recentProjectPath) {
    var absolute = Objects.requireNonNull(recentProjectPath).toAbsolutePath();

    if (this.recentProjectPaths.add(absolute)) {
      getSubscriberIterator().forEachRemaining(sub -> sub.notifyAdditionSecond(absolute));
      this.save();
    }
  }

  @Override
  public boolean importCriteriaFromFile(Path path) {
    SerializedCriteria export;
    try {
      export = serializationObjectMapper.readValue(path.toFile(), SerializedCriteria.class);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    export.populateCriteriaManager(this.getCriteriaManager());
    return true;
  }

  @Override
  public boolean exportCriteriaToFile(Path path) {
    SerializedCriteria export = new SerializedCriteria(criteriaManager);
    try {
      serializationObjectMapper.writeValue(path.toFile(), export);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
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
    unit.addSubscriber(criterionChangedSaveAdapter);
  }

  @Override
  public void notifyRemoval(PlausibilityCriterion unit) {
    save();
    unit.removeSubscriber(criterionChangedSaveAdapter);
  }

  /**
   * Saves the application data to {@link #configFilePath}.
   */
  private void save() {
    var serialized = new SerializedApplicationData(this);
    try {
      serializationObjectMapper.writeValue(this.configFilePath.toFile(), serialized);
    } catch (IOException e) {
      throw new RuntimeException("could not save application data to disk", e);
    }
  }

  /**
   * Loads the application data from {@link #configFilePath}.
   */
  private void load() {
    SerializedApplicationData serialized;
    try {
      serialized = serializationObjectMapper
          .readValue(this.configFilePath.toFile(), SerializedApplicationData.class);
    } catch (IOException e) {
      throw new RuntimeException("could not load application data from disk", e);
    }

    serialized.populateApplicationDataSystem(this);
  }
}
