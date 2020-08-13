package Tools.Files;

import sun.security.krb5.internal.crypto.Des;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;


public class PictureHandle {

    private String picture_path;

    public PictureHandle(String picture_path) {
        this.picture_path = picture_path;
    }


    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public void showImage() {

        try {


            File file = new File(this.picture_path);
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
            System.out.println("See the picture");
        }
        catch (IOException io){
            System.out.println("The image path is wrong");
        }
    }
}
