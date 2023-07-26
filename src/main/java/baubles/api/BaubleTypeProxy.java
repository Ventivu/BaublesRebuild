package baubles.api;

public enum BaubleTypeProxy {
    AMULET(BaubleType.AMULET), RING(BaubleType.RING), BELT(BaubleType.BELT), ANY(null);
    transient final BaubleType oldtype;

    BaubleTypeProxy(BaubleType oldtype) {
        this.oldtype = oldtype;
    }

    public static BaubleTypeProxy getType(BaubleType oldtype) {
        try {
            return BaubleTypeProxy.valueOf(oldtype.name());
        } catch (Exception e) {
            return ANY;
        }
    }

    public BaubleType getOldtype() {
        return oldtype;
    }
}
