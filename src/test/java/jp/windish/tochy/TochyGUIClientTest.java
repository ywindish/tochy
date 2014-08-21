package jp.windish.tochy;

import static org.junit.Assert.*;

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
	public void 入力した文字が送受信されて表示される() {
		window.textBox("txtInput").enterText("ほげほげ\n");
		String view = window.textBox("txtView").text();
		for (String line : view.split(System.getProperty("line.separator"))) {
			System.out.println(line);
			if (line != null && line.equals("yamako:ほげほげ")) {
				assertTrue("入力した文字が表示されました", true);
				return;
			}
		}
		fail("入力した文字が表示されませんでした");
	}

	@After
	public void tearDown() {
		window.cleanUp();
	}
}
