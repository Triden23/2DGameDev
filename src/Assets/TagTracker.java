package Assets;

public enum TagTracker {
    MON_BlueSlime("MON_BlueSlime"),
    NPC_OldMan("NPC_OldMan"),
    SPE_FireBall("SPE_FireBall"),
    PRO_FireBall("PRO_FireBall"),
    EFF_FireBall("EFF_FireBall"),
    OBJ_Boots("OBJ_Boots"),
    OBJ_Chest("OBJ_Chest"),
    OBJ_Door("OBJ_Door"),
    OBJ_Heart("OBJ_Heart"),
    OBJ_Key("OBJ_Key"),
    OBJ_Shield_Wood("OBJ_Shield_Wood"),
    OBJ_Sword_Normal("OBJ_Sword_Normal"),
    PLA_PLAYER("Player"),
    DEB_DEBUG("DEB_DEBUG"),
    DEB_NavMesh("DEB_NavMesh"),
    DEF_DEFAULT_TAG("DEFAULT");

    private final String tag;

    TagTracker(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String toString() {
        return tag;
    }
}
