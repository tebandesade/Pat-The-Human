package co.fresa.pat.mundo;

import java.io.Serializable;

public class Mission implements Serializable {
    private String name;
    private String criteria;
    private String description;
    private int reward;
    private int level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Mission( String name,
            String criteria,
            String description,
            int reward,
            int level,
                    String id)
    {
        this.id = id;
        this.name = name;
        this.reward = reward;
        this.criteria = criteria;
        this.description = description;
        this.level =level;
    }

    public Mission() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return name;
    }
}
