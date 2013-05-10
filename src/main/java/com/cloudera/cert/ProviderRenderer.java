package com.cloudera.cert;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class ProviderRenderer extends BasicComboBoxRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
        
        
        for (Component component : list.getComponents()) {
            if(component.equals("AWS Cloud Gov")) {
                component.setEnabled(false);  
            }
        }
        return this;
    }

}
