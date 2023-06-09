/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos.application;
import java.security.SecureRandom;
import java.util.Random;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author chris
 */
public class tokenFrame extends javax.swing.JFrame implements Pembayaran {

    private static float totalHarga;
    
    private static int currItem;
    /**
     * Creates new form tokenFrame
     */
    public tokenFrame() {
        initComponents();
        DBConnector.initDBConnection();
    }
    
    private void getItemFromDB(String sql1){       
        int kode;
        float harga;
        try {
            Statement stmt = DBConnector.connection.createStatement();
            String sql = sql1;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                NumberFormat formatter = new DecimalFormat("#,###,###");
                kode = rs.getInt("kode");
                currItem = kode;
                harga = rs.getFloat("harga");
                totalHarga = harga;
                String hargaFormat = formatter.format(harga);
                dibeliTextField.setText(hargaFormat);   
            }            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    //checker yg digunakan di cashframe, debitframe
    //untuk ngecek darimana frame tersebut dibuka
    public static boolean check(){
        if(totalHarga > 0){
            return true;
        }
        return false;
    }
    
    @Override
    public float getTotalHarga() {
        return totalHarga;
    }

    @Override
    public LocalDateTime getWaktuPembayaran() {
        LocalDateTime dt = LocalDateTime.now();
        return dt;
    }

    @Override
    public int getIDPembayaran() {
        SecureRandom rand = new SecureRandom();
        int randomInt = rand.nextInt(100000);
        return randomInt;
    }
    
    //token generator
    public static String tokenGenerator(int digits){
        // no token(cth) : 6750-1857-2325-1859-9756
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < digits; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
    
    //formatter tokenGenerator
    public static String TokenFormatter(String number) {
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < number.length(); i++) {
            formatted.append(number.charAt(i));

            if ((i + 1) % 4 == 0 && i != number.length() - 1) {
                formatted.append("-");
            }
        }

        return formatted.toString();
    }
    
    public static int getCurrentSelectedItemCode(){
        return currItem;
    }

    public static void uploadToDB(){
        tokenFrame tokenframe = new tokenFrame();
        //time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = tokenframe.getWaktuPembayaran().format(formatter);
        
        //id_transaksi / pembayaran
        int id_transaksi = tokenframe.getIDPembayaran();
        
        float totalBelanja = tokenframe.getTotalHarga();
        System.out.println(totalBelanja);
        float totalDibayar;
        float kembalian;
        int kodebarang = getCurrentSelectedItemCode();
        
        String pattern = "###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String totalBelanjadb = decimalFormat.format(totalBelanja);
        
        
        if(cashFrame.cashFrameTokenisActive()){
            System.out.println("cashframe dari token");
            totalDibayar = cashFrame.getDibayarCashFrame();
            kembalian = cashFrame.getKembalianCashFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert to transaksi
            pulsaFrame.insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //insert to detail_transaksi
            pulsaFrame.insertIntoDetailTransaksi(kodebarang, id_transaksi);

        }
        else if(debitFrame.debitFrameTokenisActive()){
            System.out.println("debitframe dari token");
            totalDibayar = debitFrame.getCurrentCardBalance();
            kembalian = debitFrame.getKembalianDebitFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert to transaksi
            pulsaFrame.insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //insert to detail_transaksi
            pulsaFrame.insertIntoDetailTransaksi(kodebarang, id_transaksi);
        }
        else if(qrisFrame.qrisFrameTokenisActive()){
            totalDibayar = qrisFrame.getImaginaryBalance();
            kembalian = qrisFrame.getKembalianQRISFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert to transaksi
            pulsaFrame.insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //insert to detail_transaksi
            pulsaFrame.insertIntoDetailTransaksi(kodebarang, id_transaksi);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        custIDTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pln20 = new javax.swing.JButton();
        pln50 = new javax.swing.JButton();
        pln75 = new javax.swing.JButton();
        pln100 = new javax.swing.JButton();
        pln150 = new javax.swing.JButton();
        pln200 = new javax.swing.JButton();
        pln500 = new javax.swing.JButton();
        pln750 = new javax.swing.JButton();
        pln1jt = new javax.swing.JButton();
        submitBTN = new javax.swing.JButton();
        dibeliTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        backBTN = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(26, 69, 224));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Meter Number / Customer ID");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pos/application/115 asparagus.png"))); // NOI18N

        custIDTextField.setBackground(new java.awt.Color(255, 255, 255));
        custIDTextField.setForeground(new java.awt.Color(0, 0, 0));
        custIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custIDTextFieldActionPerformed(evt);
            }
        });
        custIDTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                custIDTextFieldKeyTyped(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pilih Paket");

        pln20.setBackground(new java.awt.Color(255, 255, 255));
        pln20.setForeground(new java.awt.Color(0, 0, 0));
        pln20.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN20</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 22.000</div>\n     </div>\n </body>\n </html>");
        pln20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln20ActionPerformed(evt);
            }
        });

        pln50.setBackground(new java.awt.Color(255, 255, 255));
        pln50.setForeground(new java.awt.Color(0, 0, 0));
        pln50.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN50</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 52.000</div>\n     </div>\n </body>\n </html>");
        pln50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln50ActionPerformed(evt);
            }
        });

        pln75.setBackground(new java.awt.Color(255, 255, 255));
        pln75.setForeground(new java.awt.Color(0, 0, 0));
        pln75.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN75</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 77.000</div>\n     </div>\n </body>\n </html>");
        pln75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln75ActionPerformed(evt);
            }
        });

        pln100.setBackground(new java.awt.Color(255, 255, 255));
        pln100.setForeground(new java.awt.Color(0, 0, 0));
        pln100.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN100</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 102.000</div>\n     </div>\n </body>\n </html>");
        pln100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln100ActionPerformed(evt);
            }
        });

        pln150.setBackground(new java.awt.Color(255, 255, 255));
        pln150.setForeground(new java.awt.Color(0, 0, 0));
        pln150.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN150</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 152.000</div>\n     </div>\n </body>\n </html>");
        pln150.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln150ActionPerformed(evt);
            }
        });

        pln200.setBackground(new java.awt.Color(255, 255, 255));
        pln200.setForeground(new java.awt.Color(0, 0, 0));
        pln200.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN200</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 202.000</div>\n     </div>\n </body>\n </html>");
        pln200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln200ActionPerformed(evt);
            }
        });

        pln500.setBackground(new java.awt.Color(255, 255, 255));
        pln500.setForeground(new java.awt.Color(0, 0, 0));
        pln500.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN500</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 502.000</div>\n     </div>\n </body>\n </html>");
        pln500.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln500ActionPerformed(evt);
            }
        });

        pln750.setBackground(new java.awt.Color(255, 255, 255));
        pln750.setForeground(new java.awt.Color(0, 0, 0));
        pln750.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN750</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 752.000</div>\n     </div>\n </body>\n </html>");
        pln750.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln750ActionPerformed(evt);
            }
        });

        pln1jt.setBackground(new java.awt.Color(255, 255, 255));
        pln1jt.setForeground(new java.awt.Color(0, 0, 0));
        pln1jt.setText("<html>\n <head> \n    <style>\n        .container{\n            padding: 15px;\n        }\n         .container .head{\n             font-size: 18px;\n             font-weight: bold;\n         }\n          .container .body{\n             font-size: 9px;\n         }\n          .container .footer{\n             font-size: 11px;\n             font-weight: bold;\n         }\n          </style>\n </head>\n <body>\n     <div class=\"container\">\n         <div class=\"head\">PLN1JT</div>\n         <div class=\"body\">Harga</div> \n        <div class=\"footer\">Rp. 1.002.000</div>\n     </div>\n </body>\n </html>");
        pln1jt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pln1jtActionPerformed(evt);
            }
        });

        submitBTN.setBackground(new java.awt.Color(255, 255, 255));
        submitBTN.setForeground(new java.awt.Color(0, 0, 0));
        submitBTN.setText("Submit");
        submitBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBTNActionPerformed(evt);
            }
        });

        dibeliTextField.setEditable(false);
        dibeliTextField.setBackground(new java.awt.Color(26, 69, 224));
        dibeliTextField.setFont(new java.awt.Font("Montserrat Thin", 1, 18)); // NOI18N
        dibeliTextField.setForeground(new java.awt.Color(255, 255, 255));
        dibeliTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setFont(new java.awt.Font("Montserrat Thin", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Metode Pembayaran");

        comboBox.setBackground(new java.awt.Color(255, 255, 255));
        comboBox.setForeground(new java.awt.Color(0, 0, 0));
        comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Debit Card", "QRIS" }));

        backBTN.setBackground(new java.awt.Color(255, 255, 255));
        backBTN.setForeground(new java.awt.Color(0, 0, 0));
        backBTN.setText("Back to Main Menu");
        backBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dibeliTextField)
                    .addComponent(submitBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(custIDTextField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(pln20)
                                            .addComponent(pln100))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(pln50)
                                            .addComponent(pln150)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(pln500, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pln750, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pln1jt)
                                    .addComponent(pln200)
                                    .addComponent(pln75))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(backBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel2)
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(custIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pln20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pln50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pln75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pln100, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pln150, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pln200, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pln500, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pln750, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pln1jt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dibeliTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submitBTN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(backBTN)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void custIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custIDTextFieldActionPerformed
        
    }//GEN-LAST:event_custIDTextFieldActionPerformed

    private void custIDTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_custIDTextFieldKeyTyped
        String custNumber = custIDTextField.getText();
        if(custNumber.length() >= 12){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Nomor Meter/Customer ID tidak lebih dari 12 digit", "Caution!", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_custIDTextFieldKeyTyped

    private void pln50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln50ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN50'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln50ActionPerformed

    private void pln20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln20ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN20'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln20ActionPerformed

    private void pln75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln75ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN75'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln75ActionPerformed

    private void pln100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln100ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN100'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln100ActionPerformed

    private void pln150ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln150ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN150'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln150ActionPerformed

    private void pln200ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln200ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN200'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln200ActionPerformed

    private void pln500ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln500ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN500'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln500ActionPerformed

    private void pln1jtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln1jtActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN1JT'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln1jtActionPerformed

    private void pln750ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pln750ActionPerformed
        String query = "select * from barang where item_type = 'token' and nama = 'PLN750'";
        getItemFromDB(query);
    }//GEN-LAST:event_pln750ActionPerformed

    private void submitBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBTNActionPerformed
        String custNumber = custIDTextField.getText();;
        String harga = dibeliTextField.getText();
        
        while(!harga.equals("") && !custNumber.equals(" ")){
            var combo = comboBox.getSelectedItem();
            if(custNumber.isEmpty()){
                JOptionPane.showMessageDialog(null, "Masukkan Nomor Terlebih dulu!", "Caution!", JOptionPane.WARNING_MESSAGE);
                break;
            }
            else if(harga.isEmpty()){
                JOptionPane.showMessageDialog(null, "Silahkan Pilih Pulsa dulu!", "Caution!", JOptionPane.WARNING_MESSAGE);
                break;
            }
            else if(combo.equals("Cash")){
                new cashFrame().setVisible(true);
                break;
            }
            else if (combo.equals("Debit Card")){
                new debitFrame().setVisible(true);
                break;
            }
            else if (combo.equals("QRIS")){
                new qrisFrame().setVisible(true);
                break;
            }
        }
    }//GEN-LAST:event_submitBTNActionPerformed

    private void backBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBTNActionPerformed
        new userMainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backBTNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tokenFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tokenFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tokenFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tokenFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tokenFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBTN;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JTextField custIDTextField;
    private javax.swing.JTextField dibeliTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton pln100;
    private javax.swing.JButton pln150;
    private javax.swing.JButton pln1jt;
    private javax.swing.JButton pln20;
    private javax.swing.JButton pln200;
    private javax.swing.JButton pln50;
    private javax.swing.JButton pln500;
    private javax.swing.JButton pln75;
    private javax.swing.JButton pln750;
    private javax.swing.JButton submitBTN;
    // End of variables declaration//GEN-END:variables

    

}
