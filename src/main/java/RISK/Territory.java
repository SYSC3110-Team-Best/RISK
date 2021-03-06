package RISK;

import java.io.Serializable;

/**
 * The class Territory store the information of territory's name,troops number and owner
 *
 *
 * @Author: Jiatong Han, 101132931
 */
public class Territory implements Serializable {

    private String name;
    private int troops;
    private Player holder;

    /**
     * Instantiates a new Territory.
     *
     * @param name the territory name
     */
    public Territory(String name){
        this.name = name;
        troops = 0;
    }

    /**
     * This method is setting troops in this territory.
     *
     * @param troops the troops in territory
     */
    public void setTroops(int troops) {
        this.troops = troops;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets holder.
     *
     * @param holder the holder
     */
    public void setHolder(Player holder) {
        this.holder = holder;
    }

    /**
     * Gets territory name.
     *
     * @return the name of territory
     */
    public String getName() {
        return name;
    }

    /**
     * Gets troops.
     *
     * @return the troops
     */
    public int getTroops() {
        return troops;
    }

    /**
     * Gets holder.
     *
     * @return the holder
     */
    public Player getHolder() {
        return holder;
    }

    /**
     * Increase troops.
     *
     * @param increase the troops
     */
    public void increaseTroops(int increase){
        troops += increase;
    }

    /**
     * Decrease troops.
     *
     * @param decrease the troops
     */
    public void decreaseTroops(int decrease){
        troops -= decrease;
    }

    /**
     * Short description string to show this territory has how many troops.
     *
     * @return the string description
     */
    public String shortDescription(){return getName()+"("+getTroops()+" troops)";}


     /**
      * @return Territory's name
      */
    @Override
     public String toString(){
        return this.getName();
    }
}
