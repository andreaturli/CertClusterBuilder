package com.cloudera.cert;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;

public class ProviderComboBox extends JComboBox {
    
    private final HashSet<Integer> disableIndexSet = new HashSet<Integer>();
    private boolean isDisableIndex = false;
    
    public void setDisableIndex(Set<Integer> set) {
        disableIndexSet.clear();
        for(Integer i:set) {
            disableIndexSet.add(i);
        }
    }

    public ProviderComboBox() {
        super();
        final ListCellRenderer r = getRenderer();
        setRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value,
                                 int index, boolean isSelected, boolean cellHasFocus) {
                Component c;
                if(disableIndexSet.contains(index)) {
                    c = r.getListCellRendererComponent(list,value,index,false,false);
                    c.setEnabled(false);
                }else{
                    c = r.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                    c.setEnabled(true);
                }
                return c;
            }
        });
        
        AbstractAction up = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int si = getSelectedIndex();
                for(int i = si-1;i>=0;i--) {
                    if(!disableIndexSet.contains(i)) {
                        setSelectedIndex(i); break;
                    }
                }
            }
        };
        AbstractAction down = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int si = getSelectedIndex();
                for(int i = si+1;i<getModel().getSize();i++) {
                    if(!disableIndexSet.contains(i)) {
                        setSelectedIndex(i); break;
                    }
                }
            }
        };
        ActionMap amc = getActionMap();
        amc.put("myUp", up); amc.put("myDown", down);
        InputMap imc = getInputMap();
        imc.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),   "myUp");
        imc.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "myDown");
    }
    
}
