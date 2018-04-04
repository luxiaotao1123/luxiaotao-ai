package com.wuai.company.order.entity;

/**
 * Created by Administrator on 2017/6/5.
 */
public class Point {
    private float x,y;
    public Point(float x,float y){
        this.x=x;
        this.y=y;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public double distance(Point p){
        return Math.sqrt((p.getX()-x)*(p.getX()-x)+(p.getY()-y)*(p.getY()-y));
    }
}
