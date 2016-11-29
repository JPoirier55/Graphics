package com.graphics.objects;

/**
 * Created by Jake on 11/24/2016.
 */
public class Material {
    private double kAr, kAg, kAb;
    private double kDr, kDg, kDb;
    private double kSr, kSg, kSb;
    private String name;

    public Material(double kAr, double kAg, double kAb, double kDr, double kDg, double kDb, double kSr, double kSg, double kSb, String name) {
        this.kAr = kAr;
        this.kAg = kAg;
        this.kAb = kAb;
        this.kDr = kDr;
        this.kDg = kDg;
        this.kDb = kDb;
        this.kSr = kSr;
        this.kSg = kSg;
        this.kSb = kSb;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Material{" +
                "kAr=" + kAr +
                ", kAg=" + kAg +
                ", kAb=" + kAb +
                ", kDr=" + kDr +
                ", kDg=" + kDg +
                ", kDb=" + kDb +
                ", kSr=" + kSr +
                ", kSg=" + kSg +
                ", kSb=" + kSb +
                ", name='" + name + '\'' +
                '}';
    }

    public double getkAr() {
        return kAr;
    }

    public void setkAr(double kAr) {
        this.kAr = kAr;
    }

    public double getkAg() {
        return kAg;
    }

    public void setkAg(double kAg) {
        this.kAg = kAg;
    }

    public double getkAb() {
        return kAb;
    }

    public void setkAb(double kAb) {
        this.kAb = kAb;
    }

    public double getkDr() {
        return kDr;
    }

    public void setkDr(double kDr) {
        this.kDr = kDr;
    }

    public double getkDg() {
        return kDg;
    }

    public void setkDg(double kDg) {
        this.kDg = kDg;
    }

    public double getkDb() {
        return kDb;
    }

    public void setkDb(double kDb) {
        this.kDb = kDb;
    }

    public double getkSr() {
        return kSr;
    }

    public void setkSr(double kSr) {
        this.kSr = kSr;
    }

    public double getkSg() {
        return kSg;
    }

    public void setkSg(double kSg) {
        this.kSg = kSg;
    }

    public double getkSb() {
        return kSb;
    }

    public void setkSb(double kSb) {
        this.kSb = kSb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
