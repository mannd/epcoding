package org.epstudios.epcoding;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 7/21/17.
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

public class ICD10Codes {

    private static ArrayList<ICD10Code> allICD10Codes = null;
    private static ArrayList<String> allICD10CodeStrings = null;
    private final static String CODE_FILE_NAME = "icd10cm_codes_2021.txt";

    @SuppressWarnings("CharsetObjectCanBeUsed")
    public static ArrayList<ICD10Code> createCodes(final Context context) {
        if (allICD10Codes == null) {
            allICD10Codes = new ArrayList<>();

            try {
                InputStream input = context.getAssets().open(CODE_FILE_NAME);
                BufferedReader reader;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                } else {
                    reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                }
                String line;
                while ((line = reader.readLine()) != null) {
                    ICD10Code code = new ICD10Code(line);
                    allICD10Codes.add(code);
                }
                reader.close();
                // process allICD10Codes
            } catch (IOException e) {
                allICD10Codes.clear();
                allICD10Codes.add(ICD10Code.fileNotFoundICD10Code());
            }

        }
        return allICD10Codes;
    }

    public static ArrayList<String> getICD10CodeStrings(final Context context) {
        if (allICD10CodeStrings == null) {
            allICD10CodeStrings = new ArrayList<>();
            ArrayList<ICD10Code> codes = createCodes(context);
            for (ICD10Code code : codes) {
                allICD10CodeStrings.add(code.getCodeString());
            }
        }
        return allICD10CodeStrings;
    }

    public static List<String> searchCodes(String searchString, Context context) {
        List<String> result = new ArrayList<>();
        for (String code : getICD10CodeStrings(context)) {
            if (code.toLowerCase().contains(searchString.toLowerCase())) {
                result.add(code);
            }
        }
        return result;
    }

}
