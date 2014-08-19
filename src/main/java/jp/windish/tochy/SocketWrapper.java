package jp.windish.tochy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ソケット通信クラス (Socketクラスの簡単なラッパー)
 * @author yamako
 */
public class SocketWrapper {

	/** Socketクラスの本体 */
	private Socket m_socket = null ;

	/** 送受信バッファ */
	private BufferedWriter m_writer = null ;
	private BufferedReader m_reader = null ;

	/** システムの文字コード */
	private static String m_sys_encoding = null;

	/** 送受信バッファの文字コード */
	private static final String BUFFER_ENCODING = "utf-8" ;

	static {
		m_sys_encoding = System.getProperty("file.encoding");
	}
	
	/**
	 * IPアドレスとポート番号を受けとって、ソケットを作成するコンストラクタ
	 * @param remote_address
	 * @param port_number
	 * @throws UnknownHostException
	 * @throws ConnectException
	 * @throws IOException
	 */
	public SocketWrapper(String remote_address, int port_number)
		throws UnknownHostException, ConnectException, IOException	{

		if (remote_address == null || remote_address.equals("")) {
			// リモートアドレスが無効なら、ローカルへ接続。
			remote_address = "127.0.0.1" ;
		}
		m_socket = new Socket(remote_address, port_number) ;
		makeBuffer() ;
	}

	/**
	 * すでにあるソケットを渡すコンストラクタ
	 * @param sk
	 */
	public SocketWrapper(Socket sk) throws IOException {
		if (sk == null) {
			return ;
		}
		m_socket = sk ;
		makeBuffer() ;
	}

	/**
	 * 接続先へ文字列を送信します。
	 * @param strMsg
	 * @throws IOException
	 */
	public void sendMessage(String strMsg) throws IOException {
		// 送受信バッファの文字コードで送信
		String _strMsg = new String(strMsg.getBytes(BUFFER_ENCODING), BUFFER_ENCODING);
		m_writer.write(_strMsg) ;
		m_writer.newLine();
		m_writer.flush() ;
	}

	/**
	 * 接続先から文字列を１行受信して、戻します。
	 * @return
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException {
		String strResult = m_reader.readLine() ;
		// システムの文字コードで受信
		String _strResult = new String(strResult.getBytes(m_sys_encoding), m_sys_encoding);
		return _strResult ;
	}

	/**
	 * 接続を切ります。
	 * @return
	 */
	public void disconnect() throws IOException {
		m_socket.close() ;
	}

	/**
	 * 接続先アドレスを返します。
	 * @return
	 */
	public String getRemoteAddress() {
		return m_socket.getInetAddress().toString() ;
	}

	/**
	 * 指定の文字コードで送受信バッファを作成します。
	 * @throws IOException
	 */
	private void makeBuffer() throws IOException {
		m_writer = new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream(), BUFFER_ENCODING)) ;
		m_reader = new BufferedReader(new InputStreamReader(m_socket.getInputStream(), BUFFER_ENCODING)) ;
	}
}
