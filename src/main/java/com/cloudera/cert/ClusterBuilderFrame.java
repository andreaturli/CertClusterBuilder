package com.cloudera.cert;

import static com.google.common.base.Preconditions.checkNotNull;

import com.cloudera.model.Cloud;
import com.cloudera.utils.TextTransferHandler;
import com.cloudera.utils.Clouds;
import com.cloudera.model.ProviderComboBox;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.awt.Desktop;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author daniel@cloudera.com
 */
public class ClusterBuilderFrame extends javax.swing.JFrame {
    
    private static final Logger log = LoggerFactory.getLogger(ClusterBuilderFrame.class);
    
    private static final String DEFAULT_MESSAGE = "Ready";
    private static ConsoleFrame consoleFrame = null;
    private static final Timer delayTimer = new Timer();
    private TimerTask delayTask = null;
    /** the last status set (excluding {@link #setTempStatus(String)}) */
    private String lastPermanentStatus = DEFAULT_MESSAGE;

    /**
     * Creates new form ClusterBuilderFrame
     */
    public ClusterBuilderFrame() {
        initComponents();
        this.setTitle("Cloudera Connect");
        consoleFrame = new ConsoleFrame(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        console = new javax.swing.JButton();
        brooklyn = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        TextTransferHandler tth1 = new TextTransferHandler();
        passwordField.setTransferHandler(tth1);
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        deploy = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        Map<String, Boolean> nameAndEnabled = Maps.newLinkedHashMap();
        for (Cloud cloud : Clouds.getSupportedClouds()) {
            nameAndEnabled.put(cloud.getDisplayName(), cloud.isEnabled());
        }
        provider = new ProviderComboBox(nameAndEnabled);
        provider.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                Object item = event.getItem();
                if(item.equals("Octocloud") || item.equals("IBM CloudFirst Factory")) {
                    endpoint.setEnabled(true);
                    endpointLabel.setEnabled(true);
                } else {
                    endpoint.setEnabled(false);
                    endpointLabel.setEnabled(false);
                }
            }

        });
        status = new javax.swing.JLabel();
        endpoint = new javax.swing.JTextField();
        TextTransferHandler tth0 = new TextTransferHandler();
        endpoint.setTransferHandler(tth0);
        endpoint.setDragEnabled(true);
        jLabel10 = new javax.swing.JLabel();
        cpuCount = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        memorySize = new javax.swing.JTextField();

        jLabel12 = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        TextTransferHandler tth = new TextTransferHandler();
        endpoint.setTransferHandler(tth);
        endpoint.setDragEnabled(true);
        endpointLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudera_connect_logo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 32)); // NOI18N
        jLabel2.setText("Certification Cluster Builder");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel3.setText("Cloud Provider Information");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel4.setText("User credential");

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel5.setText("Private credential");

        console.setText("View Console");
        console.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewConsole(evt);
            }
        });

        brooklyn.setText("Launch Brooklyn UI");
        brooklyn.setEnabled(false);
        brooklyn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchBrooklynUI(evt);
            }
        });

        passwordField.setMaximumSize(new java.awt.Dimension(14, 28));

        jLabel7.setText("Powered by");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/brooklyn_sm.png"))); // NOI18N

        deploy.setText("Deploy Cluster");
        deploy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deployCluster(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel9.setText("Provider");

        provider.setModel(new javax.swing.DefaultComboBoxModel(Clouds.getAvailableDisplayNames()));
        provider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                providerActionPerformed(evt);
            }
        });

        status.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        status.setForeground(new java.awt.Color(51, 51, 51));
        status.setText(DEFAULT_MESSAGE);

        endpoint.setEnabled(false);
        endpoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endpointActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel10.setText("CDH node details");

        cpuCount.setText("2");
        cpuCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cpuCountActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel11.setText("number of CPUs");

        memorySize.setText("2048");
        memorySize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memorySizeActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel12.setText("memorySize (in MB)");

        userField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userFieldActionPerformed(evt);
            }
        });

        endpointLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        endpointLabel.setText("Endpoint");
        endpointLabel.setEnabled(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel6))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel2)
                            .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 467, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                        .add(layout.createSequentialGroup()
                                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(jLabel3)
                                                .add(layout.createSequentialGroup()
                                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(jLabel4)
                                                        .add(jLabel9)
                                                        .add(jLabel5))
                                                    .add(32, 32, 32)
                                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                                            .add(org.jdesktop.layout.GroupLayout.LEADING, endpoint)
                                                            .add(org.jdesktop.layout.GroupLayout.LEADING, passwordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .add(provider, 0, 187, Short.MAX_VALUE))
                                                        .add(userField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 187, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                            .add(60, 60, 60))
                                        .add(layout.createSequentialGroup()
                                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                                    .add(jLabel12)
                                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .add(memorySize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                                    .add(jLabel11)
                                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .add(cpuCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                            .add(110, 110, 110)))
                                    .add(jLabel10)
                                    .add(endpointLabel))
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(layout.createSequentialGroup()
                                                .add(100, 100, 100)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                    .add(brooklyn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .add(console, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .add(deploy, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(79, 79, 79)
                                                .add(jLabel7)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel8)
                                                .add(9, 9, 9)))
                                        .add(0, 41, Short.MAX_VALUE))
                                    .add(layout.createSequentialGroup()
                                        .add(109, 109, 109)
                                        .add(status, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(154, 154, 154)
                        .add(jLabel6))
                    .add(layout.createSequentialGroup()
                        .add(9, 9, 9)
                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(deploy)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(console)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(brooklyn)
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel10)
                                    .add(status))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(layout.createSequentialGroup()
                                .add(jLabel3)
                                .add(21, 21, 21)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel9)
                                    .add(provider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(endpointLabel)
                                    .add(endpoint, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel4)
                                    .add(userField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel5))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel11)
                            .add(cpuCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jLabel12)
                                .add(memorySize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel7)))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewConsole(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewConsole
        setTempStatus("Opening Console...");
        console.setEnabled(false);
        consoleFrame.pack();
        consoleFrame.setVisible(true);
    }//GEN-LAST:event_viewConsole

    private void launchBrooklynUI(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchBrooklynUI
        setTempStatus("Launching browser...");
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:" + CertClusterBuilder.getPort()));
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_launchBrooklynUI

    private void deployCluster(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deployCluster
        deploy.setEnabled(false);
        String cloudName = provider.getSelectedItem().toString();
        Cloud cloud = checkNotNull(Clouds.getCloud(cloudName), "cloudname " + cloudName + " is not supported");
        String cloudCode = cloud.getProvider();
        String cloudSpec = cloud.getRegion().isPresent() ?  cloud.getRegion().get() : null;

        setStatus("Deploying to "+cloudName+"...");
        consoleFrame.addOutput("Launching cluster...\n");
        if(credential == null) {
            credential = String.copyValueOf(passwordField.getPassword());
        }
        CertClusterBuilder.launch(cloudCode, cloudSpec, endpoint.getText(), userField.getText(), credential, cpuCount.getText(), memorySize.getText());

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            // Delay activating the button until Brooklyn has had time to come up.
            delayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    brooklyn.setEnabled(true);
                }
            }, 8000);
        }
    }//GEN-LAST:event_deployCluster

    private void providerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_providerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_providerActionPerformed

    private void endpointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endpointActionPerformed
    }//GEN-LAST:event_endpointActionPerformed

    private void cpuCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cpuCountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cpuCountActionPerformed

    private void memorySizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memorySizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memorySizeActionPerformed

    private void userFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userFieldActionPerformed

    void consoleClosed() {
        console.setEnabled(true);
    }

    void setStatus(String message) {
        setStatus(message, true);
    }

    void setStatus(String message, boolean permanent) {
        if (delayTask != null) {
            delayTask.cancel();
        }

        status.setText(message.trim());

        if (permanent) {
            lastPermanentStatus = message.trim();
        }
    }

    void setDefaultStatus() {
        setStatus(DEFAULT_MESSAGE);
    }

    void setTempStatus(String message) {
        setStatus(message.trim(), false);
        setDelayedStatus(lastPermanentStatus, 2);
    }

    private void setDelayedStatus(final String message, int delay) {
        delayTask = new DelayTask(message);
        delayTimer.schedule(delayTask, delay * 1000);
    }

    void addOutput(String message) {
        consoleFrame.addOutput(message);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brooklyn;
    private javax.swing.JButton console;
    private javax.swing.JTextField cpuCount;
    private javax.swing.JButton deploy;
    private javax.swing.JTextField endpoint;
    private javax.swing.JLabel endpointLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField memorySize;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JComboBox provider;
    private javax.swing.JLabel status;
    private javax.swing.JTextField userField;
    // End of variables declaration//GEN-END:variables
    private String credential = null;
    private javax.swing.JPopupMenu popupMenu;
 
    private class DelayTask extends TimerTask {
        private String message;

        DelayTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            setStatus(message);
        }
    }

   /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
    // Create and set up the window.
    JFrame frame = new ClusterBuilderFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        // Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        /*
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }*/
        createAndShowGUI();
      }
    });
  }

}
