package net.bookpage.mixin;

import net.bookpage.mixin.ScreenAccessor;
import net.bookpage.widget.PageNumberEditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookViewScreen.class)
public abstract class BookViewScreenMixin {
    @Shadow
    private int currentPage;

    @Shadow
    private int backgroundLeft() {
        return 0;
    }

    @Shadow
    private int backgroundTop() {
        return 0;
    }

    @Shadow
    public abstract boolean setPage(int page);

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

    @Inject(method = "setPage", at = @At("RETURN"))
    private void bookpage$onSetPage(int page, CallbackInfoReturnable<Boolean> cir) {
        if (this.bookpage$pageInput != null && Boolean.TRUE.equals(cir.getReturnValue())) {
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
            this.setPage(target);
            this.bookpage$pageInput.setValue(String.valueOf(this.currentPage + 1));
        } catch (NumberFormatException ignored) {
        }
    }
}
