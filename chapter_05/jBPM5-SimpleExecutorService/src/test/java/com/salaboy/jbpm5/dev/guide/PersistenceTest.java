/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.salaboy.jbpm5.dev.guide;

import com.salaboy.jbpm5.dev.guide.executor.entities.RequestInfo;
import com.salaboy.jbpm5.dev.guide.executor.entities.STATUS;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author salaboy
 */
public class PersistenceTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private Server server;
    private ApplicationContext ctx;
    public PersistenceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
         
         
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        
    }

    @Before
    public void setUp() {
        DeleteDbFiles.execute("~", "mydb", false);

        try {
            
            server = Server.createTcpServer(new String[] {"-tcp","-tcpAllowOthers","-tcpDaemon","-trace"}).start(); 
        } catch (SQLException ex) {
            System.out.println("ex: "+ex);
        }
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        emf = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
        server.stop();
    }

    @Test
    public void persistenceSimple() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setKey("HI");
        requestInfo.setStatus(STATUS.QUEUED);
        requestInfo.setMessage("Ready to execute");
        em.getTransaction().begin();
        em.persist(requestInfo);
        em.getTransaction().commit();
        List<?> resultList = em.createQuery("Select r from RequestInfo as r").getResultList();
        assertEquals(1, resultList.size());
    }
    
    
}
