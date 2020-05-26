package eigencraft.cpuArchMod.gui;

import eigencraft.cpuArchMod.backend.DataObject;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class DataObjectEditGUI extends LightweightGuiDescription {
    public static DataObjectEditGUI fromExisting(DataObject dataObject){
        return new DataObjectEditGUI(dataObject);
    }
    public static DataObjectEditGUI fromNew(){
        return new DataObjectEditGUI(null);
    }

    private DataObjectEditGUI(DataObject dataObject){
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);

        WLabel label = new WLabel(new LiteralText("new dataObject!"), 0xFFFFFF);
        if (dataObject!=null){
            label.setText(new LiteralText(dataObject.getType()));
        }
        root.add(label, 0, 1, 3, 1);

        root.validate(this);
    }
}
