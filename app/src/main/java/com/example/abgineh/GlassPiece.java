package com.example.abgineh;
import java.io.Serializable;
import org.w3c.dom.ls.LSSerializer;

public class GlassPiece implements Serializable {
    public int width;
    public int height;
    public int quantity;
    public GlassPiece(int width, int height, int quantity)
    {
        this.width=width;
        this.height=height;
        this.quantity=quantity;
    }
    }
