package jp.windish.tochy;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 複数クライアント間で文字列のやりとりをするためのサーバ。
 * いたってシンプル。認証とか一切していないし、DoS防御とかもしてません。
 * @author yamako
 *
 */
public class Server implements Observer, Runnable {

	private ServerSocket m_server_socket = null ;
	/** 接続を受け付けたクライアントの集合 */
	private List<ServerConnect> m_connections = new CopyOnWriteArrayList<ServerConnect>() ;

	/**
	 * コンストラクタ
	 */
	public Server() {}

	/**
	 * サーバを開始する
	 */
	public void start() {
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(this);
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
				ServerConnect sc = new ServerConnect(sk, this) ;
				m_connections.add( sc ) ;
			}

		} catch (BindException e) {
			printMessage("サーバを起動できません。ポート番号を変えるか、起動済みのサーバを終了して下さい。") ;
		} catch (IOException e) {
			String msg = e.getLocalizedMessage();
			if ("socket closed".equals(msg)) {
				printMessage("サーバを終了しました。");
			} else {
				printMessage(msg) ;
			}
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
		for (ServerConnect sc : m_connections) {
			sc.sendMessageToClient((String) arg) ;
		}
	}

	/**
	 * サーバをシャットダウンする。
	 * @throws IOException
	 */
	public void shutdown() {
		if (m_server_socket == null) {
			// サーバ起動時に例外が発生した場合はnullのことがある。その場合は何もしない。
			return;
		}
		try {
			for (ServerConnect sc : m_connections) {
				// クライアントとの接続をすべて閉じてから...
				sc.disconnect();
			}
			// サーバソケットを閉じる。
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
		System.out.println(Config.SYSTEM_MESSAGE_PREFIX + strMessage);
	}
}
