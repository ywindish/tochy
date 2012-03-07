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
 * 例外はもうちょっとわかりやすく投げたい。
 * @author yamako
 *
 */
public class SocketWrapper {

	/** Socketクラスの本体 */
	private Socket m_socket = null ;

	/** 送受信データの文字コード */
	private String m_encoding = "" ;

	/** 送受信バッファ */
	private BufferedWriter m_writer = null ;
	private BufferedReader m_reader = null ;
	
	/** デフォルトの文字コード */
	private static final String DEFAULT_ENCODING = "utf-8" ;
	
	/** 改行コード */
	private static final String LINE_SEPARATOR = "¥r¥n" ;
	
	/**
	 * IPアドレスとポート番号を受けとって、ソケットを作成するコンストラクタ
	 * @param remote_address
	 * @param port_number
	 * @param encoding
	 * @throws UnknownHostException
	 * @throws ConnectException
	 * @throws IOException
	 */
	public SocketWrapper(String remote_address, int port_number, String encoding)
		throws UnknownHostException, ConnectException, IOException	{
	
		if (remote_address == null || remote_address.equals("")) {
			// リモートアドレスが無効なら、ローカルへ接続。
			remote_address = "127.0.0.1" ;
		}
		m_socket = new Socket(remote_address, port_number) ;
		makeBuffer(encoding) ;
	}
	
	/**
	 * すでにあるソケットを渡すコンストラクタ
	 * @param sk
	 * @param encoding
	 */
	public SocketWrapper(Socket sk, String encoding) throws IOException {
		if (sk == null) {
			return ;
		}
		m_socket = sk ;
		makeBuffer(encoding) ;
	}
	
	/**
	 * 接続先へ文字列を送信します。
	 * @param strMsg
	 * @throws IOException
	 */
	public void sendMessage(String strMsg) throws IOException {
        // TODO リトライ
		m_writer.write(strMsg) ;
		m_writer.write(LINE_SEPARATOR) ;
		m_writer.flush() ;
	}

	/**
	 * 接続先から文字列を１行受信して、戻します。
	 * @return
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException {
        // TODO リトライ
		String strResult = m_reader.readLine() ;
		return strResult ;
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
	 * 指定の文字エンコーディングで送受信バッファを作成します。
	 * @param encoding
	 * @throws IOException
	 */
	private void makeBuffer(String encoding) throws IOException {
		if (encoding == null || encoding.equals("")) {
			m_encoding = DEFAULT_ENCODING ;
		} else {
			m_encoding = encoding ;
		}
		m_writer = new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream(), m_encoding)) ;
		m_reader = new BufferedReader(new InputStreamReader(m_socket.getInputStream(), m_encoding)) ;
	}
}
