/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Controller;

/**
 *
 * @author JunMing
 */
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class HeatmapController extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        //Retrieve values from HeatmapUI.jsp
        String dateStr = request.getParameter("date_time");
        int floor = Integer.parseInt(request.getParameter("floor"));
        
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        if (dateStr == null) {
            RequestDispatcher view = request.getRequestDispatcher("HeatmapUI.jsp");
            view.forward(request, response);
        }
        //Date date = (Date)formater.parse(dateStr);
        

        //HashMap<String, Integer> locationDensityMap = Heatmap.retrieveHeatMap(date, floor);
        //request.setAttribute("output", result);

        RequestDispatcher view = request.getRequestDispatcher("HeatmapUI.jsp");
        view.forward(request, response);

    }
}