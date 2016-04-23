package com.example.rydeldcosta.findme;

/**
 * Created by Rydel Dcosta on 4/13/2016.
 */
public class restaurant_details {
    String name,table_name,contact;
    public int[] time = new int[24];
    double xloc,yloc;
    int delivery,rid;
    public restaurant_details(String rid, String name, String table_name, String contact,String delivery)
    {
        this.rid = Integer.parseInt(rid);
        this.name = name;
        this.table_name = table_name;
        this.contact = contact;
        this.delivery = Integer.parseInt(delivery);
    }
    public restaurant_details(String name,double x,double y,int time[])
    {
        this.name = name;
        table_name = name.replace(" ","");
        this.xloc = x;
        this.yloc = y;
        int i;
        for(i=0;i<24;i++)
        {
            this.time[i] = time[i];
        }
    }

    public restaurant_details(int rid, String r_name, String r_tablename, String contact, int delivery) {
        this.rid = rid;
        this.name = r_name;
        this.table_name = r_tablename;
        this.contact = contact;
        this.delivery = (delivery);
    }

    boolean check_open(int hour)
    {
        if(this.time[hour]==1)
            return true;
        else
            return false;
    }
    public String getName(){ return name; }
    public String getTable_name(){ return table_name; }
    public  String getContact() { return contact; }

    public int getrid() {
        return rid;
    }
}
