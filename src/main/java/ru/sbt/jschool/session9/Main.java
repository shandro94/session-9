package ru.sbt.jschool.session9;

import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public final class Main {

    public static void main(String[] args) {

        Koshqa koshqa = new Koshqa("Elza");

        Sobaka sobaka = new Sobaka("Doge");
        sobaka.setKoshqa(koshqa);

        System.out.println("Was:");
        System.out.println("koshqa.getName = " + koshqa.getName());
        System.out.println("sobaka.getKoshqa().getName() = " + sobaka.getKoshqa().getName());
        System.out.println("");

        koshqa.setName("notElzaAnymore");
        System.out.println("Changed koshqa name:");
        System.out.println("koshqa.getName = " + koshqa.getName());
        System.out.println("sobaka.getKoshqa().getName() = " + sobaka.getKoshqa().getName());
        System.out.println("");

        sobaka.getKoshqa().setName("voobsheNotElza(((");
        System.out.println("Changed sobaka's koshqa name:");
        System.out.println("koshqa.getName = " + koshqa.getName());
        System.out.println("sobaka.getKoshqa().getName() = " + sobaka.getKoshqa().getName());
        System.out.println("");
    }


}

class Sobaka {
    private String name;
    private Koshqa koshqa;

    public Sobaka(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Koshqa getKoshqa() {
        return koshqa;
    }

    public void setKoshqa(Koshqa koshqa) {
        this.koshqa = koshqa;
    }
}

class Koshqa {
    private String name;

    public Koshqa(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}