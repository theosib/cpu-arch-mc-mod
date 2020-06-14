package eigencraft.cpuArchMod.gui.widgets;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class WBigTextWidget extends WWidget {
    private static final int BG_COLOR = new Color.RGB(255,20,20,20).toRgb();
    private static final int TEXT_COLOR = new Color.RGB(255,255,255,255).toRgb();
    private final static int SIDE_PADDING = 5;
    private static final int BORDER_WIDTH = 3;
    private final static int CURSOR_WIDTH = 2;
    private final static int CURSOR_SPACE_SIDE = 2;
    private final static int CURSOR_GAP_SIZE = CURSOR_SPACE_SIDE+CURSOR_WIDTH+CURSOR_SPACE_SIDE;

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

    @Environment(EnvType.CLIENT)
    @Override
    public void paintBackground(int x, int y, int mouseX, int mouseY) {
        if (this.isFocused()){
            ScreenDrawing.coloredRect(x, y, width, height, TEXT_COLOR);
            ScreenDrawing.coloredRect(x+ BORDER_WIDTH, y+ BORDER_WIDTH, width-2* BORDER_WIDTH, height-2* BORDER_WIDTH, BG_COLOR);
        } else {
            ScreenDrawing.coloredRect(x, y, width, height, BG_COLOR);
        }

        int yLevel = y+5+ BORDER_WIDTH -MinecraftClient.getInstance().textRenderer.fontHeight;

        List<String> untilCursor = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(text.substring(0, index),getWidth()-2*BORDER_WIDTH-2*SIDE_PADDING);
        int toCursorLineWidth = ((untilCursor.size()>0)?MinecraftClient.getInstance().textRenderer.getStringWidth(untilCursor.get(untilCursor.size()-1)):0);
        int widthFromCursorToLineEnd = getWidth()-(2* BORDER_WIDTH)-toCursorLineWidth-CURSOR_GAP_SIZE-(2*SIDE_PADDING);
        int charCountAfterCursor = MinecraftClient.getInstance().textRenderer.getCharacterCountForWidth(text.substring(index),widthFromCursorToLineEnd);
        String afterCursorString = text.substring(index,index+charCountAfterCursor);
        List<String> nextLineFromCursor = MinecraftClient.getInstance().textRenderer.wrapStringToWidthAsList(text.substring(index+charCountAfterCursor),getWidth()-2* BORDER_WIDTH-2*SIDE_PADDING);

        for(String line:untilCursor){
            yLevel += MinecraftClient.getInstance().textRenderer.fontHeight;
            ScreenDrawing.drawString(line,x+BORDER_WIDTH+SIDE_PADDING,yLevel, TEXT_COLOR);
        }
        ScreenDrawing.coloredRect(x+BORDER_WIDTH+toCursorLineWidth+CURSOR_SPACE_SIDE+SIDE_PADDING,yLevel,CURSOR_WIDTH,MinecraftClient.getInstance().textRenderer.fontHeight-2,TEXT_COLOR);

        ScreenDrawing.drawString(afterCursorString,x+BORDER_WIDTH +toCursorLineWidth+ CURSOR_GAP_SIZE+SIDE_PADDING,yLevel, TEXT_COLOR);

        for(String line:nextLineFromCursor){
            yLevel += MinecraftClient.getInstance().textRenderer.fontHeight;
            ScreenDrawing.drawString(line,x+ BORDER_WIDTH+SIDE_PADDING,yLevel, TEXT_COLOR);
        }

    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        System.out.println(key);
        System.out.print(text.length());
        System.out.print(" ");
        System.out.print(index);
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
