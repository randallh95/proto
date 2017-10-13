package is203.se.Entity;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bernice
 */
public class Interval {

    private String startLocationId;
    private String endLocationId;
    private Date startTime;
    private Date endTime;
    private long duration;
    
    public Interval(){
        
    }
    
    public Interval(String startLocationId, String endLocationId, Date startTime, Date endTime){
        this.startLocationId = startLocationId;
        this.endLocationId = endLocationId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public Interval overlap(Interval inputInterval){
        Date tempStartTime = new Date();
        Date tempEndTime = new Date();
        Interval interval = null;
            System.out.println("in here 0 s1 = " + startLocationId + ", s2 = " + inputInterval.startLocationId + ", e1 = " + endLocationId + ", e2 = " + inputInterval.endLocationId);
        if(startLocationId.equals(inputInterval.startLocationId) && endLocationId.equals(inputInterval.endLocationId)){
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
            //S2 E1-E2/E2
//            else if(inputInterval.startTime.before(endTime) && (inputInterval.endTime.after(endTime) || inputInterval.endTime.equals(endTime))){
//                //if startTime of interval 1 is < inputInterval.startTime (before)
//                //if startTime of interval 1 is = inputInterval.startTime (equals)
//                //S1/S2-S1 E1-E2/E2
//                if(startTime.before(inputInterval.startTime) || startTime.equals(inputInterval.startTime)){
//                    tempStartTime = inputInterval.startTime;
//                    tempEndTime = endTime;
//                }
//                //if startTime of interval 1 is > inputInterval.startTime (after)
//                //S2 S1 E1-E2/E2
//                else{
//                    tempStartTime = startTime;
//                    tempEndTime = endTime;
//                }
//            }
//            //if startTime of interval 1 is < inputInterval.endTime (before)
//            //if endTime of interval 1 is < inputInterval.endTime (before)
//            //S2 E2 E1
//            else if(inputInterval.startTime.before(endTime) && inputInterval.endTime.before(endTime)){
//                //if startTime of interval 1 is < inputInterval.startTime (before)
//                //if startTime of interval 1 is = inputInterval.startTime (equals)
//                //S2-S1/S1 E2 E1
//                if(inputInterval.startTime.before(startTime) || inputInterval.startTime.equals(startTime)){
//                    tempStartTime = startTime;
//                    tempEndTime = inputInterval.endTime;
//                }
//                //if startTime of interval 1 is > inputInterval.startTime (after)
//                //S2 S1 E2 E1
//                else{ 
//                    tempStartTime = startTime;
//                    tempEndTime = inputInterval.endTime;
//                }
//            }
            
            if(tempStartTime != null && tempEndTime != null){
                interval = new Interval(startLocationId, endLocationId, tempStartTime, tempEndTime);
                interval.setDuration(tempEndTime.getTime()-tempStartTime.getTime());
            }
        }
        return interval;
    }
    
    public boolean equals(Interval interval){
        boolean isEquals = false;
        if(startLocationId.equals(interval.startLocationId) && endLocationId.equals(interval.endLocationId)){
            if(startTime.equals(interval.startTime) && endTime.equals(interval.endTime)){
                isEquals = true;
                return isEquals;
            }
        }
        return isEquals;
    }
    
    public String getStartLocationId() {
        return startLocationId;
    }

    public void setStartLocationId(String startLocationId) {
        this.startLocationId = startLocationId;
    }
    
    public String getEndLocationId() {
        return endLocationId;
    }

    public void setEndLocationId(String endLocationId) {
        this.endLocationId = endLocationId;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }    
}
