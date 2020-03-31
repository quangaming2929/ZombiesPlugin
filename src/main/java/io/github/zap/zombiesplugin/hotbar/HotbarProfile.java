package io.github.zap.zombiesplugin.hotbar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HotbarProfile {
    public List<HotbarObject> objects = new ArrayList<>();
    public Hashtable<String, ObjectGroup> groups = new Hashtable<>();
}
