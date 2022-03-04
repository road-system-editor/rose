package edu.kit.rose.view;

import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * The Test class that is used to write Gui Tests.
 */
public abstract class GuiTest extends ApplicationTest {

  @Override
  public void start(Stage stage) {
    RoseApplication application = new RoseApplication();
    application.start(stage);
    stage.show();
  }
}
=======
}
>>>>>>> 4d0e9f4b (added CriterionGuiTest class)
