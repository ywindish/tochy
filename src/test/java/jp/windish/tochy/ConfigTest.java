package jp.windish.tochy;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.awt.Color;
import java.awt.Font;

import org.junit.Test;

/**
 * Configクラスのテスト
 * @author yamako
 */
public class ConfigTest {

	@Test
	public void 設定ファイルを読み込んで値を確認する() {

		boolean loadResult = Config.load("src/test/resources/tochy.config");
		assertThat("設定ファイル読み込み結果", loadResult, is(true));
		
		assertThat("ユーザ名", Config.getConfig(Config.C_USER_NAME), is("yamako-test"));
		assertThat("動作モード", Config.getConfig(Config.C_USER_MODE), is("サーバ"));
		assertThat("ウィンドウ位置上下", Config.getConfigInt(Config.C_WINDOW_TOP), is(100));
		assertThat("ウィンドウ位置左右", Config.getConfigInt(Config.C_WINDOW_LEFT), is(120));
		assertThat("ウィンドウ幅", Config.getConfigInt(Config.C_WINDOW_WIDTH), is(540));
		assertThat("ウィンドウ高さ", Config.getConfigInt(Config.C_WINDOW_HEIGHT), is(380));
		assertThat("ログを取る", Config.getConfigBoolean(Config.C_LOG_ENABLE), is(false));
		assertThat("IPアドレス", Config.getConfig(Config.C_NET_ADDRESS), is("192.168.1.123"));
		
		Font font = Config.getFont();
		assertThat("フォント名", font.getName(), is("Test Font"));
		assertThat("フォントサイズ", font.getSize(), is(16));

		Color foreColor = Config.getForeColor();
		assertThat("文字色R", foreColor.getRed(), is(255));
		assertThat("文字色G", foreColor.getGreen(), is(254));
		assertThat("文字色B", foreColor.getBlue(), is(253));
		
		Color backColor = Config.getBackColor();
		assertThat("背景色R", backColor.getRed(), is(0));
		assertThat("背景色G", backColor.getGreen(), is(1));
		assertThat("背景色B", backColor.getBlue(), is(2));
	}
}
