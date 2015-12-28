package com.example.kuldeepgupta.popularmoviesapp.tmdb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kuldeep.gupta on 26-12-2015.
 * A simple container for options and their Order
 */
public class SortBy {

    public static final SortBy DEFAULT = new SortBy(Option.popularity, Order.desc);

    private Map<Option, Order> sortMap;

    /**
     * helper constructor for just single option
     * @param opt
     * @param ord
     */
    public SortBy(Option opt, Order ord) {
        this();
        sortMap.put(opt, ord);
    }

    public SortBy(){
        sortMap = new HashMap<>();
    }

    public static enum Option {
        popularity,
        release_date,
        revenue,
        primary_release_date,
        original_title,
        vote_average,
        vote_count, opt;
    }

    public static enum Order {
        asc,
        desc;
    }

    public SortBy addOption(Option opt, Order ord) {
        sortMap.put(opt, ord);
        return this;
    }

    public Order getOrder(Option option) {
        return sortMap.get(option);
    }

    public Iterator<Option> options() {
        return sortMap.keySet().iterator();
    }

    @Override
    public String toString() {
        Iterator<Option> itr = options();
        StringBuilder strBldr = new StringBuilder();
        while(itr.hasNext()) {
            strBldr.append(",");
            Option opt = itr.next();
            strBldr.append(opt + "." + getOrder(opt));
        }
        strBldr.deleteCharAt(0); // remove the first separator
        return strBldr.toString();
    }
}
