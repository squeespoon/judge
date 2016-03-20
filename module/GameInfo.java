package module;

import java.util.List;

public class GameInfo {
	//出生点坐标
	private int x;
	private int y;
	//棋盘宽高
	private int width;
	private int height;
	//障碍物
	private List<Obstacle> obstacle;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<Obstacle> getObstacle() {
		return obstacle;
	}

	public void setObstacle(List<Obstacle> obstacle) {
		this.obstacle = obstacle;
	}

	
}
