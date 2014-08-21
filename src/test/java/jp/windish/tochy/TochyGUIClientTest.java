package jp.windish.tochy;

import java.util.regex.Pattern;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * GUIレベルの動作テストです。
 * @author yamazaki
 */
public class TochyGUIClientTest {

	private FrameFixture window;

	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Before
	public void setUp() {
		Client frame = GuiActionRunner.execute(new GuiQuery<Client>() {
			protected Client executeInEDT() {
				return new Client();
			}
		});
		window = new FrameFixture(frame);
		window.show();
	}

	@Test
	public void test() {
		window.textBox("txtInput").enterText("ほげほげ\n");

		Pattern pattern = Pattern.compile("yamako:ほげほげ", Pattern.MULTILINE);
		window.textBox("txtView").requireText(pattern);
	}

	@After
	public void tearDown() {
		window.cleanUp();
	}
}
