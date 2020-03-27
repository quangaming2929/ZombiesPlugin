package io.github.zap.zombiesplugin.pathfind.reflect;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.logging.MythicLogger;
import io.lumine.xikage.mythicmobs.mobs.ai.PathfinderAdapter;
import io.lumine.xikage.mythicmobs.util.annotations.MythicAIGoal;
import io.lumine.xikage.mythicmobs.util.reflections.VersionCompliantReflections;
import io.lumine.xikage.mythicmobs.volatilecode.VolatileCodeEnabled_v1_15_R1;
import io.lumine.xikage.mythicmobs.volatilecode.v1_15_R1.VolatileAIHandler_v1_15_R1;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Hack {
    //disgusting dirty code
    public static void injectCustomGoals(MythicMobs instance, Set<Class<?>> classes) throws NoSuchFieldException, IllegalAccessException {
        VolatileCodeEnabled_v1_15_R1 enabled = (VolatileCodeEnabled_v1_15_R1)instance.getVolatileCodeHandler();
        VolatileAIHandler_v1_15_R1 target = (VolatileAIHandler_v1_15_R1)enabled.getAIHandler();

        //this is what happens when you don't have good API documentation.
        Field field = VolatileAIHandler_v1_15_R1.class.getDeclaredField("AI_GOALS");
        field.setAccessible(true);
        Map<String, Class<? extends PathfinderAdapter>> AI_GOALS = (Map<String, Class<? extends PathfinderAdapter>>) field.get(target);

        Iterator var3 = classes.iterator();

        int i;
        while(var3.hasNext()) {
            Class clazz = (Class)var3.next();

            try {
                String name = ((MythicAIGoal)clazz.getAnnotation(MythicAIGoal.class)).name();
                String[] aliases = ((MythicAIGoal)clazz.getAnnotation(MythicAIGoal.class)).aliases();
                if (PathfinderAdapter.class.isAssignableFrom(clazz)) {
                    AI_GOALS.put(name.toUpperCase(), clazz);

                    for(i = 0; i < aliases.length; ++i) {
                        String alias = aliases[i];
                        AI_GOALS.put(alias.toUpperCase(), clazz);
                    }
                }
            } catch (Exception var17) {
                MythicLogger.error("Uh oh. You done went and haxed me and now this happened. Offender: {0}", new Object[]{clazz.getCanonicalName()});
            }
        }
    }
}
