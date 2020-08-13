package Tools.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FileHandler {

    /**
     * Create the file that the FileHandle Class will handle
     * @return true - The file was created or was already exists on the system, false - There was an error
     */
    public boolean CreateFile() throws IOException;

    /**
     * Create the file that the FileHandle Class will handle
     * @param file_path - The file path + name of the file to create the new file.
     * @return true - The file was created or was already exists on the system, false - There was an error
     */
    public boolean CreateFile(String file_path) throws IOException;

    /**
     * Will read the file line by line
     * @return Array of String. Each string is a line
     * @throws IOException can't access the file for some reason
     * @throws FileNotFoundException can't find the file given
     */
    public List<String> Read() throws FileNotFoundException,IOException;

    /**
     * This function reads a block of data between the start word and the end word from the file
     * @param start - The word to start the read action from
     * @param stop - The word to stop the read action at
     * @return - Array of String. Each string is a line
     * @throws Exception
     */
    public ArrayList<String> ReadBlock(String start, String stop) throws Exception;

    /**
     * This function will used to write to the end of the file
     * @param data - new data to write to the file
     * @param append - true - append to the last line, false - write to a new file
     * @throws IOException - Can't handle the file, may be cause of wrong path or invalid data.
     */
    public void WriteToFile(String[] data,boolean append) throws IOException;

    /**
     * This will clean all data from the file making it empty.
     * The file is the file that will be created in runtime by the constructor
     * @throws IOException - Can't handle the file, may be cause of wrong path or invalid data.
     */
    public void CleanFile() throws IOException;

    /**
     * Get the file size in Bytes
     * @return double the size of the file.
     */
    public double getFileSize();



}
