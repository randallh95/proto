/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Entity;

/**
 *
 * @author Bernice
 */
public class Location {
    private long locationId;
    private String semanticPlace;

    public Location() {
        
    }
    
    public Location(long locationId, String semanticPlace) {
        this.locationId = locationId;
        this.semanticPlace = semanticPlace;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getSemanticPlace() {
        return semanticPlace;
    }

    public void setSemanticPlace(String semanticPlace) {
        this.semanticPlace = semanticPlace;
    }

    @Override
    public String toString() {
        return "Location{" + "locationId=" + locationId + ", semanticPlace=" + semanticPlace + '}';
    }
}
