package chatwndPaint;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;

public class mMesPan {

	/**
	 * 在JTextPane组件里生成气泡并执行插入
	 * 
	 * @param messtr
	 *            发送或接收到的需要显示的消息
	 * @param fromwho
	 *            判断是机器人还是自己发送的消息
	 */
	private static AffineTransform atf = new AffineTransform();
	private static FontRenderContext frc = new FontRenderContext(atf, true, true);
	private JTextPane textpane;// 目标插入的JTextPane
	private int count;// 统计插入mMesBubble的组件数量
	private ImageIcon micon = new ImageIcon("images/1.jpg");// 我的头像
	private ImageIcon roboticon = new ImageIcon("images/2.jpg");// 图灵机器人的头像
	private Font font = new Font("微软雅黑", Font.PLAIN, 17);// 消息使用地址
	private int cptwidth;// 组件宽度
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 消息生成的时间

	public mMesPan(int w, JTextPane j) {
		this.cptwidth = w;
		this.textpane = j;
	}

	public void drawMesPane(String messtr, boolean fromwho) {
		// 1.准备生成气泡
		int sheight = (int) font.getStringBounds(messtr, frc).getHeight();
		int swidth = (int) font.getStringBounds(messtr, frc).getWidth();
		ArrayList<String> strarr = new ArrayList<String>();

		// 2.将字符串进行分行并储存
		int pic_count = 0;
		if (swidth > cptwidth - 100) {
			int startat = 0;
			int endat = 1;
			while (endat < messtr.length()) {
				String s = messtr.substring(startat, endat);
				if (s.contains("\\n")) {
					strarr.add(messtr.substring(startat, endat - 2));
					startat = endat;
				} else if (s.contains("图片：")) {
					if (s.contains("pend")) {
						strarr.add(messtr.substring(startat, endat - 4));
						startat = endat;
					} else {
						endat++;
						continue;
					}
					pic_count += 1;
				} else if (font.getStringBounds(s, frc).getWidth() > (cptwidth - 100)) {
					strarr.add(messtr.substring(startat, endat - 2));
					startat = endat - 1;
				} else if (endat == messtr.length() - 1) {
					strarr.add(messtr.substring(startat, messtr.length()));
				}
				endat++;
			}
		} else
			strarr.add(messtr);

		// 3.生成聊天气泡
		int str_width = (int) font.getStringBounds(strarr.get(0), frc).getWidth();
		for (int i = 0; i < strarr.size(); i++) {
			if (strarr.get(i).contains("图片："))
				continue;
			if (str_width < (int) font.getStringBounds(strarr.get(i), frc).getWidth())
				str_width = (int) font.getStringBounds(strarr.get(i), frc).getWidth();
		}
		mMesBubble mbubble = new mMesBubble(sheight, str_width, strarr, fromwho);
		mbubble.setTime(this.df.format(new Date()));
		mbubble.setPicnum(pic_count);
		// System.out.println("pic_count+"+pic_count);

		if (fromwho) {
			mbubble.setIcon(micon, true);
		} else {
			mbubble.setIcon(roboticon, false);
		}

		// 4.插入聊天气泡
		mbubble.setPreferredSize(new Dimension(cptwidth, sheight * (strarr.size() + 1) + 30 + pic_count * 45));
		textpane.insertComponent(mbubble);
		this.count++;
		textpane.setCaretPosition(count);
	}
}
