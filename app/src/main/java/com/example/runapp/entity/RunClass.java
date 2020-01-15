package com.example.runapp.entity;

/**
 * Created by 18179 on 2020/1/15.
 */

public class RunClass {
     private  String id;
     private  String classs;
     private  String classname;
     private  String classnumber;
    public RunClass(String id, String classs, String classname, String classnumber) {
        this.id = id;
        this.classs = classs;
        this.classname = classname;
        this.classnumber = classnumber;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassnumber() {
        return classnumber;
    }

    public void setClassnumber(String classnumber) {
        this.classnumber = classnumber;
    }
}
