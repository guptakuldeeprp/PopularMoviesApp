package com.example.kuldeepgupta.popularmoviesapp.provider.movie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.example.kuldeepgupta.popularmoviesapp.provider.base.AbstractSelection;

/**
 * Selection for the {@code movie} table.
 */
public class MovieSelection extends AbstractSelection<MovieSelection> {
    @Override
    protected Uri baseUri() {
        return MovieColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MovieCursor} object, which is positioned before the first entry, or null.
     */
    public MovieCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MovieCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public MovieCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MovieCursor} object, which is positioned before the first entry, or null.
     */
    public MovieCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MovieCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public MovieCursor query(Context context) {
        return query(context, null);
    }


    public MovieSelection id(long... value) {
        addEquals("movie." + MovieColumns._ID, toObjectArray(value));
        return this;
    }

    public MovieSelection idNot(long... value) {
        addNotEquals("movie." + MovieColumns._ID, toObjectArray(value));
        return this;
    }

    public MovieSelection orderById(boolean desc) {
        orderBy("movie." + MovieColumns._ID, desc);
        return this;
    }

    public MovieSelection orderById() {
        return orderById(false);
    }

    public MovieSelection mid(long... value) {
        addEquals(MovieColumns.MID, toObjectArray(value));
        return this;
    }

    public MovieSelection midNot(long... value) {
        addNotEquals(MovieColumns.MID, toObjectArray(value));
        return this;
    }

    public MovieSelection midGt(long value) {
        addGreaterThan(MovieColumns.MID, value);
        return this;
    }

    public MovieSelection midGtEq(long value) {
        addGreaterThanOrEquals(MovieColumns.MID, value);
        return this;
    }

    public MovieSelection midLt(long value) {
        addLessThan(MovieColumns.MID, value);
        return this;
    }

    public MovieSelection midLtEq(long value) {
        addLessThanOrEquals(MovieColumns.MID, value);
        return this;
    }

    public MovieSelection orderByMid(boolean desc) {
        orderBy(MovieColumns.MID, desc);
        return this;
    }

    public MovieSelection orderByMid() {
        orderBy(MovieColumns.MID, false);
        return this;
    }

    public MovieSelection title(String... value) {
        addEquals(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleNot(String... value) {
        addNotEquals(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleLike(String... value) {
        addLike(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleContains(String... value) {
        addContains(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleStartsWith(String... value) {
        addStartsWith(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection titleEndsWith(String... value) {
        addEndsWith(MovieColumns.TITLE, value);
        return this;
    }

    public MovieSelection orderByTitle(boolean desc) {
        orderBy(MovieColumns.TITLE, desc);
        return this;
    }

    public MovieSelection orderByTitle() {
        orderBy(MovieColumns.TITLE, false);
        return this;
    }

    public MovieSelection releasedate(String... value) {
        addEquals(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieSelection releasedateNot(String... value) {
        addNotEquals(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieSelection releasedateLike(String... value) {
        addLike(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieSelection releasedateContains(String... value) {
        addContains(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieSelection releasedateStartsWith(String... value) {
        addStartsWith(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieSelection releasedateEndsWith(String... value) {
        addEndsWith(MovieColumns.RELEASEDATE, value);
        return this;
    }

    public MovieSelection orderByReleasedate(boolean desc) {
        orderBy(MovieColumns.RELEASEDATE, desc);
        return this;
    }

    public MovieSelection orderByReleasedate() {
        orderBy(MovieColumns.RELEASEDATE, false);
        return this;
    }

    public MovieSelection posterpath(String... value) {
        addEquals(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieSelection posterpathNot(String... value) {
        addNotEquals(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieSelection posterpathLike(String... value) {
        addLike(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieSelection posterpathContains(String... value) {
        addContains(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieSelection posterpathStartsWith(String... value) {
        addStartsWith(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieSelection posterpathEndsWith(String... value) {
        addEndsWith(MovieColumns.POSTERPATH, value);
        return this;
    }

    public MovieSelection orderByPosterpath(boolean desc) {
        orderBy(MovieColumns.POSTERPATH, desc);
        return this;
    }

    public MovieSelection orderByPosterpath() {
        orderBy(MovieColumns.POSTERPATH, false);
        return this;
    }

    public MovieSelection plotsynopsis(String... value) {
        addEquals(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieSelection plotsynopsisNot(String... value) {
        addNotEquals(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieSelection plotsynopsisLike(String... value) {
        addLike(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieSelection plotsynopsisContains(String... value) {
        addContains(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieSelection plotsynopsisStartsWith(String... value) {
        addStartsWith(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieSelection plotsynopsisEndsWith(String... value) {
        addEndsWith(MovieColumns.PLOTSYNOPSIS, value);
        return this;
    }

    public MovieSelection orderByPlotsynopsis(boolean desc) {
        orderBy(MovieColumns.PLOTSYNOPSIS, desc);
        return this;
    }

    public MovieSelection orderByPlotsynopsis() {
        orderBy(MovieColumns.PLOTSYNOPSIS, false);
        return this;
    }

    public MovieSelection userrating(Double... value) {
        addEquals(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieSelection userratingNot(Double... value) {
        addNotEquals(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieSelection userratingGt(double value) {
        addGreaterThan(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieSelection userratingGtEq(double value) {
        addGreaterThanOrEquals(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieSelection userratingLt(double value) {
        addLessThan(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieSelection userratingLtEq(double value) {
        addLessThanOrEquals(MovieColumns.USERRATING, value);
        return this;
    }

    public MovieSelection orderByUserrating(boolean desc) {
        orderBy(MovieColumns.USERRATING, desc);
        return this;
    }

    public MovieSelection orderByUserrating() {
        orderBy(MovieColumns.USERRATING, false);
        return this;
    }

    public MovieSelection postersaveloc(String... value) {
        addEquals(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieSelection postersavelocNot(String... value) {
        addNotEquals(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieSelection postersavelocLike(String... value) {
        addLike(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieSelection postersavelocContains(String... value) {
        addContains(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieSelection postersavelocStartsWith(String... value) {
        addStartsWith(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieSelection postersavelocEndsWith(String... value) {
        addEndsWith(MovieColumns.POSTERSAVELOC, value);
        return this;
    }

    public MovieSelection orderByPostersaveloc(boolean desc) {
        orderBy(MovieColumns.POSTERSAVELOC, desc);
        return this;
    }

    public MovieSelection orderByPostersaveloc() {
        orderBy(MovieColumns.POSTERSAVELOC, false);
        return this;
    }
}
