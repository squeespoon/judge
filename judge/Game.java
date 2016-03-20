package judge;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import util.Util;

import com.alibaba.fastjson.JSON;

import module.Direction;
import module.GameInfo;
import module.Obstacle;
import module.Output;

/*
 * 单例类，贪吃蛇游戏
 */

public class Game {
	// 游戏设置
	Setting setting = Setting.getInstance();

	public boolean pause = true;
	public boolean stop = true;

	public int round;

	public int width;
	public int height;

	public List<Obstacle> obstacles;
	public List<Object>[] moves;

	public List<Point>[] bodies;

	// 当前坐标
	public int[] X;
	public int[] Y;

	public CELL[][] board;

	private static final Game game = new Game();

	private Game() {

	}

	public static Game getInstance() {
		return game;
	}

	boolean whetherGrow(int num) { // 本回合是否生长
		// 注意这里回合从0开始。。。
		if (num <= 24)
			return true;
		if ((num - 24) % 3 == 0)
			return true;
		return false;
	}

	public void Move(int[] dir) {
		// 对应PKUbotzone
		if (!whetherGrow(round - 1)) {
			bodies[0].remove(0);
			bodies[1].remove(0);
		}
		round++;
		for (int who = 0; who < 2; who++) {
			moves[who].add(new Direction(dir[who]));
			if (dir[who] == 0) {
				Y[who]--;
			} else if (dir[who] == 1) {
				X[who]++;
			} else if (dir[who] == 2) {
				Y[who]++;
			} else if (dir[who] == 3) {
				X[who]--;
			}
			bodies[who].add(new Point(X[who], Y[who]));
		}

		// 清理棋盘
		for (int i = 1; i <= height; i++) {
			for (int j = 1; j <= width; j++) {
				if (board[i][j] != CELL.EMPTY && board[i][j] != CELL.OBSTACLE) {
					board[i][j] = CELL.EMPTY;
				}
			}
		}

		// 棋盘上填上身体
		for (int who = 0; who < 2; who++) {
			for (Point pt : bodies[who]) {
				// UI有关要移走。。
				if (pt.x < 1 || pt.y < 1 || pt.x > height || pt.y > width) {
					stop = true;
					JOptionPane.showMessageDialog(null, "游戏结束!", "游戏结束",
							JOptionPane.OK_OPTION);
				} else if (board[pt.x][pt.y] != CELL.EMPTY) {
					System.out.println(board[pt.x][pt.y]);
					stop = true;
					JOptionPane.showMessageDialog(null, "游戏结束!", "游戏结束",
							JOptionPane.OK_OPTION);
				} else {
					if (who == 0) {
						board[pt.x][pt.y] = CELL.GREEN_BODY;
					} else {
						board[pt.x][pt.y] = CELL.RED_BODY;
					}
				}
			}
		}

		// 蛇头赋值
		board[X[0]][Y[0]] = CELL.GREEN_HEAD;
		board[X[1]][Y[1]] = CELL.RED_HEAD;
		// 蛇尾复制
	}

	public void InitGame() {
		pause = true;
		stop = true;
		round = 1;

		X = new int[2];
		Y = new int[2];

		moves = new List[2];
		moves[0] = new ArrayList<Object>();
		moves[1] = new ArrayList<Object>();

		obstacles = new ArrayList<Obstacle>();

		/*
		 * height-[10,13] width-[10,17] n+m奇数
		 */

		width = 0;
		height = 0;
		while ((width + height) % 2 == 0) {
			width = Util.Random(10, 18);
			height = Util.Random(10, 14);
		}

		bodies = new List[2];
		bodies[0] = new ArrayList<Point>();
		bodies[0].add(new Point(1, 1));
		bodies[1] = new ArrayList<Point>();
		bodies[1].add(new Point(height, width));

		X[0] = 1;
		Y[0] = 1;
		X[1] = height;
		Y[1] = width;
		board = new CELL[20][20];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				board[i][j] = CELL.EMPTY;
			}
		}
		board[1][1] = CELL.GREEN_BODY;
		board[height][width] = CELL.RED_BODY;

		// 随机障碍物
		if (setting.hasObstacle) {
			int num = height * width / 16;
			for (int i = 0; i < num; i++) {
				int x = Util.Random(1, height + 1);
				int y = Util.Random(1, width + 1);
				if (board[x][y] == CELL.EMPTY
						&& board[height - x + 1][width - y + 1] == CELL.EMPTY)
					board[x][y] = board[height - x + 1][width - y + 1] = CELL.OBSTACLE;
			}
		}

		// 填进list
		for (int i = 1; i <= height; i++) {
			for (int j = 1; j <= width; j++) {
				if (board[i][j] == CELL.OBSTACLE) {
					obstacles.add(new Obstacle(j, i));
				}
			}
		}

		// 造围墙
		for (int i = 0; i <= height + 1; i++) {
			board[i][0] = board[i][width + 1] = CELL.OBSTACLE;
		}
		for (int i = 0; i <= width + 1; i++) {
			board[0][i] = board[height + 1][i] = CELL.OBSTACLE;
		}
	}

	public String OutPut(int who) {
		GameInfo info = new GameInfo();
		// 由于PKU反人类的设定。。
		info.setHeight(width);
		info.setWidth(height);
		info.setX(who == 0 ? 1 : height);
		info.setY(who == 0 ? 1 : width);
		info.setObstacle(obstacles);
		List<Object> infos = new ArrayList<Object>();
		infos.add(info);
		int op = who == 0 ? 1 : 0;
		infos.addAll(moves[op]);
		Output output = new Output();
		output.setRequests(infos);

		output.setResponses((List<Object>) moves[who]);

		String json = JSON.toJSONString(output);
		return json;
	}
}
