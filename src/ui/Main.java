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
        //TODO: todo xd

        Main main = new Main();

        main.prueba();
        
    }

    public void prueba(){
        control.ReadSQLCommand("C:\\Users\\user\\OneDrive - Universidad Icesi (@icesi.edu.co)\\Escritorio\\prueba.txt");
    }
}
