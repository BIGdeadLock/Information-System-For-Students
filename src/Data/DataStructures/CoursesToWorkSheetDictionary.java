package Data.DataStructures;
import Errors.UseBackupFailureExecption;
import Tools.Files.BackupFileHandler;
import Tools.Files.CsvFileHandler;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class CoursesToWorkSheetDictionary {
    /**
     * This class is using a dictionary to map each course (represented as string)
     * to linked list where each node is a exercise.
     * This class is a data structure to handle all the work sheets related function:
     * Write to the db file, update an exercise and so on.
     */
    private static LinkedList<CourseWorksSheet> courses;
    private FileHandler fileHandler;

    public CoursesToWorkSheetDictionary() throws ParseException, FileNotFoundException,IOException, NullPointerException, SyntaxException{
        this.courses = new LinkedList<>();
        fileHandler = new CsvFileHandler(FileSearch.SearchForFile("ExercisesDataBase.csv"));
        init();
    }

    public void addNewCourseWorksheet(String course)  {

        for(int i = 0; i < courses.size();i++){
            // Check if the course already exist in the db
            String data = ((CourseWorksSheet)courses.get(i)).getCourse();
            if(data.equals(course))
                return;
        }
        CourseWorksSheet db = new CourseWorksSheet(course);
        courses.add(db);
    }

    public void addExerciseToDBFile(String course, String[] data) throws IOException{
        for(int i = 0; i < courses.size();i++) {
            if (courses.get(i).getCourse().equals(course))
                courses.get(i).addToDB(data,true);
        }
    }

    /**
     * Find the Exercise value from the CourseWorksheet dictionary
     * @param course - the course the assignment is part of
     * @param ex_number - the id number of the exercise in the ExerciseTableWin menu
     * @return
     */
    public Exercise getExerciseDetails(String course, int ex_number){
        for(int i = 0; i < courses.size();i++){
            String data = ((CourseWorksSheet)courses.get(i)).getCourse();
            if(data.equals(course))
                return courses.get(i).getExInfo(ex_number);
        }
        return null;
    }

    /**
     * Add a new exercise data to the db file on the local system and to the
     * course ExerciseDB object
     * @param course - existing course in the system
     * @param data - new exercise to add in String[] object
     */
    public void addExerciseToDS(String course, String[] data) throws IOException,ParseException {
        for(int i = 0; i < courses.size();i++){
            if(courses.get(i).getCourse().equals(course))
                    courses.get(i).newValue(data);
        }
    }

    public CourseWorksSheet getNode(String course) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourse().equals(course))
                return courses.get(i);
        }
        return null;
    }

    /**
     * Used to get a node (that is a CourseWorkSheet) in a specific place in the list
     * @param index the place in the list to look for
     * @return
     */
    public CourseWorksSheet getNode(int index) {
        return courses.get(index);
    }

    /**
     * Get the size of the linked list data structure used by the object
     * @return the size of the courses list
     */
    public int getSize(){return courses.size();}

    /**
     * This function will read from the db file and create new ExerciseDB
     * for each course in the db
     */
    public void init() throws ParseException, NullPointerException , IOException{
        List<String> data;
        String[] ex_details = new String[5];
        data = fileHandler.Read();
        // If the file is empty
        if(data.size() == 0)
            return;
        // Create CourseWorkSheet DS for each new course found in the db
        int i = 0;
        while (i<data.size()){
            addNewCourseWorksheet(data.get(i));
            i+=5;
        }
        // Get the first work before the loop
        ex_details[0] = data.get(0);
        ex_details[1] = data.get(1);
        ex_details[2] = data.get(2);
        ex_details[3] = data.get(3);
        ex_details[4] = data.get(4);
        addExerciseToDS(data.get(0),ex_details);
        i = 5;
        while (i<data.size()){
            // Index difference is 4 cells
            ex_details[0] = data.get(i);
            ex_details[1] = data.get(i+1);
            ex_details[2] = data.get(i+2);
            ex_details[3] = data.get(i+3);
            ex_details[4] = data.get(i+4);
            addExerciseToDS(data.get(i),ex_details);
            i+=5;
        }
    }

    /**
     * This function is modeled by the observable design pattern.
     * When an Exercise get updated, this function will get called and
     * update the database in the change.
     * @param add - if false the update will not be inserted in the db
     * @throws IOException - The db file couldn't be opened.
     */
    public static void UpdateDB(Exercise updated_exercise,boolean add) throws IOException{
        if(add) {
            // Update the CourseToWorkSheetDict DS with the change and update the correct exercise instance
            for (int i = 0; i < courses.size(); i++) {
                if (updated_exercise.getCourse().equals(courses.get(i).getCourse())) {
                    courses.get(i).setExecInfo(updated_exercise.getId(), updated_exercise);
                }

            }
        }
        // Rewrite all the update data to the db file
        for (int i = 0; i < courses.size(); i++) {
            int counter = 1;
            CourseWorksSheet courseWorksSheet = courses.get(i);
            String[] data = new String[5];
            boolean flag = false;
            while (counter < courseWorksSheet.getSize()+1){
                // Need a flag to check if the exercise we check is the first one.
                // If it is the first, we need to first rewrite all the data with the first exercise.
                // After that we need to append all the other exercises data
                if(counter == 1 && i == 0)
                    flag = false;
                else
                    flag = true;
                Exercise ex = courseWorksSheet.getExInfo(counter);
                data[0] = courseWorksSheet.getCourse();
                data[1] = ex.getDue_date_old_format();
                data[2] = ex.getDone();
                data[3] = ex.getUpdate_date().toString();
                data[4] = ex.getGrade();
                // Append the data to the database
                courseWorksSheet.addToDB(data,flag);
                counter++;
            }
        }
    }

    /**
     * This function will try delete the exercise given as an argument from the DS CourseWorkSheet
     * And from the db file.
     * @param exercise - The exercise to delete.
     * @throws NullPointerException - if the file is empty
     * @throws IOException - if data in invalid.
     */
    public void deleteData(Exercise exercise) throws NullPointerException, IOException{
        if(exercise == null || courses.size() == 0) {
            fileHandler.CleanFile();
            throw new NullPointerException("No exercises are in the db");
        }
        int empty_data = 0; // Uses to count empty CourseWorkSheets
        for (int i = 0; i < courses.size(); i++) {
            if (exercise.getCourse().equals(courses.get(i).getCourse()))
                courses.get(i).deleteExercise(exercise);
            if(courses.get(i).getSize() == 0)
                empty_data++;
         }

        // if All the data was deleted from the DS clean the entire
        // db file from the data.
        // empty data is a counter for how much objects to delete
        if(empty_data == courses.size())
            fileHandler.CleanFile();
    }

    /**
     * This function will write the backup data to the main db file
     * @throws IOException - if data in invalid.
     */
    public static void Backup() throws UseBackupFailureExecption {
        // Try using the backup data
        BackupFileHandler backupFileHandler = new BackupFileHandler();
        backupFileHandler.writeFromBackupToMain("ExercisesDataBase.csv");
    }
}
