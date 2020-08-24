/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pesrona.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.hibernate.Session;

import pesrona.HibernateUtil;
import pesrona.model.Resource;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ResourcesBean implements Serializable {

    private Session session;
    
    private List<Resource> resources;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        resources = session.createQuery("select o from Resource o").getResultList();
    }

    public List<Resource> getResources() {
        return resources;
    }
}
