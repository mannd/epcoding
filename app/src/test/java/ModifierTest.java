/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 2/28/17.
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

import org.epstudios.epcoding.Code;
import org.epstudios.epcoding.Codes;
import org.epstudios.epcoding.Modifier;
import org.epstudios.epcoding.Modifiers;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ModifierTest {

    @Test
    public void firstModifierTest() {
        Code code = new Code("99999", "Test code", false);
        Modifier modifier = new Modifier("99", "Test modifier");
        assert modifier.getShortDescription().equals("Test modifier");
        code.addModifier(modifier);
        assert code.modifierString().equals("-99");
        assert code.unformattedCodeNumber().equals("99999-99");
        code.setMultiplier(10);
        assert code.unformattedCodeNumber().equals("99999-99 x 10");
        code.clearModifiers();
        assert code.unformattedCodeNumber().equals("99999 x 10");
        code.setMultiplier(0);
        assert code.unformattedCodeNumber().equals("99999");
        code.addModifier(modifier);
        // modifier 26 is always first
        Modifier modifier26 = new Modifier("26", "test modifier 26");
        code.addModifier(modifier26);
        assert code.unformattedCodeNumber().equals("99999-26-99");
        Modifier modifier77 = new Modifier("77", "test modifier 77");
        code.addModifier(modifier77);
        assert code.unformattedCodeNumber().equals("99999-26-99-77");
        assert code.getUnformattedDescriptionFirst().equals("Test code (99999-26-99-77)");
    }

    @Test
    public void getModifierForNumberTest() {
        Modifier m = Modifiers.getModifierForNumber("26");
        assert m.getNumber().equals("26");
    }

    @Test
    public void getCodeAndModifiersTest() {
        Code code = new Code("88888", "Test code", false);
        String[] codeAndModifiers = code.getCodeAndModifiers();
        assert codeAndModifiers.length == 1;
        assert codeAndModifiers[0].equals("88888");
        code.addModifier(new Modifier("XX", "test"));
        codeAndModifiers = code.getCodeAndModifiers();
        assert codeAndModifiers.length == 2;
        assert codeAndModifiers[0].equals("88888");
        assert codeAndModifiers[1].equals("XX");
    }

    @Test
    public void makeCodeAndModifierArrayTest() {
        String codeNumber = "77777";
        Set<String> modifiers = new HashSet<>();
        modifiers.add("AA");
        modifiers.add("BB");
        String[] result = Code.makeCodeAndModifierArray(codeNumber, modifiers);
        assert result.length == 3;
        assert result[0].equals("77777");
        assert result[1].equals("AA");
        assert result[2].equals("BB");
    }

    @Test
    public void badCodeNumberTest() {
        Code code = Codes.getCode("BADBADBAD");
        assert code == null;
        code = Codes.getCode("76000");
        assert code.getCodeNumber().equals("76000");
    }

    @Test
    public void setModifiersForCodeTest() {
        String[] testString = new String[3];
        testString[0] = "76000";
        testString[1] = "52";
        testString[2] = "59";
        Codes.setModifiersForCode(testString);
        Code code = Codes.getCode("76000");
        assert code.unformattedCodeNumber().equals("76000-52-59");

    }

    @Test
    public  void modifiersToStringArrayTest() {
        Code code = new Code("99999", "test", false);
        // no modifiers
        String[] array = code.getModifierNumberArray();
        assert array.length == 0;
        code.addModifier(new Modifier("XX", "test modifier 1"));
        code.addModifier(new Modifier("YY", "test modifier 2"));
        array = code.getModifierNumberArray();
        assert array.length == 2;
        assert array[0].equals("XX");
        assert array[1].equals("YY");
        code.clearModifiers();
        array = code.getModifierNumberArray();
        assert array.length == 0;
    }

}
