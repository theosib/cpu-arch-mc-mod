package eigencraft.cpuArchMod.gui.widgets;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;

import java.util.List;

public class WBigTextWidget extends WWidget {
    private static final int BG_COLOR = new Color.RGB(255,20,20,20).toRgb();
    private static final int TEXT_COLOR = new Color.RGB(255,255,255,255).toRgb();
    private final static int SIDE_PADDING = 5;
    private static final int BORDER_WIDTH = 3;
    

    StringBuilder text = new StringBuilder();

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


    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (this.isFocused()){
            ScreenDrawing.coloredRect(x, y, width, height, TEXT_COLOR);
            ScreenDrawing.coloredRect(x+ BORDER_WIDTH, y+ BORDER_WIDTH, width-2* BORDER_WIDTH, height-2* BORDER_WIDTH, BG_COLOR);
        } else {
            ScreenDrawing.coloredRect(x, y, width, height, BG_COLOR);
        }

        int yLevel = y+5+ BORDER_WIDTH;
        final int usableSpace = getWidth()-2*BORDER_WIDTH-2*SIDE_PADDING;

        List<StringRenderable> lines =  MinecraftClient.getInstance().textRenderer.wrapLines(new LiteralText(text.substring(0, index) + "|" + text.substring(index)),usableSpace);
        for (StringRenderable line:lines){
            ScreenDrawing.drawString(matrices, line.getString(),x+BORDER_WIDTH+SIDE_PADDING,yLevel,TEXT_COLOR);
            yLevel += MinecraftClient.getInstance().textRenderer.fontHeight;
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (key== 123){
            index = (--index>=0)?index:0;
        } else if (key== 124){
            index = (++index<text.length())?index:text.length();
        } else if (key==51){
            if (text.length()>0){
                text.deleteCharAt((--index >= 0) ? index : 0);
                if (index < 0){
                    index = 0;
                }
            }
        }
    }

    @Override
    public void onClick(int x, int y, int button) {
        requestFocus();
    }

    @Override
    public void onFocusGained() {
        super.onFocusGained();
    }

    @Override
    public void onCharTyped(char ch) {
        text.insert(index,ch);
        index++;
    }

    public void setText(String newText) {
        text = new StringBuilder(newText);
    }

    public String getText() {
        return text.toString();
    }
}
