package com.hbk.fast_food.ui.components;

import javax.swing.*;
import java.awt.*;

public class ProductLabel extends JLabel {
    public ProductLabel(String name, Color color) {
        super(name);     
        this.setForeground(color);
    }
}
