package eigencraft.cpuArchMod.backend.simulation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class XMLChunkBuilder extends DefaultHandler {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    SimulationWorld world;
    SimulationChunk chunk;

    public XMLChunkBuilder(SimulationChunk chunk, SimulationWorld world, File chunkFile) {
        this.chunk = chunk;
        this.world = world;


        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(chunkFile, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("node")){
            BlockPos nodePos = new BlockPos(Integer.parseInt(attributes.getValue("x")),Integer.parseInt(attributes.getValue("y")),Integer.parseInt(attributes.getValue("z")));
            SimulationNode node = SimulationNode.getFromName(attributes.getValue("type")).apply(nodePos);
            chunk.addNode(node,nodePos);
        } else if (qName.equalsIgnoreCase("pipe")){
            BlockPos pipePos = new BlockPos(Integer.parseInt(attributes.getValue("x")),Integer.parseInt(attributes.getValue("y")),Integer.parseInt(attributes.getValue("z")));
            chunk.addPipe(pipePos);
        }
    }
}
