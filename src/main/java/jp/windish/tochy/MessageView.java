package jp.windish.tochy;

/**
 * サーバからのメッセージ表示用のインタフェース
 * @author yamako
 */
public interface MessageView {
	/**
	 * システムメッセージを表示する
	 * @param str
	 */
	public void printSystemMessage(String str) ;
}
