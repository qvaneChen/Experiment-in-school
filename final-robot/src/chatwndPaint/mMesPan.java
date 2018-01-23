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
	 * ��JTextPane������������ݲ�ִ�в���
	 * 
	 * @param messtr
	 *            ���ͻ���յ�����Ҫ��ʾ����Ϣ
	 * @param fromwho
	 *            �ж��ǻ����˻����Լ����͵���Ϣ
	 */
	private static AffineTransform atf = new AffineTransform();
	private static FontRenderContext frc = new FontRenderContext(atf, true, true);
	private JTextPane textpane;// Ŀ������JTextPane
	private int count;// ͳ�Ʋ���mMesBubble���������
	private ImageIcon micon = new ImageIcon("images/1.jpg");// �ҵ�ͷ��
	private ImageIcon roboticon = new ImageIcon("images/2.jpg");// ͼ������˵�ͷ��
	private Font font = new Font("΢���ź�", Font.PLAIN, 17);// ��Ϣʹ�õ�ַ
	private int cptwidth;// ������
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ��Ϣ���ɵ�ʱ��

	public mMesPan(int w, JTextPane j) {
		this.cptwidth = w;
		this.textpane = j;
	}

	public void drawMesPane(String messtr, boolean fromwho) {
		// 1.׼����������
		int sheight = (int) font.getStringBounds(messtr, frc).getHeight();
		int swidth = (int) font.getStringBounds(messtr, frc).getWidth();
		ArrayList<String> strarr = new ArrayList<String>();

		// 2.���ַ������з��в�����
		int pic_count = 0;
		if (swidth > cptwidth - 100) {
			int startat = 0;
			int endat = 1;
			while (endat < messtr.length()) {
				String s = messtr.substring(startat, endat);
				if (s.contains("\\n")) {
					strarr.add(messtr.substring(startat, endat - 2));
					startat = endat;
				} else if (s.contains("ͼƬ��")) {
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

		// 3.������������
		int str_width = (int) font.getStringBounds(strarr.get(0), frc).getWidth();
		for (int i = 0; i < strarr.size(); i++) {
			if (strarr.get(i).contains("ͼƬ��"))
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

		// 4.������������
		mbubble.setPreferredSize(new Dimension(cptwidth, sheight * (strarr.size() + 1) + 30 + pic_count * 45));
		textpane.insertComponent(mbubble);
		this.count++;
		textpane.setCaretPosition(count);
	}
}
