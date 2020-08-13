package Errors;

public class UseBackupFailureExecption extends Exception{

    public UseBackupFailureExecption()
    {
        super("Write from backup file to main failed");
    }
}
