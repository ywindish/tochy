package jp.windish.tochy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 単にテキストファイルに文字列を追記していくだけのクラス。いわゆるログ。
 * @author yamako
 *
 */
public class LogFile {

	/** ログファイルのフルパス */
	private String m_logpath = "" ;

	/** ログファイル出力先 */
	private PrintStream m_out = null ;

	/**	 */
	public LogFile() {
		makePath() ;
		loadConfig() ;
	}

	/**
	 * ログファイルの出力に関する設定を反映する
	 */
	public void loadConfig() {
		boolean isEnable = Config.getConfigBoolean(Config.C_LOG_ENABLE) ;

		if (isEnable == false && m_out != null) {
			// ログが無効で、かつ開いてあれば、それを閉じる。
			m_out.close() ;
			m_out = null ;

		} else if (m_out == null) {
			try {
				// ログが有効で、まだ開いてなかったら、追記できるように開く。
				m_out = new PrintStream(new FileOutputStream( m_logpath, true ), true) ; // 自動的にフラッシュ

			} catch (FileNotFoundException e) {
				// 開けなかったら標準出力に送る。
				m_out = System.out ;
			}
		}
	}

	/**
	 * ログファイルに文字列を１行追記する
	 */
	public void printMessage(String str) {
		if (m_out == null) {
			return ;
		}
		m_out.println(str) ;
	}

	/**
	 * ログファイルを閉じる
	 */
	public void close() {
		if (m_out == null) return;
		m_out.close();
	}

	/**
	 * ログファイルのフルパスを作る
	 *
	 */
	private void makePath() {

		// ファイル名に使う日付フォーマット文字列を作って。
		String strFilename = "" ;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(Config.getConfig(Config.C_LOG_FORMAT)) ;
			strFilename = sdf.format(new Date()) ;
		} catch (IllegalArgumentException e) {
			strFilename = "log.txt" ;
		}

		// ログファイルのフルパスを作る。
		m_logpath = Config.getConfig(Config.C_LOG_PATH)
			+ Config.getConfig(Config.C_LOG_PREFIX)
			+ strFilename
			+ Config.getConfig(Config.C_LOG_EXTENSION) ;
	}
}
