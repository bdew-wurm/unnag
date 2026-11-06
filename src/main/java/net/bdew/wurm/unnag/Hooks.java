package net.bdew.wurm.unnag;

import com.wurmonline.client.renderer.gui.BmlWindowComponent;
import com.wurmonline.client.renderer.gui.BmlWindowListener;
import com.wurmonline.client.renderer.gui.WurmComponent;

import java.util.HashSet;

@SuppressWarnings("unused")
public class Hooks {
    private static final HashSet<WurmComponent> blockedComponents = new HashSet<>();

    public static boolean blockUiComponent(WurmComponent c) {
        if (blockedComponents.contains(c)) return true;
        String name = c.getClass().getName();
        String lower = name.toLowerCase();
        for (String keyword : UnnagMod.bannedUiComponentClassKeywords) {
            if (lower.contains(keyword)) {
                UnnagMod.logInfo("Blocking UI component: " + name);
                blockedComponents.add(c);
                return true;
            }
        }

        if (c instanceof BmlWindowComponent) {
            BmlWindowComponent bml = (BmlWindowComponent) c;
            String bmlTitle = Reflection.getWindowTitle(bml);
            if (bmlTitle != null) {
                String lowerBml = bmlTitle.toLowerCase();
                for (String keyword : UnnagMod.bannedBmlTitleKeywords) {
                    if (lowerBml.contains(keyword)) {
                        UnnagMod.logInfo("Blocking BML window: " + bmlTitle);
                        blockedComponents.add(c);

                        try {
                            BmlWindowListener owner = Reflection.getWindowOwner(bml);
                            if (owner != null) {
                                owner.cancel(bml);
                            }
                        } catch (Exception e) {
                            UnnagMod.logException("Error cancelling window", e);
                        }

                        return true;
                    }
                }
            }
        }
        return false;
    }
}
