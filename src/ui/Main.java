package ui;

import model.Control;

import javax.swing.*;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Main extends JFrame{
    private Scanner sc;
    private Control control;

    public Main() {
        this.sc = new Scanner(System.in);
        this.control = new Control();
    }

    public void prueba(){
        /*control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('jijijaja', 'Colombia', 50.2, '+57')");
        System.out.println(control.toStringCountries());
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('jaja jiji', 'Cali', 'jijijaja', 2.2)");
        System.out.println(control.toStringCities());
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('PITO', 'Bogota', 'jijijaja', 2.5)");
        System.out.println(control.toStringCities());
        control.delete("DELETE FROM cities WHERE id = 'jaja jiji'");
        System.out.println(control.toStringCities());*/
    }

    public static void main(String []args){
        //TODO: todo xd

        System.out.println("Starting...");

        Main main = new Main();

        main.prueba();

        int option=-1;

        do{
            try{
                option = main.showMenu();
                main.executeOperation(option);
            }catch (InputMismatchException e){
                System.out.println("Your choice must be a number");
            }
        }while(option!=3);

        main.save();

    }

    public void save(){
        control.writeCountriesJson();
        control.writeCitiesJson();
        System.out.println("The data has been saved ;)");
    }

    public int showMenu(){
        System.out.println("\n\n"+
                "|---------------Menu--------------|\n" +
                "(1)- - - - Insert Command - - - - \n"+
                "(2)- - - - Import from SQL - - - -\n"+
                "(3)- - - - - - Exit - - - - - - -");
        int option = sc.nextInt();
        sc.nextLine();
        return option;
    }

    public void executeOperation(int option){

        switch (option){
            case 1:
                System.out.println(insertCommand());
                break;
            case 2:
                System.out.println(pathSQL());
                break;
            case 3:
                System.out.println("Bye ;)");
                break;
            default:
                break;
        }

    }

    public String insertCommand(){

        String out="";

        String command="";

        System.out.println("Please insert a command\n\n"+
                "If your going to insert a country insert co and press enter to generate the random id\n\n"+
                "If your going to insert a city insert ci and press enter to generate the random id\n\n"+
                "If you don't know the commands format, insert the word help and press enter\n\n");

        command=sc.nextLine();

        if(command.equalsIgnoreCase("co")){
            System.out.println("Your country id will be: " + UUID.randomUUID() + "\nNow insert the command");
            command=sc.nextLine();
        }

        if(command.equalsIgnoreCase("ci")){
            System.out.println("Your city id will be: " + UUID.randomUUID() + "\nNow insert the command");
            command=sc.nextLine();
        }

        if(command.equalsIgnoreCase("help")){
            System.out.println("Examples of commands:\n"+
                    "Insert --->  INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')\n\n"+
                    "Insert --->  INSERT INTO cities(id, name, countryID, population) VALUES ('e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)\n\n"+
                    "Search/Filter ----> SELECT * FROM cities WHERE name = 'Colombia'\n\n"+
                    "Search/Filter ----> SELECT * FROM countries WHERE population > 100\n\n"+
                    "Order ----> SELECT * FROM countries WHERE population > 100 ORDER BY name\n\n"+
                    "Order ----> SELECT * FROM cities WHERE name = 'Guadalajara' ORDER BY population\n\n"+
                    "Delete ----> DELETE FROM cities WHERE country = 'Colombia'\n\n"+
                    "Delete ----> DELETE FROM countries WHERE id = '6ec3e8ec-3dd0-11ed-b878-0242ac120002'\n\n");
            command = sc.nextLine();
        }

        if(command.contains("INSERT")){
            try {
                control.add(command);
                return "Succesfully addded.";
            }catch (Exception e){
                return e.getMessage();
            }
        }

        if(command.contains("DELETE")){
            try {
                control.delete(command);
                return "Succesfully deleted.";
            }catch (Exception e){
                return e.getMessage();
            }
        }

        
        if(command.contains("SELECT")){
            try {
                return control.search(command);
            }catch (Exception e){
                return e.getMessage();
            }
        }

        return "Please check the tipping of your command";
    }

    public String pathSQL(){
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        try{
            control.readSQLCommand(file);
            System.out.println(control.toStringCities());
            return "Data imported";
        }catch (Exception e){
            return e.getMessage();
        }

    }
}

