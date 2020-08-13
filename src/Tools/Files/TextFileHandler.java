package Tools.Files;
import Tools.Files.FileHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TextFileHandler extends AFileHandler {

    public TextFileHandler(String file_path) {
        super(file_path);
    }

    public TextFileHandler() {
        super();

    }

    @Override
    public List<String> Read() throws FileNotFoundException,IOException {
        return Files.readAllLines(Paths.get(file_path));
    }

    @Override
    public ArrayList<String> ReadBlock(String start, String stop) throws Exception{

        ArrayList<String> res = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(this.file_handle));

        String st;

        while (!(st = br.readLine()).contains(start))
            continue;

        res.add(st);
        while (!(st = br.readLine()).contains(stop))
            res.add(st);

        return res;
    }

    @Override
    public void WriteToFile(String[] commands,boolean append) throws IOException{
        file_writer = new FileWriter(file_path,append);
        for(String command : commands)
            file_writer.write(command + "\n");

        file_writer.close();

    }

}
