package ru.geekbrains.stargame.other;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.pool.SpritesPool;
import ru.geekbrains.stargame.ship.MainShip;

public class MedkitPool extends SpritesPool<Medkit> {

    private final Rect worldBounds;
    private Sound soundMedkit;

    public MedkitPool(Rect worldBounds, Sound soundMedkit) {
        this.worldBounds = worldBounds;
        this.soundMedkit = soundMedkit;
    }

    @Override
    protected Medkit newObject() {
        return new Medkit(worldBounds, soundMedkit);
    }
}
