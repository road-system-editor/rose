package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.kit.rose.infrastructure.language.Language;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializable data model for criteria exports and imports.
 */
class SerializedApplicationData {
  @JsonProperty("shownAttributeTypes")
  private List<AttributeType> shownAttributeTypes;
  @JsonProperty("criteria")
  private SerializedCriteria criteria;
  @JsonProperty("language")
  private Language language;

  /**
   * Creates a new serializable application data object with the data of the given {@code source}.
   */
  public SerializedApplicationData(ApplicationDataSystem source) {
    this.shownAttributeTypes = new ArrayList<>(source.getShownAttributeTypes().getSize());
    for (var type : source.getShownAttributeTypes()) {
      this.shownAttributeTypes.add(type);
    }

    this.criteria = new SerializedCriteria(source.getCriteriaManager());
    this.language = source.getLanguage();
  }

  /**
   * Empty constructor to be used by Jackson when de-serializing a file into this model.
   */
  @SuppressWarnings("unused")
  private SerializedApplicationData() {
  }

  /**
   * Populates a given {@code target} with the data of this object.
   * The target is expected to have default data only.
   */
  public void populateApplicationDataSystem(ApplicationDataSystem target) {
    this.shownAttributeTypes.forEach(target::addShownAttributeType);
    this.criteria.populateCriteriaManager(target.getCriteriaManager());
    target.setLanguage(this.language);
  }
}
