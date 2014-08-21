package jp.windish.tochy;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 設定ファイルの読み込みと保存、設定の読み出しと書き出し。
 * @author yamako
 *
 */
public class Config {

	/** 設定プロパティ */
	public static final String C_USER_NAME = "user.name";
	public static final String C_FONT_STYLE = "font.style";
	public static final String C_FONT_NAME = "font.name";
	public static final String C_FONT_SIZE = "font.size";
	public static final String C_COLOR_FORE_B = "color.fore.b";
	public static final String C_COLOR_FORE_G = "color.fore.g";
	public static final String C_COLOR_FORE_R = "color.fore.r";
	public static final String C_COLOR_BACK_B = "color.back.b";
	public static final String C_COLOR_BACK_G = "color.back.g";
	public static final String C_COLOR_BACK_R = "color.back.r";
	public static final String C_LOG_ENABLE = "log.enable";
	public static final String C_NET_ADDRESS = "net.address";
	public static final String C_USER_MODE = "user.mode";
	public static final String C_WINDOW_HEIGHT = "window.height";
	public static final String C_WINDOW_WIDTH = "window.width";
	public static final String C_WINDOW_LEFT = "window.left";
	public static final String C_WINDOW_TOP = "window.top";
	public static final String C_LOG_EXTENSION = "log.extension";
	public static final String C_LOG_FORMAT = "log.format";
	public static final String C_LOG_PREFIX = "log.prefix";
	public static final String C_LOG_PATH = "log.path";
	public static final String C_TEXT_ROW = "text.row";
	public static final String C_TEXT_COLUMN = "text.column";
	public static final String C_NET_PORT = "net.port";

	/** アプリケーション情報 */
	public static final String APP_NAME = "トーチー！" ;
	public static final String APP_VERSION = "0.6" ;
	public static final String APP_COPYRIGHT = "yamako 2007-2014" ;

	/** 起動モード */
	public static final String CLIENT_MODE = "クライアント" ;
	public static final String SERVER_MODE = "サーバ" ;

	/** システムメッセージ接頭辞 */
	public static final String SYSTEM_MESSAGE_PREFIX = "# " ;

	/** 設定ファイル名 */
	private static final String CONFIG_FILE_NAME = "tochy.config";

	/** 設定を保存するプロパティ */
	private static Properties m_config = null ;
	private static Properties m_default_config = null ;

	static {

		m_default_config = new Properties() ;

		// 最初に聞かれるし、ダイアログから設定できる。
		m_default_config.setProperty(C_USER_NAME, "yamako") ;

		// ダイアログから設定できる。
		m_default_config.setProperty(C_FONT_NAME, "ＭＳ ゴシック") ;
		m_default_config.setProperty(C_FONT_SIZE, "12") ;
		m_default_config.setProperty(C_COLOR_FORE_R, "0") ;
		m_default_config.setProperty(C_COLOR_FORE_G, "0") ;
		m_default_config.setProperty(C_COLOR_FORE_B, "0") ;
		m_default_config.setProperty(C_COLOR_BACK_R, "255") ;
		m_default_config.setProperty(C_COLOR_BACK_G, "255") ;
		m_default_config.setProperty(C_COLOR_BACK_B, "255") ;
		m_default_config.setProperty(C_LOG_ENABLE, "true") ;

		// 最初に聞かれる。
		m_default_config.setProperty(C_NET_ADDRESS, "127.0.0.1") ;
		m_default_config.setProperty(C_USER_MODE, SERVER_MODE) ;

		// UIなし。自動的に保存される。
		m_default_config.setProperty(C_WINDOW_TOP, "0") ;
		m_default_config.setProperty(C_WINDOW_LEFT, "0") ;
		m_default_config.setProperty(C_WINDOW_WIDTH, "400") ;
		m_default_config.setProperty(C_WINDOW_HEIGHT, "250") ;

		// UIなし。設定ファイルを変えないと変更できない。
		m_default_config.setProperty(C_FONT_STYLE, new Integer(Font.PLAIN).toString()) ;
		m_default_config.setProperty(C_NET_PORT, "12345") ;
		m_default_config.setProperty(C_LOG_PATH, "") ; // カレントディレクトリ
		m_default_config.setProperty(C_LOG_PREFIX, "tc") ;
		m_default_config.setProperty(C_LOG_FORMAT, "yyMMdd") ;
		m_default_config.setProperty(C_LOG_EXTENSION, ".txt") ;
		m_default_config.setProperty(C_TEXT_COLUMN, "80") ;
		m_default_config.setProperty(C_TEXT_ROW, "30") ;
		
		// デフォルト値をもっておく。
		m_config = new Properties(m_default_config)  ;
	}
	
	/**
	 * コンストラクタは使えませんよ。
	 */
	private Config() {}
	
	/**
	 * 指定したキーの設定値を戻します。
	 * @param strKey
	 * @return
	 */
	public static String getConfig(String strKey) {
		if (strKey == null) {
			return "" ;
		}
		return m_config.getProperty(strKey) ;
	}
	
	/**
	 * 指定したキーの設定値を int 型で戻します。int 型に変換できないときは 0 を戻します。
	 * @param strKey
	 * @return
	 */
	public static int getConfigInt(String strKey) {
		int result = 0 ;
		try {
			result = Integer.parseInt( getConfig(strKey) ) ;
		} catch (NumberFormatException e) {
			// 解析できなければ、そのまま 0 を返す
		}
		return result ;
	}

	/**
	 * 指定したキーの設定値を boolean 型で戻します。
	 * @param strKey
	 * @return
	 */
	public static boolean getConfigBoolean(String strKey) {
		return new Boolean(getConfig(strKey)).booleanValue() ;
	}

	/**
	 * 指定したキーに値をセットします。
	 * @param strKey
	 * @param strValue
	 */
	public static void setConfig(String strKey, String strValue) {
		if (strKey == null || strValue == null) {
			return ;
		}
		m_config.setProperty(strKey, strValue) ;
	}

	/**
	 * 指定したキーに int 型の値をセットします。
	 * @param strKey
	 * @param value
	 */
	public static void setConfig(String strKey, int value) {
		setConfig(strKey, new Integer(value).toString()) ; // 内部的にはStringで保存。
	}
	
	/**
	 * 指定したキーに boolean 型の値をセットします。
	 * @param strKey
	 * @param value
	 */
	public static void setConfig(String strKey, boolean value) {
		setConfig(strKey, new Boolean(value).toString()) ; // 内部的にはStringで保存。
	}
	
	/**
	 * 指定した設定ファイルを読み込みます。成功すると真を戻します。
	 * @param path
	 */
	public static boolean load(String path) {
		try {
			m_config.load(new FileInputStream(path)) ;
		} catch (FileNotFoundException e) {
			return false ;
		} catch (IOException e) {
			return false ;
		}
		return true ;
	}

	/**
	 * カレントディレクトリから既定の設定ファイルを読み込みます。
	 * @return
	 */
	public static boolean load() {
		return load(CONFIG_FILE_NAME);
	}

	/**
	 * 指定したパス名に設定ファイルを保存します。成功すると真を戻します。
	 * @param dir
	 */
	public static boolean save(String path) {
		try {
			m_config.store(new FileOutputStream(path), path) ;
		} catch (FileNotFoundException e) {
			return false ;
		} catch (IOException e) {
			return false ;
		}
		return true ;
	}

	/**
	 * カレントディレクトリに既定名の設定ファイルを保存します。
	 * @return
	 */
	public static boolean save() {
		return save(CONFIG_FILE_NAME);
	}
	
	/**
	 * 現在の設定値を反映した Font オブジェクトを戻します。
	 * @return
	 */
	public static Font getFont() {
		return new Font(getConfig(C_FONT_NAME), getConfigInt(C_FONT_STYLE), getConfigInt(C_FONT_SIZE)) ;
	}
	
	/**
	 * 現在の設定値を反映した、文字色用 Color オブジェクトを戻します。
	 * @return
	 */
	public static Color getForeColor() {
		return new Color(getConfigInt(C_COLOR_FORE_R), getConfigInt(C_COLOR_FORE_G), getConfigInt(C_COLOR_FORE_B)) ;
	}
	
	/**
	 * 現在の設定値を反映した、背景色用 Color オブジェクトを戻します。
	 * @return
	 */
	public static Color getBackColor() {
		return new Color(getConfigInt(C_COLOR_BACK_R), getConfigInt(C_COLOR_BACK_G), getConfigInt(C_COLOR_BACK_B)) ;
	}
	
	/**
	 * バージョン情報を戻します。
	 * @return
	 */
	public static String getVersion() {
		return Config.APP_NAME + " version " + Config.APP_VERSION + " " + Config.APP_COPYRIGHT ;
	}
}
