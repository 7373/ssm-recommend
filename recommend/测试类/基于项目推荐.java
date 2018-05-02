package recommend.测试类;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model.goods;
import com.service.goodsService;

import recommend.MyItemBasedRecommender;
import recommend.RecommendedByDB;

public class 基于项目推荐 {

	@Test
	public void testItem() {
		/**
		 * 设置用户ID、和推荐的数量
		 */
		long userID = 5;
		int size = 100;
		// 推荐电影的List
		List<RecommendedItem> recommendation = null;
		recommendation = new MyItemBasedRecommender().myItemBasedRecommender(userID, size);
		
		/**
		 * 这里创建一个service;
		 * 
		 * 
		 */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:spring.xml", "classpath:spring-mybatis.xml" });
		context.setValidating(false);
		context.refresh();
		goodsService userService = context.getBean(goodsService.class);

		/**
		 * 声明结果list
		 */
		ArrayList<goods> goodsList = new ArrayList<goods>();
		/**
		 * 进行数据库查询操作
		 */
		goodsList=new RecommendedByDB().getGoodsByRecommendedItem(userService, recommendation);
		
		
		/**
		 * 打印推荐结果
		 * 
		 */
		
		for(int i=0;i<goodsList.size();++i) {
			
			System.out.print("结果"+i+":");
			System.out.print("数据库ID"+goodsList.get(i).getId());

			System.out.print("关键词"+goodsList.get(i).getGoodsIntro());
			System.out.print("名字"+goodsList.get(i).getGoodsShortTitle());
			System.out.print("价格"+goodsList.get(i).getGoodsPrice());
			System.out.println("id"+goodsList.get(i).getGoodsId());


			
		}
	}
}
