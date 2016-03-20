package ui;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BodyPanel extends JPanel {
	/**
	 * 
	 */
	
	private String backgroundPic;

	private static final long serialVersionUID = 1L;

	public BodyPanel(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}
	
	public void SetBackground(String backgroundPic){
		this.backgroundPic = backgroundPic;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		if(backgroundPic.equals("")){
			super.paintComponent(g);
			return;
		}
		int x = 0, y = 0;
		ImageIcon icon = new ImageIcon(backgroundPic);// 003.jpg是测试图片在项目的根目录下
		g.drawImage(icon.getImage(), x, y, getSize().width, getSize().height,
				this);// 图片会自动缩放
		// g.drawImage(icon.getImage(), x, y,this);//图片不会自动缩放
	}
}
