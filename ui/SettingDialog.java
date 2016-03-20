package ui;



import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import judge.Setting;

/*
 * 设置窗口
 */
public class SettingDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Setting setting = Setting.getInstance();
	
	private void InitWindow() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getContentPane().setLayout(null);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("生成随机障碍物");
		chckbxNewCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				setting.hasObstacle = chckbxNewCheckBox.isSelected();
			}
		});
		chckbxNewCheckBox.setSelected(setting.hasObstacle);
		chckbxNewCheckBox.setBounds(40, 44, 147, 23);
		getContentPane().add(chckbxNewCheckBox);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Setting");

		setBounds(0, 0, 400, 240);
		setResizable(false);
		setVisible(true);
		
	}
	
	public SettingDialog(){
		
		InitWindow();
	}
}
