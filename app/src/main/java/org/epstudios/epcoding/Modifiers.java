package org.epstudios.epcoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 3/2/17.
 * <p>
 * This file is part of epcoding.
 * <p>
 * epcoding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epcoding is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epcoding.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Modifiers {

    private final static Map<String, Modifier> allModifiers = createMap();

    private static Map<String, Modifier> createMap() {
        Map<String, Modifier> map = new HashMap<>();

        addModifier(map, "26", "Professional component");
        addModifier(map, "51", "Multiple procedures (NB: many EP codes are modifier 51 exempt");
        addModifier(map, "52", "Reduced services");
        addModifier(map, "53", "Discontinued procedure due to patient risk");
        addModifier(map, "59", "Distinct procedural service");
        addModifier(map, "76", "Repeat procedure by same MD");
        addModifier(map, "78", "Return to OR for related procedure during post-op period");
        addModifier(map, "Q0", "Investigative clinical service (e.g. ICD for primary prevention");

        return Collections.unmodifiableMap(map);
    }

    private static void addModifier(Map<String, Modifier> map, String modifierNumber,
                                String description) {
        map.put(modifierNumber, new Modifier(modifierNumber, description));

    }

    public static List<Modifier> allModifiersSorted() {
        List<Modifier> modifiers = new ArrayList<>(allModifiers.values());
        Collections.sort(modifiers);
        return modifiers;
    }

    public static Modifier getModifierForNumber(String number) {
        return Modifiers.allModifiers.get(number);
    }

}
