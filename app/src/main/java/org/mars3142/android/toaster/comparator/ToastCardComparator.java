/*
Copyright 2014 Peter Siegmund

This file is part of Toaster.

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Foobar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.mars3142.android.toaster.comparator;

import org.mars3142.android.toaster.card.ToastCard;

import java.util.Comparator;

public class ToastCardComparator implements Comparator<ToastCard> {

    private final static String TAG = ToastCardComparator.class.getSimpleName();

    @Override
    public int compare(ToastCard lhs, ToastCard rhs) {
        return lhs.appName.compareTo(rhs.appName);
    }
}
