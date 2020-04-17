package ro.utcluj.pt;

import java.util.Formatter;

public class FileWriter {

    private static final String FILE_NAME = "ReportActivities.txt";
    private Formatter formatter;


    public void openFile() {
        try {
            formatter = new Formatter(FILE_NAME);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void addRecords(String string) {
        formatter.format(string);
    }

    public void closeFile() {
        formatter.close();
    }

}
