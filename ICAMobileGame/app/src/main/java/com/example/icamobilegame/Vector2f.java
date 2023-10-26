package com.example.icamobilegame;

import java.util.Vector;

public class Vector2f extends java.lang.Object
{
    protected double x,y;

    public Vector2f()
    {
        x=0;
        y=0;
    }

    public Vector2f(double vecX, double vecY)
    {
        x=vecX;
        y=vecY;
    }
    public Vector2f(Vector2f other)
    {
        x=other.x;
        y=other.y;
    }
    public void Scale(int value) {
        x *= value;
        y *= value;
    }
    public double Dot(Vector2f other){
        return 0;
    }
    public double DotProduct(Vector2f a, Vector2f b){
        return 0;
    }
    public double Cross(Vector2f other){
        return 0;
    }
    public double CrossProduct(Vector2f a, Vector2f b) {
        return 0;
    }
    public void Set(double vecX, double vecY){
        x=vecX;
        y=vecY;
    }
    public Vector2f Multiply(Vector2f vec, double scalar)
    {
        x*=scalar;
        y*=scalar;
        return new Vector2f(x,y);
    }
    public static Vector2f Sum(Vector2f a, Vector2f b){
        Vector2f tempVec = new Vector2f(a.x, a.y);
        tempVec.x += b.x;
        tempVec.y += b.y;
        return tempVec;
    }
    public void Divide(Vector2f vec,long scalar){
        vec.x/=scalar;
        vec.y/=scalar;

    }
    public Vector2f Sub(Vector2f vec){
        x -= vec.x;
        y -= vec.y;
        return new Vector2f(x,y);
    }
    public Vector2f Normalize()
    {
        Vector2f tempVec = new Vector2f(x,y);
        double magnitude=Math.sqrt((x*x) + (y*y));

        x /=magnitude;
        y /=magnitude;

        return tempVec;
    }

    public double Length()
    {
        return Math.sqrt(x*x+y*y);
    }
    @Override
    public java.lang.String toString(){
        String conVec;
        conVec="X: "+ x +" Y: "+ y;
        return conVec;

    }

}
