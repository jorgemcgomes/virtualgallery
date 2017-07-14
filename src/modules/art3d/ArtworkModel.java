package modules.art3d;

import java.io.File;
import java.io.IOException;

import javax.vecmath.Point3f;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.web3d.vrml.sav.ImportFileFormatException;
import org.web3d.x3d.sai.X3DComposedGeometryNode;
import org.web3d.x3d.sai.X3DGeometryNode;
import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.TriangleSet;
import org.web3d.x3d.sai.shape.Shape;
import org.xml.sax.SAXException;

import core.Converter;
import core.Gallery;
import core.StatusEvent;
import core.module.AbstractSceneObject;



class ArtworkModel extends Artwork3D {
	
	ArtworkModel(Artwork3DInfo info) {
		super(info);
	}

	@Override
	protected ObjectNode getNode() {
		X3DScene objectScene  = Gallery.getInstance().getScene(getArtworkInfo().getModel());
		X3DNode[] roots = objectScene.getRootNodes();
		Group g = (Group) Gallery.getInstance().getScene().createNode("Group");
		g.realize();
		g.addChildren(roots);
		return new ObjectNode(g, getArtworkInfo().getUpperBound(), getArtworkInfo().getLowerBound());
	}

	@Override
	protected Node getExportNode(Document doc, File output) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document object = docBuilder.parse(getArtworkInfo().getModel());
			
			NodeList l = object.getElementsByTagName("Scene");
			Node scene = l.item(0);
			NodeList roots = scene.getChildNodes();
			
			Element group = doc.createElement("Group");
			for(int i = 0 ; i < roots.getLength() ; i++) {
				Node n = roots.item(i);
				Node imported = doc.importNode(n, true);
				group.appendChild(imported);
			}
			
			return group;
		} catch (Exception e) {
			e.printStackTrace();
			return doc.createComment("Error exporting: " + getArtworkInfo().getName());
		}
	}

}
