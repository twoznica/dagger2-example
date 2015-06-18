package net.pawelhajduk.daggerdemo.api.model;

public class Repository {
    public String name;
    public int openIssues;
    public int watchers;

    public Repository(String name, int openIssues, int watchers){
        this.name = name;
        this.openIssues = openIssues;
        this.watchers = watchers;
    }

    public Repository(){

    }
}
