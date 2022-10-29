package ui;

import model.Control;

import java.util.Scanner;

public class Main {

    private Scanner sc;
    private Control control;

    public Main() {
        this.sc = new Scanner(System.in);
        this.control = new Control();
    }

    public static void main(String []args){


        Main main = new Main();

        main.prueba();
        
    }

    public void prueba(){
        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('jijijaja', 'Colombia', 50.2, '+57')");
        System.out.println(control.toStringCountries());
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('jaja jiji', 'Cali', 'jijijaja', 2.2)");
        System.out.println(control.toStringCities());
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('PITO', 'Bogota', 'jijijaja', 2.5)");
        System.out.println(control.toStringCities());
        control.delete("DELETE FROM cities WHERE id = 'jaja jiji'");
        System.out.println(control.toStringCities());
    }
}
