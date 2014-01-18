package MilitaryPlanes;

public class Plane {
    private int id;

    private Model model;

    private Country origin;

    private PlaneType type;

    private int crew;

    private PlaneParameters parameters;

    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Country getOrigin() {
        return origin;
    }

    public void setOrigin(Country origin) {
        this.origin = origin;
    }

    public PlaneType getType() {
        return type;
    }

    public void setType(PlaneType type) {
        this.type = type;
    }

    public int getCrew() {
        return crew;
    }

    public void setCrew(int crew) {
        this.crew = crew;
    }

    public PlaneParameters getParameters() {
        return parameters;
    }

    public void setParameters(PlaneParameters parameters) {
        this.parameters = parameters;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
