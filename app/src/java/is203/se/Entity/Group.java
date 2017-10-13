/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Entity;

import java.util.ArrayList;

/**
 *
 * @author Bernice
 */
public class Group {
    private ArrayList<User> users = new ArrayList<>();
    private long duration;
    private ArrayList<Interval> intervals = new ArrayList<>();

    public Group(){
        
    }
    
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }
    
    public void addInterval(Interval interval){
        this.intervals.add(interval);
    }
    
    public boolean isDuplicatedGroup(Group inputGroup){
        boolean isSameIntervals = false;
        if(inputGroup == null){
            return isSameIntervals;
        }
        int i = 0;
        ArrayList<User> userArrList = inputGroup.getUsers();
        if(inputGroup.getDuration() == duration){
            while(i < users.size()){
                if(userArrList.contains(users.get(i))){
                    Interval interval1 = inputGroup.getIntervals().get(0);
                    Interval interval2 = intervals.get(0);
                    if(interval1.getStartTime().equals(interval2.getStartTime())){
                        isSameIntervals = true;
                    }
                    else{
                        isSameIntervals = false;
                        break;
                    }
                }
                else{
                    isSameIntervals = false;
                    break;
                }
                i++;
            }
        }
        return isSameIntervals;
    }
    
    public boolean addUserIfSameInterval(Group inputGroup){
        boolean isSameIntervals = false;
        ArrayList<User> addUsers = new ArrayList<>();
        if(inputGroup == null){
            return isSameIntervals;
        }
        int i = 0;
        ArrayList<User> userArrList = inputGroup.getUsers();
        
        if(inputGroup.getDuration() == duration){
            while(i < userArrList.size()){
                if(!users.contains(userArrList.get(i))){
                    Interval interval1 = inputGroup.getIntervals().get(0);
                    Interval interval2 = intervals.get(0);
                    
                    if(interval1.getStartTime().equals(interval2.getStartTime())){
                        addUsers.add(userArrList.get(i));
                    }
                    else{
                        break;
                    }
                }
                else{
                    break;
                }
                i++;
            }
            if(addUsers.size() > 0){
                isSameIntervals = true;
                users.addAll(addUsers);
                addUsers.clear();
            }
        }
        return isSameIntervals;
    }
    
    public int isSubsetOfAnotherGroup(Group group){
        int isSubsetCount = 0;
        int maxArrListSize = 0;
        int returnInd = 0;
        ArrayList<User> validateUserArrList1 = null;
        ArrayList<User> validateUserArrList2 = null;
        ArrayList<User> userArrList1 = group.getUsers();
        
        if(userArrList1.size() > users.size()){
            returnInd = 1;
            maxArrListSize = userArrList1.size();
            validateUserArrList1 = userArrList1;
            validateUserArrList2 = users;
        }
        else{
            returnInd = 2;
            maxArrListSize = users.size();
            validateUserArrList1 = users;
            validateUserArrList2 = userArrList1;
        }
        for(int userInd = 0; userInd < validateUserArrList2.size(); userInd++){
            if(validateUserArrList1.contains(validateUserArrList2.get(userInd))){
                isSubsetCount++;
            }
        }
        if(isSubsetCount != maxArrListSize){
            returnInd = 0;
        }
        return returnInd;
    }
    
    @Override
    public String toString() {
        return "Group{" + "users size =" + users.size() + ", duration = " + duration + ", intervals size = " + intervals.size() + '}';
    }
}
