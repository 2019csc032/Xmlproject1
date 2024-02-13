import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class XmlToDatabase {
    public static void main(String[] args) {
        try {
            // Step 1: Parse XML file
            File xmlFile = new File("book.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
           Element rootElement = doc.getDocumentElement();

           System.out.println("Root element ="+rootElement.getNodeName());

            // Step 2: Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/xmldb", "root", "");



            // Step 4: Insert data into the database
            NodeList nodeList = doc.getElementsByTagName("books");
            String insertQuery = "INSERT INTO book (Id, Name, Author, Publisher) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);



            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String id = node.getAttributes().getNamedItem("id").getNodeValue();
                    String name = node.getTextContent();
                    String author = node.getTextContent();
                    String publisher = node.getTextContent();

                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, author);
                    preparedStatement.setString(4, publisher);
                    preparedStatement.addBatch();
                }
            }
           
            conn.close();
            System.out.println("Data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
