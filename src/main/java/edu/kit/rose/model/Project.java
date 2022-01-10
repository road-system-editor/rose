package edu.kit.rose.model;

import edu.kit.rose.infrastructure.Position;
import edu.kit.rose.model.plausibility.PlausibilitySystem;
import edu.kit.rose.model.plausibility.criteria.PlausibilityCriterion;
import edu.kit.rose.model.roadsystem.RoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;

import java.nio.file.Path;

/**
 * A Project holds all data specified in "Pflichtenheft: Projekt" as well as the current {@link ZoomSetting} and the
 * current {@link TimeSliceSetting}.
 */
public interface Project {

    /**
     * Provides the {@link RoadSystem} of this Project.
     * @return the road system of this project.
     */
    RoadSystem getRoadSystem();

    /**
     * Provides the {@link PlausibilitySystem} of this Project.
     * @return the plausibility system of this project.
     */
    PlausibilitySystem getPlausibilitySystem();



    /**
     * Formats the Project to fit a specified {@link ExportFormat} and exports it to a file at the given {@link Path}
     * @param exportFormat The {@link ExportFormat} to save in.
     * @param filePath The {@link Path} of where to store the export.
     */
    void exportToFile (ExportFormat exportFormat, Path filePath);

    /**
     * Saves the Project as a ROSE file. This saves everything the Project needs to be reopened in the program.
     * Including {@link PlausibilityCriterion} and the {@link Position}s
     * @param filePath The {@link Path} of where to store the file.
     */
    void save (Path filePath);

    /**
     * Loads a ROSE File. This rewrites the Project to hold the information specified in the provided file.
     * @param filePath The {@link Path} of the File.
     */
    void load (Path filePath);

    /**
     * @return the {@link ZoomSetting} of a view that displays the RoadSystem.
     */
    ZoomSetting getZoomSetting();

}
