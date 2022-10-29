package model;

import com.google.gson.Gson;

import exceptions.EmptyDatabaseException;
import exceptions.InvalidOperandException;
import exceptions.NoSuchCountryException;
import exceptions.WrongFormatException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Control {
    
    private HashMap<String,City> cities;
    private HashMap<String,Country> countries;

    public Control(){
        cities = new HashMap<>();
        countries = new HashMap<>();
    }







    public void add(String toAdd) throws NoSuchCountryException, WrongFormatException{
        String[] checker = toAdd.replaceAll(",", "").split(" ");
        String[] addable = toAdd.contains("VALUES")?toAdd.split("VALUES"):null;
        if (addable!=null&&checker[0].equals("INSERT")&&checker[1].equals("INTO")){
            if(checker[2].split("\\(")[0].equals("countries")&&(checker[2].split("\\(")[1] + checker[3] + checker[4] +  checker[5].split("\\)")[0]).equals("idnamepopulationcountryCode")&&checker[6].equals("VALUES")){
                boolean check = true;
                String[] values = addable[1].split(",");
                values[0] = values[0].replace("(", "");
                for(int i=0; i<values.length; i++) values[i] = values[i].replace(")", "").replaceFirst(" ", "");
                int i;
                for( i=0; i<values.length&&(check=((values[i].startsWith("'")&&values[i].endsWith("'"))||i==2)); i++){
                    values[i] = values[i].replaceAll("'", "");
                }
                if(check){
                    countries.put(values[0], new Country(values[0], values[1], Double.parseDouble(values[2]), values[3]));
                }
                else throw new WrongFormatException("Invalid input for " + (values[i]) + ".");
            }
            else if (checker[2].split("\\(")[0].equals("cities")&&(checker[2].split("\\(")[1] + checker[3] + checker[4] +  checker[5].split("\\)")[0]).equals("idnamecountryIDpopulation")&&checker[6].equals("VALUES")){
                boolean check = true;
                String[] values = addable[1].split(",");
                values[0] = values[0].replace("(", "");
                for (int i=0; i<values.length; i++) values[i] = values[i].replace(")", "").replaceFirst(" ", "");
                int i;
                for(i=0; i<values.length&&(check=((values[i].startsWith("'")&&values[i].endsWith("'"))||i==3)); i++){
                    values[i] = values[i].replaceAll("'", "");
                }
                if(check){
                    if(countries.containsKey(values[2])) cities.put(values[0], new City(values[0], values[1], values[2], Double.parseDouble(values[3])));
                    else throw new NoSuchCountryException("The country id specified does not exist.");
                }
                else throw new WrongFormatException("Invalid input for " + (values[i]) + ".");
            }
            else throw new WrongFormatException("Declaration of database to be updated is either incomplete or incorrect.");
        }
        else throw new WrongFormatException("Unknown command");
    }





    public String search(String toSearch){
        String out = "";
        String [] searchable = toSearch.split(" ");

        if(searchable[0].equals("SELECT") && searchable[1].equals("*") && searchable[2].equals("FROM")){

            switch (searchable[3]){
                case ("cities"):
                    ArrayList<City> forPrintCity = new ArrayList<>();
                    if(searchable.length>4){

                        if(searchable[4].equals("WHERE")){

                            switch (searchable[5]){
                                case ("population"):
                                    switch (searchable[6]){
                                        case(">"):
                                            for(Map.Entry<String,City> c : cities.entrySet()){
                                                if(c.getValue().getPopulation()>Double.parseDouble(searchable[7])){
                                                    forPrintCity.add(c.getValue());
                                                }
                                            }
                                            if(searchable.length==11){
                                                if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                    out = sortCity(forPrintCity,searchable[9]);
                                                }else {
                                                    throw new WrongFormatException("The format for sorting is invalid");
                                                }
                                            }else {
                                                for(int i = 0;i<forPrintCity.size();i++){
                                                    out += forPrintCity.get(i).toString()+"\n";
                                                }
                                            }
                                        break;
                                        case ("<"):
                                            for(Map.Entry<String,City> c : cities.entrySet()){
                                                if(c.getValue().getPopulation()<Double.parseDouble(searchable[7])){
                                                    forPrintCity.add(c.getValue());
                                                }
                                            }
                                            if(searchable.length==11){
                                                if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                    out = sortCity(forPrintCity,searchable[9]);
                                                }else {
                                                    throw new WrongFormatException("The format for sorting is invalid");
                                                }
                                            }else {
                                                for(int i = 0;i<forPrintCity.size();i++){
                                                    out += forPrintCity.get(i).toString()+"\n";
                                                }
                                            }
                                        break;
                                        case ("="):
                                            for(Map.Entry<String,City> c : cities.entrySet()){
                                                if(c.getValue().getPopulation()==Double.parseDouble(searchable[7])){
                                                    forPrintCity.add(c.getValue());
                                                }
                                            }
                                            if(searchable.length==11){//Verifico si hay que hacerle sort
                                                if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                    out = sortCity(forPrintCity,searchable[9]);
                                                }else {
                                                    throw new WrongFormatException("The format for sorting is invalid");
                                                }
                                            }else {
                                                for(int i = 0;i<forPrintCity.size();i++){
                                                    out += forPrintCity.get(i).toString()+"\n";
                                                }
                                            }
                                        break;
                                        default:
                                            throw new InvalidOperandException("This operand for this search is invalid.");
                                    }
                                break;
                                case("name"):
                                    if(searchable[6].equals("=")){
                                        String [] comillas = toSearch.split("'");
                                        for(Map.Entry<String,City> c : cities.entrySet()){
                                            if(c.getValue().getName().equals(comillas[1])){
                                                forPrintCity.add(c.getValue());
                                            }
                                        }
                                        if(searchable.length==11){//Verifico si hay que hacerle sort
                                            if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                sortCity(forPrintCity,searchable[9]);
                                            }else {
                                                throw new WrongFormatException("The format for sorting is invalid");
                                            }
                                        }else {
                                            for(int i = 0;i<forPrintCity.size();i++){
                                                out += forPrintCity.get(i).toString()+"\n";
                                            }
                                        }
                                    }else {
                                        throw new WrongFormatException("The value of " + searchable[6] + " is invalid for the variable name");
                                    }
                                break;
                                case ("id"):
                                    if(searchable[6].equals("=")){
                                        String [] comillas = toSearch.split("'");
                                        for(Map.Entry<String,City> c : cities.entrySet()){
                                            if(c.getValue().getId().equals(comillas[1])){
                                                forPrintCity.add(c.getValue());
                                            }
                                        }
                                        if(searchable.length==11){//Verifico si hay que hacerle sort
                                            if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                sortCity(forPrintCity,searchable[9]);
                                            }else {
                                                throw new WrongFormatException("The format for sorting is invalid");
                                            }
                                        }else {
                                            for(int i = 0;i<forPrintCity.size();i++){
                                                out += forPrintCity.get(i).toString()+"\n";
                                            }
                                        }
                                    }else {
                                        throw new WrongFormatException("The value of " + searchable[6] + " is invalid for the variable name");
                                    }
                                break;
                                case ("countryId"):
                                    if(searchable[6].equals("=")){
                                        String [] comillas = toSearch.split("'");
                                        for(Map.Entry<String,City> c : cities.entrySet()){
                                            if(c.getValue().getCountryId().equals(comillas[1])){
                                                forPrintCity.add(c.getValue());
                                            }
                                        }
                                        if(searchable.length==11){//Verifico si hay que hacerle sort
                                            if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                sortCity(forPrintCity,searchable[9]);
                                            }else {
                                                throw new WrongFormatException("The format for sorting is invalid");
                                            }
                                        }else {
                                            for(int i = 0;i<forPrintCity.size();i++){
                                                out += forPrintCity.get(i).toString()+"\n";
                                            }
                                        }
                                    }else {
                                        throw new WrongFormatException("The value of " + searchable[6] + " is invalid for the variable name");
                                    }
                                break;
                                default:
                                    throw new WrongFormatException("The variable for search is invalid for the format.");
                            }



                        }else {
                            throw new WrongFormatException("The format is wrong because is missing WHERE");
                        }


                    }else {
                        //show every city
                        for(Map.Entry<String,City> c : cities.entrySet()){
                            out += c.getValue().toString() + " \n";
                        }
                    }
                break;
                case ("countries"):
                    ArrayList<Country>forPrintCountry = new ArrayList<>();
                    if(searchable.length>4){
                        if (searchable[4].equals("WHERE")) {
                            switch (searchable[5]){
                                case ("population"):
                                    switch (searchable[6]){
                                        case ("<"):
                                            for(Map.Entry<String,Country> c : countries.entrySet()){
                                                if(c.getValue().getPopulation()<Double.parseDouble(searchable[7])){
                                                    forPrintCountry.add(c.getValue());
                                                }
                                            }
                                            if(searchable.length==11){//Verifico si hay que hacerle sort
                                                if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                    out = sortCountry(forPrintCountry,searchable[9]);
                                                }else {
                                                    throw new WrongFormatException("The format for sorting is invalid");
                                                }
                                            }else {
                                                for(int i = 0;i<forPrintCountry.size();i++){
                                                    out += forPrintCountry.get(i).toString()+"\n";
                                                }
                                            }
                                        break;
                                        case (">"):
                                            for(Map.Entry<String,Country> c : countries.entrySet()){
                                                if(c.getValue().getPopulation()>Double.parseDouble(searchable[7])){
                                                    forPrintCountry.add(c.getValue());
                                                }
                                            }
                                            if(searchable.length==11){//Verifico si hay que hacerle sort
                                                if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                    out = sortCountry(forPrintCountry,searchable[9]);
                                                }else {
                                                    throw new WrongFormatException("The format for sorting is invalid");
                                                }
                                            }else {
                                                for(int i = 0;i<forPrintCountry.size();i++){
                                                    out += forPrintCountry.get(i).toString()+"\n";
                                                }
                                            }
                                            break;
                                        case ("="):
                                            for(Map.Entry<String,Country> c : countries.entrySet()){
                                                if(c.getValue().getPopulation()==Double.parseDouble(searchable[7])){
                                                    forPrintCountry.add(c.getValue());
                                                }
                                            }
                                            if(searchable.length==11){//Verifico si hay que hacerle sort
                                                if(searchable[8].equals("ORDER") && searchable[9].equals("BY")){
                                                    out = sortCountry(forPrintCountry,searchable[9]);
                                                }else {
                                                    throw new WrongFormatException("The format for sorting is invalid");
                                                }
                                            }else {
                                                for(int i = 0;i<forPrintCountry.size();i++){
                                                    out += forPrintCountry.get(i).toString()+"\n";
                                                }
                                            }
                                            break;
                                        default:
                                            throw new InvalidOperandException("The operand for the search with population is not valid.");
                                    }
                                break;
                                case ("id"):

                                    if(searchable[6].equals("=")) {
                                        String [] comillas = toSearch.split("'");
                                        for (Map.Entry<String, Country> c : countries.entrySet()) {
                                            if (c.getValue().getId().equals(comillas[1])) {
                                                forPrintCountry.add(c.getValue());
                                            }
                                        }
                                        if (searchable.length == 11) {//Verifico si hay que hacerle sort
                                            if (searchable[8].equals("ORDER") && searchable[9].equals("BY")) {
                                                out = sortCountry(forPrintCountry, searchable[9]);
                                            } else {
                                                throw new WrongFormatException("The format for sorting is invalid");
                                            }
                                        } else {
                                            for (int i = 0; i < forPrintCountry.size(); i++) {
                                                out += forPrintCountry.get(i).toString() + "\n";
                                            }
                                        }
                                    } else {
                                        throw new InvalidOperandException("For search by id you must use the operand =");
                                    }
                                    break;
                                case("name"):

                                    if(searchable[6].equals("=")) {
                                        String [] comillas = toSearch.split("'");
                                        for (Map.Entry<String, Country> c : countries.entrySet()) {
                                            if (c.getValue().getName().equals(comillas[1])) {
                                                forPrintCountry.add(c.getValue());
                                            }
                                        }
                                        if (searchable.length == 11) {//Verifico si hay que hacerle sort
                                            if (searchable[8].equals("ORDER") && searchable[9].equals("BY")) {
                                                out = sortCountry(forPrintCountry, searchable[9]);
                                            } else {
                                                throw new WrongFormatException("The format for sorting is invalid");
                                            }
                                        } else {
                                            for (int i = 0; i < forPrintCountry.size(); i++) {
                                                out += forPrintCountry.get(i).toString() + "\n";
                                            }
                                        }
                                    }else {
                                        throw new InvalidOperandException("For search a name you must use the operand =");
                                    }
                                    break;
                                case ("contryCode"):

                                    if(searchable[6].equals("=")) {
                                        String [] comillas = toSearch.split("'");
                                        for (Map.Entry<String, Country> c : countries.entrySet()) {
                                            if (c.getValue().getCountryCode().equals(comillas[1])) {
                                                forPrintCountry.add(c.getValue());
                                            }
                                        }
                                        if (searchable.length == 11) {//Verifico si hay que hacerle sort
                                            if (searchable[8].equals("ORDER") && searchable[9].equals("BY")) {
                                                out = sortCountry(forPrintCountry, searchable[9]);
                                            } else {
                                                throw new WrongFormatException("The format for sorting is invalid");
                                            }
                                        } else {
                                            for (int i = 0; i < forPrintCountry.size(); i++) {
                                                out += forPrintCountry.get(i).toString() + "\n";
                                            }
                                        }
                                    }else {
                                        throw new InvalidOperandException("For search by CountryCode you must use the operand =");
                                    }
                                break;
                                default:
                                    throw new WrongFormatException("The variable is not valid for search");
                            }
                        }
                    }else {
                        //show every country
                        for(Map.Entry<String,Country> c : countries.entrySet()){
                            out += c.getValue().toString() + "\n";
                        }
                    }
                break;
                default:
                    throw new WrongFormatException("You can only search a country or a city");
            }

        }else {
            throw new WrongFormatException("The format of the command is invalid");
        }

        return out;
    }



    public String sortCity(ArrayList<City> array,String parameter){

        String out = "";

        switch (parameter){
            case ("population"):
                array.sort(new Comparator<City>() {
                    @Override
                    public int compare(City o1, City o2) {
                        if(o1.getPopulation()>o2.getPopulation()){
                            return 1;
                        }else if (o1.getPopulation()<o2.getPopulation()){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName() +"  population: " + array.get(i).getPopulation();
                }
            break;
            case ("name"):
                array.sort(new Comparator<City>() {
                    @Override
                    public int compare(City o1, City o2) {
                        if(o1.getName().compareTo(o2.getName())>0){
                            return 1;
                        }else if (o1.getName().compareTo(o2.getName())<0){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName();
                }
                break;
            case ("id"):
                array.sort(new Comparator<City>() {
                    @Override
                    public int compare(City o1, City o2) {
                        if(o1.getId().compareTo(o2.getId())>0){
                            return 1;
                        }else if (o1.getId().compareTo(o2.getId())<0){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName() + " id :" + array.get(i).getId();
                }
            case ("countryId"):
                array.sort(new Comparator<City>() {
                    @Override
                    public int compare(City o1, City o2) {
                        if(o1.getCountryId().compareTo(o2.getCountryId())>0){
                            return 1;
                        }else if (o1.getCountryId().compareTo(o2.getCountryId())<0){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName() + " id :" + array.get(i).getCountryId();
                }
            break;
            default:
                throw new WrongFormatException("That variable is not valid for order the information");
        }

        return out;
    }




    public String sortCountry(ArrayList<Country> array,String parameter){
        String out = "";

        switch (parameter){
            case ("population"):
                array.sort(new Comparator<Country>() {
                    @Override
                    public int compare(Country o1, Country o2) {
                        if(o1.getPopulation()>o2.getPopulation()){
                            return 1;
                        }else if (o1.getPopulation()<o2.getPopulation()){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName() +"  population: " + array.get(i).getPopulation();
                }
                break;
            case ("name"):
                array.sort(new Comparator<Country>() {
                    @Override
                    public int compare(Country o1, Country o2) {
                        if(o1.getName().compareTo(o2.getName())>0){
                            return 1;
                        }else if (o1.getName().compareTo(o2.getName())<0){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName();
                }
                break;
            case ("id"):
                array.sort(new Comparator<Country>() {
                    @Override
                    public int compare(Country o1, Country o2) {
                        if(o1.getId().compareTo(o2.getId())>0){
                            return 1;
                        }else if (o1.getId().compareTo(o2.getId())<0){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName() + " id :" + array.get(i).getId();
                }
            case ("countryCode"):
                array.sort(new Comparator<Country>() {
                    @Override
                    public int compare(Country o1, Country o2) {
                        if(o1.getCountryCode().compareTo(o2.getCountryCode())>0){
                            return 1;
                        }else if (o1.getCountryCode().compareTo(o2.getCountryCode())<0){
                            return -1;
                        }else
                            return 0;
                    }
                });
                Collections.reverse(array);
                for(int i = 0;i<array.size();i++){
                    out += array.get(i).getName() + " id :" + array.get(i).getCountryCode();
                }
                break;
            default:
                throw new WrongFormatException("That variable is not valid for order the information");
        }

        return out;
    }










    public void delete(String toDel) throws NoSuchCountryException, WrongFormatException, InvalidOperandException, EmptyDatabaseException{
        String[] checker = toDel.split(" ");
        String[] deleteable = (toDel +" ").split("'");
        if(checker[0].equals("DELETE")&&checker[1].equals("FROM")&&checker[3].equals("WHERE")){
            
            
            switch(checker[2]){
                
                
                case("cities"):
                    ArrayList<City> toDelete = new ArrayList<>();
                    switch(checker[4]){


                        case("id"):
                            if(deleteable.length==3){
                                if(checker[5].equals("=")) toDelete.add(cities.get(deleteable[1]));
                                else throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");
                            }
                            else throw new WrongFormatException("Invalid input for field 'id'.");
                            break;    


                        case("name"):
                            if(deleteable.length==3){
                                if(checker[5].equals("=")){
                                    for (Map.Entry<String,City> c : cities.entrySet()){
                                        if(c.getValue().getName().equals(deleteable[1])) toDelete.add(c.getValue());
                                    }
                                }
                                else throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");                               
                            }
                            else throw new WrongFormatException("Invalid input for field 'name'.");
                            break;


                        case("country"):
                            if(checker[5].equals("=")){
                                Country ct = null;
                                if(deleteable.length==3){
                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getName().equals(deleteable[1])) ct = c.getValue();
                                    }
                                    for (Map.Entry<String,City> c : cities.entrySet()){
                                        if(c.getValue().getCountryId().equals(ct.getId())) toDelete.add(c.getValue());
                                    }
                                }
                                else throw new WrongFormatException("Invalid input for field " + checker[4] + ".");
                            }
                            else throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");                            
                            break;


                        case("population"):
                            if(deleteable.length>1){
                                switch(checker[5]){
                                    case("="):
                                        for (Map.Entry<String,City> c : cities.entrySet()){
                                            if(c.getValue().getPopulation()==Double.parseDouble(checker[6])) toDelete.add(c.getValue());
                                        }
                                        break;
                                    case(">"):
                                        for (Map.Entry<String,City> c : cities.entrySet()){
                                            if(c.getValue().getPopulation()>Double.parseDouble(checker[6])) toDelete.add(c.getValue());
                                        }
                                        break;
                                    case("<"):
                                        for (Map.Entry<String,City> c : cities.entrySet()){
                                            if(c.getValue().getPopulation()<Double.parseDouble(checker[6])) toDelete.add(c.getValue());
                                        }
                                        break;
                                    default:
                                        throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");
                                }
                            }
                            else throw new WrongFormatException("Invalid input for field 'population'.");
                            break;


                        default:
                            throw new WrongFormatException("The specified field does not match expected values.");
                    }
                    try{
                        for(City c : toDelete){
                            cities.remove(c.getId(),c);
                        }
                    }
                    catch(NullPointerException e){
                        throw new EmptyDatabaseException("No cities meet the conditions.");
                    }
                    
                    break;



                case ("countries"):
                    ArrayList<Country> toDelete2 = new ArrayList<>(); 
                    switch(checker[4]){
                        case("id"):
                            if(deleteable.length==3){
                                if(checker[5].equals("=")) toDelete2.add(countries.get(checker[6]));
                                else throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");
                            }
                            else throw new WrongFormatException("Invalid input for field 'id'.");
                            break;    
                        case("name"):
                            if(deleteable.length==3){
                                if(checker[5].equals("=")){

                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getName().equals(checker[6].replaceAll("'", ""))) toDelete2.add(c.getValue());
                                    }

                                }
                                else throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");                            
                            }
                            else throw new WrongFormatException("Invalid input for field 'name'.");
                            break;
                        case("coutryCode"):
                            if(deleteable.length==3){
                                if(checker[5].equals("=")){

                                    for (Map.Entry<String,Country> c : countries.entrySet()){
                                        if(c.getValue().getCountryCode().equals(checker[6].replaceAll("'", ""))) toDelete2.add(c.getValue());
                                    }

                                }
                                else throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");                            
                            }
                            else throw new WrongFormatException("Invalid inpit for field 'countryCode'.");
                            break;
                        case("population"):
                            if(deleteable.length>1){
                                switch(checker[5]){
                                    case("="):
                                        for (Map.Entry<String,Country> c : countries.entrySet()){
                                            if(c.getValue().getPopulation()==Double.parseDouble(checker[6])) toDelete2.add(c.getValue());
                                        }
                                        break;
                                    case(">"):
                                        for (Map.Entry<String,Country> c : countries.entrySet()){
                                            if(c.getValue().getPopulation()>Double.parseDouble(checker[6])) toDelete2.add(c.getValue());
                                        }
                                        break;
                                    case("<"):
                                        for (Map.Entry<String,Country> c : countries.entrySet()){
                                            if(c.getValue().getPopulation()<Double.parseDouble(checker[6])) toDelete2.add(c.getValue());
                                        }
                                        break;
                                    default:
                                        throw new InvalidOperandException("The operand " + checker[5] + "  cannot apply to " + checker[4] + ".");
                                }
                            }
                            else throw new WrongFormatException("Invalid input for field 'population'.");
                            break;
                        default:
                            throw new WrongFormatException("The specified field does not match expected values.");
                    }
                    try{
                        for(Country c : toDelete2){
                            cities.remove(c.getId(),c);
                        }
                    }
                    catch(NullPointerException e){
                        throw new EmptyDatabaseException("No countries meet the conditions.");
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
