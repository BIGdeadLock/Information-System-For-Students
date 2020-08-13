package Tools;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class xmlParser {

    private String data_file_path;
    private File data_file;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;



    public xmlParser(String data_file_path) {
        this.data_file_path = data_file_path;

        try {
            Document doc = getDoc();

        }catch (Exception e){
            System.out.println("Error can't create the xml parser");
            e.printStackTrace();
        }

    }


    public String getRootElement(){ return doc.getDocumentElement().getNodeName(); }

    public Document getDoc() throws Exception{

        data_file = new File(data_file_path);
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(data_file);
        doc.getDocumentElement().normalize();

        return doc;
    }

    public ArrayList<String> getXMLElement(String element){

            ArrayList<String>res = new ArrayList<String>();
            try {

                Element root = doc.getDocumentElement();

                NodeList nList = doc.getElementsByTagName("courses");

                System.out.println(String.format("-------Searching for %s in the xml database--------", element));
                Thread.sleep(1500);

                // Start the search of the node
                int i = 0;
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node node = nList.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) node;
                        NodeList data = eElement.getElementsByTagName(element);
                        for (;i < data.getLength(); i++) {
                            res.add(data.item(i).getTextContent());
                        }
                    }//if
                }// out for

                //check if the data was found
                if(i == 0)
                   throw(new NullPointerException());

            }// try

            catch (NullPointerException e) {
                System.out.println("Cant find the data you mentioned try again next time");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return res;

    }// function

    public void printAllCoursesData() {

        try {

            Element root = doc.getDocumentElement();
            System.out.println(root.getNodeName() + " ---> " +root.getAttribute("name"));

            //Get all courses Information
            NodeList nList = doc.getElementsByTagName("course");
            System.out.println("============================\n");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                System.out.println("******************************");
                System.out.println("Showing the course details: \n");    //Just a separator

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Print each courses's detail
                    Element eElement = (Element) node;
                    Thread.sleep(1000);

                    //Get all the course details
                    ArrayList<String> course_data = getCourseData(eElement.getElementsByTagName("course-name").item(0).getTextContent());
                    for(int i = 0;i<course_data.size();i++)
                    {
                        System.out.println("\t" + course_data.get(i) + '\n');
                    }

                }//if
                System.out.println("******************************\n");
            }//for
        }//try

            catch(Exception e) {
            System.out.println("There was a problem in displaying the courses");
            e.printStackTrace();
            }

    }


    public ArrayList<String> getCourseData(String course) {

        ArrayList<String> data = new ArrayList<>();

        try {
            Element root = doc.getDocumentElement();
            System.out.println(root.getNodeName() + " ---> " +root.getAttribute("name"));

            //Get all courses Information
            NodeList nList = doc.getElementsByTagName("course");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);


                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) node;
                    // Search for the course name in the nodes data
                    if (eElement.getElementsByTagName("course-name").item(0).getTextContent().equals(course)) {

                        String course_name = eElement.getElementsByTagName("course-name").item(0).getTextContent();
                        String course_number = eElement.getElementsByTagName("course-number").item(0).getTextContent();

                        data.add(course_name);
                        data.add(course_number);

                        NodeList lecturers = eElement.getElementsByTagName("lecturer");
                        data.add("Lecturers:");
                        for (int i = 0; i < lecturers.getLength(); i++)
                            data.add(lecturers.item(i).getTextContent());

                        NodeList tutors = eElement.getElementsByTagName("tutor");
                        data.add("Instructors:");
                        for (int i = 0; i < tutors.getLength(); i++)
                            data.add(tutors.item(i).getTextContent());


                    }//inner if
                }//outer iff
            }//for

        }//try

        catch(Exception e) {
            System.out.println("There was a problem in displaying the courses");
            e.printStackTrace();
        }

        return data;
    }

    private static List<String> evaluateXPath(Document document, String xpathExpression) throws Exception
    {
        // Create XPathFactory object
        XPathFactory xpathFactory = XPathFactory.newInstance();

        // Create XPath object
        XPath xpath = xpathFactory.newXPath();

        List<String> values = new ArrayList<>();
        try
        {
            // Create XPathExpression object
            XPathExpression expr = xpath.compile(xpathExpression);

            // Evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                values.add(nodes.item(i).getNodeValue());
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return values;
    }






}// class






