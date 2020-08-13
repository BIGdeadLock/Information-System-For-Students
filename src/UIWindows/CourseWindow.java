package UIWindows;

import Tools.Commands.OSCommand;
import Tools.Files.FileSearch;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CourseWindow {

    public static void start(ArrayList<String> course_data) {

        Stage windows = new Stage();
        windows.setTitle("Courses information system by E.Y");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(10);
        grid.setHgap(20);

        Label course_name_label = new Label("Course name: ");
        course_name_label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(course_name_label,0,0);

        Label course_name_val = new Label(course_data.get(0).replace("Course name: ",""));
        course_name_val.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setConstraints(course_name_val,1,0);

        Label course_number_label = new Label("Course number: ");
        course_number_label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(course_number_label,0,1);

        Label course_number_val = new Label(course_data.get(1).replace("Course number: ",""));
        course_number_val.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setConstraints(course_number_val,1,1);

        Label course_lectures_label = new Label("Course lectures times:");
        course_lectures_label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(course_lectures_label,0,2);

        Label course_lectures_val = new Label(course_data.get(2).replace("Lectures: ",""));
        course_lectures_val.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setConstraints(course_lectures_val,1,2);

        Label course_cancel_label = new Label("Canceled this week? " +
                course_data.get(3).replace("Lect canceled: ",""));
        GridPane.setConstraints(course_cancel_label,3,2);

        Label course_practice_label = new Label("Course practice times:");
        course_practice_label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(course_practice_label,0,3);


        Label course_practice_val = new Label(course_data.get(5).replace("Practices: ",""));
        course_practice_val.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        GridPane.setConstraints(course_practice_val,1,3);

        Label practice_cancel_label = new Label("Canceled this week? " +
                course_data.get(6).replace("Prac canceled: ",""));
        GridPane.setConstraints(practice_cancel_label,3,3);

        grid.getChildren().addAll(course_name_label,course_name_val,course_number_label,course_number_val);
        grid.getChildren().addAll(course_lectures_label,course_lectures_val,course_practice_label,course_practice_val,
                course_cancel_label,practice_cancel_label);

        Label lecturers_label = new Label("Course lecturers:");
        lecturers_label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(lecturers_label,0,4);
        grid.getChildren().addAll(lecturers_label);
        int index = StaffValueLabel(grid, course_data.subList(9,course_data.size()));

        Label instructors_label = new Label("Course instructors:");
        instructors_label.setFont(new Font("Arial", 20));
        GridPane.setConstraints(instructors_label,0,index+1);
        grid.getChildren().addAll(instructors_label);
        index = StaffValueLabel(grid, course_data.subList(course_data.indexOf("Instructors:")+1,course_data.size()));

        Button syllabus_button = new Button();
        syllabus_button.setText("להצגת סילבוס הקורס לחץ כאן");
        syllabus_button.setOnAction(e -> displaySyllabus(course_data.get(1).replace("Course number: ","")));
        syllabus_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        syllabus_button.setStyle("-fx-background-color: #a6b5c9");
        grid.add(syllabus_button, 0 , index + 3);

        Button worksheet_button = new Button();
        worksheet_button.setText("לקבלת רשימת כל תרגלי הבית לחץ כאן");
        worksheet_button.setOnAction(e -> displayWorksheet(course_data.get(0).replace("Course name: ",""),
                getCourseWorkWeights(course_data.get(0).replace("Course name: ",""))));
        worksheet_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        worksheet_button.setStyle("-fx-background-color: #a6b5c9");
        grid.add(worksheet_button, 1 , index + 3);

        Button lectures_button = new Button();
        lectures_button.setText("לקבל רשימת מעקב אחר ההרצאות הזמינות");
        lectures_button.setOnAction(e -> displayLectures(course_data.get(0).replace("Course name: ","")));
        lectures_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        lectures_button.setStyle("-fx-background-color: #a6b5c9");
        grid.add(lectures_button, 2 , index + 3);

        Button tirgulim_button = new Button();
        tirgulim_button.setText("לקבל רשימת מעקב אחר התרגולים הזמינים");
        tirgulim_button.setOnAction(e -> displyTrigulim(course_data.get(0).replace("Course name: ","")));
        tirgulim_button.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        tirgulim_button.setStyle("-fx-background-color: #a6b5c9");
        grid.add(tirgulim_button, 3 , index + 3);

        grid.setStyle("-fx-background-color: white");
        Scene scene = new Scene(grid);
        windows.setScene(scene);
        windows.show();
    }

    /**
     * The function will get a course name in hebrew and will return the correct file format
     * for the course in english (i.e OS, APT ...)
     * @param course_name - name in hebrew
     * @return Correct course name format in english.
     */
    private static String getCourseWorkWeights(String course_name){
        if(course_name.contains("מבוא למערכות הפעלה"))
            return "25";
        else if(course_name.contains("מבוא לכלכלה לתעשייה וניהול"))
            return "0";
        else if(course_name.contains("מודלים חישוביים ואלגוריתמים"))
            return "15";
        else if(course_name.contains("מימוש מערכות בסיסי נתונים"))
            return "25";
        else if(course_name.contains("נושאים מתקדמים בתכנות"))
            return "25";
        else if(course_name.contains("שיטות נומריות בתעשייה"))
            return "10";

        return null;
    }

    /**
     * Used to display the tirgulim table
     * @param course_name - name of course in hebrew
     */
    private static void displyTrigulim(String course_name) {
        if(course_name.contains("מבוא למערכות הפעלה"))
            CourseTirgulimWindow.start(course_name,"OSTirg");
        else if(course_name.contains("מבוא לכלכלה לתעשייה וניהול"))
            CourseTirgulimWindow.start(course_name,"EcoTirg");
        else if(course_name.contains("מודלים חישוביים ואלגוריתמים"))
            CourseTirgulimWindow.start(course_name,"MNATirg");
        else if(course_name.contains("מימוש מערכות בסיסי נתונים"))
            CourseTirgulimWindow.start(course_name,"DBTirg");
        else if(course_name.contains("נושאים מתקדמים בתכנות"))
            CourseTirgulimWindow.start(course_name,"ATPTirg");
        else if(course_name.contains("שיטות נומריות בתעשייה"))
            CourseTirgulimWindow.start(course_name,"NUMTirg");
    }
    /**
     * Used to display the lectures table
     * @param course_name - name of course in hebrew
     */
    private static void displayLectures(String course_name) {
        if(course_name.contains("מבוא למערכות הפעלה"))
            CourseLecturesTableWindow.start(course_name,"OSLecs");
        else if(course_name.contains("מבוא לכלכלה לתעשייה וניהול"))
            CourseLecturesTableWindow.start(course_name,"EcoLecs");
        else if(course_name.contains("מודלים חישוביים ואלגוריתמים"))
            CourseLecturesTableWindow.start(course_name,"MNALecs");
        else if(course_name.contains("מימוש מערכות בסיסי נתונים"))
            CourseLecturesTableWindow.start(course_name,"DBLecs");
        else if(course_name.contains("נושאים מתקדמים בתכנות"))
            CourseLecturesTableWindow.start(course_name,"ATPLecs");
        else if(course_name.contains("שיטות נומריות בתעשייה"))
            CourseLecturesTableWindow.start(course_name,"NUMLecs");
    }


    private static void displayWorksheet(String course_name,String Weight) {
        CourseExercisesTableWindow.start(course_name,Weight);
    }

    /**
     * This function will pop up the syllabus for the use input course
     * @param course_number - course number as shown in the syllabus
     */
    private static void displaySyllabus(String course_number) {
        String syllabus_file = FileSearch.SearchForFile("open.bat");
        String syllabus_command = "cmd /c " + "\""+ syllabus_file +"\"" + " "+ course_number;
        OSCommand.Run(syllabus_command);
    }

    /**
     * This function is used to add items to the Course window UI
     * @param grid - an existing GridPane
     * @param lecturers_data - List<String> containing string to add to the grid
     * @return int - The row on the grid that was last updated by the function
     */
    private static int StaffValueLabel(GridPane grid, List<String> lecturers_data){
        int row = 0;
        int column = 1;
        int i = 0;
        boolean addded = false;
        while (!(lecturers_data.get(i).equals("")))
        {
            // Replace the * to an empty line in the UI
            if(lecturers_data.get(i).equals("*"))
            {
                lecturers_data.set(i,lecturers_data.get(i).replace("*", " "));
            }

            Label staff = new Label(lecturers_data.get(i));
            staff.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            if(!isConstrainExist(grid,column,row)  && !addded) {
                GridPane.setConstraints(staff, column, row);
                grid.getChildren().addAll(staff);
                addded = true;
            }
            // else - there is an existing label on the column & row
            else
                row++;

            if(addded ) {
                i++;
                addded = false;
            }
        }
        return row;
    }

    /**
     * This function will check if the grid[column][row] is set or empty
     * @param gridPane The instance grid pane
     * @param column column to check
     * @param row row to check
     * @return true - index is taken, false - available
     */
    private static boolean isConstrainExist(GridPane gridPane,int column, int row ){
        boolean result = false;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            int nr = gridPane.getRowIndex(node);
            int nc = gridPane.getColumnIndex(node);
            if(nr == row && nc == column) {
                result = true;
                break;
            }
        }
        return result;
    }


}