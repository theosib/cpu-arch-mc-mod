package eigencraft.cpuArchMod.gui;

import eigencraft.cpuArchMod.dataObject.DataObjectType;
import eigencraft.cpuArchMod.simulation.nodes.ConverterNode;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BiConsumer;

public class DataObjectConverterGUI extends LightweightGuiDescription {
    private LinkedList<WWidget> widgets = new LinkedList<>();
    ConverterNode.ConverterNodeConfiguration configuration;

    WGridPanel root;
    WListPanel<String, InputTypeEntry> typeSelectorList;
    WListPanel<String,AdapterListEntry> fieldAssignmentList;
    WButton changeInputTypeButton;
    WButton backToMainMenuButton;
    WLabel inputStringLabel;
    WLabel outputStringLabel;
    WDynamicLabel converterWhatToWhatLabel;

    public DataObjectConverterGUI(ConverterNode.ConverterNodeConfiguration config) {
        configuration = config;
        root = new WGridPanel();
        setRootPanel(root);
        root.setSize(180, 180);

        BiConsumer<String, InputTypeEntry> typeSelectorConfigurator = (String s, InputTypeEntry destination) -> {
            destination.typeName.setText(new LiteralText(s));
            destination.typeNameString = s;
        };
        typeSelectorList = new WListPanel<>(DataObjectType.getDataObjectTypeNames(), InputTypeEntry::new, typeSelectorConfigurator);
        typeSelectorList.setListItemHeight(1*18);

        inputStringLabel = new WLabel(new TranslatableText("gui.cpu_arch_mod.input"));
        outputStringLabel = new WLabel(new TranslatableText("gui.cpu_arch_mod.output"));
        converterWhatToWhatLabel = new WDynamicLabel(() -> String.format("%s -> %s",configuration.getInputType().getName(),configuration.getOutputType().getName()));
        converterWhatToWhatLabel.setAlignment(HorizontalAlignment.CENTER);

        changeInputTypeButton = new WButton(new TranslatableText("gui.cpu_arch_mod.change_data_object_type"));
        changeInputTypeButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                buildTypeMenu();
            }
        });

        backToMainMenuButton = new WButton(new TranslatableText("gui.cpu_arch_mod.back_to_main_menu"));
        backToMainMenuButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                buildMainMenu();
            }
        });

        rebuildAssignmentList();

        buildMainMenu();
        root.validate(this);
    }

    private void buildMainMenu(){
        removeAll();
        root.add(changeInputTypeButton, 0, 0, 4, 1);
        widgets.add(changeInputTypeButton);
        root.add(converterWhatToWhatLabel,5,0,4,1);
        widgets.add(converterWhatToWhatLabel);
        root.add(fieldAssignmentList,0,1,10,8);
        widgets.add(fieldAssignmentList);
    }
    private void buildTypeMenu(){
        removeAll();
        root.add(backToMainMenuButton,0,0,2,1);
        widgets.add(backToMainMenuButton);
        root.add(typeSelectorList,0,1,10,8);
        widgets.add(typeSelectorList);
        root.add(inputStringLabel,3,0,2,1);
        widgets.add(inputStringLabel);
        root.add(outputStringLabel,6,0,2,1);
        widgets.add(outputStringLabel);
    }
    private void buildAssignMenu(){
        removeAll();
    }
    private void removeAll(){
        while (!widgets.isEmpty()){
            getRootPanel().remove(widgets.remove());
        }
    }

    //When the io types are changed
    private void rebuildAssignmentList(){
        System.out.println("changed");
        BiConsumer<String, AdapterListEntry> typeSelectorConfigurator = (String s, AdapterListEntry destination) -> {
            destination.outName.setText(new LiteralText(s));
            //Not done, but later use this
            //destination.changeButton.setLabel(new LiteralText(configuration.getConnection(s)));
            destination.changeButton.setLabel(new TranslatableText("gui.cpu_arch_mod.unassigned_conversion_field"));
        };
        fieldAssignmentList = new WListPanel<>(new ArrayList<>(configuration.getOutputType().getRequiredTags().keySet()), AdapterListEntry::new, typeSelectorConfigurator);
        fieldAssignmentList.setListItemHeight(18);
    }

    //Assign screen
    private class DataObjectFieldEntry extends WPlainPanel{
        WLabel fieldName;
        WButton assign;
        public DataObjectFieldEntry(){
            fieldName = new WLabel("null");
            add(fieldName,0,2,4*18,18);
            assign = new WButton();
            add(assign,5*18,0,4*18,18);
            this.setSize(10*18,18);
        }
    }

    //Main Screen
    private class AdapterListEntry extends WPlainPanel{
        WLabel outName;
        WButton changeButton;
        public AdapterListEntry(){
            outName = new WLabel(new LiteralText("null"));
            add(outName,0,2,4*18,18);
            changeButton = new WButton(new TranslatableText("gui.cpu_arch_mod.unassigned_conversion_field"));
            add(changeButton,5*18,0,4*18,18);
            this.setSize(10*18,18);
        }
    }

    //Type screen
    private class InputTypeEntry extends WPlainPanel{
        WLabel typeName;
        WButton selectInput;
        WButton selectOutput;
        String typeNameString;
        public InputTypeEntry(){
            typeName = new WLabel("none");
            this.add(typeName,0, 2, 3*18, 18);
            selectInput = new WButton(new TranslatableText("gui.cpu_arch_mod.select"));
            this.add(selectInput,3*18-5,0,2*18,18);
            selectOutput = new WButton(new TranslatableText("gui.cpu_arch_mod.select"));
            this.add(selectOutput,6*18-6,0,2*18,18);

            selectInput.setOnClick(new Runnable() {
                @Override
                public void run() {
                    configuration.setInputType(DataObjectType.getDataObjectTypeFromNameOrNull(typeNameString));
                    rebuildAssignmentList();
                }
            });

            selectOutput.setOnClick(new Runnable() {
                @Override
                public void run() {
                    configuration.setOutputType(DataObjectType.getDataObjectTypeFromNameOrNull(typeNameString));
                    rebuildAssignmentList();
                }
            });

            this.setSize(10*18,18);
        }
    }
}
