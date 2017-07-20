import org.epstudios.epcoding.ICD10Code;
import org.junit.Test;

/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 7/20/17.
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

public class ICD10Test {
    @Test
    public void ICD10InitTest() {
        ICD10Code code = new ICD10Code("X123456 This is a test code");
        assert code.getNumber().equals("X12.3456");
        assert code.getFullDescription().equals("This is a test code");
        ICD10Code code1 = new ICD10Code("Y12     This is a short code");
        assert code1.getNumber().equals("Y12");
        assert  code1.getFullDescription().equals("This is a short code");

    }

    @Test(expected = Exception.class)
    public void ICD10BadInitTest() {
        ICD10Code code = new ICD10Code("BAD");
    }
}
