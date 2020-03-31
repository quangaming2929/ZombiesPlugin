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
    //disgusting dirty code. be warned, all ye who may enter here, for this will get broken with every minor release, no exceptions.
    public static void injectCustomAi(MythicMobs instance, Set<Class<?>> goals, Set<Class<?>> targeters) throws NoSuchFieldException, IllegalAccessException {
        VolatileCodeEnabled_v1_15_R1 enabled = (VolatileCodeEnabled_v1_15_R1)instance.getVolatileCodeHandler();
        VolatileAIHandler_v1_15_R1 target = (VolatileAIHandler_v1_15_R1)enabled.getAIHandler();

        //this is what happens when you don't have good API documentation but fortunately leave your code un-obfuscated :D
        Field aiGoals = VolatileAIHandler_v1_15_R1.class.getDeclaredField("AI_GOALS");
        aiGoals.setAccessible(true);
        Map<String, Class<? extends PathfinderAdapter>> AI_GOALS = (Map<String, Class<? extends PathfinderAdapter>>) aiGoals.get(target);

        Field aiTargets = VolatileAIHandler_v1_15_R1.class.getDeclaredField("AI_TARGETS");
        aiTargets.setAccessible(true);
        Map<String, Class<? extends PathfinderAdapter>> AI_TARGETS = (Map<String, Class<? extends PathfinderAdapter>>) aiTargets.get(target);

        Iterator goalsIterator = goals.iterator();

        int i;
        while(goalsIterator.hasNext()) {
            Class clazz = (Class)goalsIterator.next();

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
            } catch (Exception e) {
                MythicLogger.error("Uh oh. You done went and haxed me and now this happened. Offender: {0}", clazz.getCanonicalName());
            }
        }

        Iterator targetsIterator = targeters.iterator();

        int j;
        while(targetsIterator.hasNext()) {
            Class clazz = (Class)targetsIterator.next();

            try {
                String name = ((MythicAIGoal)clazz.getAnnotation(MythicAIGoal.class)).name();
                String[] aliases = ((MythicAIGoal)clazz.getAnnotation(MythicAIGoal.class)).aliases();
                if (PathfinderAdapter.class.isAssignableFrom(clazz)) {
                    AI_TARGETS.put(name.toUpperCase(), clazz);

                    for(j = 0; j < aliases.length; ++j) {
                        String alias = aliases[j];
                        AI_TARGETS.put(alias.toUpperCase(), clazz);
                    }
                }
            } catch (Exception e) {
                MythicLogger.error("Uh oh. You done went and haxed me and now this happened. Offender: {0}", clazz.getCanonicalName());
            }
        }
    }
}
