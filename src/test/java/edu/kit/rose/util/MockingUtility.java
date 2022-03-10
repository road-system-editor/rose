package edu.kit.rose.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.kit.rose.infrastructure.RoseSortedBox;
import edu.kit.rose.model.plausibility.criteria.CriteriaManager;

/**
 * Utility class that provides {@link org.mockito.Mockito} mocks for frequently mocked objects that
 * return sensible default values in method calls.
 */
public final class MockingUtility {
  /**
   * Mocks a criteria manager without any criteria.
   */
  public static CriteriaManager mockCriteriaManager() {
    CriteriaManager mock = mock(CriteriaManager.class);
    when(mock.getCriteria()).thenReturn(new RoseSortedBox<>());
    return mock;
  }
}
