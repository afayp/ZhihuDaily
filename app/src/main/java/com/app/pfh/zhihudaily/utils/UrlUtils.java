package com.app.pfh.zhihudaily.utils;


public class UrlUtils {

    //暂时先之获取指定分辨率的图片，改进：根据手机获取合适分辨率的图片
    public static String START_IMAGE_URL = "http://news-at.zhihu.com/api/4/start-image/1080*1776";
    //主题列表
    public static String THEME_URL="http://news-at.zhihu.com/api/4/themes";
    //今日热闻
    public static String LATEST_URL ="http://news-at.zhihu.com/api/4/news/latest";
    //若果需要查询 11 月 18 日的消息，before 后的数字应为 20131119
    public static String BEFORE_URL = "http://news.at.zhihu.com/api/4/news/before/";

    /**主题日报内容，需加上对应的id
     * 1--不存在
     * 2--开始游戏
     * 3--电影日报
     * 4--设计日报
     * 5--大公司日报
     * 6--财经日报
     * 7--音乐日报
     * 8--体育日报
     * 9--动漫日报
     * 10--互联网安全
     * 11--不许无聊
     * 12--用户推荐日报
     * 13--日常心理学
     */
    public static String THEME_CONTENT_URL = "http://news-at.zhihu.com/api/4/theme/";

    //消息内容，后面拼接id（今日热闻和主题列表都是这个url）
    public static String NEWS_CONTENT_URL = "http://news-at.zhihu.com/api/4/news/";

}
