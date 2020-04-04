package Commands;

import Helpers.Element;

public class MOVLW extends CommandBase {

    //TODO GIVE EVERY COMPONENT AN UNIQUE IDX
    public MOVLW() {
        super(new int[]{
                4, 5
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        //none
    }
}
