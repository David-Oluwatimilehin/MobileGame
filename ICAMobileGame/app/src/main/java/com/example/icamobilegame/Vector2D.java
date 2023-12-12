package com.example.icamobilegame;

import androidx.annotation.NonNull;

public class Vector2D {

    public float x;
    public float y;

    public Vector2D(){
        this.x=0.0f;
        this.y=0.0f;
    }
    public Vector2D(float vecX,float vecY){
        this.x=vecX;
        this.y=vecY;
    }
    public Vector2D(Vector2D vector){
        this.x= vector.x;
        this.y= vector.y;
    }
    public float Magnitude(){
        return (float) Math.sqrt(this.x*this.x+this.y*this.y);
    }

    public Vector2D Normalize(){
        float magnitude= Magnitude();
        this.x = this.x/magnitude;
        this.y = this.y/magnitude;
        return this;
    }
    public Vector2D add(Vector2D other){
        this.x+=other.x;
        this.y+=other.y;
        return this;

    }
    public static Vector2D add(Vector2D lhs, Vector2D rhs){
        return new Vector2D(lhs.x+ rhs.x, lhs.y+ rhs.y);

    }
    public static Vector2D add(Vector2D lhs, float scalar){
        return new Vector2D(lhs.x, lhs.y+ scalar);

    }

    public Vector2D subtract(Vector2D other){
        this.x-=other.x;
        this.y-=other.y;
        return this;

    }
    public static float subtract(Vector2D lhs, float other){
        lhs.y-=other;
        return lhs.y;

    }
    public static Vector2D subtract(Vector2D lhs, Vector2D rhs){
        return new Vector2D(lhs.x- rhs.x, lhs.y- rhs.y);

    }

    public static Vector2D scalar(Vector2D vector,float scalar){
        return new Vector2D(vector.x, vector.y*scalar);
    }
    public static Vector2D scalar(float scalar,Vector2D vector){
        return new Vector2D(vector.x*scalar, vector.y);
    }

    public static float DotProduct(Vector2D lhs, Vector2D rhs){
        return lhs.x*rhs.x+lhs.y*rhs.y;
    }

    @Override
    public java.lang.String toString(){
        String conVec;
        conVec="X: "+ (float)this.x +" Y: "+ (float)this.y;
        return conVec;

    }
}
