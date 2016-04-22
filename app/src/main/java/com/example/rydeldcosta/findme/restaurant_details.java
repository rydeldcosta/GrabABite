package com.example.rydeldcosta.findme;

/**
 * Created by Rydel Dcosta on 4/13/2016.
 */
public class restaurant_details {
    String name;
    public int[] time = new int[24];
    double xloc,yloc;
    public restaurant_details(String name,double x,double y,int time[])
    {
        this.name = name;
        this.xloc = x;
        this.yloc = y;
        int i;
        for(i=0;i<24;i++)
        {
            this.time[i] = time[i];
        }
    }

    boolean check_open(int hour)
    {
        if(this.time[hour]==1)
            return true;
        else
            return false;
    }
}
