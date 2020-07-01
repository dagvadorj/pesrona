/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pesrona.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pesrona.HibernateUtil;
import pesrona.model.Role;

/**
 *
 * @author dagva
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/test"})
public class TestServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Hello");
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            Role role = new Role();
            // role.setId(1L);
            role.setName("Test");
            
            session.save(role);
            
            transaction.commit();
            
            List<Role> roles = session.createQuery("select o from Role o").getResultList();
            System.out.println(roles.size());
            for (Role r : roles) {
                System.out.println(r.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Yes");
        }
    }

}
