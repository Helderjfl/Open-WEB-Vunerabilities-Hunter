/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package invuler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author Ro
 */
public class ModuloPHP implements Serializable{

   private String moduloOn;
    public static final String PROP_MODULOON = "moduloOn";

    /**
     * Get the value of moduloOn
     *
     * @return the value of moduloOn
     */
    public String getModuloOn() {
        return moduloOn;
    }

    /**
     * Set the value of moduloOn
     *
     * @param moduloOn new value of moduloOn
     */
    public void setModuloOn(String moduloOn) {
        String oldModuloOn = this.moduloOn;
        System.out.println(this.moduloOn+moduloOn);
        this.moduloOn = moduloOn;
        propertyChangeSupport.firePropertyChange(PROP_MODULOON, oldModuloOn, moduloOn);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("ModuloPHP");
        propertyChangeSupport.addPropertyChangeListener(listener);

    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("ModuloPHP");
        propertyChangeSupport.removePropertyChangeListener(listener);

    }


}
