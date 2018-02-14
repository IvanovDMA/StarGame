package ru.geekbrains.stargame.engine.math;


import java.util.Random;

/**
 * Р“РµРЅРµСЂР°С‚РѕСЂ СЃР»СѓС‡Р°Р№РЅС‹С… С‡РёСЃРµР»
 */
public class Rnd {
    private static final Random random = new Random();

    /**
     * РЎРіРµРЅРµСЂРёСЂРѕРІР°С‚СЊ СЃР»СѓС‡Р°Р№РЅРѕРµ С‡РёСЃР»Рѕ
     * @param min РјРёРЅРёРјР°Р»СЊРЅРѕРµ Р·РЅР°С‡РµРЅРёРµ СЃР»СѓС‡Р°Р№РЅРѕРіРѕ С‡РёСЃР»Р°
     * @param max РјР°РєСЃРёРјР°Р»СЊРЅРѕРµ Р·РЅР°С‡РµРЅРёРµ СЃР»СѓС‡Р°Р№РЅРѕРіРѕ С‡РёСЃР»Р°
     * @return СЂРµР·СѓР»СЊС‚Р°С‚
     */
    public static float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }
}
