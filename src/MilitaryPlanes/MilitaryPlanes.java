package MilitaryPlanes;

import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.stream.XMLStreamConstants;

public class MilitaryPlanes {

    public static boolean validateXML(String xsdPath, String xmlPath){
        try{
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        }
        catch (IOException | SAXException e){
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * DOM parsing
     */
    public static List<Plane> DOMParse(String xmlPath)
            throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlPath));

        List<Plane> result = new ArrayList<>();

        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for(int i = 0; i < nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if(node instanceof Element){
                Plane plane = new Plane();
                String id = node.getAttributes().getNamedItem("id").getNodeValue();
                plane.setId(Integer.parseInt(id));

                NodeList childNodes = node.getChildNodes();
                for(int j = 0; j < childNodes.getLength(); j++){
                    Node cNode = childNodes.item(j);

                    if(cNode instanceof Element){
                        String content = cNode.getLastChild().getTextContent().trim();
                        switch (cNode.getNodeName()){
                            case "model":
                                plane.setModel(Model.getModel(content));
                                break;
                            case "origin":
                                plane.setOrigin(Country.getCountry(content));
                                break;
                            case "chars":
                                NodeList charNodes = cNode.getChildNodes();
                                for (int k = 0; k < charNodes.getLength(); k++){
                                    Node charNode = charNodes.item(k);
                                    if(charNode instanceof Element){
                                        String charContent = charNode.getLastChild().getTextContent().trim();
                                        switch (charNode.getNodeName()){
                                            case "type":
                                                plane.setType(PlaneType.getType(charContent));
                                                break;
                                            case "crew":
                                                plane.setCrew(Integer.parseInt(charContent));
                                                break;
                                        }
                                    }
                                }
                                break;
                            case "parameters":
                                NodeList parameterNodes = cNode.getChildNodes();
                                PlaneParameters parameters = new PlaneParameters();
                                for(int k = 0; k < parameterNodes.getLength(); k++){
                                    Node parameterNode = parameterNodes.item(k);
                                    if(parameterNode instanceof Element){
                                        String parameterContent =
                                                parameterNode.getLastChild().getTextContent().trim();
                                        switch (parameterNode.getNodeName()){
                                            case "length":
                                                parameters.setLength(Double.parseDouble(parameterContent));
                                                break;
                                            case "wingspan":
                                                parameters.setWingspan(Double.parseDouble(parameterContent));
                                                break;
                                            case "weight":
                                                parameters.setWeight(Integer.parseInt(parameterContent));
                                                break;
                                        }
                                    }
                                }
                                plane.setParameters(parameters);
                                break;
                            case "price":
                                plane.setPrice(Double.parseDouble(content));
                                break;
                        }
                    }
                }
                result.add(plane);
            }
        }
        return result;
    }

    /**
     * SAX parsing
     */
    public static List<Plane> SAXParse(String xmlPath)
            throws Exception{
        class SAXHandler extends DefaultHandler{
            List<Plane> planes = new ArrayList<>();
            Plane plane;
            String content = null;

            @Override
            public void startElement(String uri, String localName,
                                     String qName, Attributes attributes)
                    throws SAXException {
                switch (qName){
                    case "plane":
                        plane = new Plane();
                        plane.setParameters(new PlaneParameters());
                        plane.setId(Integer.parseInt(attributes.getValue("id")));
                        break;
                }
            }

            @Override
            public void endElement(String uri, String localName,
                                   String qName) throws SAXException{
                switch (qName){
                    case "plane":
                        planes.add(plane);
                        break;
                    case "model":
                        plane.setModel(Model.getModel(content));
                        break;
                    case "origin":
                        plane.setOrigin(Country.getCountry(content));
                        break;
                    case "type":
                        plane.setType(PlaneType.getType(content));
                        break;
                    case "crew":
                        plane.setCrew(Integer.parseInt(content));
                        break;
                    case "length":
                        plane.getParameters().setLength(Double.parseDouble(content));
                        break;
                    case "wingspan":
                        plane.getParameters().setWingspan(Double.parseDouble(content));
                        break;
                    case "weight":
                        plane.getParameters().setWeight(Double.parseDouble(content));
                        break;
                    case "price":
                        plane.setPrice(Double.parseDouble(content));
                        break;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length)
                    throws SAXException{
                content = String.copyValueOf(ch, start, length).trim();
            }
        }

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        SAXHandler handler = new SAXHandler();

        parser.parse(new File(xmlPath), handler);
        return handler.planes;
    }

    /**
     * StAX
     */
    public static List<Plane> StAXParse(String xmlPath)
            throws Exception{
        List<Plane> planes = new ArrayList<>();
        Plane plane = new Plane();
        plane.setParameters(new PlaneParameters());
        String tagContext = null;
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader reader =
                factory.createXMLStreamReader(new FileInputStream(new File(xmlPath)));

        while(reader.hasNext()){
            int event = reader.next();

            switch (event){
                case XMLStreamConstants.START_ELEMENT:
                    if("plane".equals(reader.getLocalName())){
                        plane = new Plane();
                        plane.setParameters(new PlaneParameters());
                        plane.setId(Integer.parseInt(reader.getAttributeValue(0)));
                    }
                    if("military_planes".equals(reader.getLocalName())){
                        planes = new ArrayList<>();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContext = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()){
                        case "plane":
                            planes.add(plane);
                            break;
                        case "model":
                            plane.setModel(Model.getModel(tagContext));
                            break;
                        case "origin":
                            plane.setOrigin(Country.getCountry(tagContext));
                            break;
                        case "type":
                            plane.setType(PlaneType.getType(tagContext));
                            break;
                        case "crew":
                            plane.setCrew(Integer.parseInt(tagContext));
                            break;
                        case "length":
                            plane.getParameters().setLength(Double.parseDouble(tagContext));
                            break;
                        case "wingspan":
                            plane.getParameters().setWingspan(Double.parseDouble(tagContext));
                            break;
                        case "weight":
                            plane.getParameters().setWeight(Double.parseDouble(tagContext));
                            break;
                        case "price":
                            plane.setPrice(Double.parseDouble(tagContext));
                    }
                    break;

                case XMLStreamConstants.START_DOCUMENT:
                    planes = new ArrayList<>();
                    break;
            }
        }
        return planes;
    }

    /**
     * Writing to file
     */
    public static void toXML(List<Plane> planeObjects, String filePath){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element rootElement = document.createElement("military_planes");
            document.appendChild(rootElement);

            for(Plane currPlaneObject : planeObjects){
                Element plane = document.createElement("plane");
                rootElement.appendChild(plane);

                Attr attr = document.createAttribute("id");
                attr.setValue(Integer.toString(currPlaneObject.getId()));
                plane.setAttributeNode(attr);

                Element model = document.createElement("model");
                model.appendChild(document.createTextNode(currPlaneObject.getModel().getName()));
                plane.appendChild(model);

                Element origin = document.createElement("origin");
                origin.appendChild(document.createTextNode(currPlaneObject.getOrigin().getName()));
                plane.appendChild(origin);

                Element chars = document.createElement("chars");

                Element type = document.createElement("type");
                type.appendChild(document.createTextNode(currPlaneObject.getType().name()));
                Element crew = document.createElement("crew");
                crew.appendChild(document.createTextNode(Integer.toString(currPlaneObject.getCrew())));

                chars.appendChild(type);
                chars.appendChild(crew);

                plane.appendChild(chars);

                Element parameters = document.createElement("parameters");

                Element length = document.createElement("length");
                length.appendChild(document.createTextNode(Double.toString(currPlaneObject.getParameters().getLength())));
                Element wingspan = document.createElement("wingspan");
                wingspan.appendChild(document.createTextNode(Double.toString(currPlaneObject.getParameters().getWingspan())));
                Element weight = document.createElement("weight");
                weight.appendChild(document.createTextNode(Double.toString(currPlaneObject.getParameters().getWeight())));

                parameters.appendChild(length);
                parameters.appendChild(wingspan);
                parameters.appendChild(weight);

                plane.appendChild(parameters);

                Element price = document.createElement("price");
                price.appendChild(document.createTextNode(Double.toString(currPlaneObject.getPrice())));
                plane.appendChild(price);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);
        } catch (ParserConfigurationException |  TransformerException e) {
            e.printStackTrace();
        }
    }
}
