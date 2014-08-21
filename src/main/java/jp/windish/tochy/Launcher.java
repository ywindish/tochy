package jp.windish.tochy;

import javax.swing.JOptionPane;

/**
 * アプリケーションランチャー。いわゆる最初に実行されるところ。
 * @author yamako
 *
 */
public class Launcher {

	public static void main(String args[]) throws Exception {

		// 設定ファイルを読み込んで。
		loadConfig() ;

		// いろいろ質問。
		if (inputName() == null) return;
		if (selectMode() == null) return;
		if (inputAddress() == null) return;

		// 必要なことを聞き終わったら、いよいよ開始。
		new Client();
	}

	/**
	 *  設定ファイル読み込み
	 *
	 */
	private static void loadConfig() {
		boolean isLoaded = Config.load() ;
		if (isLoaded == false) {
			JOptionPane.showMessageDialog(null, "設定ファイルを読み込めません。", "", JOptionPane.WARNING_MESSAGE) ;
		}
	}

	/**
	 * 名前入力
	 *
	 */
	private static String inputName() {
		String userName = JOptionPane.showInputDialog("名前を入れて下さい", Config.getConfig(Config.C_USER_NAME)) ;
		Config.setConfig(Config.C_USER_NAME, userName) ;
		return userName;
	}

	/**
	 * クライアント・サーバ選択
	 *
	 */
	private static String selectMode() {
		String [] list =  { Config.CLIENT_MODE, Config.SERVER_MODE } ;
		String mode = (String) JOptionPane.showInputDialog(null,
		             "クライアントになるか、サーバになるかを選んで下さい", "",
		             JOptionPane.INFORMATION_MESSAGE, null,
		             list, Config.getConfig("user.mode"));
		Config.setConfig(Config.C_USER_MODE, mode) ;
		return mode;
	}

	/**
	 * IPアドレス入力
	 *
	 */
	private static String inputAddress() {
		String ipAddress = null ;
		if (Config.getConfig(Config.C_USER_MODE).equals(Config.CLIENT_MODE)) {
			// クライアントモードなら入力
			ipAddress = JOptionPane.showInputDialog("サーバのIPアドレスを入れて下さい", Config.getConfig(Config.C_NET_ADDRESS)) ;
			Config.setConfig(Config.C_NET_ADDRESS, ipAddress) ;
		} else {
			// サーバモードならローカルサーバ
			ipAddress = "127.0.0.1";
			Config.setConfig(Config.C_NET_ADDRESS, ipAddress) ;
		}
		return ipAddress;
	}
}
