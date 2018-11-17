package community;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model.userCommunity;
import com.service.goodsService;

public class k_means聚类算法 {

	/**
	 * 这里设置了族中心数量
	 */
	public static int centerPointCount = 6;

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static Map<Integer, Map<Integer, Integer>> getMapResult(File file) {
		/**
		 * 声明读取结果；
		 */
		Map<Integer, Map<Integer, Integer>> mapResult = new HashMap<Integer, Map<Integer, Integer>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((null != (s = br.readLine()))) {// 使用readLine方法，一次读一行
				String[] value3 = s.split(",");
				/**
				 * userID
				 */
				int userID = Integer.parseInt(value3[0]);
				/**
				 * 商品ID
				 */

				int goodsID = Integer.parseInt(value3[1]);
				/**
				 * 评分
				 */
				int score = Integer.parseInt(value3[2]);
				/**
				 * 不存在用户映射
				 */
				if (!mapResult.containsKey(userID)) {
					Map<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
					scoreMap.put(goodsID, score);
					mapResult.put(userID, scoreMap);
				}
				/**
				 * 如果存在userID记录；
				 */
				else {
					/**
					 * 读取scoreMap
					 */
					Map<Integer, Integer> scoreMap = mapResult.get(userID);
					scoreMap.put(goodsID, score);
					mapResult.put(userID, scoreMap);
				}

			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapResult;
	}

	public static void main(String[] args) {
		File file = new File("fileSource/goods_preferences.txt");
		Map<Integer, Map<Integer, Integer>> mapResult = getMapResult(file);
		for (Integer id : mapResult.keySet()) {
			Map<Integer, Integer> scoreMap = mapResult.get(id);
//			System.out.println("当前ID是：" + id);
			for (Integer goods : scoreMap.keySet()) {
				Integer score = scoreMap.get(goods);
//				System.out.print("商品是：" + goods + "  ");
//				System.out.println("评分是：" + score);
			}
		}

		excutor(mapResult);
	}

	/**
	 * 点类(x,y) 表示点；
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午12:59:12
	 * @Description:TODO
	 * @param:
	 * @throws:
	 */
	static class ScorePoint {
		private double x;
		private double y;

		/**
		 * @param x
		 * @param y
		 */
		public ScorePoint(double x, double y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * @return the x
		 */
		public double getX() {
			return x;
		}

		/**
		 * @param x
		 *            the x to set
		 */
		public void setX(double x) {
			this.x = x;
		}

		/**
		 * @return the y
		 */
		public double getY() {
			return y;
		}

		/**
		 * @param y
		 *            the y to set
		 */
		public void setY(double y) {
			this.y = y;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ScorePoint [x=" + x + ", y=" + y + "]";
		}

	}

	/**
	 * 用户评分类
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午1:32:11
	 * @Description:TODO
	 * @param:
	 * @throws:
	 */
	static class UserScorePoint {
		private Integer userID;
		private ScorePoint point;

		/**
		 * @param userID
		 * @param point
		 */
		public UserScorePoint(Integer userID, ScorePoint point) {
			this.userID = userID;
			this.point = point;
		}

		/**
		 * @return the userID
		 */
		public Integer getUserID() {
			return userID;
		}

		/**
		 * @param userID
		 *            the userID to set
		 */
		public void setUserID(Integer userID) {
			this.userID = userID;
		}

		/**
		 * @return the point
		 */
		public ScorePoint getPoint() {
			return point;
		}

		/**
		 * @param point
		 *            the point to set
		 */
		public void setPoint(ScorePoint point) {
			this.point = point;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "UserScorePoint [userID=" + userID + ", point=" + point + "]";
		}

	}

	/**
	 * 返回两点距离
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午12:51:51
	 * @Description:TODO
	 * @param:@param a(x,y)
	 * @param:@param b(x,y)
	 * @param:@return
	 * @return:double
	 * @throws:
	 */
	public static double distScorePoint(ScorePoint a, ScorePoint b) {

		return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
	}

	/**
	 * 执行入口
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午1:43:35
	 * @Description:TODO
	 * @param:@param mapResult
	 * @return:void
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	public static void excutor(Map<Integer, Map<Integer, Integer>> mapResult) {
		List<UserScorePoint> pointList = new ArrayList<UserScorePoint>();
		for (Integer id : mapResult.keySet()) {
			Map<Integer, Integer> scoreMap = mapResult.get(id);
//			System.out.println("当前ID是：" + id);
			for (Integer goods : scoreMap.keySet()) {
				Integer score = scoreMap.get(goods);
				UserScorePoint point = new UserScorePoint(id, new ScorePoint(goods, score));
				pointList.add(point);
//				System.out.print("商品是：" + goods + "  ");
//				System.out.println("评分是：" + score);
			}
		}
		/**
		 * 族中心列表 关键点；
		 */
		List<UserScorePoint> centerPointList = new ArrayList<UserScorePoint>();
		/**
		 * 点与族中心Map
		 */

		int num = 0;//控制循环次数 的值，无关紧要
		/**
		 * 重新声明参数个族心数量个List
		 */

		List<UserScorePoint>[] centerList = new List[centerPointCount];
		while (true) {
			Map<UserScorePoint, UserScorePoint> pointCenterMap = 迭代函数(pointList, centerPointList);
			// 第一次迭代结束
			/**
			 * 接下来都是去计算新的族心
			 */
			/**
			 * 重新声明参数个族心数量个List
			 */
			centerList=new List[centerPointCount];
			for (int n = 0; n < centerPointCount; ++n) {
				centerList[n] = new ArrayList<UserScorePoint>();
			}
			/**
			 * 重新计算族心列表
			 */
			for (UserScorePoint key : pointCenterMap.keySet()) {
				/**
				 * 遍历族心
				 */
				for (int m = 0; m < centerPointList.size(); ++m) {
					/**
					 * 如果有这个族心
					 */
					if (centerPointList.get(m).toString().equals(pointCenterMap.get(key).toString())) {
						
						/**
						 * 向这个族心list中添加点；
						 */
						centerList[m].add(key);
					}
				}

			}

			// 计算完成

			/**
			 * 重新找各个列表中的族心 并返回前后族中心的最小距离
			 */
			double minDist = Integer.MAX_VALUE;
			/**
			 * 获取和删除只需操作0下标。因为往前移动了下标
			 */
			for (int k = 0; k < centerPointCount; ++k) {
				double dist = distScorePoint(centerPointList.get(0).getPoint(),
						getCenterPoint(centerList[k]).getPoint());
				if (minDist > dist) {
					minDist = dist;
				}
				centerPointList.remove(0);
				centerPointList.add(getCenterPoint(centerList[k]));
				/**
				 * 这里需要计算前后族中心移动的最小距离；
				 */
			}
			System.out.println("三个新的族中心是：" + Arrays.toString(centerPointList.toArray()));
			System.out.println("最小距离：" + minDist);
			for (int l = 0; l < centerPointCount; ++l) {
				System.out.println("社群"+(l+1)+"族中心是："+centerPointList.get(l)+"该社群的成员数量："+centerList[l].size());
				
//						+"族中点包含："+Arrays.toString(centerList[l].toArray()));
	
			}
			
			
			/**
			 * 这里控制前后移动的最小阀值
			 */
			 if (minDist < 0.1) {

				break;
			/**
			 * 重新迭代
			 */
				

			 }
		}
		
//		数据层操作
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:spring.xml", "classpath:spring-mybatis.xml" });
		context.setValidating(false);
		context.refresh();
		goodsService userService = context.getBean(goodsService.class);
		for (int n = 0; n < centerPointCount; ++n) {

		for(UserScorePoint userscore:centerList[n]) {
			
			userCommunity userCommunity=new userCommunity();
			userCommunity.setUserId(userscore.getUserID());
			userCommunity.setCommunityId(n);
			userService.insertSelective(userCommunity);
		}
		
		}
	}

	/**
	 * 需要所有point 以及族中心list
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午3:14:09
	 * @Description:TODO
	 * @param:@param pointList
	 * @param:@param centerPointList
	 * @param:@return
	 * @return:Map<UserScorePoint,UserScorePoint>
	 * @throws:
	 */
	public static Map<UserScorePoint, UserScorePoint> 迭代函数(List<UserScorePoint> pointList,
			List<UserScorePoint> centerPointList) {
		/**
		 * 
		 * centerPointList可能为空；
		 */
		Map<UserScorePoint, UserScorePoint> pointCenterMap = new HashMap<UserScorePoint, UserScorePoint>();
		/**
		 * 已经初始化了族心 就往Map自动添加进去
		 */
		if (!centerPointList.isEmpty()) {
			for (UserScorePoint point : centerPointList) {
				pointCenterMap.put(point, point);
			}
			/**
			 * 在列表中去掉族中心集合
			 */
			pointList.removeAll(centerPointList);
		}
		for (int i = centerPointList.size(); i < pointList.size(); ++i) {
			/**
			 * 添加族中心列表
			 */
			if (centerPointCount > i) {
				centerPointList.add(pointList.get(i));
				/**
				 * 族心自动添加到族
				 */
				pointCenterMap.put(pointList.get(i), pointList.get(i));
			}

			/**
			 * 继续遍历 找族中心；
			 */
			else {
				UserScorePoint centerPoint = findWhichPoint(pointList.get(i), centerPointList);
				/**
				 * 向Map添加族心信息；
				 */
				pointCenterMap.put(pointList.get(i), centerPoint);
			}

		}
		
		return pointCenterMap;

	}

	/**
	 * 找属于哪个族中心
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午2:59:11
	 * @Description:TODO
	 * @param:@param userPoint
	 * @param:@param centerPointList
	 * @param:@return
	 * @return:UserScorePoint
	 * @throws:
	 */
	public static UserScorePoint findWhichPoint(UserScorePoint userPoint, List<UserScorePoint> centerPointList) {
		/**
		 * 初始化最小距离及中心族
		 */
		double minDist = Integer.MAX_VALUE;
		UserScorePoint center = userPoint;
		for (UserScorePoint centerPoint : centerPointList) {
			/**
			 * 如果小于最小距离，替换族中心,并且替换最小距离(这里注意，之前 遇到了bug)
			 */
			if (minDist > distScorePoint(userPoint.getPoint(), centerPoint.getPoint())) {
				minDist = distScorePoint(userPoint.getPoint(), centerPoint.getPoint());
				center = centerPoint;
			}
		}
		return center;
	}

	/**
	 * 根据列表算最新的族中心
	 * 
	 * @author:Yien
	 * @when:2018年5月20日下午2:58:33
	 * @Description:TODO
	 * @param:@param list
	 * @param:@return
	 * @return:UserScorePoint
	 * @throws:
	 */
	public static UserScorePoint getCenterPoint(List<UserScorePoint> list) {
		/**
		 * 初始化族心点；
		 */
		double x = 0;
		double y = 0;
		/**
		 * x,y的和
		 */
		double sumX = 0;
		double sumY = 0;
		/**
		 * 算出和
		 */
		for (UserScorePoint point : list) {
			sumX += point.getPoint().getX();
			sumY += point.getPoint().getY();
		}
		/**
		 * 算出中心族坐标
		 */

		// System.out.println(sumX);
		// System.out.println(sumY);
		// System.out.println(list.size());

		x = sumX / list.size();
		y = sumY / list.size();
		/**
		 * 返回最新的族中心
		 */
		return new UserScorePoint(0, new ScorePoint(x, y));

	}
}
