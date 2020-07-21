package eigencraft.cpuArchMod.simulation.nodes;

import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.dataObject.DataObject;
import eigencraft.cpuArchMod.dataObject.DataObjectType;
import eigencraft.cpuArchMod.dataObject.DataObjectTypes;
import eigencraft.cpuArchMod.simulation.SimulationIOManager;
import eigencraft.cpuArchMod.simulation.SimulationNode;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class ConverterNode extends SimulationNode {
    private ConverterNodeConfiguration configuration;
    public ConverterNode(BlockPos position) {
        super(position);
        configuration = new ConverterNodeConfiguration();
    }

    @Override
    public void process(DataObject inMessages, SimulationIOManager ioManager) {

    }

    @Override
    public void onUse(SimulationIOManager ioManager, PlayerEntity player) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        String configuration = CpuArchMod.GSON.toJson(this.configuration);
        System.out.println(configuration);
        data.writeString(configuration);
        ioManager.addMainTickRunnable(new SimulationIOManager.MinecraftServerRunnable() {
            @Override
            public void run(MinecraftServer server) {
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CpuArchMod.CONVERTER_GUI_OPEN_S2C_PACKET,data);
            }
        });
    }

    public class ConverterNodeConfiguration{
        DataObjectType inputType = DataObjectTypes.NULL_TYPE;
        DataObjectType outputType = DataObjectTypes.NULL_TYPE;
        HashMap<String,String> assignedStringFields = new HashMap<>();
        HashMap<String,String> assignedIntFields = new HashMap<>();
        HashMap<String,String> assignedByteFields = new HashMap<>();
        HashMap<String,String> assignedIntArrayFields = new HashMap<>();
        HashMap<String,String> assignedByteArrayFields = new HashMap<>();
        HashMap<String,String> reverseConnections = new HashMap<>();
        public DataObject convert(DataObject in){
            if ((inputType!=null)&&(outputType!=null)){
                if (inputType.matchesType(in)){
                    DataObject out = new DataObject(outputType);

                    for (Map.Entry<String, String> field : assignedStringFields.entrySet()) {
                        out.setString(field.getValue(), in.getString(field.getKey()));
                    }
                    for (Map.Entry<String, String> field : assignedIntFields.entrySet()) {
                        out.setInt(field.getValue(), in.getInt(field.getKey()));
                    }
                    for (Map.Entry<String, String> field : assignedByteFields.entrySet()) {
                        out.setByte(field.getValue(), in.getByte(field.getKey()));
                    }
                    for (Map.Entry<String, String> field : assignedIntArrayFields.entrySet()) {
                        out.setIntArray(field.getValue(), in.getIntArray(field.getKey()));
                    }
                    for (Map.Entry<String, String> field : assignedByteArrayFields.entrySet()) {
                        out.setByteArray(field.getValue(), in.getByteArray(field.getKey()));
                    }
                    return out;
                }
            }
            return null;
        }
        public void setInputType(DataObjectType type){
            inputType = type;
            HashMap<String,String> assignedStringFields = new HashMap<>();
            HashMap<String,String> assignedIntFields = new HashMap<>();
            HashMap<String,String> assignedByteFields = new HashMap<>();
            HashMap<String,String> assignedIntArrayFields = new HashMap<>();
            HashMap<String,String> assignedByteArrayFields = new HashMap<>();
        }
        public void setOutputType(DataObjectType type){
            outputType = type;
            HashMap<String,String> assignedStringFields = new HashMap<>();
            HashMap<String,String> assignedIntFields = new HashMap<>();
            HashMap<String,String> assignedByteFields = new HashMap<>();
            HashMap<String,String> assignedIntArrayFields = new HashMap<>();
            HashMap<String,String> assignedByteArrayFields = new HashMap<>();
        }
        public boolean connect(String in, String out){
            if ((inputType!=null)&&(outputType!=null)){
                System.out.println("first check");
                int inputTypeType = inputType.getFieldTypeFromName(in);
                int outputTypeType = outputType.getFieldTypeFromName(out);
                System.out.println(String.format("intype=%d outtype=%d",inputTypeType,outputTypeType));
                if (inputTypeType==0)return false;
                if (outputTypeType==0)return false;
                System.out.println("not null");
                if (inputTypeType!=outputTypeType) return false;
                switch (inputTypeType){
                    case NbtType.BYTE:{
                        assignedByteFields.put(in,out);
                        break;
                    }
                    case NbtType.INT:{
                        assignedIntFields.put(in,out);
                        break;
                    }
                    case NbtType.STRING:{
                        assignedStringFields.put(in,out);
                        break;
                    }
                    case NbtType.INT_ARRAY:{
                        assignedIntArrayFields.put(in,out);
                        break;
                    }
                    case NbtType.BYTE_ARRAY:{
                        assignedByteArrayFields.put(in,out);
                        break;
                    }
                }
                reverseConnections.put(out,in);
                return true;
            }
            return false;
        }

        public DataObjectType getInputType() {
            return inputType;
        }

        public DataObjectType getOutputType() {
            return outputType;
        }


        public StringRenderable getReverseConnection(String out) {
            if (reverseConnections.containsKey(out)) return new LiteralText(reverseConnections.get(out));
            return new TranslatableText("gui.cpu_arch_mod.unassigned_conversion_field");
        }
    }
}
