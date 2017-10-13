package is203.se.Entity;


import java.util.ArrayList;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bernice
 */
public class Group {
    private ArrayList<User> users;
    private long duration;
    private ArrayList<Interval> intervals;

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
        
        System.out.println("in same interval method, " + userArrList.size());
            System.out.println("in same interval method, " + (inputGroup.getDuration() == duration));
        if(inputGroup.getDuration() == duration){
            while(i < userArrList.size()){
                    System.out.println("in same interval method, " + !users.contains(userArrList.get(i)));
                    System.out.println("in same interval method, " + userArrList.get(i).getName());
                if(!users.contains(userArrList.get(i))){
                    Interval interval1 = inputGroup.getIntervals().get(0);
                    Interval interval2 = intervals.get(0);
                    System.out.println("in same interval method, " + interval1.getStartTime().equals(interval2.getStartTime()));
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
            System.out.println("addUsers = " + addUsers.size() + ", " + users.size());
            if(addUsers.size() > 0){
                isSameIntervals = true;
                users.addAll(addUsers);
                addUsers.clear();
            }
            System.out.println("addUsers = " + users.size());
        }
        return isSameIntervals;
    }
    
//    public boolean createNewGroupIfOverlaps(Group inputGroup){
//        int i = 0;
//        int a = 0;
//        boolean isOverlapped = false;
//        ArrayList<User> userArrList = inputGroup.getUsers();
//        ArrayList<Interval> intervalArrList = inputGroup.getIntervals();
//        while(i < intervals.size()-1){
//        
//        }
//        return isOverlapped;
//    }
}
