package com.app.pfh.zhihudaily.model;


import java.util.List;

/**
 * {
 * body: "<div class="main-wrap content-wrap">...</div>",
 * image_source: "Yestone.com 版权图片库",
 * title: "深夜惊奇 · 朋友圈错觉",
 * image: "http://pic3.zhimg.com/2d41a1d1ebf37fb699795e78db76b5c2.jpg",
 * share_url: "http://daily.zhihu.com/story/4772126",
 * js: [ ],
 * recommenders": [
 * { "avatar": "http://pic2.zhimg.com/fcb7039c1_m.jpg" },
 * { "avatar": "http://pic1.zhimg.com/29191527c_m.jpg" },
 * { "avatar": "http://pic4.zhimg.com/e6637a38d22475432c76e6c9e46336fb_m.jpg" },
 * { "avatar": "http://pic1.zhimg.com/bd751e76463e94aa10c7ed2529738314_m.jpg" },
 * { "avatar": "http://pic1.zhimg.com/4766e0648_m.jpg" }
 * ],
 * ga_prefix: "050615",
 * section": {
 * "thumbnail": "http://pic4.zhimg.com/6a1ddebda9e8899811c4c169b92c35b3.jpg",
 * "id": 1,
 * "name": "深夜惊奇"
 * },
 * type: 0,
 * id: 4772126,
 * css: [
 * "http://news.at.zhihu.com/css/news_qa.auto.css?v=1edab"
 * ]
 * }
 */

public class Content {
    private int id;
    private List<RecommendersEntity> recommenders;
    private String body;
    private String title;
    private String ga_prefix;
    private String share_url;
    private String image;
    private int type;
    private List<String> css;
    private String image_source;

    public void setId(int id) {
        this.id = id;
    }

    public void setRecommenders(List<RecommendersEntity> recommenders) {
        this.recommenders = recommenders;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public int getId() {
        return id;
    }

    public List<RecommendersEntity> getRecommenders() {
        return recommenders;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public String getShare_url() {
        return share_url;
    }


    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public List<String> getCss() {
        return css;
    }

    public String getImage_source() {
        return image_source;
    }

    public static class RecommendersEntity {
        /**
         * avatar : http://pic3.zhimg.com/bbb689a7a_m.jpg
         */
        private String avatar;

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
