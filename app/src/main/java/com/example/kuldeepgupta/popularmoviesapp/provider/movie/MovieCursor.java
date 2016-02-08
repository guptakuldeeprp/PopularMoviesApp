package com.example.kuldeepgupta.popularmoviesapp.provider.movie;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.kuldeepgupta.popularmoviesapp.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code movie} table.
 */
public class MovieCursor extends AbstractCursor implements MovieModel {
    public MovieCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(MovieColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code mid} value.
     */
    public long getMid() {
        Long res = getLongOrNull(MovieColumns.MID);
        if (res == null)
            throw new NullPointerException("The value of 'mid' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code title} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getTitle() {
        String res = getStringOrNull(MovieColumns.TITLE);
        if (res == null)
            throw new NullPointerException("The value of 'title' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code releasedate} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getReleasedate() {
        String res = getStringOrNull(MovieColumns.RELEASEDATE);
        return res;
    }

    /**
     * Get the {@code posterpath} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPosterpath() {
        String res = getStringOrNull(MovieColumns.POSTERPATH);
        return res;
    }

    /**
     * Get the {@code plotsynopsis} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPlotsynopsis() {
        String res = getStringOrNull(MovieColumns.PLOTSYNOPSIS);
        return res;
    }

    /**
     * Get the {@code userrating} value.
     * Can be {@code null}.
     */
    @Nullable
    public Double getUserrating() {
        Double res = getDoubleOrNull(MovieColumns.USERRATING);
        return res;
    }

    /**
     * Get the {@code postersaveloc} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPostersaveloc() {
        String res = getStringOrNull(MovieColumns.POSTERSAVELOC);
        return res;
    }
}
