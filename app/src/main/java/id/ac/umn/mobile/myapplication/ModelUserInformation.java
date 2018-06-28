package id.ac.umn.mobile.myapplication;

public class ModelUserInformation {
    private String userID;
    private String userName;
    private String userEmail;
    private int userToken;
    private boolean isArtist;

    public ModelUserInformation(String userID, String userName, String userEmail, int userToken, boolean isArtist) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userToken = userToken;
        this.isArtist = isArtist;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserToken() {
        return userToken;
    }

    public boolean isArtist() {
        return isArtist;
    }
}
