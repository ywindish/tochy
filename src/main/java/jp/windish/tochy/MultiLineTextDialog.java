package jp.windish.tochy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * テキストをまとめて送信するためのダイアログ
 * @author yamako
 */
public class MultiLineTextDialog extends JDialog implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "まとめて送信" ;

	private Client m_owner = null ; // 呼び出し元
	
	private JTextArea m_text = null;
	private JLabel m_alert = null;
	private JButton m_ok = null;
	private JButton m_cancel = null;

	/**
	 * コンストラクタ
	 * @param owner
	 */
	public MultiLineTextDialog(Client owner) {
		super(owner, TITLE, true);
		m_owner = owner;
		init();
	}
	
	/**
	 * 初期化。GUIパーツの配置
	 */
	private void init() {
		Container cont = getContentPane() ;

		// --------------------- 入力ここから ---------------------
		m_text = new JTextArea(20, 30);
		
		m_alert = new JLabel("送信したいテキストを入力してください。「送信する」を押すと、まとめて送信されます。");
		cont.add(m_alert, BorderLayout.NORTH);
		
		JScrollPane jsp = new JScrollPane(m_text) ;
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED) ;
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) ;
		cont.add(jsp, BorderLayout.CENTER) ;
		// --------------------- 入力ここまで ---------------------
		
		// -------------------- ボタンここから --------------------
		JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.LEFT)) ;

		m_ok = new JButton("送信する");
		m_ok.addActionListener(this);
		button_panel.add(m_ok);
		
		m_cancel = new JButton("キャンセル");
		m_cancel.addActionListener(this);
		button_panel.add(m_cancel);

		cont.add(button_panel, BorderLayout.SOUTH);
		// -------------------- ボタンここまで --------------------

		setDefaultCloseOperation(HIDE_ON_CLOSE) ;
		pack() ;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource() ;
		if (source.equals(m_ok)) {
			// OK を押されたらテキストを送信。
			m_owner.sendMultiLineText(m_text.getText());
		}
		if (source.equals(m_ok) || source.equals(m_cancel)) {
			// OK またはキャンセルを押されたら、テキストをクリアしてから隠す。
			m_text.setText("");
			setVisible(false) ;
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false) ; // キャンセルと同じ動き
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
}
