package com.personal.recommendation.manager;

import com.personal.recommendation.dao.NewsLogsDAO;
import com.personal.recommendation.model.NewsLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新闻日志Manager
 */
@Service
public class NewsLogsManager {

    private final NewsLogsDAO newsLogsDAO;

    @Autowired
    public NewsLogsManager(NewsLogsDAO newsLogsDAO) {
        this.newsLogsDAO = newsLogsDAO;
    }

    public List<NewsLogs> getHotNews(Date hotDateTime) {
        return newsLogsDAO.getHotNews(hotDateTime);
    }

    public List<Long> getNewsIdByUserId(Long userId) {
        return newsLogsDAO.getNewsIdByUserId(userId);
    }

    @SuppressWarnings("unused")
    public List<NewsLogs> getAll() {
        return newsLogsDAO.getAll();
    }

    public List<NewsLogs> getNewsLogsByUsersViewTime(Date viewTime, List<Long> userList) {
        List<NewsLogs> list = new ArrayList<>();
        for(Long userId : userList){
            list.addAll(newsLogsDAO.getNewsLogsByUserViewTime(viewTime, userId));
        }
        return list;
    }

    public void updateViewTime(Date viewTime) {
        newsLogsDAO.updateViewTime(viewTime);
    }

    public List<NewsLogs> getNewsByUsers(List<Long> userIds) {
        List<NewsLogs> list = new ArrayList<>();
        for (Long userId : userIds) {
            list.addAll(newsLogsDAO.getNewsByUserId(userId));
        }
        return list;
    }

    @SuppressWarnings("unused")
    public List<NewsLogs> getNewsByUserId(Long userId) {
        return newsLogsDAO.getNewsByUserId(userId);
    }

    public void insertNewsLogs(NewsLogs newsLog){
        newsLogsDAO.insertNewsLogs(newsLog);
    }

}
