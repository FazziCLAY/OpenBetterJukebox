package ru.fazziclay.openbetterjukebox;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JukeboxScreen extends Screen {
    private static final Text SCREEN_TITLE = new TranslatableText("block.minecraft.jukebox");
    private static final Text BUTTON_STOP_TEXT = new LiteralText("Stop");
    private static final Text BUTTON_PLAY_TEXT = new LiteralText("Play");
    private static final Text BUTTON_PAUSE_TEXT = new LiteralText("Pause");
    private static final Text BUTTON_RESUME_TEXT = new LiteralText("Resume");
    private static final Text SLIDER_VOLUME_TEXT = new LiteralText("Volume(Distance)");
    private static final Text SLIDER_PITCH_TEXT = new LiteralText("Pitch");
    private static final int BUTTON_WIDTH = 80;  // ДЛИННА
    private static final int BUTTON_HEIGHT = 20; // ВЫСОТА
    private static final int BUTTON_MARGIN = 5;
    private static final int VOLUME_SLIDER_WIDTH = 180; // ДЛИННА
    private static final int VOLUME_SLIDER_HEIGHT = 20; // ВЫСОТА
    private static final Logger LOGGER = LogManager.getLogger("openbetterjukebox-jukebox_screen");

    public double volumeValue = 0.6;
    public double pitchValue = 0.5;

    public ButtonWidget stopButton;
    public ButtonWidget playButton;
    public ButtonWidget pauseButton;
    public ButtonWidget resumeButton;
    public SliderWidget pitchSlider;
    public SliderWidget volumeSlider;


    protected JukeboxScreen() {
        super(SCREEN_TITLE);
    }

    @Override
    protected void init() {
        this.stopButton = new ButtonWidget((this.width / 2) - BUTTON_WIDTH - BUTTON_MARGIN -1, (this.height / 5) - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_STOP_TEXT, button -> {
            assert client != null;
            client.getSoundManager().stopAll();
        });

        this.playButton = new ButtonWidget((this.width / 2) + BUTTON_MARGIN, (this.height / 5) - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_PLAY_TEXT, button -> {
            assert client != null;
            assert client.player != null;
            Item item = client.player.getInventory().getMainHandStack().getItem();

            if (item instanceof MusicDiscItem musicDisc) {
                client.player.playSound(musicDisc.getSound(), (float) volumeValue*10, (float)pitchValue+0.5f);
            }
        });

        this.pauseButton = new ButtonWidget((this.width / 2) - BUTTON_WIDTH - BUTTON_MARGIN -1, (this.height / 5) - BUTTON_HEIGHT + BUTTON_HEIGHT + BUTTON_MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_PAUSE_TEXT, button -> {
            assert client != null;
            client.getSoundManager().pauseAll();
        });

        this.resumeButton = new ButtonWidget((this.width / 2) + BUTTON_MARGIN, (this.height / 5) - BUTTON_HEIGHT + BUTTON_HEIGHT + BUTTON_MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_RESUME_TEXT, button -> {
            assert client != null;
            client.getSoundManager().resumeAll();
        });

        this.pitchSlider = new SliderWidget((this.width / 2) - (VOLUME_SLIDER_WIDTH/2), (int) (this.height / 1.3f) - VOLUME_SLIDER_HEIGHT - BUTTON_MARGIN, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT, SLIDER_PITCH_TEXT, pitchValue) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                pitchValue = this.value; LOGGER.info("volumeValue="+volumeValue + "; pitchValue="+pitchValue);
            }
        };

        this.volumeSlider = new SliderWidget((this.width / 2) - (VOLUME_SLIDER_WIDTH/2), (int) (this.height / 1.3f), VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT, SLIDER_VOLUME_TEXT,volumeValue) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                volumeValue = this.value; LOGGER.info("volumeValue="+volumeValue + "; pitchValue="+pitchValue);
            }
        };

        addDrawableChild(stopButton);
        addDrawableChild(playButton);
        addDrawableChild(pauseButton);
        addDrawableChild(resumeButton);
        addDrawableChild(pitchSlider);
        addDrawableChild(volumeSlider);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 10, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
