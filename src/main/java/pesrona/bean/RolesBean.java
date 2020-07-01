/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pesrona.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author dagva
 */
@Named(value = "rolesBean")
@ViewScoped
public class RolesBean implements Serializable {

    /**
     * Creates a new instance of RolesBean
     */
    public RolesBean() {
    }
    
    private String name;

    @PostConstruct
    public void init() {
        
    }
    
    public void save() {
        System.out.println(name);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
