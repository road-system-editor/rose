package edu.kit.rose.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.kit.rose.model.plausibility.criteria.CompatibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterionType;
import edu.kit.rose.model.plausibility.criteria.validation.ValidationType;
import edu.kit.rose.model.roadsystem.attributes.AttributeType;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializable data model for criteria exports and imports.
 */
class SerializedCriteria {
  @JsonProperty("criteria")
  private List<SerializedPlausibilityCriterion<? extends PlausibilityCriterion>> criteria;

  /**
   * Creates a new serializable criteria export with the criteria of the given
   * {@code criteriaManager}.
   */
  public SerializedCriteria(CriteriaManager criteriaManager) {
    this.criteria = new ArrayList<>(criteriaManager.getCriteria().getSize());
    for (var criterion : criteriaManager.getCriteria()) {
      var serialized = SerializedPlausibilityCriterion.forRoseCriterion(criterion);
      if (serialized != null) {
        criteria.add(serialized);
      }
    }
  }

  /**
   * Empty constructor to be used by Jackson when de-serializing a file into this model.
   */
  @SuppressWarnings("unused")
  public SerializedCriteria() {
  }

  /**
   * Adds all criteria of this export to the given {@code target}.
   */
  public void populateCriteriaManager(CriteriaManager target) {
    this.criteria.forEach(criterion -> criterion.createAndPopulate(target));
  }

  /**
   * Serializable data model for {@link PlausibilityCriterion} objects.
   *
   * @param <T> the concrete type of the plausibility criterion.
   */
  @JsonTypeInfo(
      use = JsonTypeInfo.Id.NAME,
      include = JsonTypeInfo.As.EXISTING_PROPERTY,
      property = "type"
  )
  @JsonSubTypes({
      @JsonSubTypes.Type(value = SerializedCompatibilityCriterion.class, name = "COMPATIBILITY")
  })
  private abstract static class SerializedPlausibilityCriterion<T extends PlausibilityCriterion> {
    @JsonProperty("type")
    private PlausibilityCriterionType type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("segmentTypes")
    private List<SegmentType> segmentTypes;

    /**
     * Creates a new serialized plausibility criterion with data from the given {@code source}.
     */
    protected SerializedPlausibilityCriterion(T source) {
      this.type = source.getType();
      this.name = source.getName();
      this.segmentTypes = new ArrayList<>();
      for (var type : source.getSegmentTypes()) {
        this.segmentTypes.add(type);
      }
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedPlausibilityCriterion() {
    }

    /**
     * Serializes the given {@code criterion}, if there is a serializer for it.
     *
     * @param criterion the criterion to serialize, may not be {@code null}.
     * @return the serialized criterion or {@code null} if there is no serializer for the given
     *     {@code criterion}.
     */
    public static SerializedPlausibilityCriterion<? extends PlausibilityCriterion> forRoseCriterion(
        PlausibilityCriterion criterion) {
      return criterion.getType() == PlausibilityCriterionType.COMPATIBILITY
          ? new SerializedCompatibilityCriterion((CompatibilityCriterion) criterion)
          : null;
    }

    /**
     * Populates the given {@code criterion} with this object's data.
     */
    protected void populateCriterion(T criterion) {
      criterion.setName(this.name);
      for (var type : this.segmentTypes) {
        criterion.addSegmentType(type);
      }
    }

    /**
     * Creates a plausibility criterion that matches this object's data in the given criteria
     * manager.
     */
    protected abstract T createIn(CriteriaManager target);

    /**
     * Creates a matching plausibility criterion for this object in the criteria manager and
     * populates it with this object's data.
     */
    public void createAndPopulate(CriteriaManager target) {
      this.populateCriterion(this.createIn(target));
    }
  }


  /**
   * Serializable data model for {@link CompatibilityCriterion} objects.
   */
  private static class SerializedCompatibilityCriterion
      extends SerializedPlausibilityCriterion<CompatibilityCriterion> {
    @JsonProperty("attributeType")
    private AttributeType attributeType;
    @JsonProperty("validationType")
    private ValidationType validationType;
    @JsonProperty("legalDiscrepancy")
    private double legalDiscrepancy;

    /**
     * Creates a new serialized compatibility criterion with the data from the given {@code source}.
     */
    SerializedCompatibilityCriterion(CompatibilityCriterion source) {
      super(source);

      this.attributeType = source.getAttributeType();
      this.validationType = source.getValidationType();
      this.legalDiscrepancy = source.getLegalDiscrepancy();
    }

    /**
     * Empty constructor to be used by Jackson when de-serializing a file into this model.
     */
    @SuppressWarnings("unused")
    private SerializedCompatibilityCriterion() {
      super();
    }

    @Override
    public void populateCriterion(CompatibilityCriterion criterion) {
      super.populateCriterion(criterion);

      criterion.setAttributeType(this.attributeType);
      criterion.setValidationType(this.validationType);
      criterion.setLegalDiscrepancy(this.legalDiscrepancy);
    }

    @Override
    public CompatibilityCriterion createIn(CriteriaManager target) {
      return target.createCompatibilityCriterion();
    }
  }
}
