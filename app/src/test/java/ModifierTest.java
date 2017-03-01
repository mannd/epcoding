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

import android.util.Log;

import org.epstudios.epcoding.Code;
import org.epstudios.epcoding.Modifier;
import org.junit.Test;
import org.junit.runner.RunWith;

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


    }

}
