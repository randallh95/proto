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
public class Interval {
    private long locationId;
    private Date startTime;
    private Date endTime;
    private long duration;
    
    public Interval(){
        
    }
    
    public Interval(long locationId, Date startTime, Date endTime){
        this.locationId = locationId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public Interval overlap(Interval inputInterval){
        Date tempStartTime = new Date();
        Date tempEndTime = new Date();
        Interval interval = null;

        if(locationId == inputInterval.locationId){
            //if startTime of interval 1 is < inputInterval.endTime (before)
            //if endTime of interval 1 is > inputInterval.endTime (after)
            //if endTime of interval 1 is = inputInterval.endTime (equals)
            //S1 E2-E1/E1
            if(startTime.before(inputInterval.endTime) && (endTime.after(inputInterval.endTime) || endTime.equals(inputInterval.endTime))){
                //if startTime of interval 1 is < inputInterval.startTime (before)
                //if startTime of interval 1 is = inputInterval.startTime (equals)
                //S2/S1-S2 E2-E1 E1
                if(startTime.before(inputInterval.startTime) || startTime.equals(inputInterval.startTime)){
                    tempStartTime = inputInterval.startTime;
                    tempEndTime = inputInterval.endTime;
                }
                //if startTime of interval 1 is > inputInterval.startTime (after)
                //S1 S2 E2-E1 E1
                else{
                    tempStartTime = startTime;
                    tempEndTime = inputInterval.endTime;
                }
            }
            //if startTime of interval 1 is < inputInterval.endTime (before)
            //if endTime of interval 1 is < inputInterval.endTime (before)
            //S1 E1 E2
            else if(startTime.before(inputInterval.endTime) && endTime.before(inputInterval.endTime)){
                //if startTime of interval 1 is < inputInterval.startTime (before)
                //if startTime of interval 1 is = inputInterval.startTime (equals)
                //S1-S2/S2 E1 E2
                if(startTime.before(inputInterval.startTime) || startTime.equals(inputInterval.startTime)){
                    tempStartTime = inputInterval.startTime;
                    tempEndTime = endTime;
                }
                //if startTime of interval 1 is > inputInterval.startTime (after)
                //S2/S1-S2 E1 E2
                else{ 
                    tempStartTime = startTime;
                    tempEndTime = endTime;
                }
            }
            if(tempStartTime != null && tempEndTime != null){
                interval = new Interval(locationId, tempStartTime, tempEndTime);
            }
        }
        return interval;
    }
    
    public boolean equals(Interval interval){
        boolean isEquals = false;
        if(locationId == interval.locationId){
            if(startTime.equals(interval.startTime) && endTime.equals(interval.endTime)){
                isEquals = true;
                return isEquals;
            }
        }
        return isEquals;
    }
    
    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public long calculateDuration(){
        return endTime.getTime() - startTime.getTime();
    }
    
    @Override
    public String toString() {
        return "Interval{" + "locationId =" + locationId + ", startTime = " + startTime + ", endTime = " + endTime + ", duration = " + duration + '}';
    }
}
