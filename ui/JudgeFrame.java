package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import util.ExeRunner;
import module.Input;

import com.alibaba.fastjson.JSON;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import judge.CELL;
import judge.Game;

public class JudgeFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final int top = 10;
	private final int left = 220;
	private final Game game = Game.getInstance();

	HashMap<CELL, String> resMap;

	Judge thread;
	JLabel lb_path_0;
	JLabel lb_path_1;
	JLabel lb_curRound;

	private BodyPanel[][] body;

	String[] path = { "", "" };

	private JPanel board;

	public void NewGame() {
		// ��ÿ�ִ���ļ�·��
		path[0] = lb_path_0.getText();
		path[1] = lb_path_1.getText();

		// ���Ϸ���
		if (path[0].length() < 4 || path[1].length() < 4) {
			JOptionPane.showMessageDialog(null, "请选择正确的AI!", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			if (!path[0].substring(path[0].length() - 4, path[0].length())
					.equals(".exe")
					|| !path[1].substring(path[1].length() - 4,
							path[1].length()).equals(".exe")) {
				JOptionPane.showMessageDialog(null, "请选择正确的AI!", "错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		game.InitGame();

		if (board == null) {
			board = new JPanel();
		} else {
			remove(board);
			board = new JPanel();

		}

		board.setSize((game.width + 2) * 40, (game.height + 2) * 40);
		board.setLocation(left, top);
		board.setBackground(new Color(222, 222, 222));
		getContentPane().add(board);

		body = new BodyPanel[20][20];
		board.setLayout(null);

		for (int i = 0; i <= game.height + 1; i++) {
			for (int j = 0; j <= game.width + 1; j++) {
				body[i][j] = new BodyPanel(resMap.get(CELL.EMPTY));
				body[i][j].setLocation(j * 40, i * 40);
				body[i][j].setSize(40, 40);
				board.add(body[i][j]);
			}
		}

		UpdateBoard();
		repaint();

		game.pause = false;
		game.stop = false;
	}

	private void HandleMove(String[] json) {
		int[] dir = new int[2];
		for (int who = 0; who < 2; who++) {
			Input input = null;
			try {
				input = JSON.parseObject(json[who], Input.class);
			} catch (Exception e) {
				System.out.println(json[who]);
			}
			dir[who] = input.getResponse().getDirection();
		}
		game.Move(dir);
	}

	public void UpdateBoard() {
		for (int i = 0; i <= game.height + 1; i++) {
			for (int j = 0; j <= game.width + 1; j++) {
				body[i][j].SetBackground(resMap.get(game.board[i][j]));
			}
		}
		// 更新回合
		lb_curRound.setText("当前回合:" + game.round);
		repaint();
	}

	class Judge implements Runnable {
		public void run() {
			while (true) {
				while (game.pause) {
					Thread.yield();
				}
				while (game.stop) {
					Thread.yield();
				}
				System.out.println("current round is " + game.round);
				String out0 = game.OutPut(0);
				String out1 = game.OutPut(1);
				String res0 = ExeRunner.RunExe(path[0], out0);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String res1 = ExeRunner.RunExe(path[1], out1);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String[] resps = { res0, res1 };
				while (game.pause) {
					Thread.yield();
				}
				while (game.stop) {
					Thread.yield();
				}
				HandleMove(resps);
				UpdateBoard();
			}
		}
	}

	private void InitWindow() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Judge());
		service.shutdown();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("LegendZone");

		setBounds(0, 0, 1000, 725);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "\u7EFF\u65B9",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel.setBounds(10, 10, 200, 200);
		getContentPane().add(panel);
		panel.setLayout(null);

		lb_path_0 = new JLabel("...");
		lb_path_0.setBounds(16, 102, 166, 49);
		panel.add(lb_path_0);

		JButton btn_select_0 = new JButton("\u9009\u62E9\u7A0B\u5E8F");
		btn_select_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = jfc.showDialog(new JLabel(), "选择");
				if (val == JFileChooser.APPROVE_OPTION) {
					lb_path_0.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btn_select_0.setBounds(107, 163, 81, 23);
		panel.add(btn_select_0);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "\u7EA2\u65B9",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel_1.setBounds(10, 220, 200, 200);
		getContentPane().add(panel_1);

		lb_path_1 = new JLabel("...");
		lb_path_1.setBounds(17, 103, 166, 49);
		panel_1.add(lb_path_1);

		JButton btn_select_1 = new JButton("\u9009\u62E9\u7A0B\u5E8F");
		btn_select_1.setBounds(108, 164, 81, 23);
		panel_1.add(btn_select_1);
		btn_select_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int val = jfc.showDialog(new JLabel(), "选择");
				if (val == JFileChooser.APPROVE_OPTION) {
					lb_path_1.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "\u5BF9\u5C40\u63A7\u5236",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 430, 200, 246);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JButton button_1 = new JButton("\u65B0\u5F00\u5C40");
		button_1.setBounds(10, 114, 180, 23);
		panel_2.add(button_1);

		JButton button = new JButton("暂停/继续");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				game.pause = !game.pause;
			}
		});
		button.setBounds(10, 147, 180, 23);
		panel_2.add(button);

		lb_curRound = new JLabel("当前回合:");
		lb_curRound.setBounds(10, 33, 135, 15);
		panel_2.add(lb_curRound);

		JButton button_2 = new JButton("设置");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingDialog dialog = new SettingDialog();
				dialog.setModal(true);
				dialog.setLocationRelativeTo(null);
			}
		});
		button_2.setBounds(10, 180, 180, 23);
		panel_2.add(button_2);

		JButton button_3 = new JButton("帮助");
		button_3.setBounds(10, 213, 180, 23);
		panel_2.add(button_3);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewGame();
			}
		});

		setVisible(true);

	}

	private void InitRes() {
		resMap = new HashMap<CELL, String>();
		resMap.put(CELL.GREEN_HEAD, "src/pngs/gh1.png");
		resMap.put(CELL.GREEN_BODY, "src/pngs/g1.png");
		resMap.put(CELL.GREEN_TAIL, "src/pngs/g1.png");

		resMap.put(CELL.RED_HEAD, "src/pngs/rh1.png");
		resMap.put(CELL.RED_BODY, "src/pngs/r1.png");
		resMap.put(CELL.RED_TAIL, "src/pngs/r1.png");

		resMap.put(CELL.EMPTY, "");
		resMap.put(CELL.OBSTACLE, "src/pngs/obstacle.png");
	}

	public JudgeFrame() {
		InitRes();
		InitWindow();
	}
}
