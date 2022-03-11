package edu.kit.rose.view;

import java.util.concurrent.TimeoutException;
import javafx.stage.Stage;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * The Test class that is used to write Gui Tests.
 */
public abstract class GuiTest extends ApplicationTest {

  @Override
  public void start(Stage stage) throws TimeoutException {
    RoseApplication application = new RoseApplication();
    application.start(stage);
    FxToolkit.showStage();
  }
}