/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is203.se.Controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import is203.se.DAO.LocationDAO;
import is203.se.DAO.LocationReportDAO;
import is203.se.DAO.UserDAO;
import is203.se.Entity.BootstrapError;
import is203.se.Entity.LocationReport;
import is203.se.Utility.DatabaseUtility;

import is203.se.Utility.ValidateUtility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author User
 */
@WebServlet(name = "BootstrapServlet", urlPatterns = {"/BootstrapServlet"})
@MultipartConfig
public class BootstrapServlet extends HttpServlet {

    

    // saw this on Java EE tutorial. not sure what it does yet.
    private final static Logger LOGGER
            = Logger.getLogger(BootstrapServlet.class.getCanonicalName());

    // files declared at topmost scope for use across various methods
    private File locationLookupFile;
    private File demographicsFile;
    private File locationFile;
    
    private boolean isBootstrap;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();

        // Create path components to save the file
        final String path = getServletContext().getRealPath("") + "data" + File.separator;
        // if directory does not exist, create one
        File uploadDirectory = new File(path);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdir();
        }

        final Part filePart = request.getPart("bootstrap-file");
        final String fileName = getFileName(filePart);
        final String fileExtension = getFileExtension(fileName);

        // if uploaded file is not a .zip, stop execution and alert user.
        if (!fileExtension.equals("zip")) {
//            writer.println("Uploaded file " + fileName + " is not a zip file. Please upload a .zip file.");
            return;
        }

        // possible improvement: use method to create .zip file
        OutputStream out = null;
        InputStream filecontent = null;
        // upload/write the .zip file to the 'data' directory of the server
        // TODO: modify code to use try-with-resources Statement
        try {

            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
                    new Object[]{fileName, path});

        } catch (FileNotFoundException fne) {
            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                    new Object[]{fne.getMessage()});
        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
//            if (writer != null) {
//                writer.close();
//            }
        }

        // possible improvement: use method to create unzip file
        // unzip the newly created .zip file
        String zipFilePath = path + fileName; // the path of the zip file
        // e.g. C:\Users\User\Documents\NetBeansProjects\se\sample-app\build\web\data\data-se-v2.zip
        int zipFilePathLength = zipFilePath.length();

        byte[] buffer = new byte[1024]; //buffer for read and write data to file
        try (
                FileInputStream fis = new FileInputStream(zipFilePath);
                ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String unzippedFileName = ze.getName(); // e.g. data-se-v2/location-lookup.csv
                if (unzippedFileName.contains("csv")) {
                    File newFile = new File(path + File.separator + unzippedFileName);
                    LOGGER.log(Level.INFO, "Unzipping to {0}", newFile.getAbsolutePath());
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                } else {
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
            }
        }

        // unzip the files, and instantiate our File objects
        setFileAttributes(path, "location-lookup.csv", "demographics.csv", "location.csv");

        String bootstrapType = request.getParameter("submitBtn");
        // bootstrapType should never be null if user is navigated here from BootstrapUI.jsp
        // if POST directly to /BootstrapServlet, there's a chance the bootstrapType is null
        // therefore, we check to be on the safe side


        if (bootstrapType == null) {
            return; // stop execution
        } else {
            isBootstrap = bootstrapType.equals("Bootstrap");
        }

        ArrayList<BootstrapError> locationLookupFileErrors = new ArrayList<>();
        ArrayList<BootstrapError> demographicsFileErrors = new ArrayList<>();
        ArrayList<BootstrapError> locationFileErrors = new ArrayList<>();

        if (isBootstrap) {
            // first, we check if ALL three .csv files exist
            // if any one of them  does not, we cannot proceed with Bootstrap
            if (!locationLookupFile.exists() || !demographicsFile.exists() || !locationFile.exists()) {
                return;
                // stop execution
            }

            // then, we need to clear DB
            // if bootstrap fails after this point, TOO BAD - we will end up with a partially filled DB
            // am following the wiki requirements exactly, and providing no additional functionality for now
            DatabaseUtility.clearAllData();

            // then, we handle the three .csv files
            // first file: location-lookup.csv
            ArrayList<String[]> locationLookupFileArray = readAllLinesOfCsvFile(locationLookupFile);
            locationLookupFileErrors = handleLocationLookupFile(locationLookupFileArray);
            // second file: demographics.csv
            ArrayList<String[]> demographicsFileArray = readAllLinesOfCsvFile(demographicsFile);
            demographicsFileErrors = handleDemographicsFile(demographicsFileArray);
            // third & last file: demographics.csv
            ArrayList<String[]> locationFileArray = readAllLinesOfCsvFile(locationFile);
            locationFileErrors = handleLocationFile(locationFileArray);

        } else {

            if (demographicsFile.exists()) {
                ArrayList<String[]> demographicsFileArray = readAllLinesOfCsvFile(demographicsFile);
                demographicsFileErrors = handleDemographicsFile(demographicsFileArray);
            }

            if (locationFile.exists()) {
                ArrayList<String[]> locationFileArray = readAllLinesOfCsvFile(locationFile);
                locationFileErrors = handleLocationFile(locationFileArray);
            }

        }

        // upon completeion, redirect user to BoostrapUI
        HttpSession session = request.getSession(true);
        session.setAttribute("locationLookupFileErrors", locationLookupFileErrors);
        session.setAttribute("demographicsFileErrors", demographicsFileErrors);
        session.setAttribute("locationFileErrors", locationFileErrors);
        long endTime = System.currentTimeMillis();
        long timeTakenInMillis = endTime - startTime;
        long timeTakenInSeconds = timeTakenInMillis / 1000;
        session.setAttribute("timeTakenToBootstrap", timeTakenInSeconds);
        response.sendRedirect("BootstrapUI.jsp");

    }

    private ArrayList<BootstrapError> handleLocationLookupFile(ArrayList<String[]> locationLookupFileArray) {
        // initialize ArrayList that will keep track of errors
        ArrayList<BootstrapError> errorList = new ArrayList<>();

        // declare list for rows that pass all validation
        ArrayList<String[]> validLocations = new ArrayList<>();

        // first, we store the header
        String[] header = locationLookupFileArray.get(0);
        // then, remove header from list
        locationLookupFileArray.remove(0);

        int rowNum = 2; // the first row with data is rowNum 2 (should verify this)
        // iterate through the rows
        for (String[] row : locationLookupFileArray) {
            // instantiate an BootstrapError object
            BootstrapError errorForThisLine = new BootstrapError("location-lookup.csv", rowNum);

            String[] blankFields = ValidateUtility.checkForBlankFields(header, row);
            if (blankFields != null) {
                // blank field was found -> add to invalid 
                // add in the error message to the BootstrapError object
                for (String blankField : blankFields) {
                    errorForThisLine.addErrorMsg(blankField);
                }
            } else {
                String unvalidatedLocationID = row[0].trim();
                String unvalidatedSemanticPlace = row[1].trim();

                if (!ValidateUtility.validateLocationId(unvalidatedLocationID)) {
                    errorForThisLine.addErrorMsg("invalid location id");
                }

                if (!ValidateUtility.validateSemanticPlace(unvalidatedSemanticPlace)) {
                    errorForThisLine.addErrorMsg("invalid semantic place");
                }
            }

            // if this row has errors, add to errorList
            // else, add to validLocations
            if (errorForThisLine.hasNoErrors()) {
                validLocations.add(row);
            } else {
                errorList.add(errorForThisLine);
            }

            rowNum++;
        }

        // write validLocations to DB
        LocationDAO locationDAO = new LocationDAO();
        int[] insertedNumList = locationDAO.insertLocations(validLocations);
        locationDAO.closeConnection(); // is this neccessary? Or will locationDAO be garbage collected upon return completion of this method?

        return errorList;

    }

    private ArrayList<BootstrapError> handleDemographicsFile(ArrayList<String[]> demographicsFileArray) {
        // initialize ArrayList that will keep track of errors
        ArrayList<BootstrapError> errorList = new ArrayList<>();

        // declare list for rows that pass all validation
        ArrayList<String[]> validUsers = new ArrayList<>();

        // first, we store the header
        String[] header = demographicsFileArray.get(0);
        // then, remove header from list
        demographicsFileArray.remove(0);

        int rowNum = 2; // the first row with data is rowNum 2 (should verify this)
        // iterate through the rows
        for (String[] row : demographicsFileArray) {
            // instantiate an BootstrapError object
            BootstrapError errorForThisLine = new BootstrapError("demographics.csv", rowNum);

            String[] blankFields = ValidateUtility.checkForBlankFields(header, row);
            if (blankFields != null) {
                // blank field was found -> add to invalid 
                // add in the error message to the BootstrapError object
                for (String blankField : blankFields) {
                    errorForThisLine.addErrorMsg(blankField);
                }
            } else {
                String unvalidatedMacAddress = row[0].trim();
                String unvalidatedName = row[1].trim(); // there is no validation for name
                String unvalidatedPassword = row[2]; // do not remove whitespaced for password: needed for validation checks
                String unvalidatedEmail = row[3].trim();
                String unvalidatedGender = row[4].trim().toUpperCase(); // make gender upper-case, since either lower-case or upper-case would work

                if (!ValidateUtility.validateMacAddress(unvalidatedMacAddress)) {
                    errorForThisLine.addErrorMsg("invalid mac address");
                }

                if (!ValidateUtility.validatePassword(unvalidatedPassword)) {
                    errorForThisLine.addErrorMsg("invalid password");
                }

                if (!ValidateUtility.validateEmail(unvalidatedEmail)) {
                    errorForThisLine.addErrorMsg("invalid email");
                }

                if (!ValidateUtility.validateGender(unvalidatedGender)) {
                    errorForThisLine.addErrorMsg("invalid gender");
                }

            }

            // if this row has errors, add to errorList
            // else, add to validLocations
            if (errorForThisLine.hasNoErrors()) {
                validUsers.add(row);
            } else {
                errorList.add(errorForThisLine);
            }

            rowNum++;
        }

        // write validLocations to DB
        UserDAO userDAO = new UserDAO();
        int[] insertedNumList = userDAO.insertUsers(validUsers);
        userDAO.closeConnection(); // is this neccessary? Or will userDAO be garbage collected upon return completion of this method?

        return errorList;
    }

    private ArrayList<BootstrapError> handleLocationFile(ArrayList<String[]> locationFileArray) {

        // initialize ArrayList that will keep track of errors
        ArrayList<BootstrapError> errorList = new ArrayList<>();

        // declare list for rows that pass all validation
        ArrayList<String[]> validLocationReports = new ArrayList<>();

        // first, we store the header
        String[] header = locationFileArray.get(0);
        // then, remove header from list
        locationFileArray.remove(0);

        // now, we identify duplicate rows BEFORE performing other validations
        // the last row 'duplicated' row is NOT considered a duplicate, while prior dows are considered
        ArrayList<String> timestampAndMacAddressConcatenatedList = new ArrayList<>();
        for (String[] row : locationFileArray) {
            String timstamp = row[0].trim();
            String macAddress = row[1].trim();
            String concatenatedTimstampAndMacAddress = timstamp + macAddress;
            timestampAndMacAddressConcatenatedList.add(concatenatedTimstampAndMacAddress);
        }
        
        // if 'Additional' type, need account for duplicates in DB
        // so, add timestampAndMacAddress in DB to our list
        LocationReportDAO locationReportDAO = new LocationReportDAO();
        if (!isBootstrap) {
            ArrayList<LocationReport> locationReportsFromDB = locationReportDAO.retrieveAllLocationReports();
            for(LocationReport locRep : locationReportsFromDB) {
                String macAddress = locRep.getMacAddress();
                Date timsstamp = locRep.getTimestamp();
                
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timsstampAsString = formater.format(timsstamp);
                
                String concatenatedTimstampAndMacAddress = timsstampAsString + macAddress;
                timestampAndMacAddressConcatenatedList.add(concatenatedTimstampAndMacAddress);
            }
        }

        // * The Set method *
        final Set<Integer> duplicatedRowNumbers = new HashSet();
        final Set<String> uniqueConcatenatedStrings = new HashSet<String>();

        int timestampAndMacAddressConcatenatedListSize = timestampAndMacAddressConcatenatedList.size();
        for (int i = timestampAndMacAddressConcatenatedListSize - 1; i >= 0; i--) {
            String row = timestampAndMacAddressConcatenatedList.get(i);

            if (!uniqueConcatenatedStrings.add(row)) {
                duplicatedRowNumbers.add(i + 2);
            }
        }

        int rowNum = 2; // the first row with data is rowNum 2 (should verify this)
        // iterate through the rows
        for (String[] row : locationFileArray) {
            // instantiate an BootstrapError object
            BootstrapError errorForThisLine = new BootstrapError("location.csv", rowNum);

            String[] blankFields = ValidateUtility.checkForBlankFields(header, row);
            if (blankFields != null) {
                // blank field was found -> add to invalid 
                // add in the error message to the BootstrapError object
                for (String blankField : blankFields) {
                    errorForThisLine.addErrorMsg(blankField);
                }
            } else {
                // if rows is a duplicate, add that errorMsg
                // else, perform the other validations
                if (duplicatedRowNumbers.contains(rowNum)) {
                    errorForThisLine.addErrorMsg("duplicate row");
                } else {
                    String unvalidatedTimstamp = row[0].trim();
                    String unvalidatedMacAddress = row[1].trim();
                    String unvalidatedLocationId = row[2].trim();

                    if (!ValidateUtility.validateTimestamp(unvalidatedTimstamp)) {
                        errorForThisLine.addErrorMsg("invalid timestamp");
                    }

                    if (!ValidateUtility.validateMacAddress(unvalidatedMacAddress)) {
                        errorForThisLine.addErrorMsg("invalid mac address");
                    }

                    // need to add in validation for location id
                    //
                }

            }

            // if this row has errors, add to errorList
            // else, add to validLocations
            if (errorForThisLine.hasNoErrors()) {
                validLocationReports.add(row);
            } else {
                errorList.add(errorForThisLine);
            }

            rowNum++;
        }

        // write validLocations to DB
        
        int[] insertedNumList = locationReportDAO.insertLocationReports(validLocationReports);
        locationReportDAO.closeConnection(); // is this neccessary? Or will userDAO be garbage collected upon return completion of this method?

        // println for debugging
        return errorList;

    }

    private void writeToCsvFile(ArrayList<String[]> validList) {

        final String path = getServletContext().getRealPath("") + "data" + File.separator;
        String filePath = path + "valid-location.csv";

        // feed in your array (or convert your data to an array)
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // feed in your array (or convert your data to an array)
            for (String[] row : validList) {
                writer.writeNext(row);
            }
        } catch (IOException ex) {
            Logger.getLogger(BootstrapServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<String[]> readAllLinesOfCsvFile(File csvFile) {
        ArrayList<String[]> result = null;
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            result = (ArrayList<String[]>) reader.readAll();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(BootstrapServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BootstrapServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private String getFileExtension(final String fileName) {
        int fileNameLength = fileName.length();
        if (fileNameLength > 3) {
            return fileName.substring(fileNameLength - 3, fileNameLength);
        }
        return null;
    }

    private void setFileAttributes(String filePath, String locationLookup, String demographics, String location) {
        locationLookupFile = new File(filePath + File.separator + locationLookup);
        demographicsFile = new File(filePath + File.separator + demographics);
        locationFile = new File(filePath + File.separator + location);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
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
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
