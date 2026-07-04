package net.bookpage.mixin;

import net.bookpage.mixin.ScreenAccessor;
import net.bookpage.widget.PageNumberEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin {
    @Shadow
    private int currentPage;

    @Shadow
    private List<String> pages;

    @Shadow
    private int backgroundLeft() {
        return 0;
    }

    @Shadow
    private int backgroundTop() {
        return 0;
    }

    @Shadow
    private void updatePageContent() {
    }

    @Shadow
    private void updateButtonVisibility() {
    }

    private PageNumberEditBox bookpage$pageInput;
    private Button bookpage$jumpButton;

    @Inject(method = "init", at = @At("TAIL"))
    private void bookpage$onInit(CallbackInfo ci) {
        int inputX = this.backgroundLeft() + 33;
        int inputY = this.backgroundTop() + 14;

        this.bookpage$pageInput = new PageNumberEditBox(
                Minecraft.getInstance().font, inputX, inputY, 26, 12, Component.empty()
        );
        this.bookpage$pageInput.setValue(String.valueOf(this.currentPage + 1));
        this.bookpage$pageInput.setOnEnter(this::bookpage$jumpToPage);

        this.bookpage$jumpButton = Button.builder(Component.literal("\u2192"), button -> this.bookpage$jumpToPage())
                .bounds(inputX + 26, inputY, 12, 12)
                .build();

        // 通过 ScreenAccessor（@Invoker）调用 Screen 的 protected addRenderableWidget
        ScreenAccessor accessor = (ScreenAccessor) (Object) this;
        accessor.invokeAddRenderableWidget(this.bookpage$pageInput);
        accessor.invokeAddRenderableWidget(this.bookpage$jumpButton);
    }

    @Inject(method = "updateButtonVisibility", at = @At("TAIL"))
    private void bookpage$onPageChanged(CallbackInfo ci) {
        if (this.bookpage$pageInput != null) {
            this.bookpage$pageInput.setValue(String.valueOf(this.currentPage + 1));
        }
    }

    private void bookpage$jumpToPage() {
        String value = this.bookpage$pageInput.getValue();
        if (value.isEmpty()) {
            return;
        }

        try {
            int target = Integer.parseInt(value) - 1;
            int maxPage = Math.max(0, this.pages.size() - 1);
            target = Math.max(0, Math.min(target, maxPage));

            this.currentPage = target;
            this.updatePageContent();
            this.updateButtonVisibility();

            this.bookpage$pageInput.setValue(String.valueOf(this.currentPage + 1));
        } catch (NumberFormatException ignored) {
        }
    }
}
