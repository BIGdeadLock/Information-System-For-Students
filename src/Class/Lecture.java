package Class;

import Data.DataStructures.LecturesDS;

import java.io.IOException;

public class Lecture {

    protected int number;
    protected String topic;
    protected String status;
    protected String Course;

    public Lecture(int number_of_lecture) {
        this.number = number_of_lecture;
        topic = "Lecture Number " + String.valueOf(number_of_lecture);
        status = "Waiting";
    }

    public Lecture(int lectured_id, String lecture_topic, String status) {
        this.number = lectured_id;
        this.topic = lecture_topic;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) throws IOException{
        this.topic = topic;
        Update();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(boolean watched) throws IOException {

        if(watched)
          this.status = "Done";

        else
            this.status = "Waiting";

        Update();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lectures = (Lecture) o;
        return number == lectures.number;
    }

    public void Update() throws IOException{
        LecturesDS.UpdateDB(this);
    }

}
