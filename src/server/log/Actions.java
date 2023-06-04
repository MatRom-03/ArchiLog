package server.log;

public enum Actions {
    BOOKING("Reservation"),
    BORROW("Emprunt"),
    RETURN("Retour"),
    REMOVEBOOKING("RemoveBooking");

    public final String action;

    Actions(String action) {
        this.action = action;
    }
    @Override
    public String toString() {
        return action;
    }
}
