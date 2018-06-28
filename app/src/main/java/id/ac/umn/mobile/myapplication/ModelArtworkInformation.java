package id.ac.umn.mobile.myapplication;

import java.io.Serializable;

/**
 * Created by Oktavius Wiguna on 11/05/2018.
 */

public class ModelArtworkInformation implements Serializable {
    public String id;
    public String titleArtwork;
    public String descArtwork;
    public String idArtist;
    public String artistName;
    public String emailArtist;
    public String directoryData;

    public ModelArtworkInformation(String id, String titleArtwork, String idArtist, String artistName, String emailArtist,String directoryData) {
        this.id = id;
        this.titleArtwork = titleArtwork;
        this.idArtist = idArtist;
        this.artistName = artistName;
        this.emailArtist = emailArtist;
        this.directoryData = directoryData;
    }

    public ModelArtworkInformation(String id, String titleArtwork, String descArtwork, String idArtist, String artistName, String emailArtist, String directoryData) {
        this.id = id;
        this.titleArtwork = titleArtwork;
        this.descArtwork = descArtwork;
        this.idArtist = idArtist;
        this.artistName = artistName;
        this.emailArtist = emailArtist;
        this.directoryData = directoryData;
    }

    public String getId() {
        return id;
    }

    public String getTitleArtwork() {
        return titleArtwork;
    }

    public String getDescArtwork() {
        return descArtwork;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getEmailArtist() {
        return emailArtist;
    }

    public String getDirectoryData() {
        return directoryData;
    }
}
