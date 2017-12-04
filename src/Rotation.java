import java.util.HashMap;
import java.util.Map;

public enum Rotation {
    Horaire_HGauche(1),
    Horaire_HDroite(2),
    Horaire_BGauche(3),
    Horaire_BDroite(4),
    AntiHoraire_HGauche(-1),
    AntiHoraire_HDroite(-2),
    AntiHoraire_BGauche(-3),
    AntiHoraire_BDroite(-4);

    private int rotation;

    private static Map<Integer, Rotation> map = new HashMap<Integer, Rotation>();

    static {
        for (Rotation rot : Rotation.values()) {
            map.put(rot.rotation, rot);
        }
    }

    private Rotation(final int leg) { rotation = leg; }

    public static Rotation valueOf(int rotation) {
        return map.get(rotation);
    }

    public int getValue() {
        return rotation;
    }
}
