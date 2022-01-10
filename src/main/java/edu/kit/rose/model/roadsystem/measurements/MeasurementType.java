package edu.kit.rose.model.roadsystem.measurements;

/**
 * Different Types of {@link Measurement}. Used for quick access to all possible measurement types.
 */
public enum MeasurementType {

    HEAVY_TRAFFIC_PROPORTION("Heavy Traffic Proportion"),
    DEMAND("Demand"),
    CAPACITY_FACTOR("Capacity Factor"),
    RAMP_HEAVY_TRAFFIC_PROPORTION("Ramp Heavy Traffic Proportion"),
    RAMP_DEMAND("Ramp Demand"),
    RAMP_CAPACITY_FACTOR("Ramp Capacity Factor");

    private String name;

    /**
     * Constructor.
     * @param name a String containing the name of the MeasurementType.
     */
    MeasurementType(String name) {
        this.name = name;
    }

    /**
     *
     * @return a String containing the name of the MeasurementType.
     */
    public String getName() {
        return name;
    }
}
