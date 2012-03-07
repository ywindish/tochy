package jp.windish.tochy;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * 複数クライアント間で文字列のやりとりをするためのサーバ。
 * いたってシンプル。認証とか一切していないし、DoS防御とかもしてません。 
 * @author yamako
 *
 */
public class Server implements Observer, Runnable {
	
	private Thread m_thread = null ;
	private ServerSocket m_server_socket = null ;
	private Vector<ServerConnect> m_connections = new Vector<ServerConnect>() ; 	/** 接続を受け付けたクライアントの集合 */
	private MessageView m_message = null ; /** メッセージ表示 */
	
	/**
	 * メッセージ表示先を受け取るコンストラクタ
	 * @param c
	 */
	public Server(MessageView mv) {
		m_message = mv ;
	}

	/**
	 * サーバを開始する
	 */
	public void start() {
		m_thread = new Thread(this) ;
		m_thread.start() ;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		start_server() ;
	}
	
	/**
	 * 初期化処理。サーバを立ち上げて、クライアントからの接続要求を待つ。
	 * 接続要求があったら送受信用スレッドを生成する。
	 * @param iPortNumber
	 */
	private void start_server() {
		int iPort = Config.getConfigInt(Config.C_NET_PORT) ; // ポート番号は設定ファイルから。
		try {
			m_server_socket = new ServerSocket(iPort) ;
			printMessage("サーバを起動しました。") ;

			while (true) {
				Socket sk = m_server_socket.accept() ;  // クライアントの接続待ち。
				ServerConnect ysc = new ServerConnect(sk, this) ;
				m_connections.add( ysc ) ;
			}
			
		} catch (BindException e) {
			printMessage("サーバを起動できません。ポート番号を変えるか、起動済みのサーバを終了して下さい。") ;
		} catch (IOException e) {
			printMessage(e.getLocalizedMessage()) ;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	/**
	 * あるクライアントから何か受信したら、全クライアントにそれを送信する。
	 */
	synchronized public void update(Observable o, Object arg) {
		Iterator<ServerConnect> it = m_connections.iterator() ;
		while (it.hasNext()) {
			ServerConnect ysc = (ServerConnect) it.next() ;
			ysc.sendMessageToClient((String) arg) ;
		}
	}
	
	/**
	 * サーバをシャットダウンする。というか単に接続を閉じる。
	 * @throws IOException
	 */
	public void shutdown() {
		try {
			m_server_socket.close() ;
		} catch (IOException e) {
			printMessage(e.getLocalizedMessage()) ;
		}
	}

	/**
	 * サーバ側のメッセージはすべてシステムメッセージ。
	 * @param strMessage
	 */
	protected void printMessage(String strMessage) {
		if (m_message != null) {
			m_message.printSystemMessage(strMessage) ;
		}
	}
}
