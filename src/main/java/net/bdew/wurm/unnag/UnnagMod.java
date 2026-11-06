package net.bdew.wurm.unnag;

import javassist.ClassPool;
import javassist.CtClass;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnnagMod implements WurmClientMod, Initable, PreInitable, Configurable {
    private static final Logger logger = Logger.getLogger("UnnagMod");

    public static List<String> bannedUiComponentClassKeywords;
    public static List<String> bannedBmlTitleKeywords;

    public static void logInfo(String msg) {
        if (logger != null)
            logger.log(Level.INFO, msg);
    }

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    @Override
    public void configure(Properties properties) {
        bannedUiComponentClassKeywords = parseKeywords(properties, "bannedUiComponentClassKeywords");
        bannedBmlTitleKeywords = parseKeywords(properties, "bannedBmlTitleKeywords");
    }

    private static List<String> parseKeywords(Properties properties, String key) {
        List<String> result = new ArrayList<>();
        for (String s : properties.getProperty(key, "").split(",")) {
            s = s.trim().toLowerCase();
            if (!s.isEmpty())
                result.add(s);
        }
        logInfo(key + ": " + String.join(", ", result));
        return result;
    }

    @Override
    public void init() {
        logger.fine("Initializing");
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();

            CtClass ctHeadsUpDisplay = classPool.getCtClass("com.wurmonline.client.renderer.gui.HeadsUpDisplay");
            ctHeadsUpDisplay.getMethod("showComponent", "(Lcom/wurmonline/client/renderer/gui/WurmComponent;)V")
                    .insertBefore("if (net.bdew.wurm.unnag.Hooks.blockUiComponent($1)) return;");

            ctHeadsUpDisplay.getMethod("setActiveWindow", "(Lcom/wurmonline/client/renderer/gui/WurmComponent;)V")
                    .insertBefore("if (net.bdew.wurm.unnag.Hooks.blockUiComponent($1)) return;");


            logger.fine("Loaded");
        } catch (Throwable e) {
            logException("Error loading mod", e);
        }
    }

    @Override
    public void preInit() {

    }

}
