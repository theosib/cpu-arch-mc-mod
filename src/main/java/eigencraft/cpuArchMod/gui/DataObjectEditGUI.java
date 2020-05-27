package eigencraft.cpuArchMod.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import eigencraft.cpuArchMod.CpuArchMod;
import eigencraft.cpuArchMod.backend.DataObject;
import eigencraft.cpuArchMod.util.GsonDataObjectDeserializer;
import eigencraft.cpuArchMod.util.GsonDataObjectSerializer;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.PacketByteBuf;

public class DataObjectEditGUI extends LightweightGuiDescription {
    private static Gson gson;

    public static DataObjectEditGUI fromExisting(DataObject dataObject){
        return new DataObjectEditGUI(dataObject);
    }
    public static DataObjectEditGUI fromNew(){
        return new DataObjectEditGUI(null);
    }

    private DataObjectEditGUI(DataObject dataObject){
        if (gson==null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DataObject.class,new GsonDataObjectSerializer());
            gsonBuilder.registerTypeAdapter(DataObject.class,new GsonDataObjectDeserializer());
            gson = gsonBuilder.create();
        }

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);

        WTextField textField = new WTextField();
        root.add(textField,0,1,12,1);
        textField.setMaxLength(1000);

        WLabel label = new WLabel(new LiteralText("new dataObject"), 0xFFFFFF);
        if (dataObject!=null){
            label.setText(new LiteralText(dataObject.getType()));
            textField.setText(gson.toJson(dataObject));
        }
        root.add(label, 0, 0, 4, 1);


        WButton saveButton = new WButton(new TranslatableText(CpuArchMod.MODID+".gui.button.save_and_close"));
        saveButton.setOnClick(new Runnable() {

            @Override
            public void run() {
                System.out.println(textField.getText());
                try {
                    DataObject editedObject = gson.fromJson(textField.getText(), DataObject.class);
                    if (editedObject==null) return;
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    System.out.println(editedObject.toString()+"null?");
                    passedData.writeCompoundTag(editedObject.getCompoundTag());
                    // Send packet to server to change the block for us
                    ClientSidePacketRegistry.INSTANCE.sendToServer(CpuArchMod.DATAOBJECT_GUI_SAVE_C2S_PACKET, passedData);
                } catch (JsonSyntaxException e){
                    e.printStackTrace();
                }
            }
        });
        root.add(saveButton,4,0,4,1);

        root.validate(this);
    }
}
