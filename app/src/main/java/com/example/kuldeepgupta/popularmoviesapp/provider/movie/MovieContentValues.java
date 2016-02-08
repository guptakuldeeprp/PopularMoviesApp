package com.example.kuldeepgupta.popularmoviesapp.provider.movie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.kuldeepgupta.popularmoviesapp.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movie} table.
 */
public class MovieContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MovieSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MovieSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public MovieContentValues putMid(long value) {
        mContentValues.put(MovieColumns.MID, value);
        return this;
    }


    public MovieContentValues putTitle(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("title must not be null");
        mContentValues.put(MovieColumns.TITLE, value);
        return this;
    }


    public MovieContentValues putReleasedate(@Nullable String value) {
        mContentValues.put(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieContentValues putReleasedateNull() {
        mContentValues.putNull(MovieColumns.RELEASEDATE);
        return this;
    }

    public MovieContentValues putPosterpath(@Nullable String value) {
        mContentValues.put(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieContentValues putPosterpathNull() {
        mContentValues.putNull(MovieColumns.POSTERPATH);
        return this;
    }

    public MovieContentValues putPlotsynopsis(@Nullable String value) {
        mContentValues.put(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieContentValues putPlotsynopsisNull() {
        mContentValues.putNull(MovieColumns.PLOTSYNOPSIS);
        return this;
    }

    public MovieContentValues putUserrating(@Nullable Double value) {
        mContentValues.put(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieContentValues putUserratingNull() {
        mContentValues.putNull(MovieColumns.USERRATING);
        return this;
    }

    public MovieContentValues putPostersaveloc(@Nullable String value) {
        mContentValues.put(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieContentValues putPostersavelocNull() {
        mContentValues.putNull(MovieColumns.POSTERSAVELOC);
        return this;
    }
}
