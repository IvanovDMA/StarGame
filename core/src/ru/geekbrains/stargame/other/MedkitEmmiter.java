package ru.geekbrains.stargame.other;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;
import ru.geekbrains.stargame.engine.utils.Regions;

public class MedkitEmmiter {

    private static final float MEDKIT_SMALL_HEIGHT = 0.1f;
    private static final float MEDKIT_BIG_HEIGHT = 0.1f;

    private static final int MEDKIT_SMALL_HP = 10;
    private static final int MEDKIT_BIG_HP = 50;

    private final Vector2 medkitSmallV = new Vector2(0f, -0.15f);
    private final Vector2 medkitBigV = new Vector2(0f, -0.15f);

    private final TextureRegion[] medkitSmallRegion;
    private final TextureRegion[] medkitBigRegion;

    private float generateTimer;
    private float generateInterval = 20.0f;
    private final MedkitPool medkitPool;
    private Rect worldBounds;

    public MedkitEmmiter(MedkitPool medkitPool, Rect worldBounds, TextureAtlas atlas) {
        this.medkitPool = medkitPool;
        this.worldBounds = worldBounds;
        medkitSmallRegion = Regions.split(atlas.findRegion("image1"), 2, 6, 12);
        medkitBigRegion = Regions.split(atlas.findRegion("image2"), 2, 6, 12);
    }

    public void generateMedkit(float delta) {
        generateTimer += delta;
        if (generateInterval <= generateTimer) {
            generateTimer = 0f;
            Medkit medkit = medkitPool.obtain();
            float type = (float) Math.random();
            if (type < 0.85f) {
                medkit.set(
                        medkitSmallRegion,
                        medkitSmallV,
                        MEDKIT_SMALL_HEIGHT,
                        MEDKIT_SMALL_HP
                );
            } else {
                medkit.set(
                        medkitBigRegion,
                        medkitBigV,
                        MEDKIT_BIG_HEIGHT,
                        MEDKIT_BIG_HP
                );
            }
            medkit.pos.x = Rnd.nextFloat(worldBounds.getLeft() + medkit.getHalfWidth(), worldBounds.getRight() - medkit.getHalfWidth());
            medkit.setBottom(worldBounds.getTop());
        }
    }
}
