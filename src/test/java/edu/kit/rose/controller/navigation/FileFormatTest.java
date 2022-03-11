package edu.kit.rose.controller.navigation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link FileFormat} class.
 */
class FileFormatTest {
  @Test
  void testFileExtensionsValid() {
    for (var format : FileFormat.values()) {
      for (var extension : format.getFileExtensions()) {
        assertTrue(extension.startsWith("*."),
            String.format("file extension %s of FileFormat %s is invalid", extension, format));
      }
    }
  }
}
