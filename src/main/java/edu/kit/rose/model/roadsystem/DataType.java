package edu.kit.rose.model.roadsystem;

/**
 * A {@link DataType} describes a semantic data type that can be used in
 * {@link edu.kit.rose.model.roadsystem.measurements.Measurement}s and
 * {@link edu.kit.rose.model.roadsystem.attributes.AttributeAccessor}s.
 * While the Java data type (class) of a value determines how the value is stored in the computer,
 * the semantic data type specifies and restricts which values are allowed and how to interpret a
 * given value (i.e. as a percentage).
 */
public enum DataType {
  /**
   * Allows whole numbers only, stored in an {@link Integer}.
   */
  INTEGER,
  /**
   * Allows any real number, stored in a {@link Double}.
   */
  FRACTIONAL,
  /**
   * Allows any string, stored in a {@link String}.
   */
  STRING,
  /**
   * May be {@code true} or {@code false}.
   */
  BOOLEAN,
  /**
   * Speed limit. i.e. None, T80, T100, T120, SBA, Tunnel
   */
  SPEED_LIMIT,
  /**
   * Connector Type. The Type of connector.
   */
  CONNECTOR_TYPE;
}
