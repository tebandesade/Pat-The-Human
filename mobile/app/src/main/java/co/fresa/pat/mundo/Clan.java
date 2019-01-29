package co.fresa.pat.mundo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by teban on 4/2/2018.
 */
import org.json.*;

public class Clan implements Serializable
{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCantidadMiembros() {
        return cantidadMiembros;
    }

    public void setCantidadMiembros(long cantidadMiembros) {
        this.cantidadMiembros = cantidadMiembros;
    }

    public HashMap getMisiones() {
        return misiones;
    }

    public void setMisiones(HashMap misiones) {
        this.misiones = misiones;
    }

    private String name;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    private String placeID;
    private long cantidadMiembros;
    private HashMap misiones;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    private long money;



    public Clan(String nombre,String place)
    {
        name = nombre;
        misiones = new HashMap();
        cantidadMiembros = 1;
        placeID =place;
        money = 0;
    }
    public Clan()
    {

    }
    public Clan(HashMap hm)
    {

        transformClan(hm);
        /***
        name = (String) hm.get("name");
        System.out.println("this is waza");
      //  System.out.println(hm.get("misiones").getClass());
        //Aqui toca integrar con cebo
        misiones = (HashMap) hm.get("misiones");
        cantidadMiembros = (long) hm.get("cantidadMiembros");
        placeID = hm.get("placeID").toString();
        money  = (long) hm.get("money");
        ***/
    }

    private void transformClan(HashMap actualtemp )
    {
      //  actualtemp.
        name = (String) actualtemp.get("name");
        placeID = (String) actualtemp.get("placeID");
        cantidadMiembros  = (long) actualtemp.get("cantidadMiembros");
        long level = (long) actualtemp.get("money");
        money= (int)level;;
        Object descr =  actualtemp.get("misiones");
        if (descr==null)
        {

        }
        else
        {
            if (descr.getClass()==ArrayList.class)
            {
                HashMap miss = new HashMap();
                ArrayList temp_array = (ArrayList) descr;
                int i;
                System.out.println("Array parsing hashmap");
                    for(i=0;i<temp_array.size();i++)
                {

                    if(temp_array.get(i)==null)
                    {
                            System.out.print("nullmission");
                    }
                    else
                    {
                        HashMap misActual = (HashMap) temp_array.get(i);
                        Mission temp_mission = transformMission(misActual);

                        miss.put(temp_mission.getId(),temp_mission);
                        System.out.println(misActual);
                       // miss.put(misActual.getId(),misActual);
                    }

                }
                misiones=miss;
            }
            else
            {
                HashMap temp_hash = (HashMap) actualtemp.get("misiones");
              misiones = temp_hash;
            }
        }

    }

    public void removeMember()
    {
        cantidadMiembros--;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addMember() {
        cantidadMiembros++;
    }

    public void addMission(String idKey, Mission missAdd)
    {
       // HashMap hMap = new HashMap();
        misiones.put(idKey,missAdd);
        //misiones.add(hMap);
        //.put(idKey,missAdd);
        //misiones.put(idKey,missAdd);
    }

    public void addAmountMoney(int moni)
    {
        money += moni;
    }

    public void inicializarMisiones() {
        misiones = new HashMap();
    }
    private Mission transformMission(HashMap actualtemp )
    {
        String nom = (String) actualtemp.get("name");
        String crit = (String) actualtemp.get("criteria");
        String idMis = (String) actualtemp.get("id");
        String descr = (String) actualtemp.get("description");
        long rwardd = (long) actualtemp.get("reward");
        int rward = (int) rwardd;
        long level = (long) actualtemp.get("level");
        int lvl = (int)level;
        return new Mission(nom,crit,descr,rward,lvl,idMis);
    }
}
