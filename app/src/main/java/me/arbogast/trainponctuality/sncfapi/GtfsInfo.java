package me.arbogast.trainponctuality.sncfapi;

import java.util.Date;

/**
 * Created by excelsior on 18/01/17.
 * This contains data from GTFS SNCF API
 */

class GtfsInfo {
    private String fileName;
    private String fileId;
    private Date lastUpdate;

    String getFileName() {
        return fileName;
    }

    void setFileName(String fileName) {
        this.fileName = fileName;
    }

    String getFileId() {
        return fileId;
    }

    void setFileId(String fileId) {
        this.fileId = fileId;
    }

    Date getLastUpdate() {
        return lastUpdate;
    }

    void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
