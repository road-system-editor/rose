package edu.kit.rose.controller.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import edu.kit.rose.controller.commons.StorageLock;
import edu.kit.rose.controller.navigation.ErrorType;
import edu.kit.rose.controller.navigation.FileDialogType;
import edu.kit.rose.controller.navigation.FileFormat;
import edu.kit.rose.controller.navigation.Navigator;
import edu.kit.rose.model.ApplicationDataSystem;
import edu.kit.rose.model.ModelFactory;
import edu.kit.rose.model.Project;
import edu.kit.rose.model.ProjectFormat;
import edu.kit.rose.model.roadsystem.elements.SegmentType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RoseProjectController}.
 */
class RoseProjectControllerTest {
  private static final Path INVALID_PATH =
      Path.of("build", "tmp", "invalid-directory", "invalid-file.rose.json");
  private static final Path VALID_PATH =
      Path.of("build", "tmp", "RoseProjectControllerTest-export.rose.json");
  private static final Path CONFIG_FILE =
      Path.of("build", "tmp", "RoseProjectControllerTest-config.json");
  private static final Path SAMPLE_PROJECT_PATH = Path.of("src", "test", "resources",
      "edu", "kit", "rose", "controller", "project", "load-sample.rose.json");
  private static final Path BACKUP_DIRECTORY =
      Path.of("build", "tmp", "RoseProjectController-backups");

  private Runnable storageLockOnAcquire;
  private Runnable storageLockOnRelease;
  private StorageLock storageLock;
  private Navigator navigator;
  private Project project;
  private ApplicationDataSystem applicationDataSystem;
  private RoseProjectController controller;

  @BeforeEach
  void beforeEach() throws IOException {
    this.storageLock = mock(StorageLock.class);
    this.navigator = mock(Navigator.class);

    Files.deleteIfExists(CONFIG_FILE);
    deleteDirectoryRecursively(BACKUP_DIRECTORY);
    deleteDirectoryRecursively(INVALID_PATH.getParent());
    var modelFactory = new ModelFactory(CONFIG_FILE);
    this.project = modelFactory.createProject();
    this.applicationDataSystem = modelFactory.createApplicationDataSystem();

    this.controller = new RoseProjectController(
        this.storageLock,
        this.navigator,
        this.project,
        applicationDataSystem
    );

    this.storageLockOnAcquire = mock(Runnable.class);
    this.storageLockOnRelease = mock(Runnable.class);
    this.controller.subscribeToProjectIoAction(
        this.storageLockOnAcquire, this.storageLockOnRelease);
  }

  @AfterEach
  void afterEach() {
    this.controller.shutDown();
  }

  @Test
  void testConstructor() {
    assertThrows(NullPointerException.class, () -> new RoseProjectController(
        null, this.navigator, this.project, this.applicationDataSystem));
    assertThrows(NullPointerException.class, () -> new RoseProjectController(
        this.storageLock, null, this.project, this.applicationDataSystem));
    assertThrows(NullPointerException.class, () -> new RoseProjectController(
        this.storageLock, this.navigator, null, this.applicationDataSystem));
    assertThrows(NullPointerException.class, () -> new RoseProjectController(
        this.storageLock, this.navigator, this.project, null));
  }

  @Test
  void testExport() throws IOException {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.YAML))
        .thenReturn(VALID_PATH);

    this.controller.export(ProjectFormat.YAML);

    verifyStorageLockWasUsed(1);
    verify(this.navigator, never()).showErrorDialog(any());
    assertTrue(Files.exists(VALID_PATH));
    assertTrue(Files.readString(VALID_PATH).contains("Segmente:"));
  }

  @Test
  void testExportShowsErrorOnFailure() {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(INVALID_PATH);

    this.controller.export(ProjectFormat.ROSE);
    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.PROJECT_EXPORT_ERROR);
  }

  @Test
  void testSaveAs() throws IOException {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(VALID_PATH);

    this.controller.saveAs();

    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);
    verify(this.navigator, never()).showErrorDialog(any());
    assertTrue(Files.exists(VALID_PATH));
    assertTrue(Files.readString(VALID_PATH).contains("{"));
  }

  @Test
  void testSaveWithoutRecentFile() throws IOException {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(VALID_PATH);

    this.controller.save();

    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);
    verify(this.navigator, never()).showErrorDialog(any());
    assertTrue(Files.exists(VALID_PATH));
    assertTrue(Files.readString(VALID_PATH).contains("{"));
  }

  @Test
  void testSaveWithRecentFile() throws IOException {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(VALID_PATH);

    this.controller.saveAs();
    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);

    this.controller.save();

    verifyStorageLockWasUsed(2);
    verify(this.navigator, never()).showErrorDialog(any());
    verify(this.navigator, times(1)).showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);
    assertTrue(Files.exists(VALID_PATH));
    assertTrue(Files.readString(VALID_PATH).contains("{"));
  }

  @Test
  void testSaveWithInvalidRecentFile() throws IOException {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(INVALID_PATH);
    Files.createDirectories(INVALID_PATH.getParent());

    this.controller.saveAs();
    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE);
    deleteDirectoryRecursively(INVALID_PATH.getParent());

    this.controller.save();

    verifyStorageLockWasUsed(2);
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.SAVE_ERROR);
  }

  @Test
  void testSaveShowsErrorOnFailure() {
    when(this.navigator.showFileDialog(FileDialogType.SAVE_FILE, FileFormat.ROSE))
        .thenReturn(INVALID_PATH);

    this.controller.save();
    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.SAVE_ERROR);
  }

  @Test
  void testLoad() {
    when(this.navigator.showFileDialog(FileDialogType.LOAD_FILE, FileFormat.ROSE))
        .thenReturn(SAMPLE_PROJECT_PATH);

    this.controller.loadProject();

    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showFileDialog(FileDialogType.LOAD_FILE, FileFormat.ROSE);
    verify(this.navigator, never()).showErrorDialog(any());

    assertEquals(1, this.project.getRoadSystem().getElements().getSize());
  }

  @Test
  void testLoadShowsErrorOnFailure() {
    when(this.navigator.showFileDialog(FileDialogType.LOAD_FILE, FileFormat.ROSE))
        .thenReturn(INVALID_PATH);

    this.controller.loadProject();
    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.LOAD_ERROR);
  }

  @Test
  void testLoadRecent() {
    this.applicationDataSystem.addRecentProjectPath(SAMPLE_PROJECT_PATH);
    this.controller.loadRecentProject(SAMPLE_PROJECT_PATH);

    verifyStorageLockWasUsed(1);
    verify(this.navigator, never()).showFileDialog(any(), any());
    verify(this.navigator, never()).showErrorDialog(any());

    assertEquals(1, this.project.getRoadSystem().getElements().getSize());
  }

  @Test
  void testLoadRecentShowsErrorOnFailure() {
    this.applicationDataSystem.addRecentProjectPath(INVALID_PATH);
    this.controller.loadRecentProject(INVALID_PATH);

    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.LOAD_ERROR);
  }

  @Test
  void testLoadBackup() {
    this.controller.loadBackup(SAMPLE_PROJECT_PATH);

    verifyStorageLockWasUsed(1);
    verify(this.navigator, never()).showFileDialog(any(), any());
    verify(this.navigator, never()).showErrorDialog(any());

    assertEquals(1, this.project.getRoadSystem().getElements().getSize());
  }

  @Test
  void testLoadBackupShowsErrorOnFailure() {
    this.controller.loadBackup(INVALID_PATH);

    verifyStorageLockWasUsed(1);
    verify(this.navigator, times(1)).showErrorDialog(ErrorType.LOAD_ERROR);
  }

  @Test
  void testBackup() throws InterruptedException {
    this.controller.shutDown();

    long interval = 500;
    this.controller = new RoseProjectController(
        this.storageLock,
        this.navigator,
        this.project,
        this.applicationDataSystem,
        interval,
        BACKUP_DIRECTORY);

    this.project.getRoadSystem().createSegment(SegmentType.BASE);
    assumeTrue(this.project.getRoadSystem().getElements().getSize() == 1);
    Thread.sleep(interval + 100);
    assertEquals(1, this.controller.getBackupPaths().getSize());
    assertTrue(Files.exists(BACKUP_DIRECTORY));

    this.controller.createNewProject();
    assertEquals(0, this.project.getRoadSystem().getElements().getSize());

    var backupPath = this.controller.getBackupPaths().iterator().next();
    this.controller.loadBackup(backupPath);
    verifyNoInteractions(this.navigator);
    assertEquals(1, this.project.getRoadSystem().getElements().getSize());
  }

  @Test
  void testUnsubscribe() {
    this.controller.unsubscribeFromProjectIoAction(
        this.storageLockOnAcquire, this.storageLockOnRelease);

    // trigger any io action
    this.controller.loadProject();

    verifyNoInteractions(this.storageLockOnAcquire);
    verifyNoInteractions(this.storageLockOnRelease);
  }

  private void verifyStorageLockWasUsed(int wantedNumberOfInvocations) {
    verify(this.storageLock, times(wantedNumberOfInvocations)).acquireStorageLock();
    verify(this.storageLock, times(wantedNumberOfInvocations)).releaseStorageLock();
    verify(this.storageLockOnAcquire, times(wantedNumberOfInvocations)).run();
    verify(this.storageLockOnRelease, times(wantedNumberOfInvocations)).run();
  }

  private void deleteDirectoryRecursively(Path dir) throws IOException {
    if (Files.exists(dir)) {
      // copied from https://www.baeldung.com/java-delete-directory
      assertTrue(Files.walk(dir)
          .sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .allMatch(File::delete));
    }
  }
}
