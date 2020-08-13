package Department;

import Class.Lecturer;
import Class.Tutor;
import Class.StaffMember;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Course {
/*
This class will hold all the Course data
 */
    private String coursename;
    private String course_number;
    private String lectures_times;
    private String practice_times;
    private ArrayList<Lecturer> lecturers;
    private ArrayList<Tutor> tutors;
    private ArrayList<StaffMember> staff;

    public Course(String coursename, String course_number,String lectures_times, String practice_times) {
        this.coursename = coursename;
        this.course_number = course_number;
        this.lectures_times = lectures_times;
        this.practice_times = practice_times;
        this.lecturers = new ArrayList<Lecturer>();
        this.tutors = new ArrayList<Tutor>();
        this.staff = new ArrayList<StaffMember>();
    }

    public ArrayList<Lecturer> getLecturers() {
        return lecturers;
    }

    public ArrayList<Tutor> getTutors() {
        return tutors;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getCourse_number() {
        return course_number;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void addLecturers(String first_name,String last_name, String office_hours, String email){

        Lecturer lec = new Lecturer(first_name,last_name,office_hours,email);
        lecturers.add(lec);
        staff.add(lec);
    }

    public void addTutor(String first_name,String last_name, String office_hours, String email){

        Tutor tut = new Tutor(first_name,last_name,office_hours,email);
        tutors.add(tut);
        staff.add(tut);

    }

    public void ListAllStaff(){
        try {
            int i = 0;
            for (Lecturer lecturer : lecturers) {
                Thread.sleep(500);
                System.out.println(String.format("%d. %s ", i, lecturer.getFull_name()));
                i++;
            }

            for (Tutor tutor : tutors) {
                Thread.sleep(500);
                System.out.println(String.format("%d. %s ", i, tutor.getFull_name()));
                i++;
            }
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void ListAllLecturers() {
            int i = 0;
            for (Lecturer lecturer : lecturers) {
                System.out.println(String.format("%d. %s ", i, lecturer));
                i++;
            }
    }

    public void ListAllTutors(){
        int i =0;
        for(Tutor tutor: tutors) {
            System.out.println(String.format("%d. %s ", i, tutor));
            i++;
        }
    }

    private void ChooseStaffMember(){
        try {
            System.out.println("Those are all the people running the course:");
            TimeUnit.SECONDS.sleep(2);
            ListAllStaff();

            System.out.println("Choose the number of person you need (for example 3. Lecturer:... -> press 3 or " +
                    "10. Tutor.... -> press 10)");
            Scanner reader = new Scanner(System.in);
            int i = reader.nextInt();
            TimeUnit.SECONDS.sleep(1);

            System.out.println(staff.get(i));
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void getLecturerDetails(String[] name_splited){

        try {

            for (Lecturer lecturer : lecturers) {

                if (lecturer.getFirst_name().equals(name_splited[0]) && lecturer.getLast_name().equals(name_splited[1]))
                    System.out.println(lecturer);


                else {
                    //if only the last name does not match
                    if (lecturer.getFirst_name().equals(name_splited[0])) {
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("The last name does match any lecturer of the course.");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println(String.format("Maybe you meant %s %s? (Y/N)",
                                lecturer.getFirst_name(), lecturer.getLast_name()));


                        Scanner reader1 = new Scanner(System.in);
                        String answer = reader1.nextLine();
                        //The guess was correct -> print the lecturer
                        if (answer.equals("Y") || answer.equals("y") || answer.equals("yes"))
                            System.out.println(lecturer);

                        else {
                            //Else the use gave incorrect name -> print all the lecturers and let him choose
                            while (!answer.equals("q")) {
                                ChooseStaffMember();
                                TimeUnit.SECONDS.sleep(4);
                                System.out.println("To quit press q or to try again press any other key");
                                answer = reader1.nextLine();

                            }

                        }
                    } else { //Else the use gave incorrect name in the first place-> print all the lecturers and let him choose
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("The name you mention does not match any staff member name (be serious dude!)");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("Let me help you!");
                        TimeUnit.SECONDS.sleep(2);
                        String answer = "";
                        while (!answer.equals("q")) {
                            ChooseStaffMember();
                            TimeUnit.SECONDS.sleep(4);
                            System.out.println("To quit press q or to try again press any other key");
                            Scanner reader = new Scanner(System.in);
                            answer = reader.nextLine();
                        }//while

                    }//inner first else

                }//outer else
                System.out.println("GOOD BYE! TELL YOUR FRIENDS");
            }// for loop

        } // try

        catch (InterruptedException ex){
            Thread.currentThread().interrupt();

        }// catch

    }// function

    public void getTutorDetails(String[] name_splited){

        try {

            for (Tutor tutor : tutors) {

                if (tutor.getFirst_name().equals(name_splited[0]) && tutor.getLast_name().equals(name_splited[1]))
                    System.out.println(tutor);


                else {
                    //if only the last name does not match
                    if (tutor.getFirst_name().equals(name_splited[0])) {
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("The last name does match any tutor of the course.");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println(String.format("Maybe you meant %s %s? (Y/N)",
                                tutor.getFirst_name(), tutor.getLast_name()));

                        Scanner reader1 = new Scanner(System.in);
                        String answer = reader1.nextLine();
                        TimeUnit.SECONDS.sleep(2);

                        //The guess was correct -> print the lecturer
                        if (answer.equals("Y") || answer.equals("y") || answer.equals("yes"))
                            System.out.println(tutor);

                        else {
                            //Else the use gave incorrect name -> print all the lecturers and let him choose
                            while (!answer.equals("q")) {
                                ChooseStaffMember();
                                TimeUnit.SECONDS.sleep(4);
                                System.out.println("To quit press q");
                                answer = reader1.nextLine();
                            }
                            System.out.println("GOOD BYE! TELL YOUR FRIENDS");
                        }

                    } else {
                        //Else the use gave incorrect name in the first place-> print all the lecturers and let him choose
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("The name you mention does not match any staff member name (be serious dude!)");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("Let me help you!");
                        TimeUnit.SECONDS.sleep(3);
                        String answer = "";
                        while (!answer.equals("q")) {
                            ChooseStaffMember();
                            TimeUnit.SECONDS.sleep(4);
                            System.out.println("To quit press q");
                            Scanner reader = new Scanner(System.in);
                            answer = reader.nextLine();
                        }//while

                    }//inner first else

                }//outer else

            }// for loop

        } // try

        catch (InterruptedException ex){
            Thread.currentThread().interrupt();

        }// catch

    }// function

    public void getCourseEmployeeDetails(String name,String position){
        String[] name_splited = name.split(" ");

        System.out.println("Getting the details for the staff member...");

        if(position.equals("Lecturer"))
               getLecturerDetails(name_splited);
        else
            getTutorDetails(name_splited);
    }

    public void printCourseDetails() {
        System.out.println("Course Details: " + "\n" +
                " course name is " + coursename + "\n" +
                " lectures_times are: " + lectures_times + "\n" +
                " practice_times are: " + practice_times + "\n" +
                " staff members are:");
        ListAllStaff();

    }


}
