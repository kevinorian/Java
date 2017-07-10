package com.kevin;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.*;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        //for writing into a file
        String FILENAME = "C:\\Users\\user\\IdeaProjects\\parser\\src\\main\\resources\\insert.sql";
        BufferedWriter bw = null;
        FileWriter fw = null;


        try {
            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
            File fXmlFile = new File("C:\\Users\\user\\IdeaProjects\\parser\\src\\main\\resources\\countries.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("option");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String val = eElement.getAttribute("value");
                    if (val.contains("'")) {
                        val = val.replaceAll("'", "''");
                    }

                    String content = "insert into public.\"Country\" (country_id, name, description) values (" + (temp + 1) + ", '" + val + "', '');\n";
                    bw.write(content);
                    bw.flush();
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {
            Class.forName("org.postgresql.Driver");


            FileReader reader = new FileReader(new File(FILENAME));
            BufferedReader br = new BufferedReader(reader);
            String query;
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/test", "postgres",
                    "postgres");

            int count =1;
            while ((query = br.readLine()) != null) {
                System.out.println();
                Statement stmt = connection.createStatement();
                stmt.execute(query);
                count++;
            }
            connection.close();
            br.close();
            reader.close();
        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
}
