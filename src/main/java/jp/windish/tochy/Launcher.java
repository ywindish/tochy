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
		inputName() ;
		selectMode() ;
		inputAddress() ;

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
	private static void inputName() {
		String strUserName = JOptionPane.showInputDialog("名前を入れて下さい", Config.getConfig(Config.C_USER_NAME)) ;
		if (strUserName == null) {
			exit() ;
		}
		Config.setConfig(Config.C_USER_NAME, strUserName) ;
	}

	/**
	 * クライアント・サーバ選択
	 *
	 */
	private static void selectMode() {
		String [] strList =  { Config.CLIENT_MODE, Config.SERVER_MODE } ;
		String strMode = (String) JOptionPane.showInputDialog(null,
		             "クライアントになるか、サーバになるかを選んで下さい", "",
		             JOptionPane.INFORMATION_MESSAGE, null,
		             strList, Config.getConfig("user.mode"));
		if (strMode == null) {
			exit() ;
		}
		Config.setConfig(Config.C_USER_MODE, strMode) ;
	}

	/**
	 * IPアドレス入力
	 *
	 */
	private static void inputAddress() {
		String strIpAddress = null ;
		if (Config.getConfig(Config.C_USER_MODE).equals(Config.CLIENT_MODE)) {
			// クライアントモードなら入力
			strIpAddress = JOptionPane.showInputDialog("サーバのIPアドレスを入れて下さい", Config.getConfig(Config.C_NET_ADDRESS)) ;
			if (strIpAddress == null) {
				exit() ;
			}
			Config.setConfig(Config.C_NET_ADDRESS, strIpAddress) ;
		} else {
			// サーバモードならローカルサーバ
			Config.setConfig(Config.C_NET_ADDRESS, "127.0.0.1") ;
		}
	}

	/**
	 * 本アプリケーションを強制的に終了。なんだかなぁだが。
	 *
	 */
	private static void exit() {
		System.exit(0) ;
	}
}
