package edu.kit.rose.model.plausibility.criteria.validation;

import edu.kit.rose.model.roadsystem.elements.ConnectorType;
import java.util.Map;
import java.util.Set;

public class DirectionValidationStrategy extends ValidationStrategy<ConnectorType> {
  private Map<ConnectorType, Set<ConnectorType>> COMPATIBLE_CONNECTOR_TYPE_MAP =
      Map.of(ConnectorType.ENTRY, Set.of(ConnectorType.EXIT, ConnectorType.RAMP_EXIT),
          ConnectorType.EXIT, Set.of(ConnectorType.ENTRY, ConnectorType.RAMP_ENTRY),
          ConnectorType.RAMP_ENTRY, Set.of(ConnectorType.EXIT, ConnectorType.RAMP_EXIT),
          ConnectorType.RAMP_EXIT, Set.of(ConnectorType.ENTRY, ConnectorType.RAMP_ENTRY));

  public DirectionValidationStrategy() {
    super (ValidationType.DIRECTION);
  }

  @Override
  public boolean validate(ConnectorType first, ConnectorType second) {
    return COMPATIBLE_CONNECTOR_TYPE_MAP.get(first).contains(second)
        && COMPATIBLE_CONNECTOR_TYPE_MAP.get(second).contains(first);
  }

  @Override
  public boolean validate(ConnectorType first, ConnectorType second, double legalDiscrepancy) {
    return COMPATIBLE_CONNECTOR_TYPE_MAP.get(first).contains(second)
        && COMPATIBLE_CONNECTOR_TYPE_MAP.get(second).contains(first);
  }
}
