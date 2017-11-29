/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl.illuminaxml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author hmueller
 */
public class XMLParser {
    
    static int Ncount = 0;
    static int Ycount = 0;
    
    static String Flowcell = "";
    static String ApplicationName = "";
    static String ExperimentName = "";
    
    //parse read mode
    public static String getReadMode(String runinfoxml_file) {
        if(!runinfoxml_file.endsWith("RunInfo.xml")){
            return "";
        }
        try {
            File fXmlFile = new File(runinfoxml_file);
            if(!fXmlFile.exists()){
                return "";
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            String rootnode = doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName(rootnode);//RunParameters        
            Ncount = 0;
            Ycount = 0;
            parseReadMode(nList.item(0), "Read");
            //System.out.println("Ncount:\t" + Ncount + "\tYcount\t" + Ycount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prepareReadModeOutput(Ncount, Ycount);
    }
    
    private static String prepareReadModeOutput(int N, int Y){
        String result = "";
        if(N <= 0 || N > 2){
            result = "configuration error";
        }else if(N == 1 && Y == 0){
            result = "single end no index";
        }else if(N == 1 && Y == 1){
            result = "single end single index";
        }else if(N == 1 && Y == 2){
            result = "single end dual index";
        }else if(N == 2 && Y == 0){
            result = "paired end no index";
        }else if(N == 2 && Y == 1){
            result = "paired end single index";
        }else if(N == 2 && Y == 2){
            result = "paired end dual index";
        }
        return result;
    }
    
    private static void parseReadMode(Node n, String target) {
        if (n.hasChildNodes() && n.getChildNodes().getLength() > 1) {
            NodeList l = n.getChildNodes();
            for (int i = 0; i < l.getLength(); i++) {
                parseReadMode(l.item(i), target);
            }
        } else {
            if (!n.getNodeName().equals("#text")) {
                if (n.hasAttributes()) {
                    NamedNodeMap nnm = n.getAttributes();
                    for (int i = 0; i < nnm.getLength(); i++) {
                        if (n.getNodeName().equals(target)) {
                            if (nnm.item(i).getTextContent().equals("N")) {
                                Ncount++;
                            } else if (nnm.item(i).getTextContent().equals("Y")) {
                                Ycount++;
                            }
                        }
                    }
                } else {
                    if (n.getNodeName().equals(target)) {
                        if (n.getTextContent().equals("N")) {
                            Ncount++;
                        } else if (n.getTextContent().equals("Y")) {
                            Ycount++;
                        }
                    }
                }
            }
        }
    }
    
    
    
    //parse flowcell
    public static String getFlowcell(String runParametersxml_file) {
        if(!runParametersxml_file.endsWith("runParameters.xml")){
            return "";
        }
        try {
            File fXmlFile = new File(runParametersxml_file);
            if(!fXmlFile.exists()){
                return "";
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            String rootnode = doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName(rootnode);//RunParameters        
            Flowcell = "";
            parseFlowcell(nList.item(0), "Flowcell");
            //System.out.println("Ncount:\t" + Ncount + "\tYcount\t" + Ycount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Flowcell;
    }
    
    private static void parseFlowcell(Node n, String target) {        
        if (n.hasChildNodes() && n.getChildNodes().getLength() > 1) {
            NodeList l = n.getChildNodes();
            for (int i = 0; i < l.getLength(); i++) {
                parseFlowcell(l.item(i), target);
            }
        } else {   
            if (!n.getNodeName().equals("#text")) {
                if (n.hasAttributes()) {
                    NamedNodeMap nnm = n.getAttributes();
                    for (int i = 0; i < nnm.getLength(); i++) {
                        if (n.getNodeName().equals(target)) {
                            Flowcell = nnm.item(i).getTextContent();
                        }
                    }
                } else {
                    
                    if (n.getNodeName().equals(target)) {
                        Flowcell = n.getTextContent();                        
                    }
                }
            }
        }
    }
    
    
    //parse ApplicationName
    public static String getApplicationName(String runParametersxml_file) {
        if(!runParametersxml_file.endsWith("runParameters.xml")){
            return "";
        }
        try {
            File fXmlFile = new File(runParametersxml_file);
            if(!fXmlFile.exists()){
                return "";
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            String rootnode = doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName(rootnode);//RunParameters        
            ApplicationName = "";
            parseApplicationName(nList.item(0), "ApplicationName");
            //System.out.println("Ncount:\t" + Ncount + "\tYcount\t" + Ycount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApplicationName;
    }
    
    private static void parseApplicationName(Node n, String target) {        
        if (n.hasChildNodes() && n.getChildNodes().getLength() > 1) {
            NodeList l = n.getChildNodes();
            for (int i = 0; i < l.getLength(); i++) {
                parseApplicationName(l.item(i), target);
            }
        } else {   
            if (!n.getNodeName().equals("#text")) {
                if (n.hasAttributes()) {
                    NamedNodeMap nnm = n.getAttributes();
                    for (int i = 0; i < nnm.getLength(); i++) {
                        if (n.getNodeName().equals(target)) {
                            ApplicationName = nnm.item(i).getTextContent();
                        }
                    }
                } else {
                    
                    if (n.getNodeName().equals(target)) {
                        ApplicationName = n.getTextContent();                        
                    }
                }
            }
        }
    }
    
    //parse ApplicationName
    public static String getExperimentName(String runParametersxml_file) {
        if(!runParametersxml_file.endsWith("runParameters.xml")){
            return "";
        }
        try {
            File fXmlFile = new File(runParametersxml_file);
            if(!fXmlFile.exists()){
                return "";
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            String rootnode = doc.getDocumentElement().getNodeName();
            NodeList nList = doc.getElementsByTagName(rootnode);//RunParameters        
            ApplicationName = "";
            parseExperimentName(nList.item(0), "ExperimentName");
            //System.out.println("Ncount:\t" + Ncount + "\tYcount\t" + Ycount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ExperimentName;
    }
    
    private static void parseExperimentName(Node n, String target) {        
        if (n.hasChildNodes() && n.getChildNodes().getLength() > 1) {
            NodeList l = n.getChildNodes();
            for (int i = 0; i < l.getLength(); i++) {
                parseExperimentName(l.item(i), target);
            }
        } else {   
            if (!n.getNodeName().equals("#text")) {
                if (n.hasAttributes()) {
                    NamedNodeMap nnm = n.getAttributes();
                    for (int i = 0; i < nnm.getLength(); i++) {
                        if (n.getNodeName().equals(target)) {
                            ExperimentName = nnm.item(i).getTextContent();
                        }
                    }
                } else {
                    
                    if (n.getNodeName().equals(target)) {
                        ExperimentName = n.getTextContent();                        
                    }
                }
            }
        }
    }
    
    
    
    public static void main(String argv[]) {

        File f = new File("C:\\Temp\\Runs");
        File[] fa = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File[] files = fa[i].listFiles();
            for (int j = 0; j < files.length; j++) {
                if(files[j].getName().endsWith(".xml")){
                    //System.out.println(files[j].getName());
                    System.out.println(getReadMode(files[j].getAbsolutePath()));
                    System.out.println(getFlowcell(files[j].getAbsolutePath()));
                    System.out.println(getApplicationName(files[j].getAbsolutePath()));
                    System.out.println(getExperimentName(files[j].getAbsolutePath()));
                }
            }
        }
    }
    
}
