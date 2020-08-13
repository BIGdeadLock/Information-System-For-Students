package Tools.Files;

import Data.DataStructures.Exercise;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

public abstract class AFileHandler implements FileHandler {
    protected String file_path;
    protected File file_handle;
    protected FileWriter file_writer;

    public AFileHandler(String file_path) {
        this.file_path = file_path;
        this.file_handle = new File(this.file_path);
    }

    public double getFileSize(){return file_handle.length();}

    public AFileHandler() {
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
        setFile_handle(file_path);
    }

    public void setFile_handle(String file_handle) {
        this.file_handle =  new File(this.file_path);
    }

    @Override
    public boolean CreateFile() throws IOException{
        if (file_handle.createNewFile())
            return true;

        return false;
    }

    @Override
    public boolean CreateFile(String file_path) throws IOException{
        File file = new File(file_path);
        if (file.createNewFile())
            return true;

        return false;
    }

    @Override
    public void CleanFile() throws IOException {
        new FileWriter(this.file_path, false).close();
    }

}
