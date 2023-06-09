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
import javax.swing.JOptionPane;

/**
 *
 * @author chris
 */
public class pulsaFrame extends javax.swing.JFrame implements Pembayaran {

    static float totalHarga;
    static String nomorKartu;
    
    static int currItem;
    /**
     * Creates new form pulsaFrame
     */
    public pulsaFrame() {
        initComponents();
        DBConnector.initDBConnection();
    }
    
    //checker yg digunakan di cashframe, debitframe
    //untuk ngecek darimana frame tersebut dibuka
    public static boolean check(){
        if(totalHarga > 0){
            return true;
        }
        return false;
    }
    
    public static String getNomorKartu(){
        return nomorKartu;
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
                totalBayarTextField.setText(hargaFormat);   
            }            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static int getCurrentSelectedItemCode(){
        return currItem;
    }
    
    public static void insertIntoTransaksi(int id_transaksii, String total_belanja, String total_dibayar, String kembalian, String currTime){
        int id_transaksi = id_transaksii;
        String totalBelanjadb = total_belanja;
        String totalDibayardb = total_dibayar;
        String kembaliandb = kembalian;
        String currentTime = currTime;
        
        try {
                Statement stmt = DBConnector.connection.createStatement();
                String sql = "INSERT INTO transaksi(id_transaksi, total_belanja, total_bayar, kembalian, waktu_transaksi) VALUES ('"+id_transaksi+"' ,'"+totalBelanjadb+"', '"+totalDibayardb+"', '"+kembaliandb+"', '"+currentTime+"')";
                stmt.execute(sql);

            } catch (Exception ex) {
                System.out.println(ex);
            }
    }
    
    public static void insertIntoDetailTransaksi(int id_barang, int id_transaksii){
        int kodebarang = id_barang;
        int id_transaksi = id_transaksii;
        
        try {
                Statement stmt = DBConnector.connection.createStatement();
                String sql = "INSERT INTO detail_transaksi(id_barang, id_transaksi) VALUES ('"+kodebarang+"', '"+id_transaksi+"')";
                stmt.execute(sql);

            } catch (Exception ex) {
                System.out.println(ex);
            }
    }
    
    public static void uploadToDB(){
        pulsaFrame pulsaframe = new pulsaFrame();
        //time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = pulsaframe.getWaktuPembayaran().format(formatter);
        
        //id_transaksi / pembayaran
        int id_transaksi = pulsaframe.getIDPembayaran();
        
        float totalBelanja = pulsaframe.getTotalHarga();
        float totalDibayar;
        float kembalian;
        int kodebarang = getCurrentSelectedItemCode();
        
        String pattern = "###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String totalBelanjadb = decimalFormat.format(totalBelanja);
        
        
        if(cashFrame.cashFramePulsaisActive()){
            System.out.println("cashframe dari pulsa");
            totalDibayar = cashFrame.getDibayarCashFrame();
            kembalian = cashFrame.getKembalianCashFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert to transaksi
            insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //insert to detail_transaksi
            insertIntoDetailTransaksi(kodebarang, id_transaksi);

        }
        else if(debitFrame.debitFramePulsaisActive()){
            System.out.println("debitframe dari pulsa");
            totalDibayar = debitFrame.getCurrentCardBalance();
            kembalian = debitFrame.getKembalianDebitFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert to transaksi
            insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //insert to detail_transaksi
            insertIntoDetailTransaksi(kodebarang, id_transaksi);
        }
        else if(qrisFrame.qrisFramePulsaisActive()){
            totalDibayar = qrisFrame.getImaginaryBalance();
            kembalian = qrisFrame.getKembalianQRISFrame();
            String totalDibayardb = decimalFormat.format(totalDibayar);
            String kembaliandb = decimalFormat.format(kembalian);
            
            //insert to transaksi
            insertIntoTransaksi(id_transaksi, totalBelanjadb, totalDibayardb, kembaliandb, currentTime);
            
            //insert to detail_transaksi
            insertIntoDetailTransaksi(kodebarang, id_transaksi);
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
        btn5rb = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btn20rb = new javax.swing.JButton();
        btn30rb = new javax.swing.JButton();
        btn10rb = new javax.swing.JButton();
        btn15rb = new javax.swing.JButton();
        btn25rb = new javax.swing.JButton();
        btn50rb = new javax.swing.JButton();
        btn75rb = new javax.swing.JButton();
        btn100rb = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        totalBayarTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        submitBTN = new javax.swing.JButton();
        backBTN = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        nomorKartuTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        operatorTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(26, 69, 224));

        btn5rb.setBackground(new java.awt.Color(255, 255, 255));
        btn5rb.setForeground(new java.awt.Color(0, 0, 0));
        btn5rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">5.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 7.000</div>\n    </div>\n</body>\n</html>");
        btn5rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn5rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5rbActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pos/application/115 asparagus.png"))); // NOI18N
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btn20rb.setBackground(new java.awt.Color(255, 255, 255));
        btn20rb.setForeground(new java.awt.Color(0, 0, 0));
        btn20rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">20.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 21.000</div>\n    </div>\n</body>\n</html>");
        btn20rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn20rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn20rbActionPerformed(evt);
            }
        });

        btn30rb.setBackground(new java.awt.Color(255, 255, 255));
        btn30rb.setForeground(new java.awt.Color(0, 0, 0));
        btn30rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">30.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 31.000</div>\n    </div>\n</body>\n</html>");
        btn30rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn30rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn30rbActionPerformed(evt);
            }
        });

        btn10rb.setBackground(new java.awt.Color(255, 255, 255));
        btn10rb.setForeground(new java.awt.Color(0, 0, 0));
        btn10rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">10.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 12.000</div>\n    </div>\n</body>\n</html>");
        btn10rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn10rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn10rbActionPerformed(evt);
            }
        });

        btn15rb.setBackground(new java.awt.Color(255, 255, 255));
        btn15rb.setForeground(new java.awt.Color(0, 0, 0));
        btn15rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">15.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 17.000</div>\n    </div>\n</body>\n</html>");
        btn15rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn15rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn15rbActionPerformed(evt);
            }
        });

        btn25rb.setBackground(new java.awt.Color(255, 255, 255));
        btn25rb.setForeground(new java.awt.Color(0, 0, 0));
        btn25rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">25.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 26.000</div>\n    </div>\n</body>\n</html>");
        btn25rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn25rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn25rbActionPerformed(evt);
            }
        });

        btn50rb.setBackground(new java.awt.Color(255, 255, 255));
        btn50rb.setForeground(new java.awt.Color(0, 0, 0));
        btn50rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">50.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 50.000</div>\n    </div>\n</body>\n</html>");
        btn50rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn50rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn50rbActionPerformed(evt);
            }
        });

        btn75rb.setBackground(new java.awt.Color(255, 255, 255));
        btn75rb.setForeground(new java.awt.Color(0, 0, 0));
        btn75rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">75.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 75.000</div>\n    </div>\n</body>\n</html>");
        btn75rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn75rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn75rbActionPerformed(evt);
            }
        });

        btn100rb.setBackground(new java.awt.Color(255, 255, 255));
        btn100rb.setForeground(new java.awt.Color(0, 0, 0));
        btn100rb.setText("<html>\n<head>\n    <style>\n        .container{\n            padding-left: 20px;\n        }\n\n        .container .head{\n            font-size: 18px;\n            font-weight: bold;\n        }\n\n        .container .body{\n            font-size: 9px;\n        }\n\n        .container .footer{\n            font-size: 11px;\n            font-weight: bold;\n        }\n    \n    </style>\n</head>\n<body>\n    <div class=\"container\">\n        <div class=\"head\">100.000</div>\n        <div class=\"body\">Harga</div>\n        <div class=\"footer\">Rp. 100.000</div>\n    </div>\n</body>\n</html>");
        btn100rb.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn100rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn100rbActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Montserrat Thin", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nomor Kartu");

        totalBayarTextField.setEditable(false);
        totalBayarTextField.setBackground(new java.awt.Color(255, 255, 255));
        totalBayarTextField.setForeground(new java.awt.Color(0, 0, 0));
        totalBayarTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBayarTextFieldActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Montserrat Thin", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pilih Metode Pembayaran");

        comboBox.setBackground(new java.awt.Color(255, 255, 255));
        comboBox.setForeground(new java.awt.Color(0, 0, 0));
        comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Debit Card", "QRIS" }));
        comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxActionPerformed(evt);
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

        backBTN.setBackground(new java.awt.Color(255, 255, 255));
        backBTN.setForeground(new java.awt.Color(0, 0, 0));
        backBTN.setText("Back to Main Menu");
        backBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBTNActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Montserrat Thin", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Bayar ");

        nomorKartuTextField.setBackground(new java.awt.Color(255, 255, 255));
        nomorKartuTextField.setForeground(new java.awt.Color(0, 0, 0));
        nomorKartuTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomorKartuTextFieldActionPerformed(evt);
            }
        });
        nomorKartuTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomorKartuTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomorKartuTextFieldKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Montserrat Thin", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Operator");

        operatorTextField.setEditable(false);
        operatorTextField.setBackground(new java.awt.Color(255, 255, 255));
        operatorTextField.setForeground(new java.awt.Color(0, 0, 0));
        operatorTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operatorTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn50rb, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(submitBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn75rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn100rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(totalBayarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn20rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn25rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn30rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn5rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn10rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn15rb, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(nomorKartuTextField)
                                    .addComponent(operatorTextField))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nomorKartuTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(operatorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn5rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn10rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn15rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn20rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn25rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn30rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn50rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn75rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn100rb, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalBayarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backBTN)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitBTN))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn5rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 5k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn5rbActionPerformed

    private void totalBayarTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBayarTextFieldActionPerformed
        
    }//GEN-LAST:event_totalBayarTextFieldActionPerformed

    private void btn10rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn10rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 10k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn10rbActionPerformed

    private void btn15rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn15rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 15k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn15rbActionPerformed

    private void btn20rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn20rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 20k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn20rbActionPerformed

    private void btn25rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn25rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 25k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn25rbActionPerformed

    private void btn30rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn30rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 30k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn30rbActionPerformed

    private void btn50rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn50rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 50k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn50rbActionPerformed

    private void btn75rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn75rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 75k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn75rbActionPerformed

    private void btn100rbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn100rbActionPerformed
        String query = "select * from barang where item_type = 'pulsa' and nama = 'Pulsa 100k'";
        getItemFromDB(query);
    }//GEN-LAST:event_btn100rbActionPerformed

    private void submitBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBTNActionPerformed
        
        nomorKartu = nomorKartuTextField.getText();
        
        while(!totalBayarTextField.equals("") && !nomorKartu.equals(" ")){
            var combo = comboBox.getSelectedItem();
            if(nomorKartu.isEmpty()){
                JOptionPane.showMessageDialog(null, "Masukkan Nomor Terlebih dulu!", "Caution!", JOptionPane.WARNING_MESSAGE);
                break;
            }
            else if(totalBayarTextField.getText().isEmpty()){
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
        totalHarga = 0;
        this.dispose();
        new userMainMenu().setVisible(true);
    }//GEN-LAST:event_backBTNActionPerformed

    private void nomorKartuTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomorKartuTextFieldActionPerformed
        
    }//GEN-LAST:event_nomorKartuTextFieldActionPerformed

    private void nomorKartuTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomorKartuTextFieldKeyReleased
        String nomor = nomorKartuTextField.getText();
        if(nomor.equals("0881") || nomor.equals("0882") || nomor.equals("0883")
                || nomor.equals("0884") || nomor.equals("0885") || nomor.equals("0886") 
                || nomor.equals("0887") || nomor.equals("0888") || nomor.equals("0889")){
            operatorTextField.setText("Smartfren");
        }
        else if(nomor.equals("0852") || nomor.equals("0853") || nomor.equals("0813")
                 || nomor.equals("0821") || nomor.equals("0822") || nomor.equals("0851")
                 || nomor.equals("0812") || nomor.equals("0811")){
            operatorTextField.setText("Telkomsel");
        }
        else if(nomor.equals("0857") || nomor.equals("0856")){
            operatorTextField.setText("Indosat");
        }
        else if (nomor.equals("0817") || nomor.equals("0818") || nomor.equals("0819")
                 || nomor.equals("0859") || nomor.equals("0877") || nomor.equals("0878")){
            operatorTextField.setText("XL Axiata");
        }
        else if(nomor.equals("0813") || nomor.equals("0832") || nomor.equals("0833")
                 || nomor.equals("0838")){
            operatorTextField.setText("AXIS");
        }
        else if(nomor.isBlank()){
            operatorTextField.setText(null);
        }
    }//GEN-LAST:event_nomorKartuTextFieldKeyReleased

    private void nomorKartuTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomorKartuTextFieldKeyTyped
        String nomor = nomorKartuTextField.getText();
        if(nomor.length() >= 12){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Nomor tidak lebih dari 12", "Caution!", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_nomorKartuTextFieldKeyTyped

    private void operatorTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_operatorTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_operatorTextFieldActionPerformed

    private void comboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(pulsaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pulsaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pulsaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pulsaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pulsaFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBTN;
    private javax.swing.JButton btn100rb;
    private javax.swing.JButton btn10rb;
    private javax.swing.JButton btn15rb;
    private javax.swing.JButton btn20rb;
    private javax.swing.JButton btn25rb;
    private javax.swing.JButton btn30rb;
    private javax.swing.JButton btn50rb;
    private javax.swing.JButton btn5rb;
    private javax.swing.JButton btn75rb;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nomorKartuTextField;
    private javax.swing.JTextField operatorTextField;
    private javax.swing.JButton submitBTN;
    private javax.swing.JTextField totalBayarTextField;
    // End of variables declaration//GEN-END:variables

    

}
