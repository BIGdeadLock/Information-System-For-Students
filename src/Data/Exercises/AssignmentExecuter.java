package Data.Exercises;

import Data.DataStructures.Exercise;
import Tools.Commands.OSCommand;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class AssignmentExecuter {

    /**
     * Used to execute command to open the exercise given
     * @param assignment String[]: String[0] = course name, String[1] = assignment number
     */
    public static void execute(String[] assignment){
        File curDir = new File("").getAbsoluteFile();
        String exercise_course_path = curDir.getAbsolutePath() + "\\src\\Data\\Exercises\\" + assignment[0];
        String assignment_path = find(new String[]{exercise_course_path,
                "Ex" + assignment[1]});
        OSCommand.Run("cmd /c \"" + assignment_path +"\"");
    }

    /**
     * Get the absolute path of the assignment file
     * @param key - the name assignment String[] given on execute
     * @return String - the absolute path of the file
     */
    private static String find(String[] key){

        File curDir = new File(key[0]);
        Stack<File> files = new Stack<>();
        files.add(curDir);
        while (!files.empty()) {
            curDir = files.pop();
            File[] filesList = curDir.listFiles();
            for (File f : filesList) {
                if (f.isDirectory()) {
                    files.push(f);
                }
                if (f.isFile() && f.toString().contains(key[1]))
                    return f.getAbsolutePath();
            }
        }
        return null;
    }

}
