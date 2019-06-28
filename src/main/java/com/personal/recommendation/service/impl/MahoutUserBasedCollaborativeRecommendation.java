package com.personal.recommendation.service.impl;

import com.personal.recommendation.constants.RecommendationConstants;
import com.personal.recommendation.constants.RecommendationEnum;
import com.personal.recommendation.component.mahout.service.impl.MyGenericUserBasedRecommender;
import com.personal.recommendation.component.mahout.service.MyRecommender;
import com.personal.recommendation.model.Users;
import com.personal.recommendation.service.RecommendationAlgorithmFactory;
import com.personal.recommendation.service.RecommendationAlgorithmService;
import com.personal.recommendation.service.RecommendationCalculator;
import com.personal.recommendation.utils.DBConnectionUtil;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 基于协同过滤推荐实现类
 */
@Service
public class MahoutUserBasedCollaborativeRecommendation implements RecommendationAlgorithmService {

    private static final Logger logger = Logger.getLogger(MahoutUserBasedCollaborativeRecommendation.class);

    @PostConstruct
    void init() {
        RecommendationAlgorithmFactory.addHandler(RecommendationEnum.CF.getCode(), this);
    }

    /**
     * 给特定的一批用户进行新闻推荐
     */
    @Override
    public Set<Long> recommend(Users user, int recNum, List<Long> recommendedNews, List<Long> browsedNews) {
        long start = new Date().getTime();
        Long userId = user.getId();
        logger.info(RecommendationEnum.CF.getDesc() + " start at " + start + ", userId : " + userId);
        logger.info("Recommended data not enough, " + RecommendationEnum.CF.getDesc() + " need fetch " + recNum);

        // 保存推荐结果
        Set<Long> toBeRecommended = new HashSet<>();

        try {
            logger.info("CF start at " + new Date());

            MySQLBooleanPrefJDBCDataModel dataModel = new DBConnectionUtil().getMySQLJDBCDataModel();

            // 构造相似度度量器
            UserSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
            // 构造近邻查找
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(RecommendationConstants.N, similarity, dataModel);
            // 构造推荐器
            MyRecommender recommender = new MyGenericUserBasedRecommender(dataModel, neighborhood, similarity);
            // 获取推荐结果
            List<RecommendedItem> recItems = recommender.recommend(userId, RecommendationConstants.N);

            for (RecommendedItem recItem : recItems) {
                toBeRecommended.add(recItem.getItemID());
                logger.info("CF found similar user, id : " + recItem.getItemID() + ", value : " + recItem.getValue());
            }

            toBeRecommended = RecommendationCalculator.resultHandle(recommendedNews, browsedNews, toBeRecommended,
                    userId, RecommendationEnum.CF.getDesc(), recNum, false);

        } catch (TasteException e) {
            logger.error("CF算法构造偏好对象失败！");
            e.printStackTrace();

        } catch (Exception e) {
            logger.error("CF算法数据库操作失败！");
            e.printStackTrace();

        }

        long end = new Date().getTime();
        if(!toBeRecommended.isEmpty())
            logger.info("CF has contributed " + toBeRecommended.size() + " recommending news on average");
        logger.info("CF finished at " + end + ", time cost : " + (double) ((end - start) / 1000) + "s .");

        return toBeRecommended;

    }

}
