package global.inventory.enums;

public enum ActivityAction {
    VIEWED,
    COMPLETED;

    public static ActivityAction fromString(String action) {
        for (ActivityAction activityAction : ActivityAction.values()) {
            if (activityAction.name().equalsIgnoreCase(action)) {
                return activityAction;
            }
        }
        throw new IllegalArgumentException("No enum constant " + ActivityAction.class.getCanonicalName() + "." + action);
    }
}