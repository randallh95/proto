/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Entity;

import java.util.Date;

/**
 *
 * @author Bernice
 */
public class LocationReport {
    private long locationId;
    private String macAddress;
    private Date timestamp;

    public LocationReport(long locationId, String macAddress, Date timestamp) {
        this.locationId = locationId;
        this.macAddress = macAddress;
        this.timestamp = timestamp;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LocationReport{" + "locationId=" + locationId + ", macAddress=" + macAddress + ", timestamp=" + timestamp + '}';
    }
}
