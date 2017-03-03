package org.epstudios.epcoding;

/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 2/26/17.
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

public class Modifier implements Comparable<Modifier> {
    public String getNumber() {
        return number;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    private final String number;
    private final String shortDescription;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public Modifier(String number, String shortDescription) {
        this.number = number;
        this.shortDescription = shortDescription;
        isSelected = false;
    }

    public String modifierDescription() {
        return number + " " + shortDescription;
    }

    public int compareTo(Modifier m) {
        return number.compareTo(m.number);
    }
}
