/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pos.application;

import java.time.LocalDateTime;

/**
 *
 * @author chris
 */
public interface Pembayaran {
    float getTotalHarga();
//    void setTotalHarga(float totalHarga);
    
    LocalDateTime getWaktuPembayaran();
//    void setWaktuPembayaran(LocalDateTime waktuPembayaran);
    
    int getIDPembayaran();
//    void setIDPembayaran();
}
