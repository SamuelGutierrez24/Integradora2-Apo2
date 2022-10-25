package model;

import com.google.gson.Gson;
import exceptions.InvalidOperandException;
import exceptions.NoSuchCountryException;
import exceptions.WrongFormatException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Control {
    
    private HashMap<String,City> cities;
    private HashMap<String,Country> countries;

    public Control(){
        cities = new HashMap<>();
        countries = new HashMap<>();
    }







    public void add(String toAdd) throws NoSuchCountryException, WrongFormatException{
        String[] addable = toAdd.replaceAll(",", "").split(" ");
        if (addable[0].equals("INSERT")&&addable[1].equals("INTO")){
            if(addable[2].split("\\(")[0].equals("countries")&&(addable[2].split("\\(")[1] + addable[3] + addable[4] +  addable[5].split("\\)")[0]).equals("idnamepopulationcountryCode")&&addable[6].equals("VALUES")){
                boolean check = true;
                addable[7] = addable[7].replace("(", "");
                addable[10] = addable[10].replace(")", "");
                int i;
                for( i=7; i<addable.length&&(check=((addable[i].startsWith("'")&&addable[i].endsWith("'"))||i==9)); i++){
                    addable[i] = addable[i].replaceAll("'", "");
                }
                if(check){
                    countries.put(addable[7], new Country(addable[7], addable[8], Double.parseDouble(addable[9]), addable[10]));
                }
                else throw new WrongFormatException("Invalid input for " + (i==7?addable[i-5].split("\\(")[1]:addable[i-5]) + ".");
            }
            else if (addable[2].split("\\(")[0].equals("cities")&&(addable[2].split("\\(")[1] + addable[3] + addable[4] +  addable[5].split("\\)")[0]).equals("idnamecountryIDpopulation")&&addable[6].equals("VALUES")){
                boolean check = true;
                addable[7] = addable[7].replace("(", "");
                addable[10] = addable[10].replace(")", "");
                int i;
                for(i=7; i<addable.length&&(check=((addable[i].startsWith("'")&&addable[i].endsWith("'"))||i==10)); i++){
                    addable[i] = addable[i].replaceAll("'", "");
                }
                if(check){
                    if(countries.containsKey(addable[9])) cities.put(addable[7], new City(addable[7], addable[8], addable[9], Double.parseDouble(addable[10])));
                    else throw new NoSuchCountryException("The country id specified does not exist.");
                }
                else throw new WrongFormatException("Invalid input for " + (i==7?addable[i-5].split("\\(")[1]:addable[i-5]) + ".");
            }
            else throw new WrongFormatException("Declaration of database to be updated is either incomplete or incorrect.");
        }
        else throw new WrongFormatException("Unknown command");
    }


    public void searchAndSorting(String toSearch){
        String[] searchable = toSearch.split(" ");

            if(searchable[0].equals("SELECT")) {


                int longit = searchable.length;

                //sorting
                if (longit > 8) {
                    if (searchable[0].equals("SELECT") && searchable[1].equals("*") && searchable[2].equals("FROM") && searchable[4].equals("WHERE")) {


                        if (searchable[8].equals("ORDER") && searchable[9].equals("BY")) {

                            switch (searchable[3]) {

                                case ("cities"):

                                    ArrayList<City> toPrint = new ArrayList<>();

                                    switch (searchable[5]) {
                                        case ("name"):
                                            if (searchable[6].equals("=")) {
                                                if (searchable[10].equals("population")) {
                                                    //busca y los organiza por la poblacion y los inserta en el arreglo para imprimir
                                                } else if (searchable[10].equals("id")) {
                                                    //busca y los organiza por id;
                                                } else if (searchable[10].equals("countryId")) {
                                                    //Por el id del pais al que pertenece
                                                } else {
                                                    throw new InvalidOperandException("The variable " + searchable[10] + " is not valid.");
                                                }
                                            } else {
                                                throw new InvalidOperandException("Buenos dias");
                                            }
                                            break;

                                        case ("population"):
                                            if (searchable[6].equals("<")) {

                                                switch (searchable[10]) {
                                                    case ("name"):
                                                        //ordernar por nombre los que tengan una poblacion menor a searchable[7]
                                                        break;
                                                    case ("id"):
                                                        //ordernar por id los que tengan una poblacion menor a searchable[7]
                                                        break;
                                                    case ("countryId"):
                                                        //ordernar por countryId los que tengan una poblacion menor a searchable[7]
                                                        break;
                                                    default:
                                                        throw new InvalidOperandException("The variable " + searchable[10] + "is not valid");
                                                }

                                            } else if (searchable[6].equals(">")) {

                                                switch (searchable[10]) {
                                                    case ("name"):
                                                        //ordernar por nombre los que tengan una poblacion menor a searchable[7]
                                                        break;
                                                    case ("id"):
                                                        //ordernar por id los que tengan una poblacion menor a searchable[7]
                                                        break;
                                                    case ("countryId"):
                                                        //ordernar por countryId los que tengan una poblacion menor a searchable[7]
                                                        break;
                                                    default:
                                                        throw new InvalidOperandException("The variable " + searchable[10] + "is not valid");
                                                }

                                            } else if (searchable[6].equals("=")) {
                                                switch (searchable[10]) {
                                                    case ("name"):
                                                        //ordernar por nombre las ciudades que tengan una poblacion igual a searchable[7]
                                                        break;
                                                    case ("id"):
                                                        //ordernar por id las ciudades que tengan una poblacion igual a searchable[7]
                                                        break;
                                                    case ("countryId"):
                                                        //ordernar por countryId las ciudades que tengan una poblacion igual a searchable[7]
                                                        break;
                                                    default:
                                                        throw new InvalidOperandException("The variable " + searchable[10] + "is not valid");
                                                }
                                            } else {
                                                throw new InvalidOperandException("The operand " + searchable[6] + " is not valid.");
                                            }

                                    }


                                    break;

                                case ("countries"):

                                    ArrayList<Country> forPrint = new ArrayList<>();

                                    if (searchable[5].equals("population")) {

                                        switch (searchable[6]) {
                                            case (">"):
                                                //buscar paises con una poblacio mayor a searchable[7] y agregarlos al arreglo para imprimir;
                                                break;

                                            case ("<"):
                                                //buscar paises con una poblacio menor a searchable[7] y agregarlos al arreglo para imprimir;
                                                break;

                                            case ("="):
                                                //buscar paises con una poblacio igual a searchable[7] y agregarlos al arreglo para imprimir;
                                                break;

                                            default:
                                                throw new InvalidOperandException("The operand " + searchable[5] + " is not valid.");
                                        }
                                    } else {
                                        throw new InvalidOperandException("For contries you only can order them by name, because all contries have a different name,contryCode and id");
                                    }

                                    break;

                                default:
                                    throw new WrongFormatException("Ahora veo que pongo.");


                            }

                        }
                    }


                    //show everything
                } else if (longit == 4) {

                    if (searchable[0].equals("SELECT") && searchable[1].equals("*") && searchable[2].equals("FROM")) {

                        switch (searchable[3]) {

                            case ("countries"):
                                //Show every country
                                break;
                            case ("cities"):
                                //show every city
                                break;

                            default:
                                throw new InvalidOperandException("This variable is not valid");

                        }

                    }

                    //search
                } else {

                    if (searchable[0].equals("SELECT") && searchable[1].equals("*") && searchable[2].equals("FROM") && searchable[4].equals("WHERE")) {

                        switch (searchable[3]) {

                            case ("countries"):

                                ArrayList<Country> forPrint = new ArrayList<>();

                                switch (searchable[5]) {

                                    case ("id"):
                                        if (searchable[6].equals("=")) {
                                            //buscar en paises el id de ese pais
                                        } else {
                                            throw new InvalidOperandException("The operand " + searchable[6] + " is not valid.");
                                        }
                                        break;
                                    case ("name"):
                                        if (searchable[6].equals("=")) {
                                            //buscar en paises el nombre de ese pais
                                        } else {
                                            throw new InvalidOperandException("The operand " + searchable[6] + " is invalid.");
                                        }
                                        break;
                                    case ("countryCode"):
                                        if (searchable[6].equals("=")) {
                                            //buscar en paises el code de ese pais
                                        } else {
                                            throw new InvalidOperandException("The operand " + searchable[6] + " is not a valid one.");
                                        }
                                        break;
                                    case ("population"):

                                        switch (searchable[6]) {

                                            case (">"):
                                                //buscar los paises con poblacion mayor a searchable[7]
                                                break;
                                            case ("<"):
                                                //buscar los paises con poblacion menor a searchable[7]
                                                break;
                                            case ("="):
                                                //buscar los paises con poblacion igual a searchable[7]
                                                break;
                                            default:
                                                throw new InvalidOperandException("The operand " + searchable[6] + " is not valid.");

                                        }

                                        break;
                                    default:
                                        throw new InvalidOperandException("The variable is invalid");
                                }
                                break;

                            case ("cities"):

                                ArrayList<City> forPrintCity = new ArrayList<>();

                                switch (searchable[5]) {

                                    case ("id"):
                                        if (searchable[6].equals("=")) {
                                            //buscar en paises el id de ese pais
                                        } else {
                                            throw new InvalidOperandException("The operand " + searchable[6] + " is not valid.");
                                        }
                                        break;
                                    case ("name"):
                                        if (searchable[6].equals("=")) {
                                            //buscar en paises el nombre de ese pais
                                        } else {
                                            throw new InvalidOperandException("The operand " + searchable[6] + " is not valid one.");
                                        }
                                        break;
                                    case ("countryId"):
                                        if (searchable[6].equals("=")) {
                                            //buscar en paises el code de ese pais
                                        } else {
                                            throw new InvalidOperandException("The operand " + searchable[6] + " is  invalid.");
                                        }
                                        break;
                                    case ("population"):

                                        switch (searchable[6]) {

                                            case (">"):
                                                //buscar las ciudades y paises con poblacion mayor a searchable[7]
                                                break;
                                            case ("<"):
                                                //buscar las ciudades y paises con poblacion menor a searchable[7]
                                                break;
                                            case ("="):
                                                //buscar las ciudades y paises con poblacion igual a searchable[7]
                                                break;
                                            default:
                                                throw new InvalidOperandException("The operand " + searchable[6] + " is not valid.");

                                        }

                                        break;
                                    default:
                                        throw new InvalidOperandException("The variable is invalid");
                                }

                                break;


                        }

                    } else {
                        throw new WrongFormatException("The format of the command is invalid.");
                    }
                }

            }else {
                throw new WrongFormatException("The format of the command for search is invalid");
            }
    }








    public void delete(String toDel) throws NoSuchCountryException, WrongFormatException, InvalidOperandException{
        String[] deletable = toDel.split(" ");

        if(deletable[0].equals("DELETE")&&deletable[1].equals("FROM")&&deletable[3].equals("WHERE")){
            
            
            switch(deletable[2]){
                
                
                case("cities"):
                    ArrayList<City> toDelete = new ArrayList<>();
                    switch(deletable[4]){


                        case("id"):
                            if(deletable[6].startsWith("'")&&deletable[6].endsWith("'")){
                                if(deletable[5].equals("=")) toDelete.add(cities.get(deletable[6].replaceAll("'", "")));
                                else throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");
                            }
                            break;    


                        case("name"):
                            if(deletable[5].equals("=")){
                                for (Map.Entry<String,City> c : cities.entrySet()){
                                    if(c.getValue().getName().equals(deletable[6].replaceAll("'", ""))) toDelete.add(c.getValue());
                                }
                            }
                            else throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");                            
                            break;


                        case("country"):
                            if(deletable[5].equals("=")){
                                Country ct = null;
                                if(deletable[6].startsWith("'")&&deletable[6].endsWith("'")){
                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getName().equals(deletable[6].replaceAll("'", ""))) ct = c.getValue();
                                    }
                                    for (Map.Entry<String,City> c : cities.entrySet()){
                                        if(c.getValue().getCountryId().equals(ct.getId())) toDelete.add(c.getValue());
                                    }
                                }
                                else throw new WrongFormatException("Invalid input value for " + deletable[4] + ".");
                            }
                            else throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");                            
                            break;


                        case("population"):
                            switch(deletable[5]){
                                case("="):
                                    for (Map.Entry<String,City> c : cities.entrySet()){
                                        if(c.getValue().getPopulation()==Double.parseDouble(deletable[6])) toDelete.add(c.getValue());
                                    }
                                    break;
                                case(">"):
                                    for (Map.Entry<String,City> c : cities.entrySet()){
                                        if(c.getValue().getPopulation()>Double.parseDouble(deletable[6])) toDelete.add(c.getValue());
                                    }
                                    break;
                                case("<"):
                                    for (Map.Entry<String,City> c : cities.entrySet()){
                                        if(c.getValue().getPopulation()<Double.parseDouble(deletable[6])) toDelete.add(c.getValue());
                                    }
                                    break;
                                default:
                                    throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");
                            }
                            break;


                        default:
                            throw new WrongFormatException("The specified field does not match expected values.");
                    }
                    for(City c : toDelete){
                        cities.remove(c.getId(),c);
                    }
                    break;



                case ("countries"):
                    ArrayList<Country> toDelete2 = new ArrayList<>();
                    switch(deletable[4]){
                        case("id"):
                            if(deletable[5].equals("=")) toDelete2.add(countries.get(deletable[6]));
                            else throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");
                            break;    
                        case("name"):
                            if(deletable[5].equals("=")){

                                for (Map.Entry<String,Country> c : countries.entrySet()){
                                    if(c.getValue().getName().equals(deletable[6].replaceAll("'", ""))) toDelete2.add(c.getValue());
                                }

                            }
                            else throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");                            
                            break;
                        case("coutryCode"):
                            if(deletable[5].equals("=")){

                                for (Map.Entry<String,Country> c : countries.entrySet()){
                                    if(c.getValue().getCountryCode().equals(deletable[6].replaceAll("'", ""))) toDelete2.add(c.getValue());
                                }

                            }
                            else throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");                            
                            break;
                        case("population"):
                            switch(deletable[5]){
                                case("="):
                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getPopulation()==Double.parseDouble(deletable[6])) toDelete2.add(c.getValue());
                                    }
                                    break;
                                case(">"):
                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getPopulation()>Double.parseDouble(deletable[6])) toDelete2.add(c.getValue());
                                    }
                                    break;
                                case("<"):
                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getPopulation()<Double.parseDouble(deletable[6])) toDelete2.add(c.getValue());
                                    }
                                    break;
                                default:
                                    throw new InvalidOperandException("The operand " + deletable[5] + "  cannot apply to " + deletable[4] + ".");
                            }
                            break;
                        default:
                            throw new WrongFormatException("The specified field does not match expected values.");
                    }
                    for(Country c : toDelete2){
                        cities.remove(c.getId(),c);
                    }
                    break;

                default:
                    throw new WrongFormatException("Specified table does not exist.");

            }
        } 
        else throw new WrongFormatException("Unkown command."); 
    }

    public String toStringCountries(){
        return countries.toString();
    }

    public String toStringCities(){
        return cities.toString();
    }


    //Json methods

    public void WriteCitiesJson(String path, ArrayList<City> toSave){

        File file = new File(path);

        Gson gson = new Gson();

        String json = gson.toJson(toSave);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write( json.getBytes(StandardCharsets.UTF_8) );
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WriteCountriesJson(String path, ArrayList<Country> toSave){

        File file = new File(path);

        Gson gson = new Gson();

        String json = gson.toJson(toSave);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write( json.getBytes(StandardCharsets.UTF_8) );
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<City> ReadJsonCities(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String json = "";
            String line;
            if((line=reader.readLine())!=null){
                json= line;
            }
            fis.close();

            Gson gson = new Gson();
            City[] citiesFromJson = gson.fromJson(json, City[].class);
            ArrayList<City> sent = new ArrayList<>();

            if(citiesFromJson!=null)sent.addAll(List.of(citiesFromJson));

            return sent;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Country> ReadJsonContries(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String json = "";
            String line;
            if((line=reader.readLine())!=null){
                json= line;
            }
            fis.close();

            Gson gson = new Gson();
            Country[] countriesFromJson = gson.fromJson(json, Country[].class);
            ArrayList<Country> sent = new ArrayList<>();

            if(countriesFromJson!=null)sent.addAll(List.of(countriesFromJson));

            return sent;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ReadSQLCommand(String path){

        ArrayList<String> commands = new ArrayList<>();
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(fis)
            );
            String line;
            while(( line = reader.readLine()) != null){
                commands.add(line);
            }
            fis.close();
            System.out.println("Tamano del arreglo: "+commands.size());
            for(String s:commands){
                System.out.println(s);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
