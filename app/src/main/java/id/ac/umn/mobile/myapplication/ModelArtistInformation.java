package id.ac.umn.mobile.myapplication;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Oktavius Wiguna on 10/05/2018.
 */

public class ModelArtistInformation{
    private String id;
    private String name;
    private String email;
    private String desc;
    private String fbLink;
    private String twitterLink;
    private List<String> categories;
    private Integer totalCompletedCommission;

    public ModelArtistInformation(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public ModelArtistInformation(String id, String name, String email, String desc, String fbLink, String twitterLink, Integer totalCompletedCommission) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.desc = desc;
        this.fbLink = fbLink;
        this.twitterLink = twitterLink;
        this.totalCompletedCommission = totalCompletedCommission;
    }

    public ModelArtistInformation(String id, String name, String email, String desc, String fbLink, String twitterLink, List<String> categories, Integer totalCompletedCommission) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.desc = desc;
        this.fbLink = fbLink;
        this.twitterLink = twitterLink;
        this.categories = categories;
        this.totalCompletedCommission = totalCompletedCommission;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDesc() {
        return desc;
    }

    public String getFbLink() {
        return fbLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Integer getTotalCompletedCommission() {
        return totalCompletedCommission;
    }
}
