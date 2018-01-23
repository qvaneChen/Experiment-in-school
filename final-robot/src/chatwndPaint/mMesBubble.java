package chatwndPaint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.apache.http.ParseException;

import chatconnect.Chat;

public class mMesBubble extends JComponent {

	/**
	 * 生成聊天气泡
	 * 
	 * @param sHeight
	 *            单行字符串高度
	 * @param str_width
	 *            分好行后一行字符串的宽度
	 * @param temp_arr
	 *            用ArrayList存储的已经分好行的字符串消息
	 * @param fromwho
	 *            判断是自己发送的消息还是别人发送的消息
	 */
	private static final long serialVersionUID = 1L;

	private static AffineTransform atf = new AffineTransform();
	private static FontRenderContext frc = new FontRenderContext(atf, true, true);
	private boolean whosendMes;// true表示我发送的消息，false表示机器人发送的消息
	private Point point = new Point(10, 10);// 绘制图形顶点坐标
	private Image mimage;// 我的头像
	private Image robotimage;// 机器人的头像
	private Image tempimage;// 临时图片
	private int picnum;// 每行字符串的高度
	private int swidth;// 每行字符串的宽度
	private int sheight;// 每行字符串的高度
	private String mestime;// 消息生成的时间
	private String nameandtime;// 用户名和时间字符串
	private Color mmesColor = new Color(25, 185, 85);// 我的消息气泡的颜色
	private Color mfontColor = new Color(250, 250, 250);// 我的消息字体的颜色
	private Color robotmesColor = new Color(238, 238, 238);// 机器人的消息气泡的颜色
	private Color robotfontColor = new Color(0, 0, 0);// 机器人消息字体的颜色
	private Color namefontcolor = new Color(128, 128, 128);// 显示名字和时间信息的字体颜色
	private Font mesFont = new Font("微软雅黑", Font.PLAIN, 17);// 消息使用字体
	private Font nameFont = new Font("微软雅黑", Font.PLAIN, 12);// 用户名和时间使用字体
	private ArrayList<String> strArr = new ArrayList<String>();// 存储分行的字符串

	public mMesBubble(int sHeight, int str_width, ArrayList<String> temp_arr, boolean fromwho) {
		this.sheight = sHeight;
		this.swidth = str_width;
		this.strArr = temp_arr;
		this.whosendMes = fromwho;
		this.setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		// 1.绘制准备
		Graphics2D mPen = (Graphics2D) g;
		mPen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int str_num = strArr.size();// 分好行后消息的行数

		int xpts[] = new int[3];
		int ypts[] = new int[3];

		// 2.绘制气泡
		if (whosendMes) {
			// 3.我发送的消息
			nameandtime = mestime + "  我";
			int tempmmesX = this.getWidth() - point.x - mimage.getWidth(null);
			int tempmmesY = point.y + (int) nameFont.getStringBounds(nameandtime, frc).getHeight();
			xpts[0] = tempmmesX - 5;
			ypts[0] = tempmmesY + 12;
			xpts[1] = xpts[0] + 10;
			ypts[1] = tempmmesY + 18;
			xpts[2] = xpts[0];
			ypts[2] = tempmmesY + 24;

			mPen.drawImage(mimage, this.getWidth() - mimage.getWidth(null), point.y, null);

			mPen.setColor(namefontcolor);
			mPen.setFont(nameFont);
			mPen.drawString(nameandtime, tempmmesX - 4 - (int) nameFont.getStringBounds(nameandtime, frc).getWidth(),
					point.y + 10);

			mPen.setColor(mmesColor);
			mPen.fillRoundRect(tempmmesX - swidth - 22, tempmmesY + 2, swidth + 18, sheight * str_num + 20, 15, 15);

			mPen.setColor(mmesColor);
			mPen.fillPolygon(xpts, ypts, 3);

			mPen.setColor(mfontColor);
			mPen.setFont(mesFont);

			for (int i = 0; i < str_num; i++) {
				mPen.drawString(strArr.get(i), tempmmesX - swidth - 12, tempmmesY + 30 + i * sheight);
			}
		} else {
			// 4.机器人发送的消息
			nameandtime = "图灵机器人  " + mestime;
			int temprmesX = point.x + robotimage.getWidth(null);
			int temprmesY = point.y + (int) nameFont.getStringBounds(nameandtime, frc).getHeight();
			xpts[0] = temprmesX + 5;
			ypts[0] = temprmesY + 12;
			xpts[1] = xpts[0] - 10;
			ypts[1] = temprmesY + 18;
			xpts[2] = xpts[0];
			ypts[2] = temprmesY + 24;

			mPen.drawImage(robotimage, 0, point.y, null);

			mPen.setColor(namefontcolor);
			mPen.setFont(nameFont);
			mPen.drawString(nameandtime, temprmesX + 4, point.y + 10);

			// System.out.println("setPicnum00+" + picnum);
			int tempwithpic = sheight * str_num + picnum * 100;
			// System.out.println("stempwithpic+" + tempwithpic);
			mPen.setColor(robotmesColor);
			mPen.fillRoundRect(temprmesX + 4, temprmesY + 2, swidth + 20, tempwithpic + 20, 15, 15);

			mPen.setColor(robotmesColor);
			mPen.fillPolygon(xpts, ypts, 3);

			mPen.setColor(robotfontColor);
			mPen.setFont(mesFont);
			String tempPicUrl = "";
			Chat picchat = new Chat();

			for (int i = 0; i < str_num; i++) {
				if (strArr.get(i).contains("图片：")) {
					tempPicUrl = strArr.get(i).substring(3);
					try {
						tempimage = picchat.getRespondPic(tempPicUrl);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						tempimage = (new ImageIcon("images/3.jpg")).getImage();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (tempimage.getHeight(null) > 50) {
						tempimage.getScaledInstance((tempimage.getWidth(null) / tempimage.getHeight(null)) * 50, 50,
								Image.SCALE_DEFAULT);
						Image temi1 = tempimage;
						mPen.drawImage(temi1, temprmesX + 14, temprmesY + 20 + i * sheight, null);
					} else {
						mPen.drawImage(tempimage, temprmesX + 14, temprmesY + 20 + i * sheight, null);
					}
					temprmesY += tempimage.getHeight(null);
					continue;
				}
				mPen.drawString(strArr.get(i), temprmesX + 14, temprmesY + 30 + i * sheight);
			}
		}

		super.paintComponent(g);
	}

	public void setIcon(ImageIcon icon, boolean n) {
		// TODO Auto-generated method stub
		if (n) {
			this.mimage = icon.getImage();
		} else {
			this.robotimage = icon.getImage();
		}
	}

	public void setTime(String ti) {
		this.mestime = ti;
	}

	public void setPicnum(int n) {
		this.picnum = n;
		// System.out.println("setPicnum+" + picnum);
	}
}
