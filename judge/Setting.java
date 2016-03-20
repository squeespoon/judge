package judge;

/*
 * 单例类，游戏设置
 */
public class Setting {
	
	//障碍物数量
	public boolean hasObstacle = true;
	
	
	private static final Setting setting = new Setting();
	
	private Setting(){	
	}
	
	public static Setting getInstance(){
		return setting;
	}
	
}
