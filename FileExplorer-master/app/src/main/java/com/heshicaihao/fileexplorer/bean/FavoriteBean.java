/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.heshicaihao.fileexplorer.bean;

public class FavoriteBean {
    // id in the database
    public long id;

    public String title;

    // path
    public String location;

    public FileInfoBean fileInfo;

    public FavoriteBean(String t, String l) {
        title = t;
        location = l;
    }

    public FavoriteBean(long i, String t, String l) {
        id = i;
        title = t;
        location = l;
    }
}
