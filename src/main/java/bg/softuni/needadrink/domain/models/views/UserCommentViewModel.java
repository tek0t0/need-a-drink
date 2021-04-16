package bg.softuni.needadrink.domain.models.views;

public class UserCommentViewModel {

    private String fullName;

    private String imgUrl;


    public String getFullName() {
        return fullName;
    }

    public UserCommentViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public UserCommentViewModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
