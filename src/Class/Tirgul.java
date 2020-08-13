package Class;

import Data.DataStructures.TirgulimDS;

import java.io.IOException;

public class Tirgul extends Lecture {

    public Tirgul(int number_of_lecture) {
        super(number_of_lecture);
        topic = "Tirgul Number " + String.valueOf(number_of_lecture);
    }

    public Tirgul(int lectured_id, String lecture_topic, String status) {
        super(lectured_id, lecture_topic, status);
    }

    @Override
    public void Update() throws IOException {
        TirgulimDS.UpdateDB(this);
    }
}
