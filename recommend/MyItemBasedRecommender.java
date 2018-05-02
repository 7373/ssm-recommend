package recommend;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
/**
 * 基于项目推荐
 * @author:Yien
 * @when:2018年3月30日下午7:26:32
 * @Description:TODO
 * @param:
 */
public class MyItemBasedRecommender {
	
	public List<RecommendedItem> myItemBasedRecommender(long userID,int size){
		List<RecommendedItem> recommendations = null;
		try {
			String fileURI="D://Users//rah//workspace-EE//ssm-mahout//fileSource//goods_preferences.txt";
			DataModel model = new FileDataModel(new File(fileURI));//构造数据模型
			//计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
			//构造推荐引擎  
			Recommender recommender = new GenericItemBasedRecommender(model, similarity);
			//得到推荐结果
			recommendations = recommender.recommend(userID, size);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return recommendations;
	}

}
