package Tools.Files;

import java.io.*;
import java.util.Stack;


/**
 * This class is a wrapper for the file search function.
 */
public class FileSearch
{
    /**
     * This function will search for a file in the current directory
     * and all sub directories.
     * @param FILE_NAME - the file's name to search for.
     * @return String - file path, null - file was not found.
     */
    public static String SearchForFile(String FILE_NAME) {
        File curDir = new File(".");
        Stack<File> files = new Stack<>();
        files.add(curDir);
        while (!files.empty()) {
            curDir = files.pop();
            File[] filesList = curDir.listFiles();
            for (File f : filesList) {
                if (f.isDirectory()) {
                    files.push(f);
                }
                if (f.isFile() && f.toString().contains(FILE_NAME)) {
                    String temp = f.getAbsolutePath();
                    int dot = temp.indexOf(".");
                    String path_part1 = temp.substring(0,dot-1);
                    String path_part2 = temp.substring(dot+1);
                    return path_part1 + path_part2;
                }
            }
        }
        return null;
    }

}