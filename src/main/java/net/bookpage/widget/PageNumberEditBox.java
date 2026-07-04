package net.bookpage.widget;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class PageNumberEditBox extends EditBox {
    private Runnable onEnterAction = () -> {};

    public PageNumberEditBox(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
        this.setMaxLength(10);
        this.setResponder(this::filterInput);
    }

    public void setOnEnter(Runnable action) {
        this.onEnterAction = action;
    }

    private void filterInput(String value) {
        String filtered = value.replaceAll("[^0-9]", "");
        if (!filtered.equals(value)) {
            this.setValue(filtered);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == InputConstants.KEY_RETURN || event.key() == InputConstants.KEY_NUMPADENTER) {
            this.onEnterAction.run();
            return true;
        }
        return super.keyPressed(event);
    }
}
