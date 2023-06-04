package documents;

public enum DocumentStates {
    AVAILABLE("Disponible"),
    BORROWED("Emprunté"),
    RESERVED("Réservé");
    private final String name;

    DocumentStates(String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return this.name;
    }

    public static DocumentStates fromString(String name){
        for(DocumentStates state : DocumentStates.values()){
            if(state.name.equals(name)){
                return state;
            }
        }
        return null;
    }
}
