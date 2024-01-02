package io.github.maliciousfiles.jec.util;

import org.apache.logging.log4j.util.StackLocatorUtil;

import java.util.*;

public class UUIDGenerator {
    private static final Map<Class<?>, List<UUID>> uuids = new HashMap<>();

    // Gets or creates a new UUID associated with the calling class and the given id
    public static UUID getUUID(int id) {
        // Straight from log4j's LogManager#getLogger
        Class<?> clazz = StackLocatorUtil.getCallerClass(2);
        if (!uuids.containsKey(clazz)) {
            uuids.put(clazz, new ArrayList<>());
        }

        List<UUID> classUUIDS = uuids.get(clazz);
        if (id >= classUUIDS.size()) {
            for (int i = classUUIDS.size(); i <= id; i++) {
                classUUIDS.add(UUID.randomUUID());
            }
        }

        return classUUIDS.get(id);
    }
}
