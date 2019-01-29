package co.fresa.pat.mundo;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class User {

    private String name;
    private Date registryDate;
    private int monedas;
    private String idClan;
    private List<Integer> avatarInfo;

    private String patName;

    public User(String name) {
        monedas = 200;
        this.name = name;
        registryDate = new Date();
        avatarInfo = new LinkedList<>();
        for (int i = 0; i < 4; i++) avatarInfo.set(i,1);
        idClan = "none";
    }

    public User(String name, String patName) {
        monedas = 200;
        this.name = name;
        this.patName = patName;
        registryDate = new Date();
        avatarInfo = new LinkedList<>();
        for (int i = 0; i < 4; i++) avatarInfo.add(1);
        idClan = "none";
    }

    public User(){}

    public List<Integer> getAvatarInfo() { return avatarInfo; }

    public void setAvatarInfo(List<Integer> avatarInfo) { this.avatarInfo = avatarInfo; }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }



    public String getIdClan() {
        return idClan;
    }

    public void setIdClan(String idClan) {
        this.idClan = idClan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(Date registryDate) {
        this.registryDate = registryDate;
    }

    public String getPatName() { return patName; }

    public void setPatName(String patName) { this.patName = patName; }

    public void sumarMonedas(int cuantas) { monedas += cuantas; }

    public void setAvatarInfo(int posicion, int valor) { avatarInfo.set(posicion, valor); }
}
