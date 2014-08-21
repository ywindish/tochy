package jp.windish.tochy;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

/**
 * クライアント表示部（自動スクロールとログ出力ができる JTextArea）
 * @author yamako
 *
 */
public class ClientView extends JTextArea {

	private static final long serialVersionUID = 1L;

	private LogFile m_fileview = null ;  // ログファイル

	/**
	 *
	 * @param log
	 */
	public ClientView() {
		super("", Config.getConfigInt(Config.C_TEXT_ROW), Config.getConfigInt(Config.C_TEXT_COLUMN)) ;
		setEditable(false) ;
		setLineWrap(true) ;
		setFont(Config.getFont()) ;
		setForeground(Config.getForeColor()) ;
		setBackground(Config.getBackColor()) ;
		m_fileview = new LogFile() ;

		// 初期化が終わったら、バージョン情報などを表示。
		showInitMessage() ;
	}

	/**
	 * 指定されたメッセージを表示する
	 * @param msg
	 */
	synchronized public void printMessage(String msg) {

		// 表示部に送られたものは、先にログファイルに流し込んでから…
		if (m_fileview != null) {
			m_fileview.printMessage(msg) ;
		}

		// 表示部に送る。
		append(msg + System.getProperty("line.separator")) ;
		setCaretPosition(getDocument().getLength()) ;
	}

	/**
	 * システムメッセージを表示する
	 * @param str
	 */
	public void printSystemMessage(String str) {
		printMessage(Config.SYSTEM_MESSAGE_PREFIX + str) ;
	}

	/**
	 * 設定ファイルの内容を反映する
	 */
	public void loadConfig() {
		Color fg = Config.getForeColor() ;
		setForeground(fg) ;
		setCaretColor(fg) ;
		setBackground(Config.getBackColor()) ;
		setFont(Config.getFont()) ;

		if (m_fileview != null) {
			m_fileview.loadConfig() ;
		}
	}

	/**
	 * 最初に表示されるメッセージ（バージョン情報など）
	 */
	private void showInitMessage() {
		printMessage(Config.getVersion()) ;
		printMessage("") ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 kk時mm分") ;
		printSystemMessage(sdf.format(new Date())) ;
	}
}
