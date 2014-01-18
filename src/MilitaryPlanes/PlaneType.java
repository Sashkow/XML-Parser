package MilitaryPlanes;

public enum PlaneType {
    Fighter,
    Bomber,
    Attack;

    public static PlaneType getType(String name){
        for(PlaneType type : PlaneType.values()){
            if(type.toString().equals(name))
                return type;
        }
        return null;
    }
}
