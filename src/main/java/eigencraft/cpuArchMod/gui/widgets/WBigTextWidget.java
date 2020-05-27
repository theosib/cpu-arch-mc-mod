package eigencraft.cpuArchMod.gui.widgets;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class WBigTextWidget extends WWidget {
    public static final int bg_color = new Color.RGB(255,20,20,20).toRgb();
    public static final int text_color = new Color.RGB(255,255,255,255).toRgb();
    public static final int border_width = 3;
    String text = "lösfgjhsöldg jsldkghsldkfghslödhg fslödhgfs lködjfsldöjkfslködjfslö djkgsld ökghsldökhjfgsd lökhgf sdlköfhgsö dlhfg sldköhg fsdlöjkhgf";

    int index = 0;

    public void setSize(int x, int y) {
        super.setSize(x, y);
    }

    @Override
    public boolean canResize() {
        return true; // set to false if you want a static size
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paintBackground(int x, int y, int mouseX, int mouseY) {
        if (this.isFocused()){
            ScreenDrawing.coloredRect(x, y, width, height, text_color);
            ScreenDrawing.coloredRect(x+border_width, y+border_width, width-2*border_width, height-2*border_width, bg_color);
        } else {
            ScreenDrawing.coloredRect(x, y, width, height, bg_color);
        }

        int yLevel = y+5+border_width;
        for (String line:MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(text.substring(0, index) + "|" + text.substring(index),getWidth()-10)) {
            ScreenDrawing.drawString(line,x+5+border_width,yLevel,text_color);
            yLevel += MinecraftClient.getInstance().textRenderer.fontHeight;
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        System.out.println(key);
        if (key== 123){
            index = (--index>=0)?index:0;
        } else if (key== 124){
            index = (++index<text.length())?index:text.length();
        }
    }

    @Override
    public void onClick(int x, int y, int button) {
        requestFocus();
    }

    @Override
    public void onFocusGained() {
        super.onFocusGained();
        System.out.println("focus");
    }

    @Override
    public void onCharTyped(char ch) {
        text = text.substring(0, index) + ch + text.substring(index);
        index++;
    }
}
