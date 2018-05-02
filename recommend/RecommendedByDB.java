package recommend;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.model.goods;
import com.service.goodsService;

public class RecommendedByDB {

	// 根据推荐的的ID，获得的详细信息
	public ArrayList<goods> getGoodsByRecommendedItem(goodsService service, List<RecommendedItem> recommendations) {
		ArrayList<goods> goodsList = new ArrayList<goods>();

		/**
		 * 获取一个service
		 */

		for (RecommendedItem item : recommendations) {
			/**
			 * 这里可能会向下溢出，先把long转换成字符串String，然后在转行成Integer
			 */
			goods gs = service.selectByPrimaryKey(Integer.parseInt(String.valueOf(item.getItemID())));
			System.out.println("相似值:" + item.getValue());
			goodsList.add(gs);

		}

		return goodsList;
	}
}
