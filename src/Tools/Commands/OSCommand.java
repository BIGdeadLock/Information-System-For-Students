package Tools.Commands;

public class OSCommand {


    public static void Run(String command_to_run) {
        try {
            Runtime rn=Runtime.getRuntime();
            Process pr=rn.exec(command_to_run);
        }
        catch (Exception e) { e.printStackTrace();}
    }

}
