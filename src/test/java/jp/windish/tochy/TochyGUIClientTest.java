package jp.windish.tochy;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * GUIレベルの動作テストです。現在、うまく動作しませんが、参考のため残してあります。
 * @author yamazaki
 */
public class TochyGUIClientTest {

	private FrameFixture window;

	@BeforeClass
	public static void setUpOnce() {
		// ここでひっかかるため、このテストは動作しません。
		// サーバとクライアントを分けて、クライアントは単一スレッドで動作するようにしないとダメそうです。
		// なお、ここを無視して進めようとするとフリーズします。
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
		String view = window.textBox("txtView").text();

		System.out.println(view);
	}

	@After
	public void tearDown() {
		window.cleanUp();
	}
}
