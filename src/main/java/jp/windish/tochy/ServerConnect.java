package jp.windish.tochy;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

/**
 * クライアント接続受け入れ後の、受信待ちスレッド。サーバ側で作成される。
 * @author yamako
 *
 */
public class ServerConnect extends Observable implements Runnable {

	private Thread m_thread = null ;

	/** クライアントとの通信路 */
	private SocketWrapper m_network = null ;

	/** サーバ統括元 */
	private Server m_server = null ;

	/** クライアントから送られてきたユーザー名 */
	private String m_user_name = "" ;

	/**
	 * コンストラクタ
	 * @param sk
	 * @param ys
	 */
	public ServerConnect(Socket sk, Server ys) {
		m_server = ys ;
		try {
			m_network = new SocketWrapper(sk) ;
		} catch (IOException e) {
			m_server.printMessage("クライアントからの接続要求受け入れに失敗しました。") ;
		} catch (Exception e) {
			m_server.printMessage(e.getLocalizedMessage()) ;
		}
		m_thread = new Thread(this) ;
		m_thread.start() ;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		// 最初に受信した文字列がユーザー名であるとみなす。
		getUserName() ;
		if (m_user_name == null) {
			return ; // ユーザー名すら取れなかったら終了。
		}

		// それ以降は、通常の文字列。
		receiveMessageToServer() ;
	}

	/**
	 * クライアントからユーザー名を受信する。本メソッドは通信開始直後に呼び出すこと。
	 */
	private void getUserName() {
		try {
			m_user_name = m_network.receiveMessage() ;
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage()) ;
		}
		if (m_user_name == null) {
			return ;
		}
		m_server.update(this, "[" + m_user_name + "] さんが参加しました。") ;
	}

	/**
	 * クライアントから文字列を受信して、統括元に渡す
	 */
	private void receiveMessageToServer() {
		String strMessage = "" ;
		try {
			while ((strMessage = m_network.receiveMessage()) != null) {
				m_server.update(this, m_user_name + ":" + strMessage) ;
			}
		} catch (SocketException se) {
			m_server.printMessage("接続が切断されました。") ;
		} catch (IOException e) {
			m_server.printMessage(e.getLocalizedMessage()) ;
		}
	}

	/**
	 * 与えられた文字列をクライアントに送信する
	 * @param strMessage
	 */
	synchronized public void sendMessageToClient(String strMessage) {
		if (m_network == null) { return ; }
		try {
			m_network.sendMessage(strMessage)  ;
		} catch (IOException e) {
			m_server.printMessage(e.getLocalizedMessage()) ;
		}
	}
}
