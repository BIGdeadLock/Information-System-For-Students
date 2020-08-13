package Errors;

import java.io.IOException;

public class BackupFailureException extends Exception {

    public BackupFailureException()
    {
        super("Update to the backup file failed");
    }
}
