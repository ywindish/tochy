package jp.windish.tochy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * ユーザー設定を入力するダイアログ。OKでファイル保存。
 * 呼び出し元から入力値を取り出せる。即時反映するかどうかは、呼び出し元が決める。
 * @author yamako
 *
 */
public class ConfigDialog extends JDialog implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "設定" ; 
	
	private Client m_owner = null ; // 呼び出し元
	
	private JTextField m_user_name = null ;  // ユーザー名
	private JComboBox  m_font_name = null ;  // フォント名
	private JTextField m_font_size = null ;  // フォントサイズ
	private JCheckBox  m_logging = null ;    // ログを取るか否か
	private JButton    m_fore_color = null ; // 文字色
	private JButton    m_back_color = null ; // 背景色

	private JButton m_ok = null ;
	private JButton m_cancel = null ;
	
	/**
	 * コンストラクタ
	 * @param owner
	 */
	public ConfigDialog(Client owner) {
		super(owner, TITLE, true) ;
		m_owner = owner ;
		init() ;
	}

	/**
	 * ユーザー入力のユーザー名を戻します。
	 * @return
	 */
	public String getUserName() {
		if (m_user_name == null) return "" ;
		return m_user_name.getText() ;
	}
	
	/**
	 * ユーザーが選択したフォント名を戻します。
	 * @return
	 */
	public String getFontName() {
		if (m_font_name == null) return "" ;
		return (String) m_font_name.getSelectedItem() ;
	}
	
	/**
	 * ユーザー入力のフォントサイズを戻します。
	 * @return
	 */
	public int getFontSize() {
		if (m_font_size == null) return 0 ;
		int ret = 0 ;
		try {
			ret = Integer.parseInt(m_font_size.getText()) ;
		} catch (NumberFormatException e) {
			// 数字でなければ0を返す。
		}
		return ret ;
	}

	/**
	 * ユーザー入力の文字色を戻します。
	 * @return
	 */
	public Color getForeColor() {
		if (m_fore_color == null)  return null ;
		return m_fore_color.getBackground() ;
	}
	
	/**
	 * ユーザー入力の背景色を戻します。
	 * @return
	 */
	public Color getBackColor() {
		if (m_back_color == null)  return null ;
		return m_back_color.getBackground() ;
	}
	
	/**
	 * ログの有無についてのユーザー選択を戻します。
	 * @return
	 */
	public boolean isLoggingEnable() {
		if (m_logging == null) return false ;
		return m_logging.isSelected() ;
	}
	
	/**
	 * 最初にやる処理。GUIパーツの配置
	 *
	 */
	private void init() {

		Container cont = getContentPane() ;

		// --------------------- 入力ここから ---------------------
		JPanel config_panel = new JPanel(new GridLayout(6, 2)) ;
		
		m_user_name = new JTextField("", 10) ;
		config_panel.add(new JLabel("ユーザー名（次回起動後に有効）")) ;
		config_panel.add(m_user_name) ;
		
		m_font_name = new JComboBox(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) ;
		config_panel.add(new JLabel("フォント名")) ;
		config_panel.add(m_font_name) ;
		
		m_font_size = new JTextField("", 2) ;
		config_panel.add(new JLabel("フォントサイズ")) ;
		config_panel.add(m_font_size) ;
		
		m_fore_color = new JButton("変更する") ;
		m_fore_color.addActionListener(this) ;
		config_panel.add(new JLabel("文字色")) ;
		config_panel.add(m_fore_color) ;
		
		m_back_color = new JButton("変更する") ;
		m_back_color.addActionListener(this) ;
		config_panel.add(new JLabel("背景色")) ;
		config_panel.add(m_back_color) ;
		
		m_logging = new JCheckBox("ログを取る") ;
		config_panel.add(m_logging) ;

		cont.add(config_panel, BorderLayout.CENTER) ;
		// --------------------- 入力ここまで ---------------------
		
		// -------------------- ボタンここから --------------------
		JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) ;
		
		m_ok = new JButton("OK") ;
		m_ok.addActionListener(this) ;
		button_panel.add(m_ok) ;
		
		m_cancel = new JButton("キャンセル") ;
		m_cancel.addActionListener(this) ;
		button_panel.add(m_cancel) ;

		cont.add(button_panel, BorderLayout.SOUTH) ;
		// -------------------- ボタンここまで --------------------
		
		//setLocationRelativeTo(null) ;
		setDefaultCloseOperation(HIDE_ON_CLOSE) ;
		pack() ;
	}
	
	/**
	 * 設定ファイルを読み込んで、画面を表示する
	 *
	 */
	public void load() {
		if (m_user_name != null) {
			m_user_name.setText(Config.getConfig(Config.C_USER_NAME)) ;
		}
		if (m_font_name != null) {
			m_font_name.setSelectedItem(Config.getConfig(Config.C_FONT_NAME)) ;
		}
		if (m_font_size != null) {
			m_font_size.setText(Config.getConfig(Config.C_FONT_SIZE)) ;
		}
		if (m_logging != null) {
			m_logging.setSelected(Config.getConfigBoolean(Config.C_LOG_ENABLE)) ;
		}
		if (m_fore_color != null) {
			m_fore_color.setBackground(Config.getForeColor()) ;
			m_fore_color.setForeground(Config.getBackColor()) ;
		}
		if (m_back_color != null) {
			m_back_color.setBackground(Config.getBackColor()) ;
			m_back_color.setForeground(Config.getForeColor()) ;
		}
		setVisible(true) ;
	}

	/**
	 * 入力された設定を保存する
	 *
	 */
	private void save() {
		Config.setConfig(Config.C_USER_NAME, getUserName()) ;
		Config.setConfig(Config.C_FONT_NAME, getFontName()) ;
		Config.setConfig(Config.C_FONT_SIZE, getFontSize()) ;
		Config.setConfig(Config.C_LOG_ENABLE, isLoggingEnable()) ;
		
		Color fore = getForeColor() ;
		Config.setConfig(Config.C_COLOR_FORE_R, fore.getRed()) ;
		Config.setConfig(Config.C_COLOR_FORE_G, fore.getGreen()) ;
		Config.setConfig(Config.C_COLOR_FORE_B, fore.getBlue()) ;
		
		Color back = getBackColor() ;
		Config.setConfig(Config.C_COLOR_BACK_R, back.getRed()) ;
		Config.setConfig(Config.C_COLOR_BACK_G, back.getGreen()) ;
		Config.setConfig(Config.C_COLOR_BACK_B, back.getBlue()) ;

		m_owner.saveConfig() ; // ファイル保存。
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource() ;
		if (source.equals(m_ok)) {
			// OK を押されたら保存して隠れる。
			save() ;
			setVisible(false) ;
			
		} else if (source.equals(m_cancel)) {
			// キャンセルなら単に隠れる。
			setVisible(false) ;

		} else if (source.equals(m_fore_color)) {
			// 文字色変更。
			m_fore_color.setBackground(getColorFromChooser(m_fore_color.getBackground())) ;
			
		} else if (source.equals(m_back_color)) {
			// 背景色変更。
			m_back_color.setBackground(getColorFromChooser(m_back_color.getBackground())) ;
		}
	}
	
	/**
	 * 色設定ダイアログを呼んで、選んだ色を返す
	 * @param initialColor
	 * @return
	 */
	private Color getColorFromChooser(Color initialColor) {
		Color newColor = JColorChooser.showDialog(this, "色の変更", initialColor) ;
		if (newColor == null) {
			return initialColor ;
		}
		return newColor ;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		setVisible(false) ; // キャンセルと同じ動き
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
