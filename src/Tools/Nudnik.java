package Tools;

import Tools.Files.TextFileHandler;

public class Nudnik {


    private int task_interval; // In minutes
    private String path_to_batch;

    public Nudnik() {
        this.task_interval = 10;
        this.path_to_batch = "D:\\PrivateProject\\CourseProject\\src\\Tools";
    }



    public void Run(String[] command_to_run){
        try {
            Runtime.getRuntime().exec(command_to_run);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void createBatch(String message_to_present,int file_index){
        try {
            TextFileHandler f = new TextFileHandler(this.path_to_batch + "\\Annoyer" + file_index +".bat");
            f.WriteToFile(new String[]{"@if (true == false) @end /*!\n",
                    "@echo off\n",
                    "mshta \"about:<script src='file://%~f0'></script><script>close()</script>\" %*\n",
                    "goto :EOF */ \n",
                    "\n",
                    String.format("alert(\"%s!\");",message_to_present)},false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Nudnidks(){

        class Nudnik1{
            void annoy(){
                createBatch("YOU NEED TO STUDY",1);
            }
        }
        new Nudnik1().annoy();


        class Nudnik2{
            void annoy(){
                createBatch("GET YOUR LAZY ASS BACK TO WORK",2);
            }
        }
        new Nudnik2().annoy();

        class Nudnik3{
            void annoy(){
                createBatch("ALMOST DONE, YOU ARE AWSOME",3);
            }
        }
        new Nudnik3().annoy();


        String[] run1 = {"D:\\PrivateProject\\CourseProject\\src\\Tools\\Annoyer1.bat"};
        String[] run2 = {"D:\\PrivateProject\\CourseProject\\src\\Tools\\Annoyer2.bat"};
        String[] run3 = {"D:\\PrivateProject\\CourseProject\\src\\Tools\\Annoyer3.bat"};

        try {
            Run(run1);
            Thread.sleep(1500);
            Run(run2);
            Thread.sleep(1500);
            Run(run3);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
