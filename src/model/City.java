package model;

public class City {

    private String id;
    private String name;
    private double population;
    private String countryId;

    public City(String id, String name, String countryId, double population) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.countryId = countryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nPopulation: " + population + "\nCountry ID: " + countryId;
    }
}
