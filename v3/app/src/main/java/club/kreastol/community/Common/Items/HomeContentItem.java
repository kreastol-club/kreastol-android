package club.kreastol.community.Common.Items;

public class HomeContentItem {

    private String id;
    private String imgUrl;

    private String title;
    private String body;
    private String date;
    private String category;
    private String poster;


    public String getTitle() { return title; }

    public String getBody() { return body; }

    public String getDate() { return date; }

    public String getCategory() { return category; }

    public String getPoster() { return poster; }


    public String getId() {
        return id;
    }

    public void setId(String imgUrl) {
        this.imgUrl = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void HomeContentItemPost (String title, String body, String date, String category, String poster){
        this.title = title;
        this.body = body;
        this.date = date;
        this.category = category;
        this.poster = poster;
    }



    public void HomeContentItemImage(String id, String imgUrl, String title) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.title = title;
    }

}
