package Data.DataStructures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Class.*;
import Tools.Files.CsvFileHandler;
import Tools.Files.FileHandler;
import Tools.Files.FileSearch;

public class TirgulimDS {

    private static ArrayList<Tirgul> tirgulim;
    private String course_name;
    private static FileHandler fileHandler;

    public TirgulimDS(String course_name) throws NullPointerException , IOException{
        this.course_name = course_name;
        this.tirgulim = new ArrayList<>();
        fileHandler = new CsvFileHandler(FileSearch.SearchForFile(course_name+".csv"));
        init();
    }

    private void init() throws NullPointerException , IOException {

        List<String> data = fileHandler.Read();
        // If the file is empty
        if(data.size() == 0)
            return;

        for (int i = 0; i < data.size(); i+=3)
            tirgulim.add(new Tirgul(Integer.valueOf(data.get(i)),data.get(i+1),data.get(i+2)));

    }

    public ArrayList<Tirgul> getTirgulim(){
        return this.tirgulim;
    }

    public void setTirgulim(Tirgul tirgul) throws IOException{
        tirgulim.add(tirgul);
        String[] data = {String.valueOf(tirgul.getNumber()),tirgul.getTopic(),tirgul.getStatus()};
        fileHandler.WriteToFile(data,true);
    }

    public static void UpdateDB(Tirgul tirgul) throws IOException{
        String id,topic,status;
        boolean add = false;

        id = String.valueOf(tirgulim.get(0).getNumber());
        topic = tirgulim.get(0).getTopic();
        status = tirgulim.get(0).getStatus();
        if(tirgul == tirgulim.get(0)){
            topic = tirgul.getTopic();
            status = tirgul.getStatus();
            add = true;
        }

        String[] updated_data = {id,topic,status};
        // Overwrite the data with the first lecture
        fileHandler.WriteToFile(updated_data,false);
        for (int i = 1; i < tirgulim.size(); i++) {
            id = String.valueOf(tirgulim.get(i).getNumber());
            topic = tirgulim.get(i).getTopic();
            status = tirgulim.get(i).getStatus();
            if(tirgul == tirgulim.get(i) && !add){
                topic = tirgul.getTopic();
                status = tirgul.getStatus();
            }
            //Append the updated data
            updated_data = new String[]{id,topic,status};
            fileHandler.WriteToFile(updated_data,true);
        }

    }

    // Update the DB with the current state of the DS
    public static void UpdateDB() throws IOException{
        String id,topic,status;
        id = String.valueOf(tirgulim.get(0).getNumber());
        topic = tirgulim.get(0).getTopic();
        status = tirgulim.get(0).getStatus();
        String[] updated_data = {id,topic,status};
        // Overwrite the data with the first lecture

        fileHandler.WriteToFile(updated_data,false);
        for (int i = 1; i < tirgulim.size(); i++) {
            id = String.valueOf(tirgulim.get(i).getNumber());
            topic = tirgulim.get(i).getTopic();
            status = tirgulim.get(i).getStatus();
            //Append the updated data
            updated_data = new String[]{id,topic,status};
            fileHandler.WriteToFile(updated_data,true);
        }
    }


    /**
     * Delete the tirgul from the DS and from the DB
     * @param tirgul - Tirgul instance to delete from the DB
     * @throws IOException
     */
    public static void deleteData(Tirgul tirgul) throws IOException{
        if(tirgul == null)
            return;

        // If there is not data in the DS -> delete all the data from the DB file.
        if( tirgulim.size() == 0) {
            fileHandler.CleanFile();
            throw new NullPointerException("No exercises are in the db");
        }
        // Delete the data from the DS
        tirgulim.remove(tirgul);
        // if All the data was deleted from the DS clean the entire
        // db file from the data.
        if(tirgulim.size() == 0)
            fileHandler.CleanFile();
        // Update the db with the new change
        UpdateDB();
    }
}
