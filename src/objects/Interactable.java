package objects;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public interface Interactable {
    boolean interagirPeixe(GameCharacter fish, Point2D from, Point2D to, Vector2D dir);
}
