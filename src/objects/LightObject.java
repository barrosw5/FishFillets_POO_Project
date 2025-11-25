package objects;

import pt.iscte.poo.game.Room;

// Tive de criar duas novas classes abstratas porque os peixes têm movimentos distintos para o tipo de objetos moveis
// O peixe pequeno só mexe nos objetos leves 
// O peixe grande mexe em todos e além disso consegue, na horizontal empurrar dois seguidos. Na vertical só consegue um
// Portanto criei a classe abstrata isLightObject e nonLightObject que derivam ambas do MovableObject

public abstract class LightObject extends MovableObject{

    public LightObject(Room room) {
        super(room);
    }

    @Override
    public boolean isLightObject() {
        return true;
    }    
}
