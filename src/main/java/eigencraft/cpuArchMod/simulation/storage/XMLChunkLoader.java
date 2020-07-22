package eigencraft.cpuArchMod.simulation.storage;

import eigencraft.cpuArchMod.simulation.*;
import eigencraft.cpuArchMod.simulation.pipes.EndPipe;
import eigencraft.cpuArchMod.simulation.pipes.TransferPipe;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class XMLChunkLoader extends DefaultHandler {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    SimulationWorld world;
    SimulationChunk chunk;

    public static SimulationChunk load(SimulationChunk target,SimulationWorld world, File file){
        return new XMLChunkLoader(target,world,file).chunk;
    }

    private XMLChunkLoader(SimulationChunk chunk, SimulationWorld world, File chunkFile) {
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
        try {
            if (qName.equalsIgnoreCase("chunk")){
                int version = Integer.parseInt(attributes.getValue("version"));
                if (version > SaveFormatUtils.getVersion()){
                    LogManager.getLogger().warn("Found simulation chunk from newer mod version!");
                } else if (version < SaveFormatUtils.getVersion()){
                    //TODO upgrade system?
                    LogManager.getLogger().warn(String.format("Found simulation chunk from older mod version! Found: %d, Mod: %d",version,SaveFormatUtils.getVersion()));
                }
            } else if (qName.equalsIgnoreCase("node")){
                BlockPos nodePos = new BlockPos(Integer.parseInt(attributes.getValue("x")), Integer.parseInt(attributes.getValue("y")), Integer.parseInt(attributes.getValue("z")));
                SimulationNode node = SimulationNode.getFromName(attributes.getValue("type")).apply(nodePos);
                chunk.addNode(node, nodePos);
            } else if (qName.equalsIgnoreCase("pipe")){
                BlockPos pipePos = new BlockPos(Integer.parseInt(attributes.getValue("x")), Integer.parseInt(attributes.getValue("y")), Integer.parseInt(attributes.getValue("z")));
                SimulationPipe pipe = SimulationPipe.getFromName(attributes.getValue("type")).apply(pipePos);
                chunk.addPipe(pipe, pipePos);
            }
        } catch (Exception e){
            LogManager.getLogger().error("Failed to parse simulation chunk element");
        }
    }
}
