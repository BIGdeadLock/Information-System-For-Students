package Tools.Files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvFileHandler extends AFileHandler {


    public CsvFileHandler(String file_path) {
        super(file_path);

    }

    public CsvFileHandler() {
        super();
    }

    @Override
    public List<String> Read() throws FileNotFoundException,IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(this.file_path));
        ArrayList<String> res = new ArrayList<>();
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            for (int i = 0; i < data.length; i++) {
                res.add(data[i]);
            }
        }
        csvReader.close();
        return res;
    }

    @Override
    public ArrayList<String> ReadBlock(String start, String stop) throws Exception {
        return null;
    }

    /**
     * This function write to the csv file
     * @param data - new data to write to the file
     * @param app - true - append data to existing data. false - overwrite the existing data with the new data.
     * @throws Exception
     */
    @Override
    public void WriteToFile(String[] data,boolean app) throws IOException {
        String rowData = String.join(",", data);

        //Check if the file is empty -> if yes write (not append) the data
        BufferedReader csvReader = new BufferedReader(new FileReader(this.file_path));
        String row = csvReader.readLine();
        if(row == null){
            file_writer = new FileWriter(file_path);
            file_writer.append(rowData);
            file_writer.append("\n");
            file_writer.flush();
            file_writer.close();

        }
        // Append data to existing file
        else if(app) {
            file_writer = new FileWriter(file_path, true);
            file_writer.append(rowData);
            file_writer.append("\n");
            file_writer.flush();
            file_writer.close();
        }
        else {
            file_writer = new FileWriter(file_path);
            file_writer.append(rowData);
            file_writer.append("\n");
            file_writer.flush();
            file_writer.close();
        }
    }

    @Override
    public String toString() {
        return file_path;
    }
}
