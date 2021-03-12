package com.example.finalhometest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<MenuModel> listDataHeader;
    private final HashMap<MenuModel, List<MenuModel>> listDataChild;
    private TextView txtListChild;
    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroupchild, null);
        }

        txtListChild = convertView
                .findViewById(R.id.lblListItem);

        Drawable img = convertView.getContext().getDrawable(R.drawable.ic_baseline_radio_button_unchecked_24);
        img.setTint(Color.parseColor("#5e5e5e"));
        img.setBounds(0, 0, 60, 60);
        txtListChild.setCompoundDrawables(img,null,null,null);



        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroupheader, null);
        }
        // change background color item
        Drawable img;
        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
//        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        lblListHeader.setCompoundDrawablePadding(20);
        switch (groupPosition) {
            case 0:
               img = convertView.getContext().getDrawable(R.drawable.ic_baseline_home_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,null,null);

                convertView.setBackgroundColor(Color.YELLOW);
                break;
            case 1:
                img = convertView.getContext().getDrawable(R.drawable.ic_baseline_account_circle_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,null,null);

                convertView.setBackgroundColor(Color.LTGRAY);
                break;
            case 2:
                img = convertView.getContext().getDrawable(R.drawable.ic_baseline_add_circle_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,null,null);

                convertView.setBackgroundColor(Color.MAGENTA);
                break;
            case 3:
                img = convertView.getContext().getDrawable(R.drawable.ic_baseline_format_list_numbered_rtl_24);
                Drawable imgDropDown = convertView.getContext().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);

                imgDropDown.setTint(Color.BLACK);
                imgDropDown.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,imgDropDown,null);

                convertView.setBackgroundColor(Color.parseColor("#4893f0"));
                break;
            case 4:
                img = convertView.getContext().getDrawable(R.drawable.ic_baseline_style_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,null,null);
                convertView.setBackgroundColor(Color.CYAN);
                break;
            case 5:
                img = convertView.getContext().getDrawable(R.drawable.ic_baseline_style_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,null,null);
                convertView.setBackgroundColor(Color.LTGRAY);
                break;
            case 6:
                img = convertView.getContext().getDrawable(R.drawable.ic_baseline_mark_email_unread_24);
                img.setTint(Color.BLACK);
                img.setBounds(0, 0, 60, 60);
                lblListHeader.setCompoundDrawables(img,null,null,null);
                convertView.setBackgroundColor(Color.RED);
                break;
        }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
    public void clickedItem() {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = infalInflater.inflate(R.layout.listgroupchild, null);
        Drawable img = convertView.getContext().getDrawable(R.drawable.ic_baseline_radio_button_checked_24);
        img.setTint(Color.BLACK);
        img.setBounds(0, 0, 60, 60);
        txtListChild.setCompoundDrawables(img,null,null,null);
    }
}
