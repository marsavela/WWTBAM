package es.serpat.wwtbam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patmonsi on 06/11/13.
 */
public class Friend {
    private List<String> name;


    public Friend(){
    }

    public Friend(ArrayList<String> name) {
        this.name = name;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
