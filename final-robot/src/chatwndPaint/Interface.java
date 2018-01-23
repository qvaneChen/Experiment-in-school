package chatwndPaint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.http.ParseException;

import chatconnect.Chat;

public class Interface {

	JFrame mwnd = new JFrame("陈文倩-机器人聊天程序-Java期末");
	JPanel mwndPan = new JPanel(new BorderLayout());// 整个窗体主体的panel
	JPanel mSendMesPan = new JPanel(new BorderLayout());// 输入消息和发送按钮的panel
	JTextField inputMes = new JTextField();// 输入消息
	JTextPane mShowMesPan = new JTextPane();// 显示已发送和接收到的消息的Pane
	JButton senBtn = new JButton("发送");// 发送按钮
	JScrollPane jsp = new JScrollPane(mShowMesPan);// 消息显示窗口的滚轮
	int bu_width;// 气泡宽度
	mMesPan mes;// 自定义气泡生成类

	/**
	 * 生成窗口
	 */
	public void InitProgram() {
		// 1.调整窗口设置
		mwnd.setSize(800, 700);
		mwndPan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mwndPan.setBackground(Color.darkGray);

		// 2.固定窗口位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = mwnd.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		mwnd.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		// 3.初始化组件信息
		mShowMesPan.setBackground(Color.WHITE);
		mShowMesPan.setEditable(false);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		mSendMesPan.setPreferredSize(new Dimension(600, 50));
		mSendMesPan.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		mSendMesPan.setOpaque(false);

		mSendMesPan.add(inputMes, BorderLayout.CENTER);
		mSendMesPan.add(senBtn, BorderLayout.EAST);

		mwndPan.add(jsp, BorderLayout.CENTER);
		mwndPan.add(mSendMesPan, BorderLayout.SOUTH);

		bu_width = mSendMesPan.getSize().width;
		// 生成窗口
		mwnd.add(mwndPan);
		mwnd.setVisible(true);
	}

	/**
	 * 相应发送消息
	 */
	public void invaliWnd() {
		Chat chat = new Chat();
		mes = new mMesPan(750, mShowMesPan);
		senBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String question = inputMes.getText();
				try {
					mes.drawMesPane(question, true);
					String answer = chat.getRespond(question);
					inputMes.setText(null);
					mes.drawMesPane(answer, false);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		inputMes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String question = inputMes.getText();
				try {
					mes.drawMesPane(question, true);
					String answer = chat.getRespond(question);
					inputMes.setText(null);
					mes.drawMesPane(answer, false);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}

}
