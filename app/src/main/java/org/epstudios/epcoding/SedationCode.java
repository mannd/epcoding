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

package org.epstudios.epcoding;

import java.util.ArrayList;
import java.util.List;

public class SedationCode extends Code {


    // These would be better as string resources, but no rationale to translate
    // EP Coding to other languages (CPT coding is American only), so we
    // avoid importing the Context to use getString().
    // These fields are public to help with testing
    public final static String NO_SEDATION_STRING = "No sedation used during this procedure.";
    public final static String UNASSIGNED_SEDATION_STRING = "No sedation codes assigned.";
    public final static String SHORT_SEDATION_TIME_STRING = "No codes assigned as sedation time < 10 mins.";
    public final static String OTHER_MD_CALCULATED_TIME_STRING =
            "Sedation performed by other MD, who should use coding %s";

    public SedationStatus getSedationStatus() {
        return sedationStatus;
    }

    public void setSedationStatus(SedationStatus sedationStatus) {
        this.sedationStatus = sedationStatus;
    }

    private SedationStatus sedationStatus;

    public SedationCode(String code, String shortDescription, boolean isAddOn) {
        super(code, shortDescription, isAddOn);
        sedationStatus = SedationStatus.Unassigned;
    }

    public static List<Code> sedationCoding(Integer sedationTime, boolean sameMD,
                                            boolean patientOver5,
                                            SedationStatus status) {
        List<Code> codes = new ArrayList<>();
        if (SedationStatus.cannotHaveSedationCodes(status)) {
            return codes;
        }
        if (sedationTime >= 10) {
            if (sameMD) {
                if (patientOver5) {
                    codes.add(Codes.getCode("99152"));
                } else {
                    codes.add(Codes.getCode("99151"));
                }
            } else {
                if (patientOver5) {
                    codes.add(Codes.getCode("99156"));
                } else {
                    codes.add(Codes.getCode("99155"));
                }
            }
        }
        if (sedationTime >= 23) {
            Integer multiplier = codeMultiplier(sedationTime);
            Code code;
            if (sameMD) {
                code = Codes.getCode("99153");
            } else {
                code = Codes.getCode("99157");
            }
            code.setMultiplier(multiplier);
            codes.add(code);
        }
        return codes;
    }

    private static Integer codeMultiplier(Integer time) {
        if (time <= 22)
            return 0;
        double multiplier = (time - 15) / 15.0;
        multiplier = Math.round(multiplier);
        return (int) multiplier;
    }

    public static String printSedationCodes(List<Code> codes, String separator) {
        String codeString = "";
        if (codes == null || codes.size() < 1) {
            return codeString;
        }
        if (codes.size() == 1) {
            codeString = codes.get(0).unformattedCodeNumber();
        }
        else {
            codeString = String.format("%s%s%s", codes.get(0).unformattedCodeNumber(), separator,
                    codes.get(1).unformattedCodeNumber());
        }
        return codeString;
    }

    public static String sedationDetail(List<Code> codes, SedationStatus status) {
        String detail;
        String codeDetails = printSedationCodes(codes, ", ");
        switch (status) {
            case Unassigned:
                detail = UNASSIGNED_SEDATION_STRING;
                break;
            case None:
                detail = NO_SEDATION_STRING;
                break;
            case LessThan10Mins:
                detail = SHORT_SEDATION_TIME_STRING;
                break;
            case OtherMDCalculated:
                detail = String.format(OTHER_MD_CALCULATED_TIME_STRING, codeDetails);
                break;
            case AssignedSameMD:
                detail = codeDetails;
                break;
            default:
                detail = "Sedation coding error";
                break;
        }
        return detail;
    }

    public static String printSedationCodesWithDescription(List<Code> codes) {
        String codeString = "";
        if (codes == null || codes.size() < 1) {
            return codeString;
        }
        if (codes.size() == 1) {
            codeString = codes.get(0).getUnformattedNumberFirst();
        }
        else {
            codeString = String.format("%s\n%s", codes.get(0).getUnformattedNumberFirst(),
                    codes.get(1).getUnformattedNumberFirst());
        }
        return codeString;
    }


}
