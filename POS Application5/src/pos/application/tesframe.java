/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos.application;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;


/**
 *
 * @author chris
 */

//tesframe itu adalah barang/item
public class tesframe extends javax.swing.JFrame implements Pembayaran {
    
    DefaultTableModel dm;
    int jumlahBelanja = 0;
    int totalBelanjaan = 0;
    
    static float finalTotalBelanja;
    
    //nyimpan kode-kode barang dari table
    static ArrayList<Integer> savekodebarang = new ArrayList(); 
    
    /**
     * Creates new form tesframe
     */
    
    public tesframe() {       
        DBConnector.initDBConnection();
        dm = new DefaultTableModel();
        
        initComponents();
        createColumns();
        
        dm = (DefaultTableModel) barangTable.getModel();
        dm.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) { 
                if(e.getColumn() == 5){
                        int baris = e.getFirstRow();
                        System.out.println("kolom ke 4");
                        String kolom4 = (String) dm.getValueAt(baris, 4);
                        float harga = Float.parseFloat(kolom4);
                        String kolom5 = (String) dm.getValueAt(baris, 5);
                        int jumlah = Integer.parseInt(kolom5);

                        float total = harga * jumlah;
                        System.out.println(harga);
                        System.out.println(total);
                        NumberFormat formatter = new DecimalFormat("#,###,###");
                        String totalString = Float.toString(total);
                        String totalStringFormat = formatter.format(total);
                        dm.setValueAt(totalStringFormat, baris, 6);
                        dm.setValueAt(totalString, baris, 7);
                              
                        float totalBelanja = 0.0f;
                        total = 0.0f;
                        
                        for (int i = 0; i < totalBelanjaan; i++){
                            String kolom7 = (String) dm.getValueAt(i, 7);
                            total = Float.parseFloat(kolom7);             
                            totalBelanja += total;
                            System.out.println(barangTable.getModel().getValueAt(i, 7));
                        }
                        finalTotalBelanja = totalBelanja;
                        int totalBelanjaInt = (int)totalBelanja;                
                        totalBelanjaTextField.setText(formatter.format(totalBelanjaInt));
                }
            }
        });
    }
    
    private void createColumns(){
        dm = (DefaultTableModel) barangTable.getModel();
           
        dm.addColumn("No");
        dm.addColumn("Kode");
        dm.addColumn("Nama");
        dm.addColumn("Harga Satuan");
        dm.addColumn("hargaInv");
        dm.addColumn("Jumlah");
        dm.addColumn("Total");
        dm.addColumn("totalInv");
        dm.addColumn("Expired Date");
        
        TableColumnModel tcm = barangTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(4)); //hargaInv
        tcm.removeColumn( tcm.getColumn(6)); //totalInv
    }
    
    private void populate(String no, String kode, String nama, String harga, float hargaInv, int jumlahBelanja, String total, float totalInv, String exp){
        dm = (DefaultTableModel) barangTable.getModel();   
        String[] rowData = {no, kode, nama, harga, Float.toString(hargaInv), Integer.toString(jumlahBelanja), total, Float.toString(totalInv), exp};
        dm.addRow(rowData);
    }
    
    //checker yg digunakan di cashframe, debitframe
    //untuk ngecek darimana frame tersebut dibuka
    public static boolean check(){
        if(finalTotalBelanja > 0){
            return true;
        }
        return false;
    }
    
    @Override
    public float getTotalHarga() {
        return finalTotalBelanja;
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
    
    //fungsi untuk masukkan "kode" barang ke dalam arraylist savekode1
    public void kode1(int kode){
        savekodebarang.add(kode);
    }
    
    public static void uploadToDB(){
        tesframe frame = new tesframe();
        
        //time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = frame.getWaktuPembayaran().format(formatter);
      
        float totalBelanja = frame.getTotalHarga();
        float totalDibayar;
        float kembalian;
        System.out.println(totalBelanja);
        
        //id_transaksi / pembayaran
        int id_transaksi = frame.getIDPembayaran();

        String pattern = "###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String totalBelanjadb = decimalFormat.format(totalBelanja);       
        
        if(cashFrame.cashFrameItemisActive()){
            totalDibayar = cashFrame.getDibayarCashFrame();
            kembalian = cashFrame.getKembalianCashFrame();
            
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            if(totalDibayar < totalBelanja){
                JOptionPane.showMessageDialog(null, "Uang Anda Kurang!", "Caution!", JOptionPane.WARNING_MESSAGE);
            }
            
            //insert into transaksi
            pulsaFrame.insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //ambil "kode" barang yang disimpan di arraylist savekodebarang trus di insert ke db
            System.out.println(savekodebarang);
            for(int i = 0; i < savekodebarang.size(); i++){
                System.out.println(savekodebarang.get(i));
                int kodebarang = savekodebarang.get(i);
                try {
                    Statement stmt = DBConnector.connection.createStatement();
                    String sql = "INSERT INTO detail_transaksi(id_barang, id_transaksi) VALUES ('"+kodebarang+"', '"+id_transaksi+"')";
                    stmt.execute(sql);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        else if(debitFrame.debitFrameItemisActive()){
            totalDibayar = debitFrame.getCurrentCardBalance();
            kembalian = debitFrame.getKembalianDebitFrame();
            
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert into transaksi
            pulsaFrame.insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //ambil "kode" barang yang disimpan di arraylist savekodebarang trus di insert ke db
            System.out.println(savekodebarang);
            for(int i = 0; i < savekodebarang.size(); i++){
                System.out.println(savekodebarang.get(i));
                int kodebarang = savekodebarang.get(i);
                try {
                    Statement stmt = DBConnector.connection.createStatement();
                    String sql = "INSERT INTO detail_transaksi(id_barang, id_transaksi) VALUES ('"+kodebarang+"', '"+id_transaksi+"')";
                    stmt.execute(sql);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        else if(qrisFrame.qrisFrameItemisActive()){
            totalDibayar = qrisFrame.getImaginaryBalance();
            kembalian = qrisFrame.getKembalianQRISFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert into transaksi
            pulsaFrame.insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //ambil "kode" barang yang disimpan di arraylist savekodebarang trus di insert ke db
            System.out.println(savekodebarang);
            for(int i = 0; i < savekodebarang.size(); i++){
                System.out.println(savekodebarang.get(i));
                int kodebarang = savekodebarang.get(i);
                try {
                    Statement stmt = DBConnector.connection.createStatement();
                    String sql = "INSERT INTO detail_transaksi(id_barang, id_transaksi) VALUES ('"+kodebarang+"', '"+id_transaksi+"')";
                    stmt.execute(sql);

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
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
        kodeTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        barangTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        namaTextField = new javax.swing.JTextField();
        hargaTextField = new javax.swing.JTextField();
        totalBelanjaTextField = new javax.swing.JTextField();
        clearBTN = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        backBTN = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        submitBTN = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(992, 520));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(26, 69, 224));
        jPanel1.setForeground(new java.awt.Color(33, 158, 188));

        jLabel1.setFont(new java.awt.Font("Montserrat Thin", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Kode");

        kodeTextField.setBackground(new java.awt.Color(255, 255, 255));
        kodeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeTextFieldActionPerformed(evt);
            }
        });

        barangTable.setBackground(new java.awt.Color(255, 255, 255));
        barangTable.setForeground(new java.awt.Color(0, 0, 0));
        barangTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        barangTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                barangTablePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(barangTable);

        jLabel2.setFont(new java.awt.Font("Montserrat Thin", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nama Barang");

        jLabel3.setFont(new java.awt.Font("Montserrat Thin", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Harga Barang");

        jLabel4.setFont(new java.awt.Font("Montserrat Thin", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Belanja");

        namaTextField.setEditable(false);
        namaTextField.setBackground(new java.awt.Color(255, 255, 255));

        hargaTextField.setEditable(false);
        hargaTextField.setBackground(new java.awt.Color(255, 255, 255));

        totalBelanjaTextField.setEditable(false);
        totalBelanjaTextField.setBackground(new java.awt.Color(255, 255, 255));
        totalBelanjaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBelanjaTextFieldActionPerformed(evt);
            }
        });

        clearBTN.setBackground(new java.awt.Color(255, 255, 255));
        clearBTN.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        clearBTN.setForeground(new java.awt.Color(0, 0, 0));
        clearBTN.setText("Clear");
        clearBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBTNActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pos/application/115 asparagus.png"))); // NOI18N

        backBTN.setBackground(new java.awt.Color(255, 255, 255));
        backBTN.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        backBTN.setForeground(new java.awt.Color(0, 0, 0));
        backBTN.setText("Back");
        backBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBTNActionPerformed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Montserrat Thin", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Pilih Metode Pembayaran");

        comboBox.setBackground(new java.awt.Color(255, 255, 255));
        comboBox.setForeground(new java.awt.Color(0, 0, 0));
        comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Debit Card", "QRIS" }));

        submitBTN.setBackground(new java.awt.Color(255, 255, 255));
        submitBTN.setFont(new java.awt.Font("Montserrat Medium", 0, 12)); // NOI18N
        submitBTN.setForeground(new java.awt.Color(0, 0, 0));
        submitBTN.setText("Submit");
        submitBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(namaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(kodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(totalBelanjaTextField)
                                    .addComponent(comboBox, 0, 134, Short.MAX_VALUE)))
                            .addComponent(backBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(submitBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(39, 39, 39))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(kodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(namaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(totalBelanjaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addComponent(submitBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void clearBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBTNActionPerformed
        while(dm.getRowCount() > 0){
            dm.removeRow(0);
        }
        dm.setRowCount(0);
        totalBelanjaan = 0;
        kodeTextField.setText(null);
        namaTextField.setText(null);
        hargaTextField.setText(null);
        totalBelanjaTextField.setText(null);
    }//GEN-LAST:event_clearBTNActionPerformed

    private void barangTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_barangTablePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_barangTablePropertyChange

    private void kodeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeTextFieldActionPerformed
        String kode = kodeTextField.getText();
        String nama;
        
        try {
            int check = Integer.parseInt(kode);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Kode tidak boleh huruf!", "Caution!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        float harga;
        float total;
        float hargaInv;
        float totalInv;
        String exp;
       
        try {            
            Statement stmt = DBConnector.connection.createStatement();
            String sql = "select * from barang where kode='"+kode+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                NumberFormat formatter = new DecimalFormat("#,###,###");
                nama = rs.getString("nama");
                harga = rs.getFloat("harga");
                total = rs.getFloat("harga");
                hargaInv = rs.getFloat("harga");
                totalInv = rs.getFloat("harga");
                exp = rs.getString("exp_date");
                
                namaTextField.setText(nama);
                String hargaFormat = formatter.format(harga);
                String totalFormat = formatter.format(total);
                hargaTextField.setText(formatter.format(harga));

                populate(Integer.toString(dm.getRowCount()+1), kode, nama, hargaFormat, hargaInv, jumlahBelanja+1, totalFormat, totalInv, exp);
                totalBelanjaan++;
                System.out.println("total belanjaan : "+totalBelanjaan);

                float totalBelanja = 0.0f;
                total = 0.0f;
                for (int i = 0; i < totalBelanjaan; i++){
                    String kolom7 = (String) dm.getValueAt(i, 7);
                   total = Float.parseFloat(kolom7);
                    totalBelanja += total;
                }
                finalTotalBelanja = totalBelanja;
                int totalBelanjaInt = (int)totalBelanja;
                totalBelanjaTextField.setText(formatter.format(totalBelanjaInt));

            }
            else{
                System.out.println("Barang Tidak Ditemukan!");
                JOptionPane.showMessageDialog(null, "Barang Tidak Ditemukan!", "Caution!", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_kodeTextFieldActionPerformed

    private void backBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBTNActionPerformed
        finalTotalBelanja = 0;
        this.dispose();
        new userMainMenu().setVisible(true);
    }//GEN-LAST:event_backBTNActionPerformed

    private void submitBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBTNActionPerformed
        while(!totalBelanjaTextField.equals("")){
            var combo = comboBox.getSelectedItem();
            if(totalBelanjaTextField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Silahkan Belanja dulu!", "Caution!", JOptionPane.WARNING_MESSAGE);
                break;
            }
            else if(combo.equals("Cash")){
                //loop sebanyak jumlah kolom untuk nyimpan value dari kolom 1 (kode barang)
                for(int i = 0; i<dm.getRowCount(); i++){
                    String kolom1 = (String) dm.getValueAt(i, 1);
                    System.out.println(kolom1);
                    //masukkan ke fungsi
                    kode1(Integer.valueOf(kolom1));         
                }
                new cashFrame().setVisible(true);
                break;
            }
            else if (combo.equals("Debit Card")){
                //loop sebanyak jumlah kolom untuk nyimpan value dari kolom 1 (kode barang)
                for(int i = 0; i<dm.getRowCount(); i++){
                    String kolom1 = (String) dm.getValueAt(i, 1);
                    System.out.println(kolom1);
                    //masukkan ke fungsi
                    kode1(Integer.valueOf(kolom1));         
                }
                new debitFrame().setVisible(true);
                break;
            }
            else if (combo.equals("QRIS")){
                //loop sebanyak jumlah kolom untuk nyimpan value dari kolom 1 (kode barang)
                for(int i = 0; i<dm.getRowCount(); i++){
                    String kolom1 = (String) dm.getValueAt(i, 1);
                    System.out.println(kolom1);
                    //masukkan ke fungsi
                    kode1(Integer.valueOf(kolom1));         
                }
                new qrisFrame().setVisible(true);
                break;
            }     
        }
    }//GEN-LAST:event_submitBTNActionPerformed

    private void totalBelanjaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBelanjaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBelanjaTextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(tesframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tesframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tesframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tesframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tesframe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBTN;
    private javax.swing.JTable barangTable;
    private javax.swing.JButton clearBTN;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JTextField hargaTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kodeTextField;
    private javax.swing.JTextField namaTextField;
    private javax.swing.JButton submitBTN;
    private javax.swing.JTextField totalBelanjaTextField;
    // End of variables declaration//GEN-END:variables

    

}
