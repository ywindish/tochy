package jp.windish.tochy;

/**
 * ユーザむけメッセージ表示用のインタフェース
 * @author yamako
 *
 */
public interface MessageView {

	/**
	 * メッセージを表示する用 
	 * @param str
	 */
	public void printMessage(String str) ;
	
	/**
	 * メッセージを Config#SYSTEM_MESSAGE_PREFIX つきで表示する用
	 * @param str
	 */
	public void printSystemMessage(String str) ;
	
	/**
	 * 最新の Config を反映する用
	 *
	 */
	public void loadConfig() ;
}
