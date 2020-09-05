/*
  Copyright (C) 2017 EP Studios, Inc.
  www.epstudiossoftware.com
  <p>
  Created by mannd on 3/5/17.
  <p>
  This file is part of epcoding.
  <p>
  epcoding is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  <p>
  epcoding is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with epcoding.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.epstudios.epcoding.Code;
import org.epstudios.epcoding.Codes;
import org.epstudios.epcoding.SedationCode;
import org.epstudios.epcoding.SedationStatus;
import org.epstudios.epcoding.SedationTimeCalculator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ConstantConditions")
public class SedationTest {

    @Test
    public void sedationMultiplierTest() {
        int time = 23;
        boolean sameMD = true;
        boolean ptOver5 = true;
        List<Code> sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        assert sedationCodes.size() == 2;
        Code code1 = sedationCodes.get(0);
        Code code2 = sedationCodes.get(1);
        String test = code2.unformattedCodeNumber();
        assert code1.unformattedCodeNumber().equals("99152");
        assert code2.unformattedCodeNumber().equals("+99153 x 1");
        code2.setHideMultiplier(true);
        assert code2.unformattedCodeNumber().equals("+99153");
        code2.setHideMultiplier(false);
        assert code2.unformattedCodeNumber().equals("+99153 x 1");

        time = 55;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        code1 = sedationCodes.get(0);
        code2 = sedationCodes.get(1);
        assert code1.unformattedCodeNumber().equals("99152");
        assert code2.unformattedCodeNumber().equals("+99153 x 3");

        time = 70;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        code1 = sedationCodes.get(0);
        code2 = sedationCodes.get(1);
        assert code1.unformattedCodeNumber().equals("99152");
        assert code2.unformattedCodeNumber().equals("+99153 x 4");

        sameMD = false;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        code1 = sedationCodes.get(0);
        code2 = sedationCodes.get(1);
        String test2 = code2.unformattedCodeNumber();
        assert code1.unformattedCodeNumber().equals("99156");
        assert code2.unformattedCodeNumber().equals("+99157 x 4");

        ptOver5 = false;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        code1 = sedationCodes.get(0);
        code2 = sedationCodes.get(1);
        assert code1.unformattedCodeNumber().equals("99155");
        assert code2.unformattedCodeNumber().equals("+99157 x 4");

        sameMD = true;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        code1 = sedationCodes.get(0);
        code2 = sedationCodes.get(1);
        assert code1.unformattedCodeNumber().equals("99151");
        assert code2.unformattedCodeNumber().equals("+99153 x 4");

        time = 0;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        assert sedationCodes.size() == 0;
        time = 22;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        assert sedationCodes.size() == 1;
        time = 23;
        sedationCodes = SedationCode.sedationCoding(time, sameMD, ptOver5,
                SedationStatus.determineSedationStatus(time, sameMD));
        assert sedationCodes.size() == 2;
    }

    @Test
    public void sedationDetailTest() {
        SedationStatus status = SedationStatus.Unassigned;
        Code code1 = new Code("A", "B", false);
        Code code2 = new Code("X", "Y", true);
        List<Code> codes = new ArrayList<>();
        codes.add(code1);
        codes.add(code2);
        String detail = SedationCode.sedationDetail(codes, status);
        assert detail.equals(SedationCode.UNASSIGNED_SEDATION_STRING);
        status = SedationStatus.AssignedSameMD;
        codes = SedationCode.sedationCoding(23, true, true, status);
        detail = SedationCode.sedationDetail(codes, status);
        assert detail.equals("99152, +99153 x 1");
        codes = SedationCode.sedationCoding(23, false, true, SedationStatus.OtherMDCalculated);
        detail = SedationCode.sedationDetail(codes, SedationStatus.OtherMDCalculated);
        assert detail.equals(String.format(SedationCode.OTHER_MD_CALCULATED_TIME_STRING, "99156, +99157 x 1"));
        detail = SedationCode.sedationDetail(codes, SedationStatus.None);
        assert detail.equals(SedationCode.NO_SEDATION_STRING);
        detail = SedationCode.sedationDetail(null, SedationStatus.LessThan10Mins);
        assert detail.equals(SedationCode.SHORT_SEDATION_TIME_STRING);
    }

    @Test
    public void printSedationCodesTest() {
        List<Code> codes = new ArrayList<>();
        codes.add(Codes.getCode("99152"));
        codes.add(Codes.getCode("99153"));
        String result = SedationCode.printSedationCodesWithDescription(codes);
        String predictedResult = "99152 - Moderate sedation, same MD, initial 15 min, pt ≥ 5 y/o\n+99153 - Moderate sedation, same MD, each additional 15 min";
        assert result.equals(predictedResult);
        codes.clear();
        codes.add(Codes.getCode("99152"));
        result = SedationCode.printSedationCodesWithDescription(codes);
        predictedResult = "99152 - Moderate sedation, same MD, initial 15 min, pt ≥ 5 y/o";
        assert result.equals(predictedResult);
    }

    @Test
    public void minuteDifferenceTest() {
        int startHour = 0;
        int startMinute = 0;
        int endHour = 0;
        int endMinute = 0;
        assert SedationTimeCalculator.minuteDifference(startHour, startMinute,
                endHour, endMinute) == 0;
        endMinute = 1;
        assert SedationTimeCalculator.minuteDifference(startHour, startMinute,
                endHour, endMinute) == 1;
        endHour = 1;
        assert SedationTimeCalculator.minuteDifference(startHour, startMinute,
                endHour, endMinute) == 61;
        startHour = 1;
        assert SedationTimeCalculator.minuteDifference(startHour, startMinute,
                endHour, endMinute) == 1;
        startHour = 12;
        startMinute = 0;
        endHour = 11;
        endMinute = 59;
        assert SedationTimeCalculator.minuteDifference(startHour, startMinute,
                endHour, endMinute) == 60 * 24 - 1;
        startMinute = 45;
        endHour = 13;
        endMinute = 44;
        assert SedationTimeCalculator.minuteDifference(startHour, startMinute,
                endHour, endMinute) == 59;
    }
}
