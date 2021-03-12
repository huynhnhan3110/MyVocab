package com.example.finalhometest;
public class MenuModel {

    public final String menuName;
    final int idFragment;
    public final boolean hasChildren;
    public final boolean isGroup;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, int idFragment) {

        this.menuName = menuName;
        this.idFragment = idFragment;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}