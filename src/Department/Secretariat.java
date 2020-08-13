package Department;
import Class.Secretary;

import java.util.ArrayList;

public class Secretariat {

    private ArrayList<Secretary> secretaries;

    public Secretariat() {
        this.secretaries = new ArrayList<Secretary>();
    }

    public void addSecratery(Secretary secretary){
        secretaries.add(secretary);
    }

    @Override
    public String toString() {
        String res =  "The relevant secretaries for SISE are:\n";
        for(Secretary sec : secretaries)
                res+= "\t" + sec + "\n";

        return res;
    }
}
