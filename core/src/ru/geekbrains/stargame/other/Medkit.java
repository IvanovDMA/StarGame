package ru.geekbrains.stargame.other;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.ship.EnemyShip;
import ru.geekbrains.stargame.ship.MainShip;

public class Medkit extends Sprite {

    protected final Vector2 medKitV = new Vector2(); //скорость аптечки
    protected int medkitHp; //жизни в аптечке
    protected Sound soundMedkit; //звук приема аптечки
    protected Rect worldBounds; //границы мира
    private float animateInterval = 0.095f; //время между кадрами
    private float animateTimer;


    public Medkit(Rect worldBounds, Sound soundMedkit) {
        this.worldBounds = worldBounds;
        this.soundMedkit = soundMedkit;
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            float height,
            int hp
    ) {
        this.regions = regions;
        medKitV.set(v0);
        setHeightProportion(height);
        this.medkitHp = hp;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(medKitV, delta);
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                frame = 0;
            }
        }
    }

    public int getMedkitHp() {
        return medkitHp;
    }
}
