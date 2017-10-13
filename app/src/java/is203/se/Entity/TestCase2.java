package is203.se.Entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Bernice
 */
public class TestCase2 {

    private static ArrayList<User> userArrList;

    public static void testUseCases() {
        userArrList = new ArrayList<User>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            Date startDate = dateFormat.parse("12/09/2017 12:03:13");
            long startTime = startDate.getTime();
            Date endDate = dateFormat.parse("12/09/2017 12:08:59");
            long endTime = endDate.getTime();

            //User1
            User user = new User("mac1", "Bob", "BobPassword1", "bob.tay.2016@soa.smu.edu.sg", 'M');
            ArrayList<Interval> intervalArrList = new ArrayList<Interval>();

            Interval interval = new Interval("loc1", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:09:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:13:59");
            endTime = endDate.getTime();
            interval = new Interval("loc2", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:14:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:25:59");
            endTime = endDate.getTime();
            interval = new Interval("loc3", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            user.setIntervalArrList(intervalArrList);
            userArrList.add(user);

            //User2
            user = new User("mac2", "Amy", "AmyPassword1", "amy.choi.2016@sob.smu.edu.sg", 'F');
            intervalArrList = new ArrayList<Interval>();

            startDate = dateFormat.parse("12/09/2017 12:03:13");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:08:59");
            endTime = endDate.getTime();
            interval = new Interval("loc1", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:09:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:13:59");
            endTime = endDate.getTime();
            interval = new Interval("loc2", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:14:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:25:59");
            endTime = endDate.getTime();
            interval = new Interval("loc3", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            user.setIntervalArrList(intervalArrList);
            userArrList.add(user);

            //User3
            user = new User("mac3", "Trinity", "TrinityPassword1", "Trinity.tan.2016@sob.smu.edu.sg", 'F');
            intervalArrList = new ArrayList<Interval>();

            startDate = dateFormat.parse("12/09/2017 12:05:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:13:59");
            endTime = endDate.getTime();
            interval = new Interval("loc3", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:14:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:25:59");
            endTime = endDate.getTime();
            interval = new Interval("loc4", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            user.setIntervalArrList(intervalArrList);
            userArrList.add(user);

            //User4
            user = new User("mac4", "Joel", "JoelPassword1", "Joel.choi.2016@sob.smu.edu.sg", 'M');
            intervalArrList = new ArrayList<Interval>();

            startDate = dateFormat.parse("12/09/2017 12:09:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:15:59");
            endTime = endDate.getTime();
            interval = new Interval("loc3", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:16:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:25:59");
            endTime = endDate.getTime();
            interval = new Interval("loc4", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            user.setIntervalArrList(intervalArrList);
            userArrList.add(user);

            //User5
            user = new User("mac5", "Samuel", "SamuelPassword1", "Samuel.choi.2016@sob.smu.edu.sg", 'M');
            intervalArrList = new ArrayList<Interval>();

            startDate = dateFormat.parse("12/09/2017 12:03:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:09:59");
            endTime = endDate.getTime();
            interval = new Interval("loc1", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            startDate = dateFormat.parse("12/09/2017 12:10:00");
            startTime = startDate.getTime();
            endDate = dateFormat.parse("12/09/2017 12:15:59");
            endTime = endDate.getTime();
            interval = new Interval("loc3", new Timestamp(startTime), new Timestamp(endTime));
            intervalArrList.add(interval);

            user.setIntervalArrList(intervalArrList);
            userArrList.add(user);

        } catch (ParseException ex) {
            Logger.getLogger(TestCase2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        testUseCases();

        Date prevEndTime = null;
        long intervalDuration = 0;
        int userIndex1 = 0;
        int userIndex2 = 0;
        int counter = 0;
        int maxUserIndex = userArrList.size() - 1;
        User user1 = null;
        User user2 = null;
        ArrayList<Interval> intervalList1 = null;
        ArrayList<Interval> intervalList2 = null;
        ArrayList<Interval> outputIntervalList = new ArrayList<>();
        ArrayList<Group> grpsArrList = new ArrayList<>();
        ArrayList<User> userOutputArrList = new ArrayList<>();

        // loop through userArrList
        while (userIndex1 < maxUserIndex) {
            System.out.println("-----------------------------------------");

            // get user 1 and 2, and intervals for these 2 users
            // userArrList is a global variable
            user1 = userArrList.get(userIndex1);

            user2 = userArrList.get(userIndex2 + 1);
            intervalList1 = user1.getIntervalArrList();
            intervalList2 = user2.getIntervalArrList();
            int intervalIndex1 = 0;
            int intervalIndex2 = 0;

            //for every user2
            while (userIndex2 < maxUserIndex) {
                //if the while(userIndex2 < maxUserIndex) is not the first loop
                if (counter > 0) {
                    user2 = userArrList.get(userIndex2 + 1);
                    intervalList2 = user2.getIntervalArrList();
                }
                System.out.println("----------- user1 name = " + user1.getName());
                System.out.println("----------- user2 name = " + user2.getName());
                System.out.println("----------- userIndex2 = " + userIndex2);
                while (intervalIndex2 < intervalList2.size()) {
                    Interval tempInterval = intervalList1.get(intervalIndex1).overlap(intervalList2.get(intervalIndex2));
                    System.out.println("tempInterval null = " + (tempInterval == null));
                    
                    // if there is an overlap, 
                    // 
                    if (tempInterval != null) {
                        // if prevEndTime == null, then set prevEndTime, lastEndTime, firstStartTime
                        if (prevEndTime == null) {
                            System.out.println("prevEndTime = " + prevEndTime);
                            intervalDuration += tempInterval.getDuration();
                            prevEndTime = tempInterval.getEndTime();
                            tempInterval.setDuration(intervalDuration);
                            outputIntervalList.add(tempInterval);
                        } else {
                            System.out.println("prevEndTime = " + prevEndTime);
                            // adding one second to the prevEndTime - exclusive
                            long time = prevEndTime.getTime() + 1000;

                            //if prevEndTime < new startTime
                            if (prevEndTime.before(tempInterval.getStartTime())) {
                                Date newTime = new Date(time);

                                //if prevEndTime + 1 min =/> new startTime
                                if (newTime.equals(tempInterval.getStartTime())) {
                                    intervalDuration += tempInterval.getDuration() + 1000;
                                    prevEndTime = tempInterval.getEndTime();
                                    tempInterval.setDuration(intervalDuration);
                                    outputIntervalList.add(tempInterval);
                                }
                            }
                        }
                        if (intervalIndex1 < intervalList1.size() - 1) {
                            intervalIndex1++;
                        } else {
                            intervalIndex1 = 0;
                        }
                        if (intervalIndex2 < intervalList2.size() - 1) {
                            intervalIndex2++;
                        } else {
                            intervalIndex2 = 0;
                        }
                        System.out.println("a = " + intervalIndex2 + ", i = " + intervalIndex1);

                        if (intervalIndex2 == 0 && intervalIndex1 == 0) {
                            break;
                        }
                    } else {
                        if (intervalIndex2 < intervalList2.size() - 1) {
                            intervalIndex2++;
                        } else if (intervalIndex1 < intervalList1.size() - 1) {
                            intervalIndex1++;
                            intervalIndex2 = 0;
                            break;
                        } else {
                            intervalIndex2 = 0;
                            break;
                        }
                        System.out.println("a = " + intervalIndex2 + ", i = " + intervalIndex1);
                    }
                }
                Group newGrp = new Group();
                String name1 = user1.getName();
                String name2 = user2.getName();
                if (intervalDuration > 0 && !name1.equals(name2)) {
                    System.out.println("---------in here----------");

                    userOutputArrList.add(user1);
                    userOutputArrList.add(user2);

                    newGrp.setIntervals(outputIntervalList);
                    newGrp.setUsers(userOutputArrList);
                    newGrp.setDuration(intervalDuration);

                    System.out.println("user1 name = " + user1.getName());
                    System.out.println("user2 name = " + user2.getName());

                    boolean exists = false;
                    for (int c = 0; c < grpsArrList.size(); c++) {
                        System.out.println("in here =====================================================");
                        Group grp = grpsArrList.get(c);
                        exists = grp.isDuplicatedGroup(newGrp);
                        if (exists) {
                            break;
                        } else {
                            System.out.println("in same interval method ------");
                            exists = grp.addUserIfSameInterval(newGrp);
                            if (exists) {
                                break;
                            }
                        }
                    }
                    if (!exists) {
                        grpsArrList.add(newGrp);
                    }
                }
                counter++;
                userIndex2++;

                intervalDuration = 0;
                prevEndTime = null;
                userOutputArrList = new ArrayList<>();
                outputIntervalList = new ArrayList<>();
            }
            System.out.println("userIndex1 = " + userIndex1);

            counter = 0;
            userIndex2 = -1;
            userIndex1++;
        }
        grpsArrList = validateGroups(grpsArrList);
        System.out.println("-------1--------");
        for (int b = 0; b < grpsArrList.size(); b++) {
            System.out.println(grpsArrList.get(b).getDuration() / 60000);
            System.out.println((grpsArrList.get(b).getDuration() / 1000) % 60);
            for (User user : grpsArrList.get(b).getUsers()) {
                System.out.println(user.getName());
            }
            System.out.println("---------------");
        }
    }
    // This method 
    public static ArrayList<Group> validateGroups(ArrayList<Group> groups) {

//        int userInd = 0;
//        int intervalInd1 = 0;
//        int intervalInd2 = 0;
//        
//        ArrayList<User> userOutputList = new ArrayList<>();
//        ArrayList<Interval> intervalOutputList = new ArrayList<>();
//        
//        Interval tempInterval = null;
//        
//        for(int grpInd1 = 0; grpInd1 < groups.size(); grpInd1++){
//            System.out.println("in 1st for loop");
//            
//            System.out.println(grpInd1 + "********************************");
//            Group grp1 = groups.get(grpInd1);
//            
//            for(int grpInd2 = 0; grpInd2 < groups.size(); grpInd2++){            
//            System.out.println(grpInd2 + "--0000000000000000000000000000000000000000");
//                System.out.println("in 2nd for loop");
//                
//                Group grp2 = groups.get(grpInd2);
//                boolean exists = false;
//                exists = grp1.isDuplicatedGroup(grp2);
//                System.out.println("exists = " + exists);
//                
//                if(!exists){
//                    
//                    ArrayList<Interval> intervalArrList1 = grp1.getIntervals();
//                    ArrayList<Interval> intervalArrList2 = grp2.getIntervals();
//                    
//                    System.out.println("intervalArrList2 = " + intervalArrList1.size());
//                    
//                            System.out.println("intervalInd2 = " + intervalInd2 + " intervalInd1 = " + intervalInd1);
//                    while(intervalInd1 < intervalArrList1.size()-1){
//                        System.out.println("in 1st while loop");
//                        
//                        Interval interval1 = intervalArrList1.get(intervalInd1);
//                        Interval interval2 = intervalArrList2.get(intervalInd2);
//                        
//                        tempInterval = interval1.overlap(interval2);
//                        
//                        System.out.println("interval1 = " + interval1.getStartTime() + ", " + interval1.getEndTime());
//                        System.out.println("interval2 = " + interval2.getStartTime() + ", " + interval2.getEndTime());
//                        System.out.println("tempInterval = " + tempInterval);
//                        if(tempInterval != null){
//                            
//                            ArrayList<User> userArrList1 = grp1.getUsers();
//                            ArrayList<User> userArrList2 = grp2.getUsers();
//
//                            while(userInd < userArrList2.size()-1){
//                                System.out.println("in 2nd while loop");
//                                User user = userArrList2.get(userInd);
//
//                                System.out.println("!userArrList1.contains(user) = " + !userArrList1.contains(user));
//                                if(!userArrList1.contains(user)){
//                                    userOutputList.add(user);
//                                }
//                            }
//                            intervalOutputList.add(tempInterval);
//                            if((intervalInd1+1) < intervalArrList1.size()-1){
//                                System.out.println("1st if");
//                                intervalInd1++;
//                            }
//                            else{
//                                System.out.println("1st else");
//                                intervalInd1 = 0;
//                            }
//                            if((intervalInd2+1) < intervalArrList2.size()-1){
//                                System.out.println("2nd if");
//                                intervalInd2++;
//                            }
//                            else{
//                                System.out.println("2nd else");
//                                intervalInd2 = 0;
//                            }
//                            System.out.println("intervalInd2 = " + intervalInd2 + " intervalInd1 = " + intervalInd1);
//                            if(intervalInd2 == 0 && intervalInd1 == 0){
//                                break;
//                            }
//                        }
//                        else{
//                            System.out.println("intervalInd2 = " + intervalInd2 + " intervalInd1 = " + intervalInd1 + ", intervalArrList2.size()-1 = " + (intervalArrList2.size()-1));
//                            
//                            if((intervalInd2+1) < intervalArrList2.size()-1){
//                                System.out.println("3rd if");
//                                intervalInd2++;
//                            }
//                            else if((intervalInd1+1) < intervalArrList1.size()-1){
//                                System.out.println("3rd else if");
//                                intervalInd1++;
//                                intervalInd2 = 0;
//                                break;
//                            }
//                            else{
//                                System.out.println("3rd else");
//                                intervalInd2 = 0;
//                                break;
//                            }
//                        }
//                    }
//                    intervalInd1 = 0;
//                }
//                if(userOutputList.size() > 0){
//                    
//                    Group newGrp = new Group();
//                    long intervalDuration = 0;
//                    
//                    System.out.println("intervalOutputList.size() == 1 = " + (intervalOutputList.size() == 1));
//                    if(intervalOutputList.size() == 1){
//                        Interval interval1 = intervalOutputList.get(0);
//                        intervalDuration = interval1.getEndTime().getTime() - interval1.getStartTime().getTime();
//                        newGrp.setDuration(intervalDuration);
//                    }
//                    else if(intervalOutputList.size() > 1){
//                        Interval interval1 = intervalOutputList.get(0);
//                        Interval interval2 = intervalOutputList.get(intervalOutputList.size()-1);
//                        intervalDuration = interval2.getEndTime().getTime() - interval1.getStartTime().getTime();
//                        newGrp.setDuration(intervalDuration);
//                    }
//                    newGrp.setUsers(userOutputList);
//                    newGrp.setIntervals(intervalOutputList);
//                    groups.add(newGrp);
//                }
//                exists = false;
//            }
//        }
        return groups;
    }
}
