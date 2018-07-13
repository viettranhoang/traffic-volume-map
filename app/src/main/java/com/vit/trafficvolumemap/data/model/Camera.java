package com.vit.trafficvolumemap.data.model;

public class Camera {
    private float lat;
    private float lng;
    private float area;
    private int car;
    private int motorbike;
    private String place;


    public Camera() {
    }

    public Camera(float lat, float lng, float area, int car, int motorbike, String place) {
        this.lat = lat;
        this.lng = lng;
        this.area = area;
        this.car = car;
        this.motorbike = motorbike;
        this.place = place;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }

    public int getMotorbike() {
        return motorbike;
    }

    public void setMotorbike(int motorbike) {
        this.motorbike = motorbike;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return place + "\n" +
                "Tắc đường: " + area +
                "%\nÔ tô: " + car +
                "\nXe máy: " + motorbike;
    }
}
