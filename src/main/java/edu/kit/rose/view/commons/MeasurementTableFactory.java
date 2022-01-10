package edu.kit.rose.view.commons;

import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import edu.kit.rose.model.roadsystem.elements.Segment;
import edu.kit.rose.model.roadsystem.measurements.MeasurementType;

import java.util.Collection;

public class MeasurementTableFactory {
    private final Collection<Segment> segments;
    private final TimeSliceSetting timeSliceSetting;

    public MeasurementTableFactory(Collection<Segment> segments, TimeSliceSetting timeSliceSetting) {
        this.segments = segments;
        this.timeSliceSetting = timeSliceSetting;
    }
    public MeasurementTable<?> forMeasurementType(MeasurementType type) {
        switch (type) {
            case DEMAND:
                return new MeasurementTable<Integer>(type, segments, timeSliceSetting);
            case RAMP_DEMAND:
                return new MeasurementTable<Double>(type, segments, timeSliceSetting);
            case CAPACITY_FACTOR:
                return new MeasurementTable<Double>(type, segments, timeSliceSetting);
            case RAMP_CAPACITY_FACTOR:
                return new MeasurementTable<Double>(type, segments, timeSliceSetting);
            case HEAVY_TRAFFIC_PROPORTION:
                return new MeasurementTable<Double>(type, segments, timeSliceSetting);
            case RAMP_HEAVY_TRAFFIC_PROPORTION:
                return new MeasurementTable<Double>(type, segments, timeSliceSetting);
            default:
                return null;
        }
    }
}
