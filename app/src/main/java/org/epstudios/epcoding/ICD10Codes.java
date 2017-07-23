package org.epstudios.epcoding;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

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

    private static ArrayList<ICD10Code> array = null;
    private final static String CODE_FILE_NAME = "cardICD10codes2017.txt";

    public final static ArrayList<ICD10Code> createCodes(final Context context) {
        if (array == null) {
            array = new ArrayList<>();

            try {
                StringBuilder buf = new StringBuilder();
                InputStream input = context.getAssets().open(CODE_FILE_NAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buf.append(line);
                }
                reader.close();
                // process array
            } catch (IOException e) {
                array.clear();
                array.add(ICD10Code.fileNotFoundICD10Code());
            }

        }
        return array;

    }

}
