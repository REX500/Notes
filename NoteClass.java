package sample;

/**
 * Created by Filip on 07-04-2016.
 */
public class NoteClass {
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private String note;

    public NoteClass(String note){
        this.note = note;
    }

}
