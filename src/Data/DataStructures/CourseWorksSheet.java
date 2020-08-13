package Data.DataStructures;

import java.io.IOException;
import java.text.ParseException;

import java.util.ArrayList;


import Errors.BackupFailureException;
import Tools.Files.*;

public  class CourseWorksSheet {

    private ArrayList<Exercise> Execdb;
    private String course;
    private AFileHandler fileHandler;
    private BackupFileHandler backup = new BackupFileHandler();
    private int next;

    public CourseWorksSheet(String course) {
        this.course = course;
        next = 1;
        Execdb = new ArrayList<>();
        fileHandler = new CsvFileHandler(FileSearch.SearchForFile("ExercisesDataBase.csv"));
    }

    public String getCourse() {
        return course;
    }

    public int getSize() {
        return Execdb.size();
    }

    /**
     * Returns the Execdb object
     * @return Execdb object representing ArrayList of Exercises objects
     */
    public ArrayList<Exercise> getExecdb() {
        return Execdb;
    }

    public void setExecdb(ArrayList execdb) {
        Execdb = execdb;
    }

    /**
     * Check if an exercise exists in the database
     * @param exercise - exercise object ot check
     * @return true - exists, false - not exists
     */
    public boolean exerciseExist(Exercise exercise){
        return Execdb.contains(exercise);
    }

    /**
     * Get the Exercise object that the id points to
     * @param id - the id representing the exercise in the table
     * @return The Exercise the id points to
     */
    public Exercise getExInfo(int id){
        for (int i = 0; i < Execdb.size(); i++) {
            if(id == Execdb.get(i).getId())
                return Execdb.get(i);
        }
        return null;
    }

    /**
     * Gets a key - id representing an exercise in the Table worksheet and
     * set the new Exercise for the current id.
     * Meaning id 1 - Old Exercise -> id 1 - New Exercise
     * @param id
     * @param new_exercise
     */
    public void setExecInfo(int id, Exercise new_exercise) {
        for (int i = 0; i < Execdb.size(); i++) {
            if (id == Execdb.get(i).getId())
                Execdb.set(i, new_exercise);
        }
    }

    /**
     * Used to add new data to the Execdb data base.
     * In case the data does not exist in the db file it will add as well.
     * @param ex - exercise data used to create new Exercise data.
     * @throws Exception - Exception can be raised from the FileHander.WriteToFile function in case the FileWrite
     * object throws and error.
     */
    public void newValue(String[] ex) throws ParseException,IOException{
        Execdb.add(new Exercise(next,ex[1],ex[2], ex[3],course,ex[4]));
        next++;
    }

    /**
     * Used to add Data to the Exercise database file
     * @param data - data to add
     * @param AddorWrite - true: Overwrite the file with the existing data, false: append the data to the existing data
     */
    public void addToDB(String[] data, boolean AddorWrite) throws IOException {
        fileHandler.WriteToFile(data,AddorWrite);
    }

    /**
     * Used to add Data to the Backup Exercise database file.
     * Separate function was used to not contaminate the backup data with corrupt data.
     * @param data - data to add
     * @param AddorWrite - true: Overwrite the file with the existing data, false: append the data to the existing data
     */
    public void addToBackupDB(String[] data, boolean AddorWrite) throws BackupFailureException {
        backup.WriteToBackup(data,AddorWrite);
    }

    /**
     * Use to remove an exercise
     * @param exercise - exercise to remove from the array
     */
    public void deleteExercise(Exercise exercise){
        Execdb.remove(exercise);
        next = 1;
        for (int i = 0; i < Execdb.size(); i++) {
            Execdb.get(i).setId(next);
            next++;
        }
    }

}
