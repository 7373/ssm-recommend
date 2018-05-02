package recommend;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;

//import com.rcd.model.MyDataModel;

import java.io.File;
import java.util.*;
/**
 * 基于用户推荐
 * @author:Yien
 * @when:2018年3月30日下午6:17:03
 * @Description:TODO
 * @param:
 */
public class MyUserBasedRecommender {
	public List<RecommendedItem> userBasedRecommender(long userID,int size) {
		// step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
		List<RecommendedItem> recommendations = null;
		try {
			/**
			 * 构造数据模型--- Dao操作
			 */
//			DataModel model = MyDataModel.myDataModel();
			String fileURI="D://Users//rah//workspace-EE//ssm-mahout//fileSource//goods_preferences.txt";
			DataModel model = new FileDataModel(new File(fileURI));
			/**
			 * 用PearsonCorrelation 算法计算用户相似度
			 */
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			/**
			 * 计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
			 * 计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
			 */
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
			/**
			 * 采用 CachingRecommender 为 RecommendationItem 进行缓存
			 */
			Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(model, neighborhood, similarity));
			/**
			 * 得到推荐的结果，size是推荐结果的数目
			 */
			recommendations = recommender.recommend(userID, size);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return recommendations;
	}

	
}