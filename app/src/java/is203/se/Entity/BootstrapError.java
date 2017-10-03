package is203.se.Entity;

import java.util.ArrayList;

public class BootstrapError {
    private String fileName; // the name of the .csv file where this error was found
    private int rowNum; // the row number of the error
    private ArrayList<String> errorMsgList; 

    public BootstrapError(String fileName, int rowNum) {
        this.fileName = fileName;
        this.rowNum = rowNum;
        errorMsgList = new ArrayList<>();
    }
    
    public void addErrorMsg(String errorMsg) {
        errorMsgList.add(errorMsg);
    }
    
    public void addErrorMsgList(ArrayList<String> errorMsgList) {
        this.errorMsgList = errorMsgList;
    }

    public String getFileName() {
        return fileName;
    }

    public int getRowNum() {
        return rowNum;
    }

    public ArrayList<String> getErrorMsgList() {
        return errorMsgList;
    }

    public boolean hasNoErrors() {
        return errorMsgList.isEmpty();
    }
    
    @Override
    public String toString() {
        String result = "";
        int errorMsgListLength = errorMsgList.size();
        for (int i = 0; i < errorMsgListLength; i++) {
            if (i == errorMsgListLength - 1) {
                result += errorMsgList.get(i);
            } else {
                result += errorMsgList.get(i) + ", ";
            }
        }
        return result;
    }
    
}
