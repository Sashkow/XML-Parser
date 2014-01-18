package MilitaryPlanes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MilitaryPlanesTest {

    String xmlPath = "./src/MilitaryPlanes/militaryPlanes.xml";
    String xsdPath = "./src/MilitaryPlanes/militaryPlanes.xsd";
    String invalidXSDPath = "./src/MilitaryPlanes/invalidMilitaryPlanes.xsd";

    @Test
    public void testValidateXMLSuccess() throws Exception {
        boolean result = MilitaryPlanes.validateXML(xsdPath, xmlPath);

        assertTrue(result);
    }

    @Test
    public void testValidateXMLFail() throws Exception {
        boolean result = MilitaryPlanes.validateXML(invalidXSDPath, xmlPath);

        assertFalse(result);
    }

    @Test
    public void testDOMParse() throws Exception {
        List<Plane> planes = MilitaryPlanes.DOMParse(xmlPath);

        assertEquals(5, planes.size());
    }

    @Test
    public void testSAXParse() throws Exception {
        List<Plane> planes = MilitaryPlanes.SAXParse(xmlPath);

        assertEquals(5, planes.size());
    }

    @Test
    public void testStAXParse() throws Exception {
        List<Plane> planes = MilitaryPlanes.StAXParse(xmlPath);

        assertEquals(5, planes.size());
    }

    @Test
    public void toXMLTest(){
        Plane plane = new Plane();

        plane.setId(101);
        plane.setModel(Model.Raptor);
        plane.setOrigin(Country.USA);
        plane.setType(PlaneType.Fighter);
        plane.setCrew(1);
        plane.setPrice(146.2);

        PlaneParameters parameters = new PlaneParameters();
        parameters.setLength(18.9);
        parameters.setWingspan(13.56);
        parameters.setWeight(19700);

        plane.setParameters(parameters);

        List<Plane> planes = new ArrayList<>();
        planes.add(plane);

        MilitaryPlanes.toXML(planes, "./src/MilitaryPlanes/toMilitaryPlanes.xml");
    }
}
