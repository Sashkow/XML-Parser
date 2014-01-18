package MilitaryPlanes;

public enum Country {
    Russia("Russia"),
    USA("USA"),
    UnitedKingdom("United Kingdom");

    private String name;

    Country(String name){
        this.name = name;
    }

    public static Country getCountry(String name){
        for(Country country : Country.values()){
            if(country.name.equals(name))
                return country;
        }
        return null;
    }

    public String getName(){
        return name;
    }
}
