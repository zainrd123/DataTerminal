package com.drozal.dataterminal.util;

import javafx.scene.control.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class treeViewUtils {
    public static String findXMLValue(String selectedValue, String value, String path) {
        try {
            // Load XML file
            File file = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = factory.newDocumentBuilder().parse(file);

            // Search for the selected value in the XML document
            Element selectedElement = findElementByValue(document.getDocumentElement(), selectedValue);
            if (selectedElement != null) {
                // Retrieve the 'fine_k' attribute value
                String XMLValue = selectedElement.getAttribute(value);
                return XMLValue;
            } else {
                System.out.println("Element not found for value: " + selectedValue);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return selectedValue;
    }

    private static Element findElementByValue(Element parentElement, String value) {
        NodeList childNodes = parentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getAttribute("name").equals(value)) {
                    return childElement;
                }
                // Recursively search child elements
                Element foundElement = findElementByValue(childElement, value);
                if (foundElement != null) {
                    return foundElement;
                }
            }
        }
        return null;
    }

    public static void parseTreeXML(Element parentElement, TreeItem<String> parentItem) {
        NodeList childNodes = parentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                String nodeName = childElement.getNodeName();
                if (childElement.hasAttribute("name")) {
                    nodeName = childElement.getAttribute("name");
                }
                TreeItem<String> item = new TreeItem<>(nodeName);
                parentItem.getChildren().add(item);
                parseTreeXML(childElement, item);
            }
        }
    }

    public static void expandTreeItem(TreeItem<String> root, String itemName) {
        if (root.getValue().equals(itemName)) {
            root.setExpanded(true);
            return;
        }
        for (TreeItem<String> child : root.getChildren()) {
            expandTreeItem(child, itemName);
        }
    }
}