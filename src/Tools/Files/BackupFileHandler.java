package Tools.Files;

import Errors.BackupFailureException;
import Errors.UseBackupFailureExecption;
import UIWindows.AlarmBox;

import java.io.IOException;
import java.util.List;

public class BackupFileHandler {

    private  FileHandler fileHandler;
    private String backup_path;

    public BackupFileHandler() {
        backup_path = FileSearch.SearchForFile("ExercisesDataBaseBackup.csv");
        fileHandler = new CsvFileHandler(backup_path);
    }

    @Override
    public String toString() {
        return backup_path;
    }
    public void writeFromMainToBackup(String main_file_path) throws BackupFailureException {
        try {
            // Try using the backup data
            FileHandler main_fileHandler = new CsvFileHandler(FileSearch.SearchForFile(main_file_path));
            List<String> main_data = main_fileHandler.Read();
            if (main_data.size() > 0) {
                // delete the old data and write the first line
                String[] write = new String[5];
                // Course name
                write[0] = main_data.get(0);
                // Due date
                write[1] = main_data.get(1);
                // Done/Not done
                write[2] = main_data.get(2);
                // Last Update data
                write[3] = main_data.get(3);
                // Grade data
                write[4] = main_data.get(4);
                WriteToBackup(write, false);
                // append all the other data.
                for (int i = 5; i < main_data.size(); i += 5) {
                    // Course name
                    write[0] = main_data.get(i);
                    // Due date
                    write[1] = main_data.get(i + 1);
                    // Done/Not done
                    write[2] = main_data.get(i + 2);
                    // Last Update data
                    write[3] = main_data.get(i + 3);
                    // Grade data
                    write[4] = main_data.get(i+ 4);
                    WriteToBackup(write, true);
                }
            }
        }
        /* Throw the correct exception to know when the IOException came from backup file */
        catch (IOException i ){throw new BackupFailureException();}
    }


    public void writeFromBackupToMain(String main_file_path) throws UseBackupFailureExecption {
        try {
            // Try using the backup data
            FileHandler main_fileHandler = new CsvFileHandler(FileSearch.SearchForFile(main_file_path));
            List<String> backup_data = fileHandler.Read();
            if (backup_data.size() > 0) {
                // delete the old data and write the first line
                String[] write = new String[4];
                // Course name
                write[0] = backup_data.get(0);
                // Due date
                write[1] = backup_data.get(1);
                // Done/Not done
                write[2] = backup_data.get(2);
                // Last Update data
                write[3] = backup_data.get(3);
                main_fileHandler.WriteToFile(write, false);
                // append all the other data.
                for (int i = 4; i < backup_data.size(); i += 4) {
                    // Course name
                    write[0] = backup_data.get(i);
                    // Due date
                    write[1] = backup_data.get(i + 1);
                    // Done/Not done
                    write[2] = backup_data.get(i + 2);
                    // Last Update data
                    write[3] = backup_data.get(i + 3);
                    main_fileHandler.WriteToFile(write, true);
                }
            }
        }
        /* Throw the correct exception to know when the IOException came from backup file */
        catch (IOException i ){throw new UseBackupFailureExecption();}
    }

    public void WriteToBackup(String[] data, boolean AddorWrite) throws BackupFailureException{
        try{fileHandler.WriteToFile(data,AddorWrite);}
        // Throw new backup failure exception to distinguish it from regular IOExceptions
        catch (IOException i){throw new BackupFailureException();}
    }
    public void UpdateBackup(String[] data, boolean AddorWrite) throws BackupFailureException{
        try{fileHandler.WriteToFile(data,AddorWrite);}
        // Throw new backup failure exception to distinguish it from regular IOExceptions
        catch (IOException i){throw new BackupFailureException();}
    }

    public boolean CompareMainAndBackup(FileHandler main_file) throws IOException{
        return main_file.getFileSize() == fileHandler.getFileSize();
    }

    /**
     * Get the backup file size in bytes
     * @return size of the
     */
    public double getfileSize(){return fileHandler.getFileSize();}
}
