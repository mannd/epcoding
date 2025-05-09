/*
  Copyright (C) 2017 EP Studios, Inc.
  www.epstudiossoftware.com
  <p>
  Created by mannd on 3/6/17.
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

import java.util.EnumSet;

public enum SedationStatus {
        Unassigned,
        None,
        LessThan10Mins,
        OtherMDCalculated,
        AssignedSameMD;

        public static SedationStatus stringToSedationStatus(String s) {
                try {
                        return valueOf(s);
                }
                catch (Exception e) {
                        return Unassigned;
                }
        }

        public static boolean canHaveSedationCodes(SedationStatus status) {
                return EnumSet.of(OtherMDCalculated, AssignedSameMD).contains(status);
        }

        public static boolean cannotHaveSedationCodes(SedationStatus status) {
                return !canHaveSedationCodes(status);
        }

        public static SedationStatus determineSedationStatus(int sedationTime,
                                                             boolean sameMD) {
                if (sedationTime < 10) {
                        return SedationStatus.LessThan10Mins;
                }
                if (!sameMD) {
                        return SedationStatus.OtherMDCalculated;
                }
                else {
                        return SedationStatus.AssignedSameMD;
                }
        }
}
