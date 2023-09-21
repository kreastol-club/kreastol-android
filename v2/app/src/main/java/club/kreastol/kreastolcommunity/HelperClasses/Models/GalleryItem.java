package club.kreastol.kreastolcommunity.HelperClasses.Models;

public class GalleryItem {

    private String id;
    private String imgUrl;
    private String title;


    public GalleryItem(String id, String imgUrl, String title) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.title = title;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
