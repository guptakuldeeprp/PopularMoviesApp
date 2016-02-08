package com.example.kuldeepgupta.popularmoviesapp.provider.movie;

import com.example.kuldeepgupta.popularmoviesapp.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Data model for the {@code movie} table.
 */
public interface MovieModel extends BaseModel {

    /**
     * Get the {@code mid} value.
     */
    long getMid();

    /**
     * Get the {@code title} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getTitle();

    /**
     * Get the {@code releasedate} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleasedate();

    /**
     * Get the {@code posterpath} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterpath();

    /**
     * Get the {@code plotsynopsis} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPlotsynopsis();

    /**
     * Get the {@code userrating} value.
     * Can be {@code null}.
     */
    @Nullable
    Double getUserrating();

    /**
     * Get the {@code postersaveloc} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPostersaveloc();
}
