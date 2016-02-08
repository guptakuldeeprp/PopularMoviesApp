package com.example.kuldeepgupta.popularmoviesapp.provider.movie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.kuldeepgupta.popularmoviesapp.provider.MovieProvider;
import com.example.kuldeepgupta.popularmoviesapp.provider.movie.MovieColumns;

/**
 * Columns for the {@code movie} table.
 */
public class MovieColumns implements BaseColumns {
    public static final String TABLE_NAME = "movie";
    public static final Uri CONTENT_URI = Uri.parse(MovieProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String MID = "mid";

    public static final String TITLE = "title";

    public static final String RELEASEDATE = "releaseDate";

    public static final String POSTERPATH = "posterPath";

    public static final String PLOTSYNOPSIS = "plotSynopsis";

    public static final String USERRATING = "userRating";

    public static final String POSTERSAVELOC = "posterSaveLoc";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MID,
            TITLE,
            RELEASEDATE,
            POSTERPATH,
            PLOTSYNOPSIS,
            USERRATING,
            POSTERSAVELOC
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MID) || c.contains("." + MID)) return true;
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(RELEASEDATE) || c.contains("." + RELEASEDATE)) return true;
            if (c.equals(POSTERPATH) || c.contains("." + POSTERPATH)) return true;
            if (c.equals(PLOTSYNOPSIS) || c.contains("." + PLOTSYNOPSIS)) return true;
            if (c.equals(USERRATING) || c.contains("." + USERRATING)) return true;
            if (c.equals(POSTERSAVELOC) || c.contains("." + POSTERSAVELOC)) return true;
        }
        return false;
    }

}
