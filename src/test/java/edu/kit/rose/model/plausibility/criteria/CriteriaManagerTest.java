package edu.kit.rose.model.plausibility.criteria;

import edu.kit.rose.infrastructure.SetObserver;
import edu.kit.rose.model.roadsystem.GraphRoadSystem;
import edu.kit.rose.model.roadsystem.TimeSliceSetting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CriteriaManagerTest {
  private CriteriaManager criteriaManager;

  @BeforeEach
  public void setUp() {
    this.criteriaManager = new CriteriaManager();
    this.criteriaManager.setRoadSystem(
            new GraphRoadSystem(criteriaManager, Mockito.mock(TimeSliceSetting.class)));
  }


  @Test
  void getCriteria() {
    Assertions.assertTrue(criteriaManager.getCriteria().getSize() > 0);
  }

  @Test
  void getCriteriaOfType() {
    for (PlausibilityCriterion criterion : criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.VALUE)) {
      Assertions.assertEquals(criterion.getType(), PlausibilityCriterionType.VALUE);
    }
  }

  @Test
  void createCompatibilityCriterion() {
    int found = 0;
    criteriaManager.createCompatibilityCriterion();

    for (PlausibilityCriterion criterion : criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.COMPATIBILITY)) {
      if (criterion.getType().equals(PlausibilityCriterionType.COMPATIBILITY)) {
        found++;
      }
    }

    Assertions.assertEquals(1, found);
  }

  @Test
  void removeCriterion() {
    int found = 0;

    criteriaManager.createCompatibilityCriterion();
    PlausibilityCriterion auxCriterion = criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.COMPATIBILITY).get(0);
    criteriaManager.removeCriterion(auxCriterion);

    for (PlausibilityCriterion criterion : criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.VALUE)) {
      if (criterion.getType().equals(PlausibilityCriterionType.COMPATIBILITY)) {
        found++;
      }
    }

    Assertions.assertEquals(0, found);
  }

  @Test
  void removeAllCriteria() {
    criteriaManager.removeAllCriteria();
    Assertions.assertEquals(0, criteriaManager.getCriteria().getSize());
  }

  @Test
  void removeAllCriteriaOfType() {
    criteriaManager.removeAllCriteriaOfType(PlausibilityCriterionType.VALUE);
    Assertions.assertEquals(0, criteriaManager
            .getCriteriaOfType(PlausibilityCriterionType.VALUE).getSize());
  }


  @Test
  void notifyChange() {
    SetObserver<PlausibilityCriterion, CriteriaManager> observer = Mockito
            .mock(SetObserver.class);
    criteriaManager.addSubscriber(observer);
    criteriaManager.notifyChange(null);
    Mockito.verify(observer, Mockito.times(1)).notifyChange(Mockito.any());
  }
}