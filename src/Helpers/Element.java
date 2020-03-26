package Helpers;

import Interfaces.Stepable;
import Interfaces.Updateable;
import elements.Bus;

public abstract class Element implements Updateable, Stepable {

    protected boolean active;
    protected Bus[] busses;

    public Element(int busConceptions) {
        busses = new Bus[busConceptions];
    }
}
