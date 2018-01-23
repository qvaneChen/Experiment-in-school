import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.UIManager;

import chatwndPaint.Interface;

public class cwqDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Font font = new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 16);
		UIManager.put("TextArea.font", font);
		UIManager.put("TextField.font", font);
		Interface wnd = new Interface();
		wnd.InitProgram();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				wnd.invaliWnd();
			}
		});
	}
}
