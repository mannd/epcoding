package org.epstudios.epcoding;

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

public class ICD10Code {
    private final String number;

    public String getNumber() {
        return number;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    private final String fullDescription;

    public ICD10Code(String number, String description) {
        this.number = number;
        fullDescription = description;
    }

    // assumes valid string > 8 chars long!!
    // otherwise will throw exception
    public ICD10Code(String codeString) {
        String rawNumber = codeString.substring(0, 7).trim();
        if (rawNumber.length() > 3) {
            rawNumber = rawNumber.substring(0, 3) + "." + rawNumber.substring(3);
        }
        String description = codeString.substring(8).trim();
        this.number = rawNumber;
        fullDescription = description;
    }

    static public ICD10Code fileNotFoundICD10Code() {
        return new ICD10Code("Error", "Could not open ICD-10 code list file");
    }

    public String getCodeString() {
        return number + " - " + fullDescription;
    }

}
