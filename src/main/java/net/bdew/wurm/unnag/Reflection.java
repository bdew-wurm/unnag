package net.bdew.wurm.unnag;

import com.wurmonline.client.renderer.gui.BmlWindowComponent;
import com.wurmonline.client.renderer.gui.BmlWindowListener;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;

import java.lang.reflect.Field;

public class Reflection {
    private static Field fBmlWindowComponentTitle;
    private static Field fBmlWindowComponentOwner;

    public static void setup() {
        try {
            fBmlWindowComponentTitle = ReflectionUtil.getField(BmlWindowComponent.class, "title");
            fBmlWindowComponentOwner = ReflectionUtil.getField(BmlWindowComponent.class, "owner");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWindowTitle(BmlWindowComponent bml) {
        if (fBmlWindowComponentTitle == null) setup();
        try {
            return ReflectionUtil.getPrivateField(bml, fBmlWindowComponentTitle);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static BmlWindowListener getWindowOwner(BmlWindowComponent bml) {
        if (fBmlWindowComponentOwner == null) setup();
        try {
            return ReflectionUtil.getPrivateField(bml, fBmlWindowComponentOwner);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
