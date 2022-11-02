package Test;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import exceptions.NoSuchCountryException;
import junit.framework.TestCase;
import model.*;

public class TestDataBase extends TestCase {

    private Control control = new Control();

    public void setUpStage1(){
        control = new Control();
    }

    @Test
    public void testAdd(){
        setUpStage1();
        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('jijijaja', 'Colombia', 50.2, '+57')");
        Country contri = control.getCountries().get("jijijaja");
        assertEquals( contri.getName(),"Colombia");
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('jaja jiji', 'Cali', 'jijijaja', 2.2)");
        City city = control.getCities().get("jaja jiji");
        assertEquals( city.getName(),"Cali");
        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('arepa', 'Colombia', 50.2, '+57')");
        Country contri2 = control.getCountries().get("arepa");
        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('taco', 'Mexico', 100.14, '+52')");
        Country contri3 = control.getCountries().get("taco");
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('CaliOfDuty', 'Cali', 'arepa', 2.2)");
        City city2 = control.getCities().get("CaliOfDuty");
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('Narcos', 'Jalisco', 'taco', 4.2)");
        City city3 = control.getCities().get("Narcos");
        //cities
        assertEquals( city2.getName(),"Cali");
        assertEquals( city3.getName(),"Jalisco");
        //countries
        assertEquals( contri2.getName(),"Colombia");
        assertEquals( contri3.getName(),"Mexico");

        
    }

    @Test
    public void testDelete(){
        setUpStage1();
        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('arepa', 'Colombia', 50.2, '+57')");
        control.delete("DELETE FROM countries WHERE id = 'arepa'");
        //first delete
        Country contri = control.getCountries().get("arepa");
        assertEquals( contri,null);

        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('taco', 'Mexico', 100.14, '+52')");
        control.delete("DELETE FROM countries WHERE name = 'Mexico'");
        //second delete
        Country contri2 = control.getCountries().get("taco");
        assertEquals( contri2,null);

        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('mate', 'Uruguay', 2.3, '+32')");
        control.delete("DELETE FROM countries WHERE population = 2.3");
        //thrid delete
        Country contri3 = control.getCountries().get("mate");
        assertEquals( contri3,null);

        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('gringo', 'USA', 290.4, '+1')");
        control.delete("DELETE FROM countries WHERE population < 300");
        //four delete
        Country contri4 = control.getCountries().get("gringo");
        assertEquals( contri4,null);

        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('hambre', 'Venezuela', 40.9, '+58')");
        control.delete("DELETE FROM countries WHERE population > 30");
        //fith delete
        Country contri5 = control.getCountries().get("hambre");
        assertEquals( contri5,null);

        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('pisco', 'Peru', 30.14, '+51')");
        control.delete("DELETE FROM countries WHERE countryCode = '+51'");
        //six delete
        Country contri6 = control.getCountries().get("pisco");
        assertEquals( contri6,null);

        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('taco', 'Mexico', 100.14, '+52')");
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('Narcos', 'Jalisco', 'taco', 4.2)");
        control.delete("DELETE FROM cities WHERE countryID = 'taco'");
        //seven delete
        City citi = control.getCities().get("Narcos");
        assertEquals( citi,null);
    }

    @Test
    public void testSearch(){
        setUpStage1();
        control.add("INSERT INTO countries(id, name, population, countryCode) VALUES ('taco', 'Mexico', 100.14, '+52')");
        control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('Narcos', 'Jalisco', 'taco', 4.2)");
        boolean ans = control.searchBool("SELECT * FROM cities WHERE name = 'Jalisco'");
        assertEquals(ans, true);
        
    }

    @Test
    public void testInsertException(){
        setUpStage1();
        try {
            control.add("INSERT INTO cities(id, name, countryID, population) VALUES ('Narcos', 'Pipipupu', 'taco', 4.2)");
        } catch (NoSuchCountryException e) {
            //Check the type of the exception is the one we're looking for
            assertEquals(NoSuchCountryException.class, e.getClass());
        }
        
    }
    
}
