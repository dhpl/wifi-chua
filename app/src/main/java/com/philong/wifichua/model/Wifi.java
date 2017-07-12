package com.philong.wifichua.model;

/**
 * Created by Long on 7/6/2017.
 */

public class Wifi {

    private String name;
    private double lattitude;
    private double longitude;
    private String matkhau;
    private String diachi;

    public Wifi() {
    }

    public Wifi(String name, double lattitude, double longitude, String matkhau, String diachi) {
        this.name = name;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.matkhau = matkhau;
        this.diachi = diachi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
