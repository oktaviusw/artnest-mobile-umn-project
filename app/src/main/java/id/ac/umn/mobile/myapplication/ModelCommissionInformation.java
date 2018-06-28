package id.ac.umn.mobile.myapplication;

import java.util.Date;

/**
 * Created by Oktavius Wiguna on 11/05/2018.
 */

public class ModelCommissionInformation {
    private String idCommission;
    private String idCustomer;
    private String nameCustomer;
    private String idArtist;
    private String nameArtist;
    private String statusRequest;
    private String statusProject;
    private int tokenValue;
    private String titleCommission;
    private Date startDate;
    private Date deadlineDate;
    private boolean sketchBaseAvailable;

    public ModelCommissionInformation(String idCommission, String nameArtist, String statusRequest, String titleCommission, Date startDate, Date deadlineDate, boolean sketchBaseAvailable) {
        this.idCommission = idCommission;
        this.nameArtist = nameArtist;
        this.statusRequest = statusRequest;
        this.titleCommission = titleCommission;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
        this.sketchBaseAvailable = sketchBaseAvailable;
    }

    public ModelCommissionInformation(String idCommission, String nameArtist, String statusRequest, String statusProject, String titleCommission, Date startDate, Date deadlineDate, boolean sketchBaseAvailable) {
        this.idCommission = idCommission;
        this.nameArtist = nameArtist;
        this.statusRequest = statusRequest;
        this.statusProject = statusProject;
        this.titleCommission = titleCommission;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
        this.sketchBaseAvailable = sketchBaseAvailable;
    }

    public ModelCommissionInformation(String idCommission, String nameArtist, String statusRequest, String statusProject, String titleCommission, Date startDate, Date deadlineDate) {
        this.idCommission = idCommission;
        this.nameArtist = nameArtist;
        this.statusRequest = statusRequest;
        this.statusProject = statusProject;
        this.titleCommission = titleCommission;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
    }

    public ModelCommissionInformation(String idCommission, String idCustomer, String nameCustomer, String idArtist, String nameArtist, String statusRequest, String statusProject, int tokenValue, String titleCommission, Date startDate, Date deadlineDate) {
        this.idCommission = idCommission;
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.idArtist = idArtist;
        this.nameArtist = nameArtist;
        this.statusRequest = statusRequest;
        this.statusProject = statusProject;
        this.tokenValue = tokenValue;
        this.titleCommission = titleCommission;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
    }

    public ModelCommissionInformation(String idCommission, String idCustomer, String nameCustomer, String idArtist, String nameArtist, int tokenValue, String titleCommission, Date startDate, Date deadlineDate) {
        this.idCommission = idCommission;
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.idArtist = idArtist;
        this.nameArtist = nameArtist;
        this.tokenValue = tokenValue;
        this.titleCommission = titleCommission;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
    }

    public String getIdCommission() {
        return idCommission;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public String getStatusRequest() {
        return statusRequest;
    }

    public String getStatusProject() {
        return statusProject;
    }

    public int getTokenValue() {
        return tokenValue;
    }

    public String getTitleCommission() {
        return titleCommission;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public boolean isSketchBaseAvailable() {
        return sketchBaseAvailable;
    }
}
