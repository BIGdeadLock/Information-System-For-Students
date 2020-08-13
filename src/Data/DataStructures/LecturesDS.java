package Data.DataStructures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Class.Lecture;
import Tools.Files.CsvFileHandler;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;

public class LecturesDS {

    private static ArrayList<Lecture> lectures;
    private String course_name;
    private static FileHandler fileHandler;

    public LecturesDS(String course_name) throws NullPointerException , IOException{
        this.course_name = course_name;
        this.lectures = new ArrayList<>();
        fileHandler = new CsvFileHandler(FileSearch.SearchForFile(course_name+".csv"));
        init();
    }

    private void init() throws NullPointerException , IOException {

        List<String> data = fileHandler.Read();
        // If the file is empty
        if(data.size() == 0)
            return;

        for (int i = 0; i < data.size(); i+=3)
            lectures.add(new Lecture(Integer.valueOf(data.get(i)),data.get(i+1),data.get(i+2)));

    }

    public ArrayList<Lecture> getLectures(){
        return this.lectures;
    }

    public void setLectures(Lecture lecture) throws IOException{
        lectures.add(lecture);
        String[] data = {String.valueOf(lecture.getNumber()),lecture.getTopic(),lecture.getStatus()};
        fileHandler.WriteToFile(data,true);
    }

    public static void UpdateDB(Lecture lecture) throws IOException{
        String id,topic,status;
        boolean add = false;

        id = String.valueOf(lectures.get(0).getNumber());
        topic = lectures.get(0).getTopic();
        status = lectures.get(0).getStatus();
        if(lecture == lectures.get(0)){
            topic = lecture.getTopic();
            status = lecture.getStatus();
            add = true;
        }

        String[] updated_data = {id,topic,status};
        // Overwrite the data with the first lecture
        fileHandler.WriteToFile(updated_data,false);
        for (int i = 1; i < lectures.size(); i++) {
            id = String.valueOf(lectures.get(i).getNumber());
            topic = lectures.get(i).getTopic();
            status = lectures.get(i).getStatus();
            if(lecture == lectures.get(i) && !add){
                topic = lecture.getTopic();
                status = lecture.getStatus();
            }
            //Append the updated data
            updated_data = new String[]{id,topic,status};
            fileHandler.WriteToFile(updated_data,true);
        }
    }

    // Update the DB with the current state of the DS
    public static void UpdateDB() throws IOException{
        String id,topic,status;
        id = String.valueOf(lectures.get(0).getNumber());
        topic = lectures.get(0).getTopic();
        status = lectures.get(0).getStatus();
        String[] updated_data = {id,topic,status};
        // Overwrite the data with the first lecture
        fileHandler.WriteToFile(updated_data,false);

        for (int i = 1; i < lectures.size(); i++) {
            id = String.valueOf(lectures.get(i).getNumber());
            topic = lectures.get(i).getTopic();
            status = lectures.get(i).getStatus();
            //Append the updated data
            updated_data = new String[]{id,topic,status};
            fileHandler.WriteToFile(updated_data,true);
        }
    }

    public static void deleteData(Lecture lecture) throws IOException{
        if(lecture == null)
            return;

        // If there is not data in the DS -> delete all the data from the DB file.
        if( lectures.size() == 0) {
            fileHandler.CleanFile();
            throw new NullPointerException("No exercises are in the db");
        }
        // Delete the data from the DS
        lectures.remove(lecture);
        // if All the data was deleted from the DS clean the entire
        // db file from the data.
        if(lectures.size() == 0)
            fileHandler.CleanFile();
        // Update the db with the new change
        UpdateDB();
    }
}
