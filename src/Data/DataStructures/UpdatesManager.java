package Data.DataStructures;
import Errors.BackupFailureException;
import Tools.Commands.OSCommand;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;
import Tools.Files.TextFileHandler;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class UpdatesManager {
    /**
     * This class will be responsible the ExerciseUpdate Object.
     * Each Exercise will have its own UpdatesManager to handle all the updates created by the user.
     * The UpdatesManager will create a new ExerciseUpdate when needed and update the db file as well.
     * Upon delete / Update the Update manager will update the DB file as well with the change
     * and the updates ArrayList.
     */
    private FileHandler fileHandler;
    private FileHandler BackupfileHandler;
    private String backup_path;
    private String file_name;
    private String file_path;
    private int exercise_number;
    private ArrayList<ExerciseUpdate> updates;
    private int next_update = 1;
    private String course;


    public UpdatesManager(int exercise_number,String course_name) throws IOException,ParseException{
        this.exercise_number = exercise_number;
        this.file_name = "Ex" + exercise_number + "Updates.txt";
        updates = new ArrayList<>();
        this.course = course_name;
        init(course_name);
    }


    private void init(String course_name) throws IOException,ParseException {
        File current_directory=new File("").getAbsoluteFile();
        file_path = current_directory.getAbsolutePath() + "\\src\\Data\\Exercises\\" +course_name;
        fileHandler = new TextFileHandler(file_path+"\\"+file_name);
        // Create the new file if not exists
        // If the file already exists (return false) read the data and create the ExerciseUpdate objects
        if(!fileHandler.CreateFile()){
            List<String> file_data = fileHandler.Read();
            String update_number,update_text,update_update_date;
            for (int i = 0; i < file_data.size(); i++) {
                // If enter was pushed by accident don't access the row
                if(file_data.get(i).equals(""))
                    continue;
                // Get the beginning of the date substring
                String to_find = "(Update Date: ";
                int start_date_substring = file_data.get(i).indexOf(to_find);
                update_number = file_data.get(i).substring(0,1);
                // The text end in the "(" of the date
                update_text = file_data.get(i).substring(3,start_date_substring);
                // Get the update_date right format
                update_update_date = file_data.get(i).substring(start_date_substring + to_find.length());
                update_update_date=update_update_date.replace(")","");
                update_update_date=update_update_date.replace(" ","");
                updates.add(new ExerciseUpdate(Integer.valueOf(update_number),update_text,update_update_date));
                next_update++;
            }
        }

        // Create the backup db (if the db exists nothing will change.
        backup_path = file_path+"\\"+file_name.replace(".txt","") + "Backup.txt";
        BackupfileHandler = new TextFileHandler(backup_path);
        BackupfileHandler.CreateFile();
    }

    public void addNewUpdate(ExerciseUpdate EU) throws IOException{
        // Add the new update to the DS
        updates.add(EU);
        next_update++;
        // Rewrite the DB with the change
        UpdateDB();
    }

    /**
     * Wrapper function for finding existing Updates in the updates list
     * @param update - the object to find
     * @return The existing instance of the ExerciseUpdate instance
     */
    private ExerciseUpdate findUpdate(ExerciseUpdate update){
        for (int i = 0; i < updates.size(); i++) {
            if(update == updates.get(i))
                return updates.get(i);
        }

        return null;
    }

    /**
     * Get a new Text to put in the update.
     * The text was given by the user
     * @param update - the object to update
     * @param new_data - the new text data
     * @throws IOException - Can't update the db file
     */
    public void updateExistingUpdateText(ExerciseUpdate update,String new_data) throws IOException,OBJECT_NOT_EXIST {
        // The update does not exist in the db
        if(findUpdate(update) == null)
            throw new OBJECT_NOT_EXIST();

        findUpdate(update).setText(new_data);
        UpdateDB();
    }

    /**
     * Update the update_date of the update updated by the user.
     * @param update - the object to update
     * @throws IOException - Can't update the db file
     */
    public void updateExistingUpdateDate(ExerciseUpdate update) throws IOException,OBJECT_NOT_EXIST {
        // The update does not exist in the db
        if(findUpdate(update) == null)
            throw new OBJECT_NOT_EXIST();

        findUpdate(update).setUpdate_date();
        UpdateDB();

    }

    /**
     * This function will update the DB file with all of the objects data
     * that are stored in the updates array list of ExerciseUpdates
     * @throws IOException  - Can't access the Ex_X_Updates.txt
     */
    private void UpdateDB() throws IOException{
        // The last update was deleted. Clean the database.
        if(updates == null || updates.size()==0) {
            fileHandler.CleanFile();
            next_update = 1;
            return;
        }

        String new_line;
        new_line = String.valueOf(updates.get(0).getUpdate_number()) + ". " + updates.get(0).getText() +
        " (Update Date: " + updates.get(0).getUpdate_date() + ")";
        String[] updated_data = {new_line};
        // Overwrite the data with the first update
        fileHandler.WriteToFile(updated_data,false);

        for (int i = 1; i < updates.size(); i++) {
            new_line = String.valueOf(updates.get(i).getUpdate_number()) + ". " + updates.get(i).getText() +
                    " (Update Date: " + updates.get(0).getUpdate_date() + ")";
            updated_data = new String[]{new_line};
            fileHandler.WriteToFile(updated_data,true);
        }

    }

    public ArrayList<ExerciseUpdate> deleteUpdate(int update_number) throws IOException{
        updates.remove(update_number - 1);
        next_update--;
        // Create a new DS with the correct Updates number after decrementing the numbers
        ArrayList<ExerciseUpdate> temp = new ArrayList<>();
        for (int i = 0; i < updates.size(); i++)
            temp.add(new ExerciseUpdate(i + 1, updates.get(i).getText()));

        updates.removeAll(updates);
        updates = temp;
        UpdateDB();

        return updates;
    }

    public ArrayList<ExerciseUpdate> getUpdates() {
        return updates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdatesManager that = (UpdatesManager) o;
        return exercise_number == that.exercise_number;
    }

    /**
     * Used when the exercise is deleted from the CourseSelectionWidow.
     * The function will delete the db file and the backup db file.
     */
    public static void destroyDB(int ExNumber) {
        String file_path = FileSearch.SearchForFile("Ex" + ExNumber + "Updates.txt");
        String backup_file_path = FileSearch.SearchForFile("Ex" + ExNumber + "UpdatesBackup.txt");
        String delete_main_command = "cmd /c del \"" + file_path + "\"";
        String delete_backup_command = "cmd /c del \"" + backup_file_path + "\"";
        OSCommand.Run(delete_main_command);
        OSCommand.Run(delete_backup_command);
    }

    public void updateBackupFromMain() throws IOException{
        if(fileHandler.getFileSize() == 0) {
            BackupfileHandler.CleanFile();
            return;
        }

        List<String> data_from_main = fileHandler.Read();
        // Delete the db and write the first line to the backup data with the true flag
        BackupfileHandler.WriteToFile(new String[]{data_from_main.get(0)},false);
        // Append the rest of the data with the true flag.
        for (int i = 1; i < data_from_main.size(); i++)
            BackupfileHandler.WriteToFile(new String[]{data_from_main.get(i)},true);
    }

    public void updateMainFromBackup() throws IOException, ParseException{
        List<String> data_from_backup = BackupfileHandler.Read();
        // Delete the db and write the first line to the main data with the false flag
        fileHandler.WriteToFile(new String[]{data_from_backup.get(0)},false);
        // Append the rest of the data with the true flag.
        for (int i = 1; i < data_from_backup.size(); i++)
            fileHandler.WriteToFile(new String[]{data_from_backup.get(i)},true);

        // Update the DS with the new data from the the backup file
        updates.removeAll(updates);
        init(course);
    }


}
