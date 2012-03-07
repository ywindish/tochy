package jp.windish.tochy;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * 入力した文字列をサーバーに送信するGUIクライアント。
 * @author yamako
 * 
 */
public class Client extends JFrame
	implements KeyListener, WindowListener, ActionListener {
	
	private static final long serialVersionUID = 1L;

	private SocketWrapper m_network = null ; // サーバとの接続
	private Server m_server = null ;         // ローカルサーバ（サーバモード時に保持）
	
	private ClientView m_view = null ;      // 表示部 + ログファイル
	private JTextField m_textfield = null ; // 入力部
	private ConfigDialog m_config = null ;  // 設定ダイアログ
	
	private JButton m_button_config = null ; // 設定ボタン
	private JButton m_button_quit = null ;   // 通信終了ボタン
	
	/**
	 * コンストラクタ
	 * @throws HeadlessException
	 */
	public Client() throws HeadlessException {
		super(Config.getConfig(Config.C_USER_NAME)) ;
	}

	/**
	 * クライアントを初期化して開始する
	 * @param strUserName
	 * @param strIpAddress
	 */
	public void init() {

		m_view = new ClientView() ;
		m_config = new ConfigDialog(this) ;
		makeUI() ;

		if (Config.getConfig(Config.C_USER_MODE).equals(Config.SERVER_MODE) == true) {
			// サーバモードなら、ここでサーバを起動。
			m_server = new Server(m_view) ;
			m_server.start() ;
		}
		connect() ;
		sendUserName() ;
		receiveText() ;
	}
	
	/**
	 * UI作成。ウィジェットを配置する
	 */
	private void makeUI() {
		
		Font font = Config.getFont() ;
		Container cont = getContentPane() ;

		// ボタン
		JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.LEFT)) ;
		m_button_config = new JButton("設定") ;
		m_button_config.addActionListener(this) ;
		button_panel.add(m_button_config) ;
		
		// ボタン
		m_button_quit = new JButton("通信終了") ;
		m_button_quit.addActionListener(this) ;
		button_panel.add(m_button_quit) ;
		cont.add(button_panel, BorderLayout.NORTH) ;

		// 表示部
		JScrollPane jsp = new JScrollPane(m_view) ;
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED) ;
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) ;
		cont.add(jsp, BorderLayout.CENTER) ;
		
		// 入力部
		m_textfield = new JTextField("", Config.getConfigInt(Config.C_TEXT_COLUMN)) ;
		m_textfield.addKeyListener(this) ;
		m_textfield.setFont(font) ;
		cont.add(m_textfield, BorderLayout.SOUTH) ;

		addWindowListener(this) ;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE) ;

		// 仕上げ (必ずこの順番で。)
		pack() ;
		setBounds(Config.getConfigInt(Config.C_WINDOW_LEFT), Config.getConfigInt(Config.C_WINDOW_TOP),
				  Config.getConfigInt(Config.C_WINDOW_WIDTH), Config.getConfigInt(Config.C_WINDOW_HEIGHT)) ;
		setVisible(true) ;
		m_textfield.requestFocusInWindow() ; // 起動したら即入力できるように。
	}
	
	/**
	 * サーバに接続する
	 * @param strIpAddress
	 * @return
	 */
	private void connect() {
		String strIpAddress = Config.getConfig(Config.C_NET_ADDRESS) ;
		try {
			m_network = new SocketWrapper(strIpAddress, Config.getConfigInt(Config.C_NET_PORT), null) ;
		} catch (ConnectException e) {
			m_view.printSystemMessage("サーバが見つかりません。接続先アドレスを確認して下さい。") ;
		} catch (UnknownHostException e) {
			m_view.printSystemMessage("サーバに接続できません。接続先サーバが起動しているか確認して下さい。") ;
		} catch (Exception e) {
			m_view.printSystemMessage(e.getLocalizedMessage()) ;
		}
		if (m_network == null) {
			return ;
		}
		m_view.printSystemMessage("サーバに接続しました。") ;
	}
	
	/** 
	 * ユーザ名をサーバに送信する
	 */
	private void sendUserName() {
		String strUserName = Config.getConfig(Config.C_USER_NAME) ;
		try {
			m_network.sendMessage(strUserName) ;
		} catch (IOException e) {
			m_view.printSystemMessage(e.getLocalizedMessage()) ;
		}
	}
	
	/**
	 * サーバからメッセージを受信して、表示エリアに表示しつづける。
	 * 通信が切断されるか、nullを受信したら、受信終了。
	 */
	private void receiveText() {
		String strMessage = "" ;
		try {
			while ((strMessage = m_network.receiveMessage()) != null) {
				m_view.printMessage(strMessage) ;
			}
		} catch (SocketException se) {
			m_view.printSystemMessage("接続が切断されました。") ;
		} catch (IOException e) {
			m_view.printSystemMessage(e.getLocalizedMessage()) ;
		}
	}

	/**
	 * 入力された文字列をサーバに送信する
	 */
	private void sendInputText() {

		try {
			m_network.sendMessage(m_textfield.getText()) ;
		} catch (IOException e) {
			m_view.printSystemMessage(e.getLocalizedMessage()) ;
		}
		m_textfield.setText("") ;
	}
	
	/**
	 * 設定ファイルを保存する
	 *
	 */
	public void saveConfig() {

		// ウィンドウサイズを保存
		Config.setConfig(Config.C_WINDOW_LEFT, getX()) ;
		Config.setConfig(Config.C_WINDOW_TOP, getY()) ;
		Config.setConfig(Config.C_WINDOW_WIDTH, getWidth()) ;
		Config.setConfig(Config.C_WINDOW_HEIGHT, getHeight()) ;

		if (Config.save() == false) {
			m_view.printSystemMessage("設定ファイルを保存できません。") ;
		}
	}
	
	/**
	 * 終了するときの動作
	 */
	private void exit() {
		
		// ダイアログを出して、いいえだったら終了しない。
		int answer = JOptionPane.showConfirmDialog(this, "終了しますか？", "", JOptionPane.YES_NO_OPTION) ;
		if (answer != JOptionPane.YES_OPTION) {
			return ;
		}
		saveConfig() ; // 設定ファイルを保存する。
		
		if (m_network != null) {
			try {
				// サーバへの接続を切る。
				m_network.disconnect() ;
				m_view.printSystemMessage("接続を切断しました。") ;
			} catch (IOException e) {
				m_view.printSystemMessage(e.getLocalizedMessage()) ;
			}
		}
		if (m_server != null) {
			// サーバモード時は、サーバの後始末。
			m_server.shutdown() ;
		}
		dispose() ;
		
		System.exit(0) ; // なぜかこれ書かないと終わらない。
	}
	
	/**
	 * 設定ダイアログ呼び出し後の再設定
	 *
	 */
	private void reconfig() {
		m_textfield.setFont(Config.getFont()) ;
		m_view.loadConfig() ;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource() ;
		if (source.equals(m_button_config)) {
			m_config.load() ; // 設定ダイアログ呼び出し
			reconfig() ;
			m_textfield.requestFocusInWindow() ; // 設定後、すぐ入力できるように。
		} else if (source.equals(m_button_quit)) {
			exit() ; // 終了処理
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getSource().equals(m_textfield) != true) {
			return ;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			// Enterを押されたら送信。
			sendInputText() ;
			break;
		case KeyEvent.VK_ESCAPE:
			// Escを押されたら終了。
			exit() ;
			break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		// ウィンドウが閉じられたら終了処理をする。
		exit() ;
	}

	// 未実装の keyListener インタフェースメソッド
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	// 未実装の WindowListener インタフェースメソッド
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
