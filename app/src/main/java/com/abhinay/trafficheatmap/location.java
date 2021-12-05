package com.abhinay.trafficheatmap;

public class location {
    private String name;
    private double startlongitude,endlongitude;
    private double startlatitude,endlatitude;
    private double index,capacity;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartlatitude(double latitude) {
        this.startlatitude = latitude;
    }

    public void setStartlongitude(double startlongitude)
    {
        this.startlongitude = startlongitude;
    }

    public void setEndlatitude(double endlatitude)
    {
        this.endlatitude = endlatitude;
    }

    public void setEndLongitude(double longitude) {
        this.endlongitude = longitude;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getStartlatitude() {
        return startlatitude;
    }

    public double getStartlongitude() {
        return startlongitude;
    }

    public double getEndlatitude() {
        return endlatitude;
    }

    public double getEndlongitude() {
        return endlongitude;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getIndex() {
        return index;
    }
    public location(int id,String name,double startlatitude,double startlongitude,double endlatitude,double endlongitude,double capacity,double index)
    {
        this.id =id;
        this.name=name;
        this.startlatitude=startlatitude;
        this.startlongitude=startlongitude;
        this.endlatitude=endlatitude;
        this.endlongitude=endlongitude;
        this.capacity=capacity;
        this.index=index;
    }
}
