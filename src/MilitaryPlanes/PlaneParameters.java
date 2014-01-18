package MilitaryPlanes;

public class PlaneParameters {
    private double length;

    private double wingspan;

    private double weight;

    PlaneParameters(){}

    PlaneParameters(double length, double wingspan, double weight){
        this.length = length;
        this.wingspan = wingspan;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWingspan() {
        return wingspan;
    }

    public void setWingspan(double wingspan) {
        this.wingspan = wingspan;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
