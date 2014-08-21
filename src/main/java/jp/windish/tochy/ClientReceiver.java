package jp.windish.tochy;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import javax.swing.SwingWorker;

/**
 * クライアントのメッセージ受信処理を非同期で行うためのクラス
 * @author yamazaki
 */
public class ClientReceiver extends SwingWorker<Object, String> {

	private ClientView m_view;
	private SocketWrapper m_network;

	/** コンストラクタ */
	public ClientReceiver(ClientView view, SocketWrapper network) {
		m_view = view;
		m_network = network;
	}

	/**
	 * サーバからのメッセージを受信しつづけます。
	 */
	@Override
	protected Object doInBackground() throws Exception {
		String strMessage = "";
		try {
			while ((strMessage = m_network.receiveMessage()) != null) {
				publish(strMessage) ;
			}
		} catch (SocketException se) {
			publish("接続が切断されました。") ;
		} catch (IOException e) {
			publish(e.getLocalizedMessage()) ;
		}
		return null;
	}

	/**
	 * 受信したメッセージをクライアント表示部に表示します。
	 */
	@Override
	protected void process(List<String> chunks) {
		for (String msg : chunks) {
			m_view.printMessage(msg);
		}
	}
}
