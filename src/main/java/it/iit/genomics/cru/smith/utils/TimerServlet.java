package it.iit.genomics.cru.smith.utils;

//import it.iit.genomics.cru.analysis.AnalysisManagerSEMM;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.iit.genomics.cru.smith.defaults.Preferences;

import it.iit.genomics.cru.analysis.AnalysisManagerCEMM;
import it.iit.genomics.cru.analysis.DailyTask;
import java.util.Date;
import javax.faces.context.FacesContext;

/**
 * @(#)TimerServlet.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Utility class for triggering analyses.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@WebServlet(name = "TimerServlet", urlPatterns = {"/TimerServlet"})
public class TimerServlet extends HttpServlet {
    

    private static final long serialVersionUID = 1L;

    private Timer timer = null;
    private long runfolderscaninterval = 60 * 60 *1000; 
    //public static final long HOURS_6 = 6 * 3600 * 1000;
    //public static final long HOURS_1 = 1 * 3600 * 1000;
    //public static final long MINS_1 = 60 * 1000;
    //public static final long MINS_10 = 600 * 1000;
    //public static final long SECS_10 = 10 * 1000;

    /**
     * Init.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void init() {
        if(Preferences.getVerbose()){
            System.out.println("init TimerServlet");
        }
        FacesContext context = FacesContext.getCurrentInstance();   
        //runfolderscaninterval = Preferences.getRunfolderScanInterval();
        Object sess = (Object)context.getExternalContext().getSession(true);
        if (timer == null) {
            //System.out.println("TIMER");
            timer = new Timer();
            DailyTask dt = new DailyTask();
            dt.setAnalysisManager(new AnalysisManagerCEMM());
            timer.scheduleAtFixedRate(dt,   new Date(System.currentTimeMillis()), runfolderscaninterval); 
        }
    }

    /**
     * Destroys servlet.
     *
     * @author Heiko Muller
     * @since 1.0
     */
        
    public void destroy() {
        if(Preferences.getVerbose()){
            System.out.println("destroy TimerServlet");
        }
        if(timer != null){
            timer.cancel();
            timer = null;
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @author Heiko Muller
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @since 1.0
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TimerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TimerServlet at " + request.getContextPath() + "</h1>");
            out.println("<h1>Servlet Timer started" + "</h1>");
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
