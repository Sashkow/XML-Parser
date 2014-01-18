package MilitaryPlanes;

public enum Model {
    Raptor("F-22 Raptor"),
    PAK("PAK FA"),
    B2("B-2"),
    A10Thunderbolt("A-10 Thunderbolt II"),
    BAE("BAE Harrier II");

    private String name;

    Model(String name){
        this.name = name;
    }

    public static Model getModel(String name){
        for(Model currModel : Model.values()){
            if(currModel.name.equals(name))
                return currModel;
        }
        return null;
    }

    public String getName(){
        return name;
    }
}
