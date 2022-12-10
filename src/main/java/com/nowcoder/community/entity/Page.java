package com.nowcoder.community.entity;

/**
 * @author Oliver
 * @create 2022-11-25 20:18
 */
public class Page {

    private int current = 1; //当前页码
    private int limit= 10; //显示条数
    private int rows; //数据总数
    private String path; //查询路径

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        //当前页码必须大于1
        if(current >= 1)
            this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        //限制每页显示的条数
        if(limit >=1 && limit <=100)
        this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        //数据总数大于0
        if(rows > 0)
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        return   rows % limit == 0 ? rows / limit : rows /limit + 1;
    }
    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset(){
       return  (current -1) * limit;
    }

    /**
     * 获取起始页码
     * @return
     */
    public int getStart(){
        int start = current - 2;
        return start < 1 ? 1 : start;
    }

    /**
     * 获取结束页码
     * @return
     */
    public int getEnd(){
        int end = current + 2;
        return end > getTotal() ? getTotal() : end;
    }
}
