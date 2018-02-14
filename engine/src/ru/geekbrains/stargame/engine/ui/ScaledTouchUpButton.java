package ru.geekbrains.stargame.engine.ui;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Sprite;

public class ScaledTouchUpButton extends Sprite {

    private int pointer;
    private boolean pressed;
    private float pressScale;
    private final ActionListener actionListener;


    public ScaledTouchUpButton(TextureRegion region,
                               float pressScale,
                               ActionListener actionListener) {
        super(region);
        this.actionListener = actionListener;
        this.pressScale = pressScale;
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if (pressed || !isMe(touch)) {
            return;
        }
        this.pointer = pointer;
        scale = pressScale;
        pressed = true;
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if (this.pointer != pointer || !pressed) {
            return;
        }
        if (isMe(touch)) {
            actionListener.actionPerformed(this);
        }
        pressed = false;
        scale = 1.0f;
    }
}
