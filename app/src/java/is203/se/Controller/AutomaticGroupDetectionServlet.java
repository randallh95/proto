/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Controller;

import is203.se.DAO.LocationReportDAO;
import is203.se.Entity.Group;
import is203.se.Entity.Interval;
import is203.se.Entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bernice
 */
@WebServlet(name = "AutomaticGroupDetectionServlet", urlPatterns = {"/AutomaticGroupDetectionServlet"})
public class AutomaticGroupDetectionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if intervalIndex2 servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AutomaticGroupDetectionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AutomaticGroupDetectionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if intervalIndex2 servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if intervalIndex2 servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(formatter.parse("2017-02-06 10:15:00").getTime());

            LocationReportDAO locationReportDao = new LocationReportDAO();
            ArrayList<User> userArrList = locationReportDao.retrieveUsersLocationReportBaseOnDateTime(date);
            if (userArrList != null) {
                System.out.println(userArrList.toString());
                groupUsers(userArrList);
            }
        } catch (ParseException ex) {
            Logger.getLogger(LocationReportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Group> groupUsers(ArrayList<User> userArrList) {
        Date prevEndTime = null;
        long duration = 0;
        int userIndex1 = 0;
        int userIndex2 = 0;
        int counter = 0;
        int maxUserIndex = userArrList.size() - 1;
        User user1 = null;
        User user2 = null;
        ArrayList<Interval> intervals1 = null;
        ArrayList<Interval> intervals2 = null;
        ArrayList<Interval> outputIntervals = new ArrayList<>();
        ArrayList<Group> grpsArrList = new ArrayList<>();
        ArrayList<User> userOutputArrList = new ArrayList<>();

        // Loop through all users
        // While loop cause can range due to timestamp
        while (userIndex1 < maxUserIndex) {
            System.out.println("-----------------------------------------");

            // 
            user1 = userArrList.get(userIndex1);
            user2 = userArrList.get(userIndex2 + 1);
            intervals1 = user1.getIntervalArrList();
            intervals2 = user2.getIntervalArrList();
            int intervalIndex1 = 0;
            int intervalIndex2 = 0;

            while (userIndex2 < maxUserIndex) {
                if (counter > 0) {
                    user2 = userArrList.get(userIndex2 + 1);
                    intervals2 = user2.getIntervalArrList();
                }
                while (intervalIndex2 < intervals2.size()) {
                    Interval tempInterval = intervals1.get(intervalIndex1).overlap(intervals2.get(intervalIndex2));

                    if (tempInterval != null) {
                        // if prevEndTime == null, then set prevEndTime, lastEndTime, firstStartTime
                        if (prevEndTime == null) {
                            duration += tempInterval.calculateDuration();
                            prevEndTime = tempInterval.getEndTime();
                            outputIntervals.add(tempInterval);
                        } else {
                            // adding one second to the prevEndTime - exclusive
                            long time = prevEndTime.getTime() + 1000;

                            //if prevEndTime < new startTime
                            if (prevEndTime.before(tempInterval.getStartTime())) {
                                Date newTime = new Date(time);

                                //if prevEndTime + 1 min =/> new startTime
                                if (newTime.equals(tempInterval.getStartTime())) {
                                    duration += tempInterval.calculateDuration() + 1000;
                                    prevEndTime = tempInterval.getEndTime();
                                    outputIntervals.add(tempInterval);
                                }
                            }
                        }
                        if (intervalIndex1 < intervals1.size() - 1) {
                            intervalIndex1++;
                        } else {
                            intervalIndex1 = 0;
                        }
                        if (intervalIndex2 < intervals2.size() - 1) {
                            intervalIndex2++;
                        } else {
                            intervalIndex2 = 0;
                        }

                        if (intervalIndex2 == 0 && intervalIndex1 == 0) {
                            break;
                        }
                    } else {
                        if (intervalIndex2 < intervals2.size() - 1) {
                            intervalIndex2++;
                        } else if (intervalIndex1 < intervals1.size() - 1) {
                            intervalIndex1++;
                            intervalIndex2 = 0;
                            break;
                        } else {
                            intervalIndex2 = 0;
                            break;
                        }
                    }
                }
                Group newGrp = new Group();
                String name1 = user1.getName();
                String name2 = user2.getName();
                if (duration > 0 && !name1.equals(name2)) {
//                    System.out.println("---------in here----------");

                    userOutputArrList.add(user1);
                    userOutputArrList.add(user2);

                    newGrp.setIntervals(outputIntervals);
                    newGrp.setUsers(userOutputArrList);
                    newGrp.setDuration(duration);

                    boolean exists = false;
                    for (int c = 0; c < grpsArrList.size(); c++) {
                        Group grp = grpsArrList.get(c);
                        exists = grp.isDuplicatedGroup(newGrp);
                        if (exists) {
                            break;
                        } else {
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

                duration = 0;
                prevEndTime = null;
                userOutputArrList = new ArrayList<>();
                outputIntervals = new ArrayList<>();
            }

            counter = 0;
            userIndex2 = -1;
            userIndex1++;
        }
        System.out.println("-------1--------");
        removeSubsetGroups(grpsArrList);
        for (int b = 0; b < grpsArrList.size(); b++) {
            System.out.println(grpsArrList.get(b).getDuration() / 60000);
            System.out.println((grpsArrList.get(b).getDuration() / 1000) % 60);
            for (User user : grpsArrList.get(b).getUsers()) {
                System.out.println(user.getName());
            }
            System.out.println("---------------");
        }
        return grpsArrList;
    }

    public static ArrayList<Group> removeSubsetGroups(ArrayList<Group> groups) {
        int grpInd1 = 0;
        int grpInd2 = 0;
        int grpSize = groups.size();

        ArrayList<Group> groupsToBeRemoved = new ArrayList<>();

        while (grpInd1 < grpSize - 1) {
            Group grp1 = groups.get(grpInd1);
            while (grpInd2 < grpSize - 1) {
                Group grp2 = groups.get(grpInd2);
                int returnInd = grp1.isSubsetOfAnotherGroup(grp2);
                if (returnInd == 2) {
                    groupsToBeRemoved.add(grp1);
                } else if (returnInd == 1) {
                    groupsToBeRemoved.add(grp2);
                }
                grpInd2++;
            }
            grpInd1++;
        }
        groups.removeAll(groupsToBeRemoved);
        return groups;
    }

    /**
     * Returns intervalIndex2 short description of the servlet.
     *
     * @return intervalIndex2 String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
