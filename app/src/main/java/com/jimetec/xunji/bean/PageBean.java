package com.jimetec.xunji.bean;

import java.util.List;

/**
 * 作者:capTain
 * 时间:2019-07-17 16:46
 * 描述:
 */
public class PageBean<T>{


    /**
     * pageNum : 1
     * pageSize : 0
     * size : 0
     * startRow : 0
     * endRow : 0
     * total : 0
     * pages : 0
     * list : []
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : []
     * navigateFirstPage : 0
     * navigateLastPage : 0
     * lastPage : 0
     * firstPage : 0
     */

    public int pageNum;
    public int pageSize;
    public int size;
    public int startRow;
    public int endRow;
    public int total;
    public int pages;
    public int prePage;
    public int nextPage;
    public boolean isFirstPage;
    public boolean isLastPage;
    public boolean hasPreviousPage;
    public boolean hasNextPage;
    public int navigatePages;
    public int navigateFirstPage;
    public int navigateLastPage;
    public int lastPage;
    public int firstPage;
    public List<T> list;
    public List<T> navigatepageNums;
}
