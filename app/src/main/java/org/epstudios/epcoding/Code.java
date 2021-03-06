/*  
Copyright (C) 2013, 2014 EP Studios, Inc.
www.epstudiossoftware.com

This file is part of EP Coding.

    EP Coding is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    EP Coding is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with EP Coding.  If not, see <http://www.gnu.org/licenses/>.

    Note also:

    CPT copyright 2012 American Medical Association. All rights
    reserved. CPT is a registered trademark of the American Medical
    Association.

    A limited number of CPT codes are used in this program under the Fair Use
    doctrine of US Copyright Law.  See README.md for more information.
 */

package org.epstudios.epcoding;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressLint("DefaultLocale")
public class Code {
    private final String code;
    private final String shortDescription;
    private final boolean isAddOn;
    private boolean plusShown = true;
    private boolean descriptionShortened = false;
    private boolean descriptionShown = true;

    public void setHideMultiplier(boolean hideMultiplier) {
        this.hideMultiplier = hideMultiplier;
    }

    private boolean hideMultiplier = false;

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    private int multiplier = 0;

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    private final List<Modifier> modifiers = new ArrayList<>();

    public Set<Modifier> getModifierSet() {
        return new HashSet<>(modifiers);
    }

    public String[] getModifierNumberArray() {
        String [] array = new String[modifiers.size()];
        for (int i = 0; i < modifiers.size(); i++) {
            array[i] = modifiers.get(i).getNumber();
        }
        return array;
    }

    public Code(String code, String shortDescription, boolean isAddOn) {
        this.code = code;
        this.shortDescription = shortDescription;
        this.isAddOn = isAddOn;
    }

    private String getCodeNumberWithAddOn() {
        return unformattedCodeNumber();
    }

    public String getCodeNumber() {
        return code;
    }

    public boolean isAddOn() {
        return isAddOn;
    }

    public void setPlusShown(boolean plusShown) {
        this.plusShown = plusShown;
    }

    public void setDescriptionShortened(boolean descriptionShortened) {
        this.descriptionShortened = descriptionShortened;
    }

    public void setDescriptionShown(boolean descriptionShown) {
        this.descriptionShown = descriptionShown;
    }

    // applies _all_ formatting settings with code given first
    public String getCodeFirstFormatted() {
        return getCodeNumberWithAddOn()
                + (descriptionShown ? " (" + (getFormattedDescription()) + ")"
                : "");
    }

    private String getFormattedDescription() {
        return descriptionShortened ? truncateString(shortDescription)
                : shortDescription;
    }

    private String truncateString(final String s) {
        if (24 > s.length())
            return s;
        return s.substring(0, 24 - 3) + "...";
    }

    public String unformattedCodeNumber() {
        String plus = plusShown ? "+" : "";
        if (multiplier < 1 || hideMultiplier) {
            return String.format("%s%s%s", isAddOn ? plus : "", getCodeNumber(), modifierString());
        }
        else {
            return String.format("%s%s%s x %d", isAddOn ? plus : "", getCodeNumber(), modifierString(), multiplier);
        }
    }

    // use this for CodeCheckBox text
    public String getUnformattedDescriptionFirst() {
        return shortDescription + " (" + unformattedCodeNumber() + ")";
    }

    public String getUnformattedNumberFirst() {
        return unformattedCodeNumber() + " - " + shortDescription;

    }

    @SuppressLint("DefaultLocale")
    boolean codeContains(String searchString) {
        return (code.contains(searchString) || shortDescription.toLowerCase()
                .contains(searchString.toLowerCase()));
    }

    public void addModifier(Modifier modifier) {
        // don't duplicate modifiers
        for (Modifier m : modifiers) {
            if (m.getNumber().equals(modifier.getNumber())) {
                return;
            }
        }
        // Modifier 26 is a "pricing modifier" and must have first position in modifiers.
        // There are other such modifiers, but none in the small subset of modifiers used here.
        if (modifier.getNumber().equals("26")) {
            modifiers.add(0, modifier);
        }
        else {
            modifiers.add(modifier);
        }
    }

    public void addModifiers(List<Modifier> modifiers) {
        for (Modifier m : modifiers) {
            addModifier(m);
        }
    }

    public void clearModifiers() {
        modifiers.clear();
    }

    public String modifierString() {
        if (modifiers.isEmpty()) {
            return "";
        }
        StringBuilder modString = new StringBuilder();
        for (Modifier m : modifiers) {
            String newModifier = String.format("-%s", m.getNumber());
            modString.append(newModifier);
        }
        return modString.toString();
    }

    // This converts code and modifiers into something that can be put into a putExtra()
    public String[] getCodeAndModifiers() {
        String [] array = new String[modifiers.size() + 1];
        array[0] = getCodeNumber();
        for (int i = 0; i < modifiers.size(); i++) {
            array[i + 1] = modifiers.get(i).getNumber();
        }
        return array;

    }

    public static String[] makeCodeAndModifierArray(String codeNumber, Set<String> modifierNumbers) {
        String[] array = new String[modifierNumbers.size() + 1];
        array[0] = codeNumber;
        int i = 1;
        for (String number : modifierNumbers) {
            array[i++] = number;
        }
        return array;
    }

}
