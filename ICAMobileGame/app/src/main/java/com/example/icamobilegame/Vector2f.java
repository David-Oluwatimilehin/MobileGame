package com.example.icamobilegame;

import android.util.FloatMath;

public class Vector2f extends java.lang.Object
{
    protected double x,y;

    public static float TO_RADIANS=(1 / 180.0f) * (float) Math.PI;
    public static float TO_DEGREES=(1 / (float) Math.PI) * 180.0f;
    public Vector2f()
    {
        this.x=0;
        this.y=0;
    }

    public Vector2f(double vecX, double vecY)
    {
        this.x=vecX;
        this.y=vecY;
    }
    public Vector2f(Vector2f other)
    {
        this.x=other.x;
        this.y=other.y;
    }
    public void Scale(int value) {
        this.x *= value;
        this.y *= value;
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
        this.x=vecX;
        this.y=vecY;
    }
    public Vector2f Multiply(Vector2f vec, double scalar)
    {
        this.x*=scalar;
        this.y*=scalar;
        return this;
    }
    public static Vector2f Sum(Vector2f a, Vector2f b){
        Vector2f tempVec = new Vector2f(a.x, a.y);
        tempVec.x += b.x;
        tempVec.y += b.y;
        return tempVec;
    }
    public Vector2f Add(Vector2f vec){
        this.x= vec.x;
        this.y= vec.y;
        return this;
    }
    public Vector2f Copy(){
        return new Vector2f(x,y);
    }
    public Vector2f Rotate(float angle)
    {
        float rad = angle * TO_RADIANS;

        double cos = Math.cos((double) rad);
        double sin = Math.sin((double) rad);

        float newX= (float) (this.x * (float)cos - this.y * (float)sin);
        float newY= (float) (this.x * (float)sin + this.y * (float)cos);

        this.x = newX;
        this.y = newY;
        return this;
    }
    public float Angle(){
        float angle= (float) Math.atan2(y,x)*TO_DEGREES;
        if(angle < 0){
            angle+=360.0f;
        }
        return angle;
    }
    public void Divide(Vector2f vec,long scalar){

        if(scalar != 0){
            this.x/=scalar;
            this.y/=scalar;
        }


    }
    public Vector2f Sub(Vector2f vec){
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }
    public Vector2f Normalize()
    {
        double magnitude=Math.sqrt((this.x*this.x) + (this.y*this.y));

        this.x /=magnitude;
        this.y /=magnitude;

        return this;
    }

    public double Length()
    {
        return Math.sqrt(x*x+y*y);
    }
    @Override
    public java.lang.String toString(){
        String conVec;
        conVec="X: "+ (float)this.x +" Y: "+ (float)this.y;
        return conVec;

    }

}
